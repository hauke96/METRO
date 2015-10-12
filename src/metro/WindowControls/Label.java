/**
 * 
 */
package metro.WindowControls;

import java.awt.Color;
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

public class Label extends ActionObservable implements ControlElement
{
	private String _text = "";
	private Point _position;
	private int _areaWidth = 0;
	private Window _windowHandle;
	private Color _color;
	private boolean _enabled;

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
		_position = position;
		_windowHandle = window;
		_areaWidth = areaWidth;
		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
//		METRO.__registerControl(this);
		_color = Color.black;
		_enabled = true;
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
	public void draw()
	{
		if(_enabled) Draw.setColor(_color);
		else Draw.setColor(Color.gray);

		if(_areaWidth == 0)
		{
			Draw.String(_text, _position.x, _position.y);
		}
		else
		{
			Draw.String(_text, _position.x, _position.y, _areaWidth);
		}
	}

	@Override
	public boolean clickOnControlElement()
	{
		if(!_enabled) return false;
		Point mPos = METRO.__originalMousePosition;
		
		Rectangle pos = new Rectangle(_position.x, _position.y, Draw.getStringSize(_text).width, Draw.getStringSize(_text).height);

		return pos.contains(mPos);
	}

	@Override
	public void setPosition(Point pos)
	{
		_position = pos;
	}

	@Override
	public Point getPosition()
	{
		return _position;
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
}