package metro.TrainManagement.Trains;

import java.awt.Color;
import java.awt.Point;

import metro.GameState;
import metro.Graphics.Draw;
import metro.Graphics.Fill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A train station with the amount of waiting passengers and a list of connected stations. This class also manages the drawing stuff.
 * 
 * @author hauke
 *
 */

public class TrainStation
{
	private int _waitingPassengers;
	private Point _position; // Position
	private GameState _gameState;

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
}
