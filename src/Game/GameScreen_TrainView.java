package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;


/**
 * 
 */

/**
 * @author Hauke
 *
 */
public class GameScreen_TrainView implements GameScreen
{
	private boolean _dragMode = false;
	private Point _oldMousePos; // Mouse position from last frame
	private int _baseNetSpacing = 25; // amount of pixel between lines of the base net
	private Point2D _offset = new Point2D.Float(0, 0); // offset for moving the map
	
	static public GameScreen _cityGameScreen; // to go into the city game-screen without loosing reference
	
	public GameScreen_TrainView()
	{
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
		_oldMousePos = MouseInfo.getPointerInfo().getLocation();

		drawBaseNet(g, Color.lightGray);
		drawBaseDot(g);
		
		printDebugStuff(g);
	}
	/**
	 * Prints all the debug stuff, that is -more or less- important.
	 * @param g Graphics handle to draw on
	 */
	private void printDebugStuff(Graphics2D g)
	{
		g.setColor(Color.blue);
		g.drawString("MapPosition:", 5, 65);
		g.drawString("MousePosition:", 5, 85);
		g.setColor(Color.black);
		g.drawString(_offset + "", 100, 65);
		g.drawString(MouseInfo.getPointerInfo().getLocation() + "", 100, 85);
	}
	/**
	 * Draws the basic gray net for kind of orientation.
	 * @param g The graphic handle to draw on
	 * @param color The color of the net
	 * @param offset An user made offset
	 */
	private void drawBaseNet(Graphics2D g, Color color, int offset)
	{
		g.setColor(color);
		for(int y = ((int)_offset.getY() + offset) % _baseNetSpacing; y < METRO.__SCREEN_SIZE.height; y += _baseNetSpacing)
		{
			g.drawLine(offset, y, METRO.__SCREEN_SIZE.width, y);
		}
		for(int x = ((int)_offset.getX() + offset) % _baseNetSpacing; x < METRO.__SCREEN_SIZE.width; x += _baseNetSpacing)
		{
			g.drawLine(x, offset, x, METRO.__SCREEN_SIZE.height);
		}
		g.setColor(Color.white);
	}
	/**
	 * Draws the basic gray net for kind of orientation.
	 * @param g The graphic handle to draw on
	 * @param color The color of the net
	 */
	private void drawBaseNet(Graphics2D g, Color color)
	{
		drawBaseNet(g, color, 0);
	}
	/**
	 * Calculates the position and draws this fancy jumping dot near the cursor.
	 * @param g The graphic handle to draw on
	 */
	private void drawBaseDot(Graphics2D g)
	{
		Point cursorPos = new Point(Math.abs((int)(MouseInfo.getPointerInfo().getLocation().x - 7 - _offset.getX()) % _baseNetSpacing), 
				Math.abs((int)(MouseInfo.getPointerInfo().getLocation().y - 7 - _offset.getY()) % _baseNetSpacing));
		
		Point offsetMarker = new Point(_baseNetSpacing - cursorPos.x, _baseNetSpacing - cursorPos.y);
		if(cursorPos.x <= _baseNetSpacing / 2) offsetMarker.x = cursorPos.x;
		if(cursorPos.y <= _baseNetSpacing / 2) offsetMarker.y = cursorPos.y;

		g.setColor(Color.darkGray);
		g.fillRect(MouseInfo.getPointerInfo().getLocation().x + offsetMarker.x - 8, 
				MouseInfo.getPointerInfo().getLocation().y + offsetMarker.y - 8, 
				3, 3);
		g.setColor(Color.white);
	}
	public void mouseClicked(MouseEvent e)
	{
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			_dragMode = true;
		}
		if(METRO.__viewPortButton_City.isPressed(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y))
		{
			METRO.__currentGameScreen = _cityGameScreen;
			METRO.__viewPortButton_City.setPosition(new Point(METRO.__SCREEN_SIZE.width / 2 - 200, -5));
			METRO.__viewPortButton_Train.setPosition(new Point(METRO.__SCREEN_SIZE.width / 2, -15));
		}
	}
	public void mouseReleased(MouseEvent e)
	{
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			_dragMode = false;
		}
	}
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			METRO.__close();
		}
	}
}
