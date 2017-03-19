package metro.UI.Renderable;

import java.util.LinkedList;
import java.util.List;

import metro.UI.Renderer.CloseObserver;

/**
 * A CloseObservable will notify its observers about a close event. When this control element is closed, all observers will be notified.
 * 
 * @author hauke
 *
 */
public abstract class CloseObservable extends ControlElement implements Closable
{
	private List<CloseObserver> _listOfCloseObserver;

	/**
	 * Instantiates the list of all observers.
	 */
	protected CloseObservable()
	{
		// To not invest the creation time of new array inside ArrayList, we use LinkedList here
		_listOfCloseObserver = new LinkedList<CloseObserver>();
	}

	/**
	 * Notifies all observer about the close event passing {@code this} as argument.
	 */
	protected void notifyAboutClose()
	{
		for(CloseObserver closeObserver : _listOfCloseObserver)
		{
			closeObserver.reactToClosedControlElement(this);
		}
	}

	/**
	 * Registers a new observer. There might be multiple identical observer which will then get a callback multiple times.
	 * 
	 * @param observer The observer to add.
	 */
	public void registerCloseObserver(CloseObserver observer)
	{
		_listOfCloseObserver.add(observer);
	}

	/**
	 * Removes an observer from this observable control.
	 * 
	 * @param observer The observer to remove.
	 */
	public void removeCloseObserver(CloseObserver observer)
	{
		_listOfCloseObserver.remove(observer);
	}
}
