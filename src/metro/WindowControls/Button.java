/**
 * Provides a Button with basic functions.
 */
package metro.WindowControls;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import metro.METRO;
import metro.Graphics.Draw;

/**
 * A button with text ot an image attached to a window. It also manages the click-event.
 * 
 * @author hauke
 *
 */

public class Button extends ActionObservable implements ControlElement
{
	private TextureRegion _texture;
	private Rectangle _position,
		_positionOnImage;
	private Window _windowHandle;
	private boolean _hasBeenClicked = false, // true if control has been clicked since last check
		_enabled;
	private String _text = "";

	/**
	 * Creates a new Button.
	 * 
	 * @param position The position on the screen/window (absolute).
	 * @param positionOnImage The position of the button texture on an image (absolute).
	 * @param texture The button texture.
	 */
	public Button(Rectangle position, Rectangle positionOnImage, TextureRegion texture)
	{
		this(position, positionOnImage, texture, null);
	}

	/**
	 * Creates a new Button.
	 * 
	 * @param position The position on the screen/window (absolute).
	 * @param positionOnImage The position of the button texture on an image (absolute).
	 * @param texture The button texture.
	 * @param window The window it should be on.
	 */
	public Button(Rectangle position, Rectangle positionOnImage, TextureRegion texture, Window window)
	{
		_texture = texture;
		_position = position;
		_positionOnImage = positionOnImage;
		_windowHandle = window;
		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
		_enabled = true;
	}

	/**
	 * Creates a new button.
	 * 
	 * @param position The position on the screen/window (absolute).
	 * @param text The text of the button.
	 * @param window The window it should be on.
	 */
	public Button(Rectangle position, String text, Window window)
	{
		_text = text;
		_position = position;
		_positionOnImage = new Rectangle(0, 0, _position.width, _position.height);
		_windowHandle = window;
		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
		_enabled = true;
	}

	/**
	 * Creates a new button.
	 * 
	 * @param position The position on screen (absolute).
	 * @param text The text of the button.
	 */
	public Button(Rectangle position, String text)
	{
		this(position, text, null);
	}

	/**
	 * Checks if the mouse is in the button area or has been clicked a while ago.
	 * 
	 * @param x x-coordinate of mouse.
	 * @param y y-coordinate of mouse.
	 * @return True if mouse is in area or Button has been pressed.
	 */
	public boolean isPressed(int x, int y)
	{
		if(_enabled && (_position.contains(x, y) || _hasBeenClicked))
		{
			_hasBeenClicked = false;
			return true;
		}
		return false;
	}

	/**
	 * Returns if the button has been clicked since last frame.
	 * 
	 * @return True is button has been pressed.
	 */
	public boolean isPressed()
	{
		boolean temp = _hasBeenClicked;
		_hasBeenClicked = false;
		return _enabled ? temp : false;
	}

	/**
	 * Draws the button with its text/texture.
	 */
	public void draw()
	{
		if(!_text.equals(""))
		{
			if(_enabled) Draw.setColor(METRO.__metroRed);
			else Draw.setColor(Color.gray);
			Draw.String(_text, _position.x + (_position.width - Draw.getStringSize(_text).width) / 2,
				_position.y + (_position.height - Draw.getStringSize(_text).height) / 2);
			
			if(_enabled)Draw.setColor(METRO.__metroBlue); 
			else Draw.setColor(Color.lightGray);
			Draw.Rect(_position.x, _position.y, _position.width, _position.height);
		}
		else
		{
			Draw.Image(_texture, _position, _positionOnImage);
		}
	}

	/**
	 * Sets the position of the button.
	 * 
	 * @param newPosition The new position. It's NOT an offset!
	 */
	public void setPosition(Point newPosition)
	{
		_position = new Rectangle(newPosition.x, newPosition.y, _position.width, _position.height);
	}

	/**
	 * Returns the position of the button as point.
	 */
	public Point getPosition()
	{
		return new Point(_position.x, _position.y);
	}

	/**
	 * Check if mouse is on the button and sets its clicked-flag to true (isPressed() would return true now).
	 * 
	 * @return boolean True when user clicked on control, false when not.
	 */
	public boolean clickOnControlElement()
	{
		if(!_enabled) return false;
		Point mPos = METRO.__originalMousePosition;

		if(_position.contains(mPos))
		{
			_hasBeenClicked = true;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mouseClicked(int screenX, int screenY, int button)
	{
		if(clickOnControlElement())
		{
			notifyClickOnControl(this);
			return true;
		}
		return false;
	}

	@Override
	public void moveElement(Point offset)
	{
		_position.x += offset.x;
		_position.y += offset.y;
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public void keyPressed(int key)
	{
	}

	@Override
	public void keyUp(int keyCode)
	{
	}

	@Override
	public void setText(String text)
	{
		_text = text;
	}

	@Override
	public void setState(boolean enable)
	{
		_enabled = enable;
	}

	@Override
	public Rectangle getArea()
	{
		return _position;
	}
}