package metro.GameUI.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import metro.Common.Technical.Contract;

/**
 * Classes who are able to switch a game screen are observable by this class.
 * 
 * @author hauke
 *
 */
public abstract class GameScreenSwitchedObservable extends Observable
{
	private List<GameScreenSwitchedObserver> _observerList;

	/**
	 * Creates an empty list of observers.
	 */
	public GameScreenSwitchedObservable()
	{
		_observerList = new ArrayList<GameScreenSwitchedObserver>();
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
	 * Removes the given observer from the list. It won't get any switched calles any more.
	 * 
	 * @param observer The observer to remove.
	 */
	public void unregisterSwitchedObserver(GameScreenSwitchedObserver observer)
	{
		Contract.RequireNotNull(observer);
		
		_observerList.remove(observer);
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
