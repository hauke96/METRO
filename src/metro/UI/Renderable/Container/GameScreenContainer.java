package metro.UI.Renderable.Container;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import metro.METRO;
import metro.Common.Technical.Contract;
import metro.GameUI.Screen.GameScreen;
import metro.GameUI.Screen.GameScreenSwitchedObserver;
import metro.UI.Renderer.ContainerRenderer;

/**
 * A GameScreenContainer is a fullscreen always-on-top container that contains all controls that are usable right now.
 * 
 * @author hauke
 *
 */
public class GameScreenContainer extends StaticContainer
{
	private List<GameScreenSwitchedObserver> _observerList;
	private ContainerRenderer _containerRenderer;

	/**
	 * Creates a new GameScreenPanel which is fullscreen and always on top
	 * @param renderer The renderer for the controls
	 */
	public GameScreenContainer(ContainerRenderer renderer)
	{
		_area = new Rectangle(0, 0, METRO.__SCREEN_SIZE.width, METRO.__SCREEN_SIZE.height);
		_observerList = new ArrayList<>();
		_containerRenderer = renderer;
	}

	public void draw()
	{
		// TODO call the renderer here WHEN it works
		// _containerRenderer.notifyDraw();
	}

	@Override
	public int compareTo(AbstractContainer otherContainer)
	{
		Contract.RequireNotNull(otherContainer);

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

	/**
	 * Adds the given observer to the list. It will be notified when a game screen will be switched.
	 * 
	 * @param observer The observer to add.
	 */
	public void registerSwitchedObserver(GameScreenSwitchedObserver observer)
	{
		Contract.RequireNotNull(observer);

		_observerList.add(observer);
	}

	/**
	 * Notifies all observer about the switch of the game screen and passes the new game screen to them.
	 * 
	 * @param newGameScreen The new game screen to switch to.
	 */
	public void notifyAllAboutSwitch(GameScreen newGameScreen)
	{
		Contract.RequireNotNull(_observerList);
		Contract.RequireNotNull(newGameScreen);

		for(GameScreenSwitchedObserver observer : _observerList)
		{
			observer.reactToGameScreenSwitch(newGameScreen);
		}
	}
}
