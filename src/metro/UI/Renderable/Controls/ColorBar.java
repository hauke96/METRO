package metro.UI.Renderable.Controls;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.Common.Technical.Logger;
import metro.UI.Renderable.ControlElement;

/**
 * A color bar is a stripe of all RGB colors.
 * The {@link #clickOnControlElement()} event gets fired when the selected color of this bar changed in any way.
 * 
 * @author hauke
 *
 */
public class ColorBar extends ControlElement
{
	private float	_saturation, _brightness;
	private boolean	_drawBorder;
	private Color	_clickedColor;
	private int		_clickedXPosition;
	
	/**
	 * Creates a new color bar.
	 * 
	 * @param area
	 *            The position of the bar.
	 */
	public ColorBar(Rectangle area)
	{
		this(area, 1f, 1f);
	}
	
	/**
	 * Creates a new color bar.
	 * 
	 * @param area
	 *            The position of the bar.
	 * @param saturation
	 *            The saturation of its color.
	 * @param brithness
	 *            The brightness of its color.
	 */
	public ColorBar(Rectangle area, float saturation, float brithness)
	{
		_area = area;
		_saturation = saturation;
		_brightness = brithness;
		_drawBorder = true;
		_clickedXPosition = -1;
	}
	
	/**
	 * Gets the last clicked color of this control. When a user click on it, the color which is under the mouse will be stored.
	 * 
	 * @return The color which has been clicked.
	 */
	public Color getSelectedColor()
	{
		return _clickedColor;
	}
	
	/**
	 * Sets the visibility of the border.
	 * 
	 * @param visible
	 *            When true, the border will be visible, when false it'll not ;)
	 */
	public void setBorderVisibility(boolean visible)
	{
		_drawBorder = visible;
	}
	
	/**
	 * Clears the color bar and sets it color to null (default value).
	 */
	public void clear()
	{
		_clickedColor = null;
		_clickedXPosition = -1;
	}
	
	/**
	 * Selects a new color.
	 * 
	 * @param color
	 *            The Color that should be selected.
	 */
	public void setValue(Color color)
	{
		int hue = getHue(color.getRed(), color.getGreen(), color.getBlue());
		_clickedColor = Color.getHSBColor(hue / 360f, _saturation, _brightness);
		_clickedXPosition = getHue(_clickedColor.getRed(), _clickedColor.getGreen(), _clickedColor.getBlue());
		Logger.__debug("Set color bar value:\n" +
				_area.width + " - " +
				getHue(_clickedColor.getRed(), _clickedColor.getGreen(), _clickedColor.getBlue()) + " - " +
				_clickedXPosition);
	}
	
	/**
	 * Calculates the hue-value of the given rgb-values.
	 * This implements just one way of calculating this value and there are many other ways but this is absolutely sufficient.
	 * 
	 * @param red
	 *            The red-value of the color.
	 * @param green
	 *            The green-value of the color.
	 * @param blue
	 *            The blue-value of the color.
	 * @return The hue of the color.
	 */
	public int getHue(int red, int green, int blue)
	{
		float min = Math.min(Math.min(red, green), blue);
		float max = Math.max(Math.max(red, green), blue);
		
		float hue = 0f;
		if (max == red)
		{
			hue = (green - blue) / (max - min);
		}
		else if (max == green)
		{
			hue = 2f + (blue - red) / (max - min);
		}
		else
		{
			hue = 4f + (red - green) / (max - min);
		}
		
		hue = hue * 60;
		if (hue < 0) hue = hue + 360;
		
		return Math.round(hue);
	}
	
	@Override
	protected void draw()
	{
		Color color;
		
		for (int i = 0; i < _area.width; ++i)
		{
			color = Color.getHSBColor((float) i / (float) _area.width, _saturation, _brightness);
			if (!_state) // calculate grayscale color
			{
				int avg = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
				color = new Color(avg, avg, avg);
			}
			Draw.setColor(color);
			Draw.Line(_area.x + i, _area.y, _area.x + i, _area.y + _area.height);
		}
		
		if (_drawBorder)
		{
			Draw.setColor(Color.darkGray);
			Draw.Rect(new Rectangle(_area.x, _area.y, _area.width, _area.height));
			// draw also the selection:
			if (_clickedXPosition != -1)
			{
				Draw.Line(_area.x + _clickedXPosition - 1, _area.y, _area.x + _clickedXPosition - 1, _area.y + _area.height);
				Draw.Line(_area.x + _clickedXPosition + 1, _area.y, _area.x + _clickedXPosition + 1, _area.y + _area.height);
			}
		}
	}
	
	/**
	 * Does all actions when the mouse clicked on this control.
	 * 
	 * @return True when user clicked on control, false if not.
	 */
	public boolean clickOnControlElement()
	{
		if (!_state) return false;
		
		Point mPos = new Point(METRO.__mousePosition.x - 3, METRO.__mousePosition.y);
		
		if (_area.contains(mPos))
		{
			_clickedXPosition = mPos.x - _area.x;
			_clickedColor = Color.getHSBColor((float) _clickedXPosition / (float) _area.width, _saturation, _brightness);
		}
		return _area.contains(mPos);
	}
	
	@Override
	public boolean mouseClicked(int screenX, int screenY, int button)
	{
		if (clickOnControlElement())
		{
			notifyClickOnControl(this);
			return true;
		}
		return false;
	}
	
	@Override
	public void mouseReleased(int screenX, int screenY, int button)
	{
	}
	
	@Override
	public void moveElement(Point offset)
	{
		_area.add(offset);
	}
	
	@Override
	public void mouseScrolled(int amount)
	{
		if (_area.contains(METRO.__mousePosition))
		{
			_clickedXPosition += 5 * amount;
			_clickedXPosition += _area.width;
			_clickedXPosition %= _area.width;
			_clickedColor = Color.getHSBColor((float) _clickedXPosition / (float) _area.width, _saturation, _brightness);
			
			notifyClickOnControl(this);
		}
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
