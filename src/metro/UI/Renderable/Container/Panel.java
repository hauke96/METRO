package metro.UI.Renderable.Container;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.UI.Renderable.Closable;
import metro.UI.Renderable.ControlElement;

/**
 * A panel is a static container which position is fixed and can only be changed programmatically.
 * 
 * @author hauke
 *
 */
public class Panel extends StaticContainer
{
	private Color _backgroundColor;

	/**
	 * Creates an empty transparent panel.
	 * 
	 * @param area The area of the panel including absolute position and size.
	 */
	public Panel(Rectangle area)
	{
		_area = area;
		_backgroundColor = Color.white;
	}

	/**
	 * Sets the background color of this panel.
	 * 
	 * @param newBackgroundColor The new background Color.
	 */
	public void setBackgroundColor(Color newBackgroundColor)
	{
		_backgroundColor = newBackgroundColor;
	}

	@Override
	protected void draw()
	{
		Fill.setColor(_backgroundColor);
		Fill.Rect(getArea());
		super.draw();
	}

	@Override
	public void moveElement(Point offset)
	{
	}

	@Override
	public void close()
	{
		for(ControlElement element : _listOfControlElements)
		{
			if(element instanceof Closable)
			{
				((Closable)element).close();
			}
		}
		_listOfControlElements.clear();
		notifyAboutClose();
	}
}
