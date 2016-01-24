package metro.GameScreen.MainView.CityView;

import java.awt.Color;
import java.awt.Point;

import metro.METRO;
import metro.Graphics.Draw;
import metro.Graphics.Fill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A traveler spot is a center where many people/traveler are. This will effect its environment so there are areas around it with a higher amount of traveler.
 * 
 * @author hauke
 *
 */

public class CityTravelerSpot
{
	/**
	 * The difference between two circles in pixel.
	 */
	public static int _circleRadiusStep = 25 * 2;
	private Point _position;
	private int _strength;

	/**
	 * Creates a new traveler spot on a specific position an with a certain strength.
	 * 
	 * @param position Position of the center of the spot in pixel.
	 * @param strength The strength (usually from 0 to 10).
	 */
	public CityTravelerSpot(Point position, int strength)
	{
		_position = position;
		_strength = strength > 10 ? 10 : strength;
	}

	/**
	 * Checks if mouse is in ONLY this circle or greater circles (but not in smaller/next ones).
	 * 
	 * @param layerIndex The circle index.
	 * @return Boolean if mouse is in circle.
	 */
	public boolean isMouseInCircle(int layerIndex)
	{
		layerIndex = (int)_strength - layerIndex;
		if(layerIndex < 0) return false;

		Point position = new Point(_position.x + (int)CityView._offset.getX(), _position.y + (int)CityView._offset.getY());

		boolean isInCurrentCircle = Math.pow(METRO.__mousePosition.x - position.x, 2)
			+ Math.pow(METRO.__mousePosition.y - position.y, 2) < Math.pow(_circleRadiusStep * layerIndex, 2); // true: Mouse cursor is in circle
		boolean isInNextCircle = Math.pow(METRO.__mousePosition.x - position.x, 2)
			+ Math.pow(METRO.__mousePosition.y - position.y, 2) < Math.pow(_circleRadiusStep * (layerIndex - 1), 2); // true: Mouse cursor is in circle

		return isInCurrentCircle && !isInNextCircle;
	}

	/**
	 * Draws the circles of the Hotspot.
	 * 
	 * @param g The graphic handle to draw an.
	 * @param layerIndex Index of the circle to draw
	 * @param circleSelected If the circle is selected and therefore drawn in a different color.
	 * @param drawNumbers if numbers should be drawn on circle
	 */
	// public void draw(SpriteBatch sp, int layerIndex, boolean circleSelected, boolean drawNumbers)
	// {
	// draw(sp, layerIndex, circleSelected, drawNumbers, false);
	// }

	/**
	 * Draws the circles of the hot-spot.
	 * 
	 * @param sp The SpriteBatch to draw on.
	 * @param layerIndex Index of the circle to draw
	 * @param circleSelected If the circle is selected and therefore drawn in a different color.
	 * @param onlyEdges If only edges should be drawn
	 */
	public void draw(SpriteBatch sp, int layerIndex, boolean circleSelected, boolean onlyEdges)
	{
		layerIndex = _strength - layerIndex;
		if(layerIndex < -1) return;

		// get the position with offset
		Point position = new Point(_position.x + (int)CityView._offset.getX() + 1, _position.y + (int)CityView._offset.getY() + 1);

		if(circleSelected)
		{
			int gray = (int)(255 - 2 * (_strength - layerIndex + 1) - 8); // gray color based on layer
			Fill.setColor(new Color(gray, gray, gray));
			Fill.Circle(position.x - layerIndex * _circleRadiusStep + 1,
				position.y - layerIndex * _circleRadiusStep + 1,
				_circleRadiusStep * 2 * layerIndex - 3);
		}
		else
		{
			int gray = 255 - 5 * (_strength - layerIndex + 1); // gray color based on layer
			gray = 255;
			Fill.setColor(new Color(gray, gray, gray));
			Fill.Circle(position.x - layerIndex * _circleRadiusStep + 1,
				position.y - layerIndex * _circleRadiusStep + 1,
				_circleRadiusStep * 2 * layerIndex - 3);
		}
		if(onlyEdges)
		{
			int gray = 255 - 9 * (_strength - layerIndex + 1) - 30; // gray color based on layer
			Draw.setColor(new Color(gray, gray, gray));
			Draw.Circle(position.x - layerIndex * _circleRadiusStep,
				position.y - layerIndex * _circleRadiusStep,
				_circleRadiusStep * 2 * layerIndex - 1);
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
