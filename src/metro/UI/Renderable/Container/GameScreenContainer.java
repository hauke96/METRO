package metro.UI.Renderable.Container;

import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;

/**
 * A GameScreenContainer is a fullscreen always-on-top container that contains all controls that are usable right now.
 * 
 * @author hauke
 *
 */
public class GameScreenContainer extends StaticContainer
{
	/**
	 * Creates a new GameScreenPanel which is fullscreen and always on top
	 */
	public GameScreenContainer()
	{
		_area = new Rectangle(0, 0, METRO.__SCREEN_SIZE.width, METRO.__SCREEN_SIZE.height);
	}
	
	@Override
	public int compareTo(AbstractContainer otherContainer)
	{
		// always on top
		return Integer.MAX_VALUE;
	}

	@Override
	public void close()
	{
		throw new UnsupportedOperationException("close() in GameScreenPanel Not Implemented!"); 
	}

	@Override
	public void moveElement(Point offset)
	{
		throw new UnsupportedOperationException("A GameScreen is always fullscreen and can not be moved!");
	}
}
