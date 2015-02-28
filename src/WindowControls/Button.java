/**
 * Provides a Button with basic functions.
 */
package WindowControls;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

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

	/**
	 * 
	 */
	public Button(Rectangle position, Rectangle positionOnImage, Image texture)
	{
		this(position, positionOnImage, texture, null);
	}
	public Button(Rectangle position, Rectangle positionOnImage, Image texture, Window window)
	{
		_texture = texture;
		_position = position;
		_positionOnImage = positionOnImage;
		_windowHandle = window;
		if(_windowHandle != null) _windowHandle.addControlElement(this);
	}

	public boolean isPressed(int x, int y)
	{
		return x >= _position.x 
			&& x <= _position.x + _position.width
			&& y >= _position.y
			&& y <= _position.y + _position.height;
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(_texture, _position.x, _position.y, _position.x + _position.width, _position.y + _position.height, 
				_positionOnImage.x, _positionOnImage.y, _positionOnImage.x + _positionOnImage.width, _positionOnImage.y + _positionOnImage.height, null);
	}
	public void setPosition(Point newPosition)
	{
		_position = new Rectangle(newPosition.x, newPosition.y, _position.width, _position.height);
	}
	@Override
	public void update() 
	{
	}
}