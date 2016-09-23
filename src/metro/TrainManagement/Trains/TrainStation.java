package metro.TrainManagement.Trains;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.Common.Game.GameState;
import metro.Graphics.Draw;
import metro.Graphics.Fill;

/**
 * A train station with the amount of waiting passengers and a list of connected stations. This class also manages the drawing stuff.
 * 
 * @author hauke
 *
 */

public class TrainStation
{
	private int _waitingPassengers,
		_movingPassengerWaitingTime,
		_randomAddPassengerWaitingTime;
	private Point _position; // Position
	private GameState _gameState;
	private int[] _movingPassengersAmount,
		_movingPassengersDuration;
	private long _movingPassengerLastCall,
		_randomAddPassengerLastCall;

	/**
	 * The price to build one train station.
	 */
	public static int __price = 5000;

	/**
	 * Creates a new train station.
	 * 
	 * @param position The position in fields (not in pixel).
	 */
	public TrainStation(Point position)
	{
		this(position, 0);
	}

	/**
	 * Creates a new train station.
	 * 
	 * @param position The position in fields (not in pixel).
	 * @param waitingPassengers The amount of waiting passengers.
	 */
	public TrainStation(Point position, int waitingPassengers)
	{
		_waitingPassengers = waitingPassengers;
		_position = position;
		_gameState = GameState.getInstance();

		_randomAddPassengerWaitingTime = (int)750e6; // 750ms == 750*10^6 ns
		_randomAddPassengerLastCall = 0;

		_movingPassengerWaitingTime = (int)250e6; // 250ms == 250*10^6 ns
		_movingPassengersAmount = new int[8]; // there can never be more then 8 trains in one station
		_movingPassengersDuration = new int[8];
		_movingPassengerLastCall = 0;
	}

	/**
	 * Draws the station with passenger amount.
	 * 
	 * @param sp The SpriteBatch to draw on
	 * @param offset The offset of the current game screen.
	 */
	public void draw(SpriteBatch sp, Point offset)
	{
		Point position = new Point(offset.x + _position.x * _gameState.getBaseNetSpacing() - 4,
			offset.y + _position.y * _gameState.getBaseNetSpacing() - 7); // Position with offset etc.
		Fill.setColor(Color.white);
		Fill.Rect(position.x, position.y, 8, 15);
		Draw.setColor(Color.black);
		Draw.Rect(position.x, position.y, 8, 15);

		Draw.String("" + _waitingPassengers,
			offset.x + _position.x * _gameState.getBaseNetSpacing() - Draw.getStringSize("" + _waitingPassengers).width / 2 - 1,
			offset.y + _position.y * _gameState.getBaseNetSpacing() - 25);
	}

	/**
	 * Returns the position with offset.
	 * 
	 * @param offset The offset of the current game screen.
	 * @return The position with offset.
	 */
	public Point getPositionOnScreen(Point offset)
	{
		return new Point(offset.x + _position.x * _gameState.getBaseNetSpacing(),
			offset.y + _position.y * _gameState.getBaseNetSpacing()); // Position with offset etc.
	}

	/**
	 * @return The position of this station in baseNetSpacing-units.
	 */
	public Point getPosition()
	{
		return _position;
	}

	/**
	 * Adds a job for the station to move some passengers. The amount and the amount of time for this job can be specified.
	 * Moving passengers is handled by the {@link #handlePassenger()} method.
	 * 
	 * @param amount The amount of passenger to move.
	 * @param duration The duration of the job in nanoseconds.
	 */
	public void movePassenger(int amount, long duration)
	{
		for(int i = 0; i < _movingPassengersAmount.length; ++i)
		{
			if(_movingPassengersDuration[i] == 0) // free slot in array
			{
				_movingPassengersDuration[i] = (int)(duration / _movingPassengerWaitingTime); // amount of iterations until the job is done.
				_movingPassengersAmount[i] = amount / (int)_movingPassengersDuration[i];
				break;
			}
		}
	}

	/**
	 * Increases/decreases the amount of passenger according to the creates jobs via the {@link #movePassenger(int, long)} method.
	 */
	public void handlePassenger()
	{
		long thisCall = System.nanoTime();
		if(System.nanoTime() > _movingPassengerLastCall + _movingPassengerWaitingTime)
		{
			for(int i = 0; i < _movingPassengersAmount.length && _waitingPassengers > 0; ++i)
			{
				if(_movingPassengersDuration[i] != 0) // valid entry/running job.
				{
					if(_waitingPassengers >= _movingPassengersAmount[i])
					{
						_waitingPassengers -= _movingPassengersAmount[i];
					}
					else
					{
						_waitingPassengers = 0;
					}
					_movingPassengersDuration[i]--; // automatically becomes 0 --> free array field for new job
				}
			}
			_movingPassengerLastCall = thisCall;
		}
	}

	/**
	 * Adds a random amount of passenger between 0 and 5 * (citizenDensity + 1).
	 * 
	 * @param citizenDensity The city density of this station.
	 */
	public void addRandomPassenger(int citizenDensity)
	{
		citizenDensity++; // there's also the 0-density but there should be some passenger there as well ;)
		long thisCall = System.nanoTime();
		if(System.nanoTime() > _randomAddPassengerLastCall + _randomAddPassengerWaitingTime)
		{
			_waitingPassengers += new Random().nextInt(5 * citizenDensity);
			_randomAddPassengerLastCall = thisCall;
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		Point pos = null;
		if(obj instanceof Point)
		{
			pos = (Point)obj;
		}
		else if(obj instanceof TrainStation)
		{
			pos = ((TrainStation)obj).getPosition();
		}

		return _position.equals(pos);
	}
}
