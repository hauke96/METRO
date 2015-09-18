package metro.WindowControls;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import metro.METRO;
import metro.Graphics.Draw;
import metro.Graphics.Fill;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Creates a simple but fancy window with some extra functions like Control-management (managing buttons, labels, etc.).
 * 
 * @author hauke
 * 
 */
public class Window extends ActionObservable implements ControlElement
{
	private Point _position,
		_size,
		_oldMousePos;
	private String _title = "";
	private ArrayList<ControlElement> _elementList = new ArrayList<ControlElement>();
	private boolean _dragMode = false, // to drag the window
		_closed = false; // even if window has been deleted from the window list, that does not mean, that this object doesn't exist anymore, to this indicates that this window has been deleted
	private Color _color; // color of borders

	/**
	 * Creates a new window.
	 * 
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
		METRO.registerControl(this);
	}

	/**
	 * Creates a new window.
	 * 
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
	 * 
	 * @param g The graphic handle.
	 */
	public void draw(SpriteBatch g)
	{
		update();

		// Clear background
		Fill.setColor(Color.white);
		Fill.Rect(_position.x,
			_position.y,
			_size.x + 1,
			_size.y + 1);

		// Draw head bar of window
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

		// Draw Border
		Draw.setColor(_color);
		Draw.Rect(_position.x, _position.y, _size.x + 1, _size.y + 1);
		Draw.Rect(_position.x, _position.y, _size.x + 1, 20);
		Draw.Rect(_position.x + _size.x - 19, _position.y, 20, 20); // close box

		// Draw Window title
		Draw.setColor(Color.white);
		Draw.String(_title, _position.x + (_size.x - 20) / 2 - Draw.getStringSize(_title).width / 2,
			_position.y + Draw.getStringSize(_title).height - 9);

		// Close cross
		Draw.Image(METRO.__iconSet,
			new Rectangle(_position.x + _size.x - 19, _position.y + 1, 19, 19),
			new Rectangle(0, 0, 19, 19));

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
			Point positionDiff = new Point(METRO.__originalMousePosition.x - _oldMousePos.x,
				METRO.__originalMousePosition.y - _oldMousePos.y);

			for(ControlElement cElement : _elementList)
			{
				cElement.moveElement(positionDiff);
			}

			_position.x += positionDiff.x;
			_position.y += positionDiff.y;
			_oldMousePos = METRO.__originalMousePosition;
		}
	}

	/**
	 * Adds a new control to the window control list. If the new Control is already in the list, it won't be added again, so there are no doubles in this list.
	 * 
	 * @param cElement The WindowControl element thta should be added.
	 */
	public void addControlElement(ControlElement cElement)
	{
		if(!_elementList.contains(cElement))
		{
			Point pos = cElement.getPosition();
			cElement.setPosition(new Point(pos.x + _position.x, pos.y + _position.y + 20));
			_elementList.add(cElement); // there wont be doubles ;)
		}
	}

	/**
	 * Returns true is mouse is in window, false if not and also false if in window but e.g. on a button.
	 * 
	 * @param screenX x-coordinate of mouse
	 * @param screenY y-coordinate of mouse
	 * @return boolean True/false if mouse is in window.
	 */
	public boolean isMouseOnWindow(int screenX, int screenY)
	{
		return screenX >= _position.x
			&& screenX <= _position.x + _size.x
			&& screenY >= _position.y
			&& screenY <= _position.y + _size.y;
	}

	/**
	 * Makes things when mouse is pressed.
	 * 
	 * @param screenX The x-coordinate of the click
	 * @param screenY The y-coordinate of the click
	 * @param mouseButton The mouse button (using gdx.Input.Buttons)
	 */
	public void mousePressed(int screenX, int screenY, int mouseButton)
	{
		boolean inputPressed = false; // true if an input field has been clicked
		// for(ControlElement cElement : _elementList)
		// {
		// boolean b = cElement.clickOnControlElement();
		//
		// if(b && cElement instanceof Input) // if clicked element is an input field, set this as selected field
		// {
		// inputPressed = true;
		// METRO.__currentGameScreen.setSelectedInput((Input)cElement);
		// }
		// }
		if(!inputPressed) METRO.__currentGameScreen.setSelectedInput(null); // reset the selected input field

		// Check for drag-mode:
		if(screenX >= _position.x
			&& screenX <= _position.x + _size.x - 20
			&& screenY >= _position.y
			&& screenY <= _position.y + 20
			&& mouseButton == Buttons.LEFT)
		{
			_dragMode = true;
			_oldMousePos = new Point(screenX, screenY);
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
	 * Fires when users scrolls.
	 * 
	 * @param amount Positive or negative amount of steps since last frame.
	 */
	public void mouseScrolled(int amount)
	{
		for(ControlElement cElement : _elementList)
		{
			cElement.mouseScrolled(amount);
		}
	}

	/**
	 * Closes the window if the mouse is on the cross. NO CLICK is needed in this function, be careful. This function calls the METRO.__close() function to close itself.
	 * 
	 * @return True if the window has been closed, false if not.
	 */
	public boolean closeIfNeeded(int screenX, int screenY, int mouseButton)
	{
		if(screenX >= _position.x + _size.x - 20
			&& screenX <= _position.x + _size.x
			&& screenY >= _position.y
			&& screenY <= _position.y + 20)
		{
			close(); // closes this window and removes it from the memory
			return true;
		}
		return false;
	}

	/**
	 * Returns the position of the window.
	 * 
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
		METRO.__closeWindow(this); // closes this window and removes it from the list
		_elementList.clear();
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

	/**
	 * Gets called when a key is pressed. Controls should
	 * 
	 * @param keyCode Key code of the pressed key.
	 */
	public void keyPressed(int keyCode)
	{
		for(ControlElement cElement : _elementList)
		{
			cElement.keyPressed(keyCode);
		}
	}

	public void keyUp(int keyCode)
	{
		for(ControlElement cElement : _elementList)
		{
			cElement.keyUp(keyCode);
		}
	}

	@Override
	public void draw()
	{
	}

	@Override
	public boolean clickOnControlElement()
	{
		return false;
	}

	@Override
	public void setPosition(Point pos)
	{
	}

	@Override
	public void setText(String text)
	{
	}

	@Override
	public void setState(boolean enable)
	{
	}

	@Override
	public boolean mouseLeftClicked(int screenX, int screenY, int button)
	{
		if(screenX >= _position.x + _size.x - 20
			&& screenX <= _position.x + _size.x
			&& screenY >= _position.y
			&& screenY <= _position.y + 20)
		{
			notifyClosed();
			return true;
		}
		return false;
	}

	@Override
	public void moveElement(Point offset)
	{
	}
}
