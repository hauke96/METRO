package metro.WindowControls;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.Graphics.Draw;

/**
 * A color bar is a stripe of all RGB colors. When the user clicked on this control, the color that has been clicked can be
 * 
 * @author hauke
 *
 */
public class ColorBar implements ControlElement
{
	private Rectangle _position;
	private float _saturation, _brightness;
	private boolean _enabled,
		_drawBorder;
	private Window _windowHandle;
	private Color _clickedColor;
	private int _clickedXPosition;

	/**
	 * Creates a new color bar.
	 * 
	 * @param position The position of the bar.
	 * @param window The window it's attached to.
	 */
	public ColorBar(Rectangle position, Window window)
	{
		this(position, window, 1f, 1f);
	}

	/**
	 * Creates a new color bar.
	 * 
	 * @param position The position of the bar.
	 * @param window The window it's attached to.
	 * @param saturation The saturation of its color.
	 * @param brithness The brightness of its color.
	 */
	public ColorBar(Rectangle position, Window window, float saturation, float brithness)
	{
		_position = position;
		_saturation = saturation;
		_brightness = brithness;
		_windowHandle = window;
		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
		_enabled = true;
		_drawBorder = true;
		_clickedXPosition = -1;
	}

	/**
	 * Gets the last clicked color of this control. When a user click on it, the color which is under the mouse will be stored.
	 * 
	 * @return The color which has been clicked.
	 */
	public Color getClickedColor()
	{
		return _clickedColor;
	}

	/**
	 * Sets the visibility of the border.
	 * 
	 * @param visible When true, the border will be visible, when false it'll not ;)
	 */
	public void setBorderVisibility(boolean visible)
	{
		_drawBorder = visible;
	}

	@Override
	public void draw()
	{
		Color color;

		for(int i = 0; i < _position.width; i++)
		{
			color = Color.getHSBColor((float)i / (float)_position.width, _saturation, _brightness);
			if(!_enabled) // calculate grayscale color
			{
				int avg = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
				color = new Color(avg, avg, avg);
			}
			Draw.setColor(color);
			Draw.Line(_position.x + i, _position.y, _position.x + i, _position.y + _position.height);
		}

		if(_drawBorder)
		{
			Draw.setColor(Color.darkGray);
			Draw.Rect(new Rectangle(_position.x, _position.y, _position.width, _position.height));
			// draw also the selection:
			if(_clickedXPosition != -1)
			{
				Draw.Line(_position.x + _clickedXPosition - 1, _position.y, _position.x + _clickedXPosition - 1, _position.y + _position.height);
				Draw.Line(_position.x + _clickedXPosition + 1, _position.y, _position.x + _clickedXPosition + 1, _position.y + _position.height);
			}
		}
	}

	@Override
	public boolean clickOnControlElement()
	{
		if(!_enabled) return false;
		// Point mPos = METRO.__originalMousePosition;
		Point mPos = new Point(METRO.__mousePosition.x - 3, METRO.__mousePosition.y);

		if(_position.contains(mPos))
		{
			_clickedXPosition = mPos.x - _position.x;
			_clickedColor = Color.getHSBColor((float)_clickedXPosition / (float)_position.width, _saturation, _brightness);
		}
		return _position.contains(mPos);
	}

	@Override
	public void setPosition(Point pos)
	{
		_position.setLocation(pos);
	}

	@Override
	public void setText(String text)
	{
	}

	@Override
	public void setState(boolean enable)
	{
		_enabled = enable;
	}

	@Override
	public Point getPosition()
	{
		return _position.getLocation();
	}

	@Override
	public void moveElement(Point offset)
	{
		_position.add(offset);
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public void keyPressed(int keyCode)
	{
	}

	@Override
	public void keyUp(int keyCode)
	{
	}
}
