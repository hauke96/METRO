package metro.Game;

import java.awt.Color;
import java.awt.Point;

import metro.Graphics.Draw;
import metro.Graphics.Fill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A traveler spot is a center where many people/traveler are. This will effect its environment so there are areas around it with a higher amount of traveler.
 * @author hauke
 *
 */

public class CityTravelerSpot
{
	public static int _circleRadiusStep = 25*2; // difference between the circles radius
	private Point _position;
	private int _strength;
	
	/**
	 * Creates a new traveler spot on a specific position an with a certain strength.
	 * @param position Position of the center of the spot in pixel.
	 * @param strength The strength (usually from 0 to 10).
	 */
	public CityTravelerSpot(Point position, int strength)
	{
		_position = position;
		_strength = strength;
	}
	
	/**
	 * Checks if mouse is in ONLY this circle or greater circles (but not in smaller/next ones).
	 * @param layerIndex The circle index.
	 * @return Boolean if mouse is in circle.
	 */
	public boolean isMouseInCircle(int layerIndex)
	{
		layerIndex = (int)_strength - layerIndex;
		if(layerIndex < 0) return false;
		
		Point position = new Point(_position.x + (int)GameScreen_CityView._offset.getX(), _position.y + (int)GameScreen_CityView._offset.getY());

		boolean isInCurrentCircle = Math.pow(METRO.__mousePosition.x - position.x, 2) 
				+ Math.pow(METRO.__mousePosition.y - position.y, 2) 
				< Math.pow(_circleRadiusStep * layerIndex, 2); // true: Mouse cursor is in circle
		boolean isInNextCircle = Math.pow(METRO.__mousePosition.x - position.x, 2) 
				+ Math.pow(METRO.__mousePosition.y - position.y, 2) 
				< Math.pow(_circleRadiusStep * (layerIndex - 1), 2); // true: Mouse cursor is in circle
		
		return isInCurrentCircle && !isInNextCircle;
	}
	
	/**
	 * Draws the circles of the Hotspot.
	 * @param g The graphic handle to draw an.
	 * @param layerIndex Index of the circle to draw
	 * @param circleSelected If the circle is selected and therefore drawn in a different color.
	 * @param drawNumbers if numbers should be drawn on circle
	 */
	public void draw(SpriteBatch sp, int layerIndex, boolean circleSelected, boolean drawNumbers)
	{
		draw(sp, layerIndex, circleSelected, drawNumbers, false);
	}
	
	/**
	 * Draws the circles of the Hotspot.
	 * @param g The graphic handle to draw an.
	 * @param layerIndex Index of the circle to draw
	 * @param circleSelected If the circle is selected and therefore drawn in a different color.
	 * @param drawNumbers if numbers should be drawn on circle
	 * @param onlyEdges If only edges should be drawn
	 */
	public void draw(SpriteBatch sp, int layerIndex, boolean circleSelected, boolean drawNumbers, boolean onlyEdges)
	{
		layerIndex = _strength - layerIndex;
		if(layerIndex < -1) return;
		
		int gray = 255 - 25 * (_strength - layerIndex + 1); // gray color based on layer
		
		// get the position with offset
		Point position = new Point(_position.x + (int)GameScreen_CityView._offset.getX(), _position.y + (int)GameScreen_CityView._offset.getY());

		if(circleSelected)
		{
			Fill.setColor(new Color(200 - (int)(2f * (_strength - layerIndex)), 
				220 - (int)(1.5f * (_strength - layerIndex)), 
				255));
			Fill.Circle(position.x - layerIndex *_circleRadiusStep + 1, 
				position.y - layerIndex * _circleRadiusStep + 1,
				_circleRadiusStep * 2 * layerIndex - 2);
			
			Draw.setColor(new Color(70, 126, 179));
			if(drawNumbers && layerIndex > 1)
			{
				Draw.String(_strength - layerIndex + "", position.x + _circleRadiusStep * layerIndex - _circleRadiusStep / 2 - 5, position.y - 6); // right
				Draw.String(_strength - layerIndex + "", position.x - _circleRadiusStep * layerIndex + _circleRadiusStep / 2 - 1, position.y - 6); // left
				
				Draw.String(_strength - layerIndex + "", position.x - 3, position.y + _circleRadiusStep * layerIndex - _circleRadiusStep / 2 - 5); // bottom
				Draw.String(_strength - layerIndex + "", position.x - 3, position.y - _circleRadiusStep * layerIndex + _circleRadiusStep - 28); // top
			}
			else if (layerIndex <= 1 && drawNumbers) // draw only one number on last circle
			{
				Draw.String((_strength - 1) + "", position.x - 3, position.y - 5);
			}
		}
		else if(!onlyEdges)
		{
			gray = 255 - 5 * (_strength - layerIndex + 1); // gray color based on layer
			Fill.setColor(new Color(gray, gray, gray));
			Fill.Circle(position.x - layerIndex *_circleRadiusStep + 1,
				position.y - layerIndex * _circleRadiusStep + 1,
				_circleRadiusStep * 2 * layerIndex - 2);
		}
		if(onlyEdges)
		{
			Draw.setColor(new Color(gray, gray, gray));
			Draw.Circle(position.x - layerIndex *_circleRadiusStep,
				position.y - layerIndex * _circleRadiusStep,
				_circleRadiusStep * 2 * layerIndex);
		}
	}
	
	/**
	 * Gets the strength of the TravelerSpot
	 * @return Strength
	 */
	public float getStrength()
	{
		return _strength;
	}
}
