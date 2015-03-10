/**
 * 
 */
package WindowControls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import Game.METRO;

/**
 * @author hauke
 *
 */
public class Label implements ControlElement
{
	private String _text = "";
	private Point _position;
	private int _areaWidth = 0;
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
		_areaWidth = 0;
		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
	}
	public Label(String text, Point position, int areaWidth, Window window)
	{
		_text = text;
		_position = position;
		_windowHandle = window;
		_areaWidth = areaWidth;
		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
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
	public Label(String text, Point position, int areaWidth)
	{
		this(text, position, areaWidth, null);
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
		g.setFont(METRO.__stdFont);
		g.setColor(Color.black);
		
		if(_areaWidth == 0)
		{
			g.drawString(_text, _position.x, _position.y);
		}
		else
		{
			int stringHeight = g.getFontMetrics(METRO.__stdFont).getHeight(),
				vOffset = 0;
			String[] segments = _text.split("\n"); // split by new line
			
			for(String segment : segments)
			{
				String[] subSegments = segment.split(" "); // each word
				String line = "";
				
				// recunstruct string with length < area width
				for(int i = 0; i < subSegments.length; i++)
				{
					if(g.getFontMetrics(METRO.__stdFont).stringWidth(line + " " + subSegments[i]) >= _areaWidth) // if next addition would be out of area
					{
						g.drawString(line, _position.x, _position.y + vOffset);
						vOffset += stringHeight + 2; // y-pos for next line
						line = subSegments[i] + " "; // choose first char for next line
					}
					else // if new addition is in area
					{
						line += subSegments[i] + " ";
					}
				}

				g.drawString(line, _position.x, _position.y + vOffset);
				vOffset += stringHeight + 2; // y-pos for next line
			}
		}
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
