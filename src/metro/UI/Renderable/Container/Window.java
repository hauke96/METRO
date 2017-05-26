package metro.UI.Renderable.Container;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.Input.Buttons;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
import metro.UI.Renderable.ControlElement;

/**
 * Creates a simple but fancy window with some extra functions like Control-management (managing buttons, labels, etc.).
 * 
 * @author hauke
 * 
 */
public class Window extends FloatingContainer
{
	private Point	_oldMousePos;
	private boolean	_dragMode,						// to drag the window
	        _closed;								// even if window has been deleted from the window list, that does not mean, that this object doesn't exist anymore, to this indicates that this
	                                                // window has been deleted
	private Color	_color;							// color of borders
	
	/**
	 * Creates a new window.
	 * 
	 * @param title
	 *            The title of the Window, is shown in the top area.
	 * @param area
	 *            The position on the Screen (absolute)
	 */
	public Window(String title, Rectangle area)
	{
		this(title, area, METRO.__metroBlue);
	}
	
	/**
	 * Creates a new window.
	 * 
	 * @param title
	 *            The title of the Window, is shown in the top area.
	 * @param area
	 *            The size of the area of the window in pixel.
	 * @param color
	 *            The color of the border
	 */
	public Window(String title, Rectangle area, Color color)
	{
		_text = title;
		_area = area;
		_area.translate(20, 1);
		_area.setSize(_area.width, _area.height + 20);
		_color = color;
		
		_closed = false;
		_dragMode = false;
	}
	
	/**
	 * Draws the window and all controls on it.
	 */
	@Override
	protected void draw()
	{
		update();
		drawWindow();
		super.draw();
	}
	
	/**
	 * Draws the background, border, head bar with title and the close cross.
	 */
	private void drawWindow()
	{
		// Clear background
		Fill.setColor(Color.white);
		Fill.Rect(_area.x, _area.y, _area.width + 1, _area.height + 1);
		
		// Draw head bar of window
		Fill.setColor(_color);
		Fill.Rect(_area.x, _area.y, _area.width - 20, 20);
		Draw.setColor(Color.white);
		Draw.Rect(_area.x + 1, _area.y + 1, _area.width - 20, 18);
		
		// Draw Border
		Draw.setColor(_color);
		Draw.Rect(_area.x, _area.y, _area.width + 1, _area.height + 1);
		Draw.Rect(_area.x, _area.y, _area.width + 1, 20);
		Draw.Rect(_area.x + _area.width - 19, _area.y, 20, 20); // close box
		
		// Draw Window title
		Draw.setColor(Color.white);
		Draw.String(_text, _area.x + (_area.width - 20) / 2 - Draw.getStringSize(_text).width / 2, _area.y + Draw.getStringSize(_text).height - 9);
		
		// Close cross
		Draw.Image(METRO.__iconSet, new Rectangle(_area.x + _area.width - 19, _area.y + 1, 19, 19), new Rectangle(0, 0, 19, 19));
	}
	
	/**
	 * Updates everything. Is very important for e.g. drag-feature and Controls
	 */
	public void update()
	{
		if (_color == null)
		{
			_color = METRO.__metroBlue;
		}
		if (_dragMode)
		{
			Point positionDiff = new Point(METRO.__originalMousePosition.x - _oldMousePos.x, METRO.__originalMousePosition.y - _oldMousePos.y);
			
			for (ControlElement cElement : _listOfControlElements)
			{
				cElement.moveElement(positionDiff);
			}
			
			_area.x += positionDiff.x;
			_area.y += positionDiff.y;
			_oldMousePos = METRO.__originalMousePosition;
		}
	}
	
	@Override
	public void add(ControlElement cElement)
	{
		if (!_listOfControlElements.contains(cElement))
		{
			Point pos = cElement.getPosition();
			cElement.setPosition(new Point(pos.x + _area.x, pos.y + _area.y + 20));
		}
		super.add(cElement);
	}
	
	/**
	 * Returns true is mouse is in window, false if not and also false if in window but on a control element.
	 * 
	 * @param screenX
	 *            x-coordinate of mouse
	 * @param screenY
	 *            y-coordinate of mouse
	 * @return boolean True/false if mouse is in plain window area without touching controls.
	 */
	public boolean isMouseOnWindow(int screenX, int screenY)
	{
		boolean mouseOnControl = false;
		Point mPos = new Point(screenX, screenY);
		for (ControlElement control : _listOfControlElements)
		{
			mouseOnControl |= control.isInArea(mPos.x - _area.x, mPos.y + _area.y);
		}
		
		return !mouseOnControl && isMouseOnWindowArea(screenX, screenY);
	}
	
	/**
	 * Checks if the mouse is in the window. This will NOT consider controls like the {@link #isMouseOnWindow(int, int)} method does.
	 * 
	 * @param screenX
	 *            x-coordinate of mouse
	 * @param screenY
	 *            y-coordinate of mouse
	 * @return boolean True/false if mouse is in window.
	 */
	public boolean isMouseOnWindowArea(int screenX, int screenY)
	{
		return screenX >= _area.x
		        && screenX <= _area.x + _area.width
		        && screenY >= _area.y
		        && screenY <= _area.y + _area.height;
	}
	
	@Override
	public boolean mouseClicked(int screenX, int screenY, int mouseButton)
	{
		super.mouseClicked(screenX, screenY, mouseButton);
		// Check for drag-mode:
		if (isMouseOnWindow(screenX, screenY)
		        && mouseButton == Buttons.LEFT)
		{
			_dragMode = true;
			_oldMousePos = new Point(screenX, screenY);
			
			closeIfNeeded(screenX, screenY, mouseButton);
			return true;
		}
		return false;
	}
	
	@Override
	public void mouseReleased(int screenX, int screenY, int button)
	{
		_dragMode = false;
	}
	
	/**
	 * Closes the window if the mouse is on the cross. NO CLICK is needed in this function, be careful. This function calls the close() function to mark himself as closed.
	 * 
	 * @param screenX
	 *            The x-coordinate of the click.
	 * @param screenY
	 *            The y-coordinate of the click.
	 * @param mouseButton
	 *            The button that has been clicked.
	 * @return True if the window has been closed, false if not.
	 */
	public boolean closeIfNeeded(int screenX, int screenY, int mouseButton)
	{
		if (screenX >= _area.x + _area.width - 20
		        && screenX <= _area.x + _area.width
		        && screenY >= _area.y
		        && screenY <= _area.y + 20)
		{
			close(); // mark whis window as "closed" to tell the METRO class to remove it
			return true;
		}
		return false;
	}
	
	/**
	 * Marks this window as closed and notifies its observers about the close event.
	 */
	@Override
	public void close()
	{
		notifyAboutClose();
		_closed = true;
	}
	
	/**
	 * Returns true if window has been successfully removed from the window list.
	 * 
	 * @return True - Already removed, False - Still exists
	 */
	public boolean isClosed()
	{
		return _closed;
	}
	
	@Override
	public void moveElement(Point offset)
	{
	}
}
