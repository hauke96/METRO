package metro.TrainManagement.Trains;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.GameState;
import metro.Graphics.Fill;
import metro.TrainManagement.Lines.TrainLine;

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
	private float _relativeOnLine;
	private int _currPassengers, _direction;
	protected TrainLine _trainLine;
	private long _lastRenderTime;

	/**
	 * Creates a new train with the following properties.
	 * Every data for the trains are in the ./data/trains.txt file and will be read & parsed by the GameState class.
	 * 
	 * @param name The model name of the train.
	 * @param manufacturer Manufacturer of the train.
	 * @param price Price for buying the train.
	 * @param costs Costs per month.
	 * @param costsFactor Monthly increase of the costs.
	 * @param passengers Maximum amount of passengers per train.
	 * @param speed The speed in nodes per second.
	 */
	public Train(String name, String manufacturer, int price, int costs, float costsFactor, int passengers, float speed)
	{
		super(name, manufacturer, price, costs, costsFactor, passengers, speed);
		_currPassengers = 0;
		_relativeOnLine = 0.0f;
		_direction = 1;
		_lastRenderTime = System.nanoTime();
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
		_trainLine = line;
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
	 * @param sp The sprite Batch to draw on.
	 * @param offset The offset of the map.
	 */
	public void draw(SpriteBatch sp, Point offset)
	{
		Point2D position = calcPosition();
		Fill.setColor(Color.lightGray);
		Fill.Rect(
			(int)(offset.x + position.getX() * GameState.getInstance().getBaseNetSpacing()),
			(int)(offset.y + position.getY() * GameState.getInstance().getBaseNetSpacing()),
			10,
			10);

		moveTrain();
	}

	private void moveTrain()
	{
		_relativeOnLine += _direction * (_speed / (_trainLine.getAmountOfNodes() - 1) * ((System.nanoTime() - _lastRenderTime) / (float)1e9));
		_lastRenderTime = System.nanoTime();
		
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
	}

	/**
	 * Calculates the position of the train considering the relative position on the line.
	 * 
	 * @return The coordinates of the train.
	 */
	private Point2D calcPosition()
	{
		// TODO change 0 to real start node
		Point2D p = _trainLine.getPositionOfTrain(_relativeOnLine, 0);
		return p;
	}
}
