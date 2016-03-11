package metro.TrainManagement.Trains;

import java.awt.Color;
import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.GameState;
import metro.METRO;
import metro.Graphics.Draw;
import metro.Graphics.Fill;

/**
 * A traveler spot is a center where many people/traveler are. This will effect its environment so there are areas around it with a higher amount of traveler.
 * 
 * @author hauke
 *
 */

public class TravelerSpot
{
	private Point _position;
	private int _strength;
	private GameState _gameState;

	private static int __scale = 2;

	/**
	 * Creates a new traveler spot on a specific position an with a certain strength.
	 * 
	 * @param position Position of the center of the spot in pixel.
	 * @param strength The strength (usually from 0 to 10).
	 */
	public TravelerSpot(Point position, int strength)
	{
		_position = position;
		_strength = strength > 10 ? 10 : strength;
		_gameState = GameState.getInstance();
	}

	/**
	 * Checks if mouse is in ONLY this circle or greater circles (but not in smaller/next ones).
	 * 
	 * @param layerIndex The circle index.
	 * @param offset The current map offset.
	 * @return Boolean if mouse is in circle.
	 */
	public boolean isMouseInCircle(int layerIndex, Point offset)
	{
		layerIndex = (int)_strength - layerIndex;
		if(layerIndex < 0) return false;

		Point position = new Point(_position.x * _gameState.getBaseNetSpacing() + offset.x,
			_position.y * _gameState.getBaseNetSpacing() + offset.y);

		boolean isInCurrentCircle = Math.pow(METRO.__mousePosition.x - position.x, 2)
			+ Math.pow(METRO.__mousePosition.y - position.y, 2) < Math.pow(_gameState.getBaseNetSpacing() * __scale * layerIndex, 2); // true: Mouse cursor is in circle
		boolean isInNextCircle = Math.pow(METRO.__mousePosition.x - position.x, 2)
			+ Math.pow(METRO.__mousePosition.y - position.y, 2) < Math.pow(_gameState.getBaseNetSpacing() * __scale * (layerIndex - 1), 2); // true: Mouse cursor is in circle

		return isInCurrentCircle && !isInNextCircle;
	}

	/**
	 * Draws the circles of the hot-spot.
	 * 
	 * @param sp The SpriteBatch to draw on.
	 * @param layerIndex Index of the circle to draw
	 * @param circleSelected If the circle is selected and therefore drawn in a different color.
	 * @param onlyEdges If only edges should be drawn
	 * @param offset The current map offset.
	 */
	public void draw(SpriteBatch sp, int layerIndex, boolean circleSelected, boolean onlyEdges, Point offset)
	{
		int circleRadius = _gameState.getBaseNetSpacing() * __scale;

		layerIndex = _strength - layerIndex;
		if(layerIndex < -1) return;

		// get the position with offset
		Point position = new Point(_position.x * _gameState.getBaseNetSpacing() + offset.x + 1,
			_position.y * _gameState.getBaseNetSpacing() + offset.y + 1);

		if(circleSelected)
		{
			int gray = (int)(255 - 2 * (_strength - layerIndex + 1) - 8); // gray color based on layer
			Fill.setColor(new Color(gray, gray, gray));
			Fill.Circle(position.x - layerIndex * circleRadius + 1,
				position.y - layerIndex * circleRadius + 1,
				circleRadius * 2 * layerIndex - 3);
		}
		else
		{
			int gray = 255 - 5 * (_strength - layerIndex + 1); // gray color based on layer
			gray = 255;
			Fill.setColor(new Color(gray, gray, gray));
			Fill.Circle(position.x - layerIndex * circleRadius + 1,
				position.y - layerIndex * circleRadius + 1,
				circleRadius * 2 * layerIndex - 3);
		}
		if(onlyEdges)
		{
			int gray = 255 - 9 * (_strength - layerIndex + 1) - 30; // gray color based on layer
			Draw.setColor(new Color(gray, gray, gray));
			Draw.Circle(position.x - layerIndex * circleRadius,
				position.y - layerIndex * circleRadius,
				circleRadius * 2 * layerIndex - 1);
		}
	}

	/**
	 * Gets the strength of the TravelerSpot
	 * 
	 * @return Strength
	 */
	public float getStrength()
	{
		return _strength;
	}
}
