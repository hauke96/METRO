package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;

import Game.GameScreen_CityView;
import Game.METRO;


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
	 * @param circleIndex The circle index.
	 * @return Boolean if mouse is in circle.
	 */
	public boolean isMouseInCircle(int circleIndex)
	{
		circleIndex = _strength - circleIndex;
		if(circleIndex < 0) return false;
		
		Point position = new Point(_position.x + (int)GameScreen_CityView._offset.getX(), _position.y + (int)GameScreen_CityView._offset.getY());

		boolean isInCurrentCircle = Math.pow(MouseInfo.getPointerInfo().getLocation().x - position.x, 2) 
				+ Math.pow(MouseInfo.getPointerInfo().getLocation().y - position.y, 2) 
				< Math.pow(_circleRadiusStep * circleIndex, 2); // true: Mouse cursor is in circle
		boolean isInNextCircle = Math.pow(MouseInfo.getPointerInfo().getLocation().x - position.x, 2) 
				+ Math.pow(MouseInfo.getPointerInfo().getLocation().y - position.y, 2) 
				< Math.pow(_circleRadiusStep * (circleIndex - 1), 2); // true: Mouse cursor is in circle
		
		return isInCurrentCircle && !isInNextCircle;
	}
	/**
	 * Draws the circles of the Hotspot.
	 * @param g The graphic handle to draw an.
	 * @param circleIndex Index of the circle to draw
	 * @param circleSelected If the circle is selected and therefore drawn in a different color.
	 * @param drawNumbers if numbers should be drawn on circle
	 */
	public void draw(Graphics2D g, int circleIndex, boolean circleSelected, boolean drawNumbers)
	{
		draw(g, circleIndex, circleSelected, drawNumbers, false);
	}
	/**
	 * Draws the circles of the Hotspot.
	 * @param g The graphic handle to draw an.
	 * @param circleIndex Index of the circle to draw
	 * @param circleSelected If the circle is selected and therefore drawn in a different color.
	 * @param drawNumbers if numbers should be drawn on circle
	 * @param onlyEdges If only edges should be drawn
	 */
	public void draw(Graphics2D g, int circleIndex, boolean circleSelected, boolean drawNumbers, boolean onlyEdges)
	{
		circleIndex = _strength - circleIndex;
		if(circleIndex < 0) return;
		
		// get the position with offset
		Point position = new Point(_position.x + (int)GameScreen_CityView._offset.getX(), _position.y + (int)GameScreen_CityView._offset.getY());

		if(circleSelected)
		{
			g.setColor(new Color(230 - 5 * (_strength - circleIndex), 240 - (int)(4f * (float)(_strength - circleIndex)), 255));
			g.fillOval(position.x - circleIndex *_circleRadiusStep + 1, 
					position.y - circleIndex * _circleRadiusStep + 1,
					_circleRadiusStep * 2 * circleIndex - 1, 
					_circleRadiusStep * 2 * circleIndex - 1);

			g.setColor(METRO.__metroBlue);
			g.setColor(new Color(70, 126, 179));
			if(drawNumbers && circleIndex > 1)
			{
				g.drawString((_strength - circleIndex) + "", position.x + _circleRadiusStep * circleIndex - _circleRadiusStep / 2 - 5, position.y + 4);
				g.drawString((_strength - circleIndex) + "", position.x - _circleRadiusStep * circleIndex + _circleRadiusStep / 2 - 1, position.y + 4);
				
				g.drawString((_strength - circleIndex) + "", position.x - 3, position.y + _circleRadiusStep * circleIndex - _circleRadiusStep / 2 + 5);
				g.drawString((_strength - circleIndex) + "", position.x - 3, position.y - _circleRadiusStep * circleIndex + _circleRadiusStep - 15);
			}
			else if (circleIndex == 1 && drawNumbers) 
			{
				g.drawString((_strength - 1) + "", position.x, position.y + 4);
			}
		}
		else
		{
			int gray = 255 - 25 * (_strength - circleIndex + 1); // gray color based on layer
			if(onlyEdges)
			{
				g.setColor(new Color(gray, gray, gray));
				g.drawOval(position.x - circleIndex *_circleRadiusStep, 
						position.y - circleIndex * _circleRadiusStep,
						_circleRadiusStep * 2 * circleIndex, 
						_circleRadiusStep * 2 * circleIndex);
			}
			else
			{
				gray = 255 - 5 * (_strength - circleIndex + 1); // gray color based on layer
				g.setColor(new Color(gray, gray, gray));
				g.fillOval(position.x - circleIndex *_circleRadiusStep + 1, 
						position.y - circleIndex * _circleRadiusStep + 1,
						_circleRadiusStep * 2 * circleIndex - 1, 
						_circleRadiusStep * 2 * circleIndex - 1);
			}
		}
	}
	public int getStrength()
	{
		return _strength;
	}
}
