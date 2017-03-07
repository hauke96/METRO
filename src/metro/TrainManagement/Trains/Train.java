package metro.TrainManagement.Trains;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import juard.Contract;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;

/**
 * A Train can carry passengers, costs a bit to buy and maintain it.
 * The costs of maintaining will also increase every month by a given factor (_costsFactor) and there is a maximum amount of passengers per train as well.
 * All data about the trains are stored in the ./data/trains.txt file and are read & parsed by the GameState class.
 * 
 * @author hauke
 *
 */

public class Train extends TrainTemplate
{
	private float _relativeOnLine, _textureRotation;
	private int _currPassengers, _direction, _textureYOffset;
	private TrainLine _trainLine;
	private Point _currentNode, _nextNode;
	private long _waitInStationSince;

	/**
	 * Creates a new train with the following properties.
	 * Every data for the trains are in the ./data/trains.txt file and will be read & parsed by the GameState class.
	 * 
	 * @param name The model name of the train which can be anything but mostly like "CT-1 (3)".
	 * @param modelName The name of the train model like "CT-1".
	 * @param manufacturer Manufacturer of the train.
	 * @param price Price for buying the train.
	 * @param costs Costs per month.
	 * @param costsFactor Monthly increase of the costs.
	 * @param passengers Maximum amount of passengers per train.
	 * @param speed The speed in nodes per second.
	 */
	public Train(String name, String modelName, String manufacturer, int price, int costs, float costsFactor, int passengers, float speed)
	{
		super(name, modelName, manufacturer, price, costs, costsFactor, passengers, speed);
		_currPassengers = 0;
		_relativeOnLine = 0.0f;
		_direction = 1;

		_textureYOffset = 0;
		_textureRotation = 0f;

		_waitInStationSince = -1;
	}

	/**
	 * Creates a new Train with the properties of the given one.
	 * This constructor constructs a copy if you want so.
	 * 
	 * @param trainTemplate A train that should be copied.
	 */
	public Train(TrainTemplate trainTemplate)
	{
		this(trainTemplate.getName(),
			trainTemplate.getModelName(),
			trainTemplate.getManufacturer(),
			trainTemplate.getPrice(),
			trainTemplate.getCosts(),
			trainTemplate.getCostFactor(),
			trainTemplate.getMaxPassenger(),
			trainTemplate.getSpeed());
	}

	/**
	 * Sets the line of this train.
	 * 
	 * @param line The new line.
	 */
	public void setLine(TrainLine line)
	{
		Contract.EnsureNotNull(line);

		_trainLine = line;

		_currentNode = calcCurrentNode();
		_nextNode = calcNextNode();
		adjustTexture();
	}

	/**
	 * Sets the name of this train.
	 * The convention is <NAME>_<NUMBER> so please be sure that your train has this name.
	 * 
	 * @param newName The new name of the train from the format <NAME>_<NUMBER>.
	 */
	public void setName(String newName)
	{
		_name = newName;
	}

	/**
	 * @return Gets the current amount of passenger that are traveling with this train.
	 */
	public int getCurrPassenger()
	{
		return _currPassengers;
	}

	/**
	 * @return The train line this train is driving on.
	 */
	public TrainLine getLine()
	{
		return _trainLine;
	}

	/**
	 * Draws the train on its train line.
	 * 
	 * @param offset The offset of the map.
	 * @param baseNetSpacing The current base net spacing.
	 */
	public void draw(Point offset, int baseNetSpacing)
	{
		Point2D position = calcPosition();

		if(getTexture() == null)
		{
			Fill.setColor(Color.lightGray);
			Fill.Rect(
				(int)(offset.x + position.getX() * baseNetSpacing),
				(int)(offset.y + position.getY() * baseNetSpacing + ((_direction - 1) * 5)),
				10,
				10);
		}
		else
		{
			Draw.Image(getTexture(),
				new Rectangle(
					(int)(offset.x + position.getX() * baseNetSpacing) - baseNetSpacing / 2,
					(int)(offset.y + position.getY() * baseNetSpacing) - baseNetSpacing / 2,
					baseNetSpacing,
					baseNetSpacing),
				new Rectangle(
					0,
					_textureYOffset,
					64,
					64),
				baseNetSpacing / 2,
				baseNetSpacing / 2,
				_textureRotation);
		}
	}

	/**
	 * Moves the train based on the elapsed time since the last moving and its speed.
	 * Even if the train should not move, it's a good idea to call this method with a correct parameter in order to keep the time-calculation correct.
	 * 
	 * @param move When true the train will move, when false the train wont move and just wait.
	 * @param deltaTime The delta time (current time - last time of this method call) to determine the mved distance.
	 */
	public void drive(boolean move, float deltaTime)
	{
		if(move && System.nanoTime() >= _waitInStationSince)
		{
			if(_waitInStationSince != -1) // end of waiting --> update nodes and reset timer
			{
				updatePositionNodes();
				_waitInStationSince = -1;
			}

			if(_relativeOnLine >= _trainLine.getLength())
			{
				_relativeOnLine = (float)_trainLine.getLength();
				_direction *= -1;
			}
			else if(_relativeOnLine <= 0)
			{
				_relativeOnLine = 0.0f;
				_direction *= -1;
			}
			_relativeOnLine += getMovedDistance(deltaTime);

			if(!_currentNode.equals(calcCurrentNode())) // train passed a node and the angle might has changed
			{
				setChanged();
				notifyObservers();
			}
		}
	}

