package metro.TrainManagement.Trains;

import java.awt.Color;
import java.awt.Point;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;

/**
 * A traveler spot is a center where many people/traveler are. This will effect its environment so there are areas around it with a higher amount of traveler.
 * 
 * @author hauke
 *
 */
public class TravelerSpot
{
	private Point	_position;
	private int		_strength;
	
	private static int __scale = 2;
	
	/**
	 * Creates a new traveler spot on a specific position an with a certain strength.
	 * 
	 * @param position
	 *            Position of the center of the spot in pixel.
	 * @param strength
	 *            The strength (usually from 0 to 10).
	 */
	public TravelerSpot(Point position, int strength)
	{
		_position = position;
		_strength = strength > 10 ? 10 : strength;
	}
	
	/**
	 * Checks if mouse is in ONLY this circle or greater circles (but not in smaller/next ones).
	 * 
	 * @param layerIndex
	 *            The circle index.
	 * @param offset
	 *            The current map offset.
	 * @param baseNetSpacing
	 *            The current base net spacing.
	 * @return Boolean if mouse is in circle.
	 */
	public boolean isMouseInCircle(int layerIndex, Point offset, int baseNetSpacing)
	{
		layerIndex = (int) _strength - layerIndex;
		if (layerIndex < 0) return false;
		
		Point position = new Point(_position.x * baseNetSpacing + offset.x, _position.y * baseNetSpacing + offset.y);
		
		boolean isInCurrentCircle = Math.pow(METRO.__mousePosition.x - position.x, 2)
		        + Math.pow(METRO.__mousePosition.y - position.y, 2) < Math.pow(baseNetSpacing * __scale * layerIndex, 2); // true: Mouse cursor is in circle
		boolean isInNextCircle = Math.pow(METRO.__mousePosition.x - position.x, 2)
		        + Math.pow(METRO.__mousePosition.y - position.y, 2) < Math.pow(baseNetSpacing * __scale * (layerIndex - 1), 2); // true: Mouse cursor is in circle
		
		return isInCurrentCircle && !isInNextCircle;
	}
	
	/**
	 * Draws the circles of the hot-spot.
	 * 
	 * @param layerIndex
	 *            Index of the circle to draw
	 * @param circleSelected
	 *            If the circle is selected and therefore drawn in a different color.
	 * @param onlyEdges
	 *            If only edges should be drawn
	 * @param offset
	 *            The current map offset.
	 * @param baseNetSpacing
	 *            The current base net spacing.
	 */
	public void draw(int layerIndex, boolean circleSelected, boolean onlyEdges, Point offset, int baseNetSpacing)
	{
		int circleRadius = baseNetSpacing * __scale;
		
		layerIndex = _strength - layerIndex;
		if (layerIndex < -1) return;
		
		// get the position with offset
		Point position = new Point(_position.x * baseNetSpacing + offset.x + 1, _position.y * baseNetSpacing + offset.y + 1);
		
		if (circleSelected)
		{
			int gray = (int) (255 - 2 * (_strength - layerIndex + 1) - 8); // gray color based on layer
			Fill.setColor(new Color(gray, gray, gray));
			Fill.Circle(position.x - layerIndex * circleRadius + 1, position.y - layerIndex * circleRadius + 1, circleRadius * 2 * layerIndex - 3);
		}
		else
		{
			int gray = 255 - 5 * (_strength - layerIndex + 1); // gray color based on layer
			gray = 255;
			Fill.setColor(new Color(gray, gray, gray));
			Fill.Circle(position.x - layerIndex * circleRadius + 1, position.y - layerIndex * circleRadius + 1, circleRadius * 2 * layerIndex - 3);
		}
		if (onlyEdges)
		{
			int gray = 255 - 9 * (_strength - layerIndex + 1) - 30; // gray color based on layer
			Draw.setColor(new Color(gray, gray, gray));
			Draw.Circle(position.x - layerIndex * circleRadius, position.y - layerIndex * circleRadius, circleRadius * 2 * layerIndex - 1);
		}
	}
	
	/**
	 * Gets the strength of the TravelerSpot
	 * 
	 * @return Strength The strength of this traveler spot.
	 */
	public int getStrength()
	{
		return _strength;
	}
	
	/**
	 * Gets the strength of the TravelerSpot
	 * 
	 * @param pos
	 *            The position that strength you want to know. If this point is not in the spot, the result will be 0.
	 * 
	 * @return Strength The strength at this point. 0 when the point is not in the circle.
	 */
	public int getStrength(Point pos)
	{
		for (int i = 0; i < _strength; ++i) // go through all layers
		{
			boolean isInCurrentCircle = Math.pow(pos.x - _position.x, 2)
			        + Math.pow(pos.y - _position.y, 2) < Math.pow(__scale * i, 2);
			
			boolean isInNextCircle = Math.pow(pos.x - _position.x, 2)
			        + Math.pow(pos.y - _position.y, 2) < Math.pow(__scale * (i - 1), 2);
			
			if (isInCurrentCircle && !isInNextCircle)
			{
				return _strength - i;
			}
		}
		return 0;
	}
	
	/**
	 * Check if the given point is in this circle.
	 * 
	 * @param pos
	 *            The position to check.
	 * @return True when the given point is in this circle, false if not.
	 */
	public boolean contains(Point pos)
	{
		return Math.sqrt((pos.x - _position.x) ^ 2 + (pos.y - _position.y) ^ 2) <= __scale * _strength;
	}
}
