package Game;

import java.awt.Color;
import java.awt.Graphics;
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
	 */
	public void draw(Graphics g, int circleIndex, boolean circleSelected, boolean drawNumbers)
	{
		circleIndex = _strength - circleIndex;
		if(circleIndex < 0) return;
		
		Point position = new Point(_position.x + (int)GameScreen_CityView._offset.getX(), _position.y + (int)GameScreen_CityView._offset.getY());

		int gray = 255 - 25 * (_strength - circleIndex + 1);
		g.setColor(new Color(gray, gray, gray));
		g.fillOval(position.x - circleIndex *_circleRadiusStep, 
				position.y - circleIndex * _circleRadiusStep,
				_circleRadiusStep * 2 * circleIndex, 
				_circleRadiusStep * 2 * circleIndex);
		
		if(circleSelected)
		{
			g.setColor(new Color(100, 180, 255, 40)); //TODO just do one draw with blue-gray-color instead of this if... shit
			g.fillOval(position.x - circleIndex *_circleRadiusStep, 
					position.y - circleIndex * _circleRadiusStep,
					_circleRadiusStep * 2 * circleIndex, 
					_circleRadiusStep * 2 * circleIndex);
			g.setColor(new Color(0, 0, 200));
		}

		g.setColor(METRO.__metroBlue);
		if(circleIndex > 1 && drawNumbers)
		{
			g.drawString((_strength - circleIndex) + "", position.x + _circleRadiusStep * circleIndex - _circleRadiusStep / 2 - 4, position.y + 4);
			g.drawString((_strength - circleIndex) + "", position.x - _circleRadiusStep * circleIndex + _circleRadiusStep / 2, position.y + 4);
			
			g.drawString((_strength - circleIndex) + "", position.x, position.y + _circleRadiusStep * circleIndex - _circleRadiusStep / 2 + 6);
			g.drawString((_strength - circleIndex) + "", position.x, position.y - _circleRadiusStep * circleIndex + _circleRadiusStep - 14);
		}
		else if (circleIndex == 1 && drawNumbers) 
		{
			g.drawString((_strength - 1) + "", position.x, position.y + 4);
		}

		g.setColor(Color.white);
	}
}
