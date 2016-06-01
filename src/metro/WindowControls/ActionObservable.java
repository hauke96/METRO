package metro.WindowControls;

import java.util.ArrayList;

/**
 * Every window control (or other classes) should inherit this class to be able to send information about state changes,
 * button clicks, etc. to other classes in a clean and simple way.
 * 
 * @author hauke
 *
 */
public class ActionObservable
{
	private ArrayList<ActionObserver> _listOfObserver;

	/**
	 * Creates the list of all observers of this observable object.
	 */
	public ActionObservable()
	{
		_listOfObserver = new ArrayList<ActionObserver>();
	}

	/**
	 * Registers an observer to this observable class.
	 * 
	 * @param observer The observer.
	 */
	public void register(ActionObserver observer)
	{
		_listOfObserver.add(observer);
	}

	/**
	 * Notifies all observers about an click event on a control.
	 * 
	 * @param arg Some useful information.
	 */
	protected void notifyClickOnControl(Object arg)
	{
		for(ActionObserver observer : _listOfObserver)
		{
			observer.clickedOnControl(arg);
		}
	}

	/**
	 * Notifies all observers about changed text of this control.
	 * 
	 * @param text The new text.
	 */
	protected void notifyGotInput(String text)
	{
		for(ActionObserver observer : _listOfObserver)
		{
			observer.gotInput(text);
		}
	}

	/**
	 * Notifies all observers about changes in the state of the observable control.
	 * 
	 * @param newState The new state.
	 */
	protected void notifyStateChanged(boolean newState)
	{
		for(ActionObserver observer : _listOfObserver)
		{
			observer.checkStateChanged(newState);
		}
	}

	/**
	 * Notifies all observers about a changed selection.
	 * 
	 * @param entry A useful string.
	 */
	protected void notifySelectionChanged(String entry)
	{
		for(ActionObserver observer : _listOfObserver)
		{
			observer.selectionChanged(entry);
		}
	}
}
