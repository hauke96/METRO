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
		drawControls(g);
		
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
	/**
	 * Draws the basic Controls onto the screen.
	 * @param g The graphic handle to draw on
	 */
	private void drawControls(Graphics2D g)
	{
		g.drawImage(METRO.__viewPortButton_Texture, METRO.__SCREEN_SIZE.width / 2 - 40, 45, METRO.__SCREEN_SIZE.width / 2 + 40, 85, 
			161, 60, 161+79, 60+40, null);
		METRO.__viewPortButton_City.draw(g);
		METRO.__viewPortButton_Train.draw(g);
		drawPlayerInfos(g);
	}
	/**
	 * Draws all significant player infos onto the upper left corner.
	 * @param g The graphic handle to draw on
	 */
	private void drawPlayerInfos(Graphics g)
	{
		// clear area
		g.setColor(Color.white);
		g.fillRect(0, 0, 100, 26);
		
		g.setColor(METRO.__metroBlue);
		// Draw a   _|
		g.drawLine(0, 26, 100, 26);
		g.drawLine(100, 0, 100, 26);
		
		// draw amount of money like 935.258.555$
		String money = ("" + METRO.__money).substring(("" + METRO.__money).length() - 3, ("" + METRO.__money).length());
		for(int i = ("" + METRO.__money).length() - 4; i > 2; i -= 3)
		{
			money = ("" + METRO.__money).substring(i - 3, i) + money;
		}
		money = ("" + METRO.__money).substring(0, ("" + METRO.__money).length()%3) + money;
		
		g.setColor(METRO.__metroBlue);
		g.setFont(new Font("Huxley Titling", Font.PLAIN, 24));
		g.drawString("" + money, 5, 22);
		g.setColor(METRO.__metroRed);
		g.drawString("$", 7 + g.getFontMetrics().stringWidth("" + money), 22);
		g.setFont(METRO.__STDFONT);
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
