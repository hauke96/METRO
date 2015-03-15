/**
 * Provides a Button with basic functions.
 */
package WindowControls;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import Game.METRO;

/**
 * @author Hauke
 *
 */
public class Button implements ControlElement
{
	private Image _texture;
	private Rectangle _position,
		_positionOnImage;
	private Window _windowHandle;
	private boolean hasBeenClicked = false; // true if control has been clicked since last check

	/**
	 * Creates a new Button.
	 * @param position The position on the screen/window (absolute).
	 * @param positionOnImage The position of the button texture on an image (absolute).
	 * @param texture The button texture.
	 */
	public Button(Rectangle position, Rectangle positionOnImage, Image texture)
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
	public Button(Rectangle position, Rectangle positionOnImage, Image texture, Window window)
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
		BufferedImage bufferedImage = new BufferedImage(position.width, position.height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(METRO.__metroBlue);
		g2d.setFont(METRO.__stdFont);

		g2d.drawString(text, position.width / 2 - g2d.getFontMetrics(METRO.__stdFont).stringWidth(text) / 2, 
			g2d.getFontMetrics(METRO.__stdFont).getHeight() - 5);
		
		g2d.drawRect(0, 0, position.width - 1, position.height - 1);

		_texture = bufferedImage;
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
	public void draw(Graphics g)
	{
		g.drawImage(_texture, _position.x, _position.y, _position.x + _position.width, _position.y + _position.height, 
				_positionOnImage.x, _positionOnImage.y, _positionOnImage.x + _positionOnImage.width, _positionOnImage.y + _positionOnImage.height, null);
	}
	/**
	 * Sets the position of the button.
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
		
		if(mPos.x >= _position.x + _windowHandle.getPosition().x
			&& mPos.x <= _position.x + _position.width + _windowHandle.getPosition().x
			&& mPos.y >= _position.y + _windowHandle.getPosition().y
			&& mPos.y <= _position.y + _position.height + _windowHandle.getPosition().y)
		{
			hasBeenClicked = true;
			return true;
		}
		return false;
	}
	public void update() 
	{
	}
}