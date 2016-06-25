package metro.UI.Renderable.Container;

import java.awt.Point;
import java.awt.Rectangle;

import metro.UI.Renderable.Closable;
import metro.UI.Renderable.ControlElement;

public class Panel extends StaticContainer
{
	//TODO draw panel with background color (which is transparent at default)
	
	/**
	 * Creates an empty transparent panel.
	 * 
	 * @param area The area of the panel including absolute position and size.
	 */
	public Panel(Rectangle area)
	{
		_area = area;
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
