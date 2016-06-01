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

public class Button extends ControlElement
{
	private TextureRegion _texture;
	private Rectangle _areaOnImage;
	private boolean _hasBeenClicked = false; // true if control has been clicked since last check

	/**
	 * Creates a new Button.
	 * 
	 * @param area The position on the screen/window (absolute).
	 * @param areaOnImage The position of the button texture on an image (absolute).
	 * @param texture The button texture.
	 */
	public Button(Rectangle area, Rectangle areaOnImage, TextureRegion texture)
	{
		_texture = texture;
		_area = area;
		_areaOnImage = areaOnImage;
		_state = true;
	}

	/**
	 * Creates a new button.
	 * 
	 * @param area The position on the screen/window (absolute).
	 * @param text The text of the button.
	 */
	public Button(Rectangle area, String text)
	{
		_text = text;
		_area = area;
		_areaOnImage = new Rectangle(0, 0, _area.width, _area.height);
		_state = true;
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
		if(_state && (_area.contains(x, y) || _hasBeenClicked))
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
		return _state ? temp : false;
	}

	/**
	 * Draws the button with its text/texture.
	 */
	void draw()
	{
		if(!_text.equals(""))
		{
			if(_state) Draw.setColor(METRO.__metroRed);
			else Draw.setColor(Color.gray);
			Draw.String(_text, _area.x + (_area.width - Draw.getStringSize(_text).width) / 2,
				_area.y + (_area.height - Draw.getStringSize(_text).height) / 2);

			if(_state) Draw.setColor(METRO.__metroBlue);
			else Draw.setColor(Color.lightGray);
			Draw.Rect(_area.x, _area.y, _area.width, _area.height);
		}
		else
		{
			Draw.Image(_texture, _area, _areaOnImage);
		}
	}

	/**
	 * Check if mouse is on the button and sets its clicked-flag to true (isPressed() would return true now).
	 * 
	 * @return boolean True when user clicked on control, false when not.
	 */
	public boolean clickOnControlElement()
	{
		if(!_state) return false;
		Point mPos = METRO.__originalMousePosition;

		if(_area.contains(mPos))
		{
			_hasBeenClicked = true;
			return true;
		}
		return false;
	}

	@Override
	boolean mouseClicked(int screenX, int screenY, int button)
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
		_area.translate(offset.x, offset.y);
	}

	@Override
	void mouseScrolled(int amount)
	{
	}

	@Override
	void keyPressed(int key)
	{
	}

	@Override
	void keyUp(int keyCode)
	{
	}

	/**
	 * Sets the texture of this button and the area of it on the raw image.
	 * 
	 * @param positionOnImage The area of the texture for this button.
	 * @param image The raw image where the texture is on.
	 */
	public void setImage(Rectangle positionOnImage, TextureRegion image)
	{
		_areaOnImage = positionOnImage;
		_texture = image;
	}
}