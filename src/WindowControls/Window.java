package WindowControls;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import Game.METRO;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Creates a simple but fancy window with some extra functions like Control-management (managing buttons, labels, etc.).
 * @author hauke
 *
 */
public class Window 
{
	private Point _position,
		_size,
		_oldMousePos;
	private String _title = "";
	private ArrayList<ControlElement> _elementList = new ArrayList<ControlElement>();
	private boolean _dragMode = false; // to drag the window
	private Color _color; // color of borders
	
	/**
	 * Creates a new window.
	 * @param title The title of the Window, is shown in the top area.
	 * @param position The position on the Screen (absolute)
	 * @param size The size in pixel (absolute)
	 * @param color The color of the border
	 */
	public Window(String title, Point position, Point size, Color color)
	{
		_title = title;
		_position = position;
		_position.y += 20;
		_position.x += 1;
		_size = size;
		_size.y += 20; // for title
		_color = color;
		
		METRO.__windowList.add(this);
	}
	/**
	 * Creates a new window.
	 * @param title The title of the Window, is shown in the top area.
	 * @param position The position on the Screen (absolute)
	 * @param size The size in pixel (absolute)
	 */
	public Window(String title, Point position, Point size)
	{
		this(title, position, size, METRO.__metroBlue);
	}
	/**
	 * Draws the window and all controls on it.
	 * @param g The graphic handle.
	 */
	public void draw(SpriteBatch g)
	{
		update();
		//TODO: Recreate the draw routine for the window
//		BufferedImage bufferedImage = new BufferedImage(_size.x + 2, _size.y + 22, BufferedImage.TYPE_INT_ARGB);
//		
//		Graphics2D g2d = bufferedImage.createGraphics();
//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//		
//		//Clear background
//		g2d.setColor(Color.white);
//		g2d.fillRect(0, 0, _size.x + 1, _size.y + 1);
//		//Draw head bar of window
//		g2d.setColor(_color);
//		g2d.fillRect(0,  0, _size.x - 20, 20);
//		g2d.setColor(Color.white);
//		g2d.drawRect(1, 1, _size.x - 3, 18);
//		//Draw Border
//		g2d.setColor(_color);
//		g2d.drawRect(0, 0, _size.x + 1, _size.y + 1);
//		g2d.drawRect(0, 0, _size.x + 1, 20);
//		g2d.drawRect(_size.x - 19, 0, _size.x + 1, 20); // close box
//		//Draw Window title
//		g2d.setColor(Color.white);
//		g2d.setFont(METRO.__stdFont);
//		g2d.drawString(_title, (_size.x - 20) / 2 - g2d.getFontMetrics(METRO.__stdFont).stringWidth(_title) / 2, 
//			g2d.getFontMetrics(METRO.__stdFont).getHeight() - 5);
//		//Close cross
//		g2d.drawImage(METRO.__iconSet, _size.x - 19, 0, _size.x + 1, 20, 0, 0, 20, 20, null);
//
//		BufferedImage bufferedImage_controls = new BufferedImage(_size.x, _size.y, BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g2d_controls = bufferedImage_controls.createGraphics();
//		g2d_controls.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2d_controls.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//		
//		for(ControlElement cElement : _elementList)
//		{
//			cElement.draw(g2d_controls);
//		}
//		
//		g2d.drawImage(bufferedImage_controls, 1, 21, null);
//		g.drawImage(bufferedImage, _position.x - 1, _position.y - 20, null);
	}
	/**
	 * Updates everything. Is very important for e.g. drag-feature and Controls
	 */
	public void update()
	{
		if(_dragMode)
		{
			Point positionDiff = new Point(MouseInfo.getPointerInfo().getLocation().x - _oldMousePos.x,
					MouseInfo.getPointerInfo().getLocation().y - _oldMousePos.y);
			
			_position.x += positionDiff.x;
			_position.y += positionDiff.y;
			_oldMousePos = MouseInfo.getPointerInfo().getLocation();
		}
		for(ControlElement cElement : _elementList)
		{
			cElement.update();
		}
	}
	/**
	 * Adds a new control to the window control list. If the new Control is already in the list, it won't be added again, so there are no doubles in this list.
	 * @param cElement The WindowControl element thta should be added.
	 */
	public void addControlElement(ControlElement cElement)
	{
		if(!_elementList.contains(cElement)) _elementList.add(cElement); // there wont be doubles ;)
	}
	/**
	 * Returns true is mouse is in window, false if not and also false if in window but e.g. on a button.
	 * @return boolean True/false if mouse is in window.
	 */
	public boolean isMouseOnWindow()
	{
		Point mPos = MouseInfo.getPointerInfo().getLocation(); // use real position, because __mousePosition is old when mouse is on window (to ignore the mouse on the game screen)
		
		return mPos.x >= _position.x
			&& mPos.x <= _position.x + _size.x
			&& mPos.y >= _position.y - 20
			&& mPos.y <= _position.y + _size.y - 20;
			//&& !isOnWindow; // if mouse is on NO control element (only on the window area)
				
	}
	/**
	 * Makes things when mouse is pressed.
	 * @param e MouseEvent
	 */
	public void mousePressed(MouseEvent e)
	{
		Point mousePosition = e.getPoint(); // use e.getPoint(), because __mousePosition is old when mouse is on window (to ignore the mouse on the game screen)
		
		for(ControlElement cElement : _elementList)
		{
			cElement.clickOnControlElement();
		}

		// Check for drag-mode:
		if(mousePosition.x >= _position.x
			&& mousePosition.x <= _position.x + _size.x - 20
			&& mousePosition.y >= _position.y - 20
			&& mousePosition.y <= _position.y
			&& SwingUtilities.isLeftMouseButton(e))
		{
			_dragMode = true;
			_oldMousePos = mousePosition;
		}
	}
	/**
	 * Makes things when mouse is released (important for drag-mode of the window).
	 */
	public void mouseReleased()
	{
		_dragMode = false;
	}
	/**
	 * Closes the window if the mouse is on the cross. NO CLICK is needed in this function, be careful. This function calls the METRO.__close() function to close itself.
	 * @param e MouseEvent
	 * @return True if the window has been closed, false if not.
	 */
	public boolean closeIfNeeded(MouseEvent e)
	{
		if(e.getPoint().x >= _position.x + _size.x - 20
			&& e.getPoint().x <= _position.x + _size.x
			&& e.getPoint().y >= _position.y - 20
			&& e.getPoint().y <= _position.y)
		{
			close(); // closes this window and removes it from the memory
			return true;
		}
		return false;
	}
	/**
	 * Returns the position of the window.
	 * @return Position.
	 */
	public Point getPosition()
	{
		return _position;
	}
	/**
	 * Closes the window softly.
	 */
	public void close()
	{
		METRO.__closeWindow(this); // closes this window and removes it from the memory
		_elementList.clear();
	}
}
