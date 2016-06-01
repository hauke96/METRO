/**
 * 
 */
package metro.WindowControls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.Graphics.Draw;

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

	/**
	 * Creates a new label.
	 * 
	 * @param text The text that should be displayed.
	 * @param position The position on the screen/window (absolute)
	 * @param window The window it's on.
	 */
	public Label(String text, Point position, Window window)
	{
		this(text, position, 0, window);
	}

	/**
	 * Creates a new label.
	 * 
	 * @param text The text that should be displayed.
	 * @param position The position on the screen/window (absolute)
	 * @param areaWidth Maximum width of label for automatic line breaks.
	 * @param window The window it's on.
	 */
	public Label(String text, Point position, int areaWidth, Window window)
	{
		_text = text;
		Dimension size = calcSize();
		_area = new Rectangle(position.x, position.y, size.width, size.height);
		_areaWidth = areaWidth;
		_color = Color.black;
	}

	/**
	 * Creates a new label.
	 * 
	 * @param text The text that should be displayed.
	 * @param position The position on the screen/window (absolute)
	 */
	public Label(String text, Point position)
	{
		this(text, position, 0, null);
	}

	/**
	 * Creates a new label.
	 * 
	 * @param text The text that should be displayed.
	 * @param position The position on the screen/window (absolute)
	 * @param areaWidth The maximum width of the label. This will enable automatic line breaks.
	 */
	public Label(String text, Point position, int areaWidth)
	{
		this(text, position, areaWidth, null);
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
	void draw()
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
	}

	/**
	 * Does all actions when the mouse clicked on this control.
	 * 
	 * @return True when user clicked on control, false if not.
	 */
	public boolean clickOnControlElement()
	{
		if(!_state) return false;
		Point mPos = METRO.__originalMousePosition;

		Rectangle pos = new Rectangle(_area.x, _area.y, Draw.getStringSize(_text).width, Draw.getStringSize(_text).height);

		return pos.contains(mPos);
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
		_area.x += offset.x;
		_area.y += offset.y;
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
}