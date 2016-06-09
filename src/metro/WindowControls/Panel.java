package metro.WindowControls;

import java.awt.Point;
import java.awt.Rectangle;

public class Panel extends StaticContainer
{

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
