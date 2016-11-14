package metro.UI.Renderable.Container;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
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
	private Color _backgroundColor,
		_borderColor;
	private boolean _drawBorder;

	/**
	 * Creates an empty transparent panel.
	 * 
	 * @param area The area of the panel including absolute position and size.
	 */
	public Panel(Rectangle area)
	{
		this(area, false);
	}

	/**
	 * Creates an empty transparent panel with a specified border flag.
	 * The default border color is {@link METRO.__metroBlue} and can be changed via the {@link setDrawBorder}.
	 * 
	 * @param area The area of the panel including absolute position and size.
	 * @param drawBorder The flag if the border should be drawed (true) or not drawed (false).
	 */
	public Panel(Rectangle area, boolean drawBorder)
	{
		_area = area;
		_backgroundColor = Color.white;
		_borderColor = METRO.__metroBlue;
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

	/**
	 * Sets the state to draw the border or don't draw the border.
	 * 
	 * @param drawBorder True to draw the border, false to don't draw the border.
	 * @param borderColor The color of the border.
	 */
	public void setDrawBorder(boolean drawBorder, Color borderColor)
	{
		_drawBorder = drawBorder;
		_borderColor = borderColor;
	}

	@Override
	protected void draw()
	{
		Fill.setColor(_backgroundColor);
		Fill.Rect(getArea());

		if(_drawBorder)
		{
			Draw.setColor(METRO.__metroBlue);
			Draw.Rect(_area.x, _area.y, _area.width + 1, _area.height + 1);
		}

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
