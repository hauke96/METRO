/**
 * Provides a Button with basic functions.
 */
package metro.WindowControls;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

import metro.Game.METRO;
import metro.graphics.Draw;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * @author Hauke
 *
 */
public class Button implements ControlElement
{
	private TextureRegion _texture;
	private Rectangle _position,
		_positionOnImage;
	private Window _windowHandle;
	private boolean hasBeenClicked = false; // true if control has been clicked since last check
	private String _text = "";

	/**
	 * Creates a new Button.
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
	}
	/**
	 * Creates a new button.
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
	}
	/**
	 * Creates a new button.
	 * @param position The position on screen (absolute).
	 * @param text The text of the button.
	 */
	public Button(Rectangle position, String text)
	{
		this(position, text, null);
	}
	/**
	 * Checks if the mouse is in the button area or has been clicked a while ago.
	 * @param x x-coordinate of mouse.
	 * @param y y-coordinate of mouse.
	 * @return True if mouse is in area or Button has been pressed.
	 */
	public boolean isPressed(int x, int y)
	{
		if(x >= _position.x 
			&& x <= _position.x + _position.width
			&& y >= _position.y
			&& y <= _position.y + _position.height
			|| hasBeenClicked)
		{
			hasBeenClicked = false;
			return true;
		}
		return false;
	}
	/**
	 * Returns if the button has been clicked since last frame.
	 * @return True is button has been pressed.
	 */
	public boolean isPressed()
	{
		boolean temp = hasBeenClicked;
		hasBeenClicked = false;
		return temp;
	}
	/**
	 * Draws the button with its text/texture.
	 */
	public void draw()
	{
		if(!_text.equals("")) 
		{
			Draw.setColor(METRO.__metroRed);
			Draw.String(_text, _position.x + (_position.width - Draw.getStringSize(_text).width) / 2,
					_position.y + (_position.height - Draw.getStringSize(_text).height) / 2);
			Draw.setColor(METRO.__metroBlue);
			Draw.Rect(_position.x, _position.y, _position.width, _position.height);
		}
		else
		{
			Draw.Image(_texture, _position, _positionOnImage);
		}
	}
	/**
	 * Sets the position of the button.
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
	 */
	public boolean clickOnControlElement()
	{
		Point mPos = MouseInfo.getPointerInfo().getLocation();

		if(mPos.x >= _position.x
			&& mPos.x <= _position.x + _position.width
			&& mPos.y >= _position.y
			&& mPos.y <= _position.y + _position.height)
		{
			System.out.println("Pressed" + _text);
			hasBeenClicked = true;
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
}