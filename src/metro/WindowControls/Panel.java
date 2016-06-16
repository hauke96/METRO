package metro.WindowControls;

import java.awt.Point;
import java.awt.Rectangle;

public class Panel extends StaticContainer
{
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
		notifyAboutClose();
	}
}
