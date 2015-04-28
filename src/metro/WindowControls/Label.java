/**
 * 
 */
package metro.WindowControls;

import java.awt.Color;
import java.awt.Point;

import com.badlogic.gdx.Input.Keys;

import metro.Graphics.Draw;

/**
 * Create a label to hold text with optional automatic line breaks.
 * @author hauke
 *
 */

public class Label implements ControlElement
{
	private String _text = "";
	private Point _position;
	private int _areaWidth = 0;
	private Window _windowHandle;
	private Color _color;

	/**
	 * Creates a new label.
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
		_color = Color.black;
	}
	
	/**
	 * Creates a new label.
	 * @param text The text that should be displayed.
	 * @param position The position on the screen/window (absolute)
	 */
	public Label(String text, Point position)
	{
		this(text, position, 0, null);
	}
	
	/**
	 * Creates a new label.
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
		Draw.setColor(_color);
		
		if(_areaWidth == 0)
		{
			Draw.String(_text, _position.x, _position.y);
		}
		else
		{
			Draw.String(_text, _position.x, _position.y, _areaWidth);
		}
	}

	/* (non-Javadoc)
	 * @see WindowControls.ControlElement#clickOnControlElement()
	 */
	@Override
	public boolean clickOnControlElement()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see WindowControls.ControlElement#setPosition(java.awt.Point)
	 */
	@Override
	public void setPosition(Point pos)
	{
		_position = pos;
	}

	/* (non-Javadoc)
	 * @see WindowControls.ControlElement#getPosition()
	 */
	@Override
	public Point getPosition()
	{
		return _position;
	}
	@Override
	public void moveElement(Point offset)
	{
		_position.x += offset.x;
		_position.y += offset.y;
	}
	@Override
	public void mouseScrolled(int amount) {}

	@Override
	public void keyPressed(int key){}
}