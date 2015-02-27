import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

/**
 * 
 */

/**
 * @author Hauke
 *
 */
public class GameScreen_CityView implements GameScreen
{
	public static GameScreen _trainGameScreen;
	public static Point2D _offset = new Point2D.Float(0, 0);
	
	private List<CityTravelerSpot> _travelerSpots = new ArrayList<CityTravelerSpot>(); 
	private Point _oldMousePos; // Mouse position from last frame
	private boolean _dragMode = false;

	/**
	 * Constructor to load all the important stuff
	 */
	public GameScreen_CityView()
	{
		_travelerSpots.add(new CityTravelerSpot(new Point(500, 500), 6));
		_travelerSpots.add(new CityTravelerSpot(new Point(700, 700), 4));
	}

	/* (non-Javadoc)
	 * @see GameScreen#update(java.awt.Graphics2D)
	 */
	@Override
	public void update(Graphics2D g)
	{
		if(_dragMode)
		{
			_offset = new Point2D.Float((float)_offset.getX() + (MouseInfo.getPointerInfo().getLocation().x - _oldMousePos.x) * 2f,
				(float)_offset.getY() + (MouseInfo.getPointerInfo().getLocation().y - _oldMousePos.y) * 2f);
		}
		
		drawCircles(g);	
		
		_oldMousePos = MouseInfo.getPointerInfo().getLocation();
	}
	/**
	 * Draws the traveler-circles.
	 * @param g The graphic handle to draw on
	 */
	private void drawCircles(Graphics2D g)
	{
		int circleNumber = -1; // the circle, the mouse is in. -1 means: Mouse out of any circle.
		
		for(int i = 0; i < 10; i++)
		{
			boolean isIndexSelected = false; // is this layer selected -> true -> draw whole layer in blue
			for(CityTravelerSpot cSpot : _travelerSpots)
			{
				isIndexSelected |= cSpot.isMouseInCircle(i); // if mouse is in ANY circle of this layer
				if(isIndexSelected)
				{
					circleNumber = i;
				}
				cSpot.draw(g, i, isIndexSelected);
			}
		}
		if(circleNumber != -1) 
		{
			int gray = 55 * circleNumber;
			if(gray > 255) gray = 255;
			g.setColor(new Color(0, 0, 200));
			g.drawString(circleNumber + "", MouseInfo.getPointerInfo().getLocation().x - 3.5f, MouseInfo.getPointerInfo().getLocation().y + 4);
			g.setColor(Color.white);
		}
	}

	/* (non-Javadoc)
	 * @see GameScreen#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			_dragMode = true;
		}
		if(METRO.__viewPortButton_Train.isPressed(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y))
		{
			METRO.__currentGameScreen = _trainGameScreen;
			METRO.__viewPortButton_City.setPosition(new Point(METRO.__SCREEN_SIZE.width / 2 - 200, -15));
			METRO.__viewPortButton_Train.setPosition(new Point(METRO.__SCREEN_SIZE.width / 2, -5));
		}
	}

	/* (non-Javadoc)
	 * @see GameScreen#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			_dragMode = false;
		}
	}

}