	/**
	 * Calculates new values for the current and next node of this train. It also updates the texture of the train.
	 */
	public void updatePositionNodes()
	{
		_currentNode = calcCurrentNode();
		_nextNode = calcNextNode();
		adjustTexture();
	}

	/**
	 * Calculates the rotation and position of the texture.
	 */
	private void adjustTexture()
	{
		Point currentNode = getCurrentNode(),
			nextNode = getNextNode();
		int directionMagicNumber = (nextNode.x - currentNode.x) + 10 * (nextNode.y - currentNode.y);

		switch(directionMagicNumber)
		{
			case 1:
				_textureYOffset = 64;
				_textureRotation = 0f;
				break;
			case -1:
				_textureYOffset = 0;
				_textureRotation = 0f;
				break;
			case -9:
				_textureYOffset = 64;
				_textureRotation = -45f;
				break;
			case 9:
				_textureYOffset = 0;
				_textureRotation = -45f;
				break;
			case 11:
				_textureYOffset = 64;
				_textureRotation = 45f;
				break;
			case -11:
				_textureYOffset = 0;
				_textureRotation = 45f;
				break;
			case -10:
				_textureYOffset = 128;
				_textureRotation = 0f;
				break;
			case 10:
				_textureYOffset = 192;
				_textureRotation = 0f;
		}
	}

	/**
	 * Calculates the distance of this train that it moved in the given time.
	 * 
	 * @param deltaTime The time where the train has moved.
	 * @return The distance in amount of baseNetSpacing units.
	 */
	private float getMovedDistance(float deltaTime)
	{
		return _direction * (_speed * (deltaTime / (float)1e9));
	}

	/**
	 * Calculates the position of the train considering the relative position on the line.
	 * 
	 * @return The coordinates of the train.
	 */
	public Point2D calcPosition()
	{
		// TODO change 0 to real start node of the train
		return _trainLine.getPositionOfTrain(_relativeOnLine, 0);
	}

	/**
	 * Calculates the node this train visited at last. This uses the {@code _ralitiveOnLine} value of this train.
	 * 
	 * @return The node this train is assigned to (the last visited node).
	 */
	public Point calcCurrentNode()
	{
		return getCurrentNode(0f);
	}

	/**
	 * Calculates the node this train will visit next. This uses the {@code _ralitiveOnLine} value of this train.
	 * 
	 * @return The node the train will visit next.
	 */
	public Point calcNextNode()
	{
		return getNextNode(0f);
	}

	/**
	 * Gets the last node this train passed. This does not calculate it, please use {@link #calcCurrentNode()} to update it.
	 * 
	 * @return The node this train is assigned to (the last visited node).
	 */
	public Point getCurrentNode()
	{
		return _currentNode;
	}

	/**
	 * Gets the node this train will visit next. This does not calculate it, please use {@link #calcNextNode()} to update it.
	 * 
	 * @return The node the train will visit next.
	 */
	public Point getNextNode()
	{
		return _nextNode;
	}

	/**
	 * Gets the two nodes that are around this train.
	 * 
	 * @param deltaTime The elapsed time since last move.
	 * @return The node this train is assigned to (the last visited node).
	 */
	public Point getCurrentNode(float deltaTime)
	{
		Contract.RequireNotNull(_trainLine);

		float relativeOnLine = _relativeOnLine + getMovedDistance(deltaTime);
		// TODO change 0 to real start node of the train
		return _trainLine.getNode(_direction > 0 ? relativeOnLine : _trainLine.getLength() - relativeOnLine,
			_direction > 0 ? 0 : _trainLine.getAmountOfNodes(),
			_direction)[0];
	}

	/**
	 * Gets the two nodes that are ahead this train.
	 * 
	 * @param deltaTime The elapsed time since last move.
	 * @return The node the train will visit next.
	 */
	public Point getNextNode(float deltaTime)
	{
		float relativeOnLine = _relativeOnLine + getMovedDistance(deltaTime);
		// TODO change 0 to real start node of the train
		return _trainLine.getNode(_direction > 0 ? relativeOnLine : _trainLine.getLength() - relativeOnLine,
			_direction > 0 ? 0 : _trainLine.getAmountOfNodes(),
			_direction)[1];
	}

	/**
	 * Saves the time when the train can move on. This is used in the {@link #drive(boolean, float)} method to prevent the train for driving.
	 * 
	 * @param duration The duration the train should wait at its position in milliseconds.
	 */
	public void waitFor(long duration)
	{
		_waitInStationSince = System.nanoTime() + duration * (long)1e6;
	}
}
