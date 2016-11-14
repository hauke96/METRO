/**
 * 
 */
package metro.UI.Renderable.Controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.UI.Renderable.ControlElement;

/**
 * Create a label to hold text with optional automatic line breaks.
 * 
 * @author hauke
 *
 */

public class Label extends ControlElement
{
	private int _areaWidth = 0;
	private Color _color;
	private Color _underlineColor;
	protected boolean _underlined;

	/**
	 * Creates a new label.
	 * 
	 * @param text The text that should be displayed.
	 * @param position The position on the screen/window (absolute)
	 */
	public Label(String text, Point position)
	{
		this(text, position, 0);
	}

	/**
	 * Creates a new label.
	 * 
	 * @param text The text that should be displayed.
	 * @param position The position on the screen/window (absolute)
	 * @param areaWidth Maximum width of label for automatic line breaks.
	 */
	public Label(String text, Point position, int areaWidth)
	{
		_text = text;
		Dimension size = calcSize();
		_area = new Rectangle(position.x, position.y, size.width, size.height);
		_areaWidth = areaWidth;
		_color = Color.black;
		_underlineColor = _color;
	}

	/**
	 * Sets the color of the text to newColor.
	 * 
	 * @param newColor The new color.
	 */
	public void setColor(Color newColor)
	{
		_color = newColor;
	}

	/**
	 * Draws the label onto the screen. The Color of the Graphics-handle will be restores after drawing.
	 */
	@Override
	protected void draw()
	{
		if(_state) Draw.setColor(_color);
		else Draw.setColor(Color.gray);

		if(_areaWidth == 0)
		{
			Draw.String(_text, _area.x, _area.y);
		}
		else
		{
			Draw.String(_text, _area.x, _area.y, _areaWidth);
		}

		if(_underlined)
		{
			Dimension textArea = Draw.getStringSize(_text);
			Draw.setColor(_underlineColor);
			Draw.Line(_area.x, _area.y + textArea.height + 2, _area.x + textArea.width, _area.y + textArea.height + 2);
		}
	}

	/**
	 * Does all actions when the mouse clicked on this control.
	 * 
	 * @return True when user clicked on control, false if not.
	 */
	private boolean mouseOnControlElement()
	{
		if(!_state) return false;
		Point mPos = METRO.__originalMousePosition;

		Rectangle pos = new Rectangle(_area.x, _area.y, Draw.getStringSize(_text).width, Draw.getStringSize(_text).height);

		return pos.contains(mPos);
	}

	@Override
	public boolean mouseClicked(int screenX, int screenY, int button)
	{
		if(mouseOnControlElement())
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
		_area.x += offset.x;
		_area.y += offset.y;
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
	public Rectangle getArea()
	{
		Dimension size = calcSize();
		return new Rectangle(_area.x, _area.y, size.width, size.height);
	}

	private Dimension calcSize()
	{
		return new Dimension(Draw.getStringSize(_text).width, Draw.getStringSize(_text).height);
	}

	/**
	 * Let the text be underlined or not.
	 * 
	 * @param underlined True to underline the text, false to not do that.
	 */
	public void underlined(boolean underlined)
	{
		_underlined = underlined;
	}

	/**
	 * Colors the underline line into the given color.
	 * 
	 * @param color The color of the underline line.
	 */
	public void setUnderlineColor(Color color)
	{
		_underlineColor = color;
	}
}