/**
 * 
 */
package WindowControls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import Game.METRO;

/**
 * @author hauke
 *
 */
public class Label implements ControlElement
{
	private String _text = "";
	private Point _position;
	private Window _windowHandle;

	/**
	 * Creates a new label.
	 * @param text The text that should be displayed.
	 * @param position The position on the screen/window (absolute)
	 * @param window The window it's on.
	 */
	public Label(String text, Point position, Window window)
	{
		_text = text;
		_position = position;
		_windowHandle = window;
		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
	}
	/**
	 * Creates a new label.
	 * @param text The text that should be displayed.
	 * @param position The position on the screen/window (absolute)
	 */
	public Label(String text, Point position)
	{
		this(text, position, null);
	}

	/* (non-Javadoc)
	 * @see WindowControls.ControlElement#update()
	 */
	@Override
	public void update()
	{
	}

	/**
	 * Draws the label onto the screen. The Color of the Graphics-handle will be restores after drawing.
	 */
	@Override
	public void draw(Graphics g)
	{
		Color c = g.getColor();
		g.setColor(METRO.__metroBlue);
		g.drawString(_text, _position.x, _position.y);
		g.setColor(c);
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
	}

	/* (non-Javadoc)
	 * @see WindowControls.ControlElement#getPosition()
	 */
	@Override
	public Point getPosition()
	{
		return null;
	}

}
