package metro.Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import metro.Game.GameScreen_CityView;
import metro.Game.METRO;



public class CityTravelerSpot
{
	public static int _circleRadiusStep = 25*2; // difference between the circles radius
	private Point _position;
	private int _strength;
	
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
	public void draw(Graphics2D g, int layerIndex, boolean circleSelected, boolean drawNumbers)
	{
		draw(g, layerIndex, circleSelected, drawNumbers, false);
	}
	/**
	 * Draws the circles of the Hotspot.
	 * @param g The graphic handle to draw an.
	 * @param layerIndex Index of the circle to draw
	 * @param circleSelected If the circle is selected and therefore drawn in a different color.
	 * @param drawNumbers if numbers should be drawn on circle
	 * @param onlyEdges If only edges should be drawn
	 */
	public void draw(Graphics2D g, int layerIndex, boolean circleSelected, boolean drawNumbers, boolean onlyEdges)
	{
		layerIndex = _strength - layerIndex;
		if(layerIndex < -1) return;
		
		int gray = 255 - 25 * (_strength - layerIndex + 1); // gray color based on layer
		
		// get the position with offset
		Point position = new Point(_position.x + (int)GameScreen_CityView._offset.getX(), _position.y + (int)GameScreen_CityView._offset.getY());

		if(circleSelected)
		{
			g.setColor(new Color(200 - (int)(2f * (_strength - layerIndex)), 
					220 - (int)(1.5f * (_strength - layerIndex)), 
					255));
			g.fillOval(position.x - layerIndex *_circleRadiusStep + 1, 
				position.y - layerIndex * _circleRadiusStep + 1,
				_circleRadiusStep * 2 * layerIndex - 2, 
				_circleRadiusStep * 2 * layerIndex - 2);

			g.setColor(METRO.__metroBlue);
			g.setColor(new Color(70, 126, 179));
			if(drawNumbers && layerIndex > 2)
			{
				g.drawString(_strength - layerIndex + "", position.x + _circleRadiusStep * layerIndex - _circleRadiusStep / 2 - 5, position.y + 4);
				g.drawString(_strength - layerIndex + "", position.x - _circleRadiusStep * layerIndex + _circleRadiusStep / 2 - 1, position.y + 4);
				
				g.drawString(_strength - layerIndex + "", position.x - 3, position.y + _circleRadiusStep * layerIndex - _circleRadiusStep / 2 + 5);
				g.drawString(_strength - layerIndex + "", position.x - 3, position.y - _circleRadiusStep * layerIndex + _circleRadiusStep - 15);
			}
			else if (layerIndex <= 2 && drawNumbers) 
			{
				g.drawString((_strength - 1) + "", position.x, position.y + 4);
			}
		}
		else if(!onlyEdges)
		{
			gray = 255 - 5 * (_strength - layerIndex + 1); // gray color based on layer
			g.setColor(new Color(gray, gray, gray));
			g.fillOval(position.x - layerIndex *_circleRadiusStep + 1,
				position.y - layerIndex * _circleRadiusStep + 1,
				_circleRadiusStep * 2 * layerIndex - 2,
				_circleRadiusStep * 2 * layerIndex - 2);
		}
		if(onlyEdges)
		{
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(new Color(gray, gray, gray));
			g.drawOval(position.x - layerIndex *_circleRadiusStep,
				position.y - layerIndex * _circleRadiusStep,
				_circleRadiusStep * 2 * layerIndex,
				_circleRadiusStep * 2 * layerIndex);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}
	public float getStrength()
	{
		return _strength;
	}
}
