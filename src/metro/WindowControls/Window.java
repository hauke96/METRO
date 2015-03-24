package metro.WindowControls;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import metro.Game.METRO;
import metro.graphics.Draw;
import metro.graphics.Fill;

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
		
		//Clear background
		Fill.setColor(Color.white);
		Fill.Rect(_position.x, 
			_position.y, 
			_size.x + 1, 
			_size.y + 1);
		
		//Draw head bar of window
		Fill.setColor(_color);
		Fill.Rect(_position.x, 
			_position.y, 
			_size.x - 20, 
			20);
		Draw.setColor(Color.white);
		Draw.Rect(_position.x + 1, 
			_position.y + 1, 
			_size.x - 21, 
			18);
		
		//Draw Border
		Draw.setColor(_color);
		Draw.Rect(_position.x, _position.y, _size.x + 1, _size.y + 1);
		Draw.Rect(_position.x, _position.y, _size.x + 1, 20);
		Draw.Rect(_position.x + _size.x - 19, _position.y, 20, 20); // close box
		
		//Draw Window title
		Draw.setColor(Color.white);
		Draw.String(_title, _position.x + (_size.x - 20) / 2 - Draw.getStringSize(_title).width / 2, 
			_position.y + Draw.getStringSize(_title).height - 8);
		
		//Close cross
		Draw.Image(METRO.__iconSet, 
				new Rectangle(_position.x + _size.x - 20, _position.y, 20, 20), 
				new Rectangle(0, 0, 20, 20));
//		
		for(ControlElement cElement : _elementList)
		{
			cElement.draw();
		}
	}
	/**
	 * Updates everything. Is very important for e.g. drag-feature and Controls
	 */
	public void update()
	{
		if(_color == null)
		{
			_color = METRO.__metroBlue;
		}
		if(_dragMode)
		{
			Point positionDiff = new Point(MouseInfo.getPointerInfo().getLocation().x - _oldMousePos.x,
					MouseInfo.getPointerInfo().getLocation().y - _oldMousePos.y);
			
			_position.x += positionDiff.x;
			_position.y += positionDiff.y;
			_oldMousePos = MouseInfo.getPointerInfo().getLocation();
		}
	}
	/**
	 * Adds a new control to the window control list. If the new Control is already in the list, it won't be added again, so there are no doubles in this list.
	 * @param cElement The WindowControl element thta should be added.
	 */
	public void addControlElement(ControlElement cElement)
	{
		if(!_elementList.contains(cElement))
		{
			Point pos = cElement.getPosition();
			System.out.println(cElement.getClass().getName() + " - " + pos);
			cElement.setPosition(new Point(pos.x + _position.x, pos.y + _position.y));
			_elementList.add(cElement); // there wont be doubles ;)
		}
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
