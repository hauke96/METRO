package metro.UI.Renderable;

import metro.UI.Renderable.Container.Window;

/**
 * Who ever wants to be able to get information about state changed, button clicks, etc. of window controls,
 * should implement this interface.
 * 
 * All implementations of this method are optional and keeps the code of the implementation cleaner and smaller.
 * 
 * @author hauke
 *
 */
public abstract class ActionObserver
{
	/**
	 * Is called when a control element has been clicked.
	 * 
	 * @param arg Any message returning from the control.
	 */
	public void clickedOnControl(Object arg)
	{
	}

	/**
	 * Gets called when an input control got new input (or input changed).
	 * 
	 * @param text The new text of the input control
	 */
	public void gotInput(String text)
	{
	}

	/**
	 * Is called when the check state changed
	 * 
	 * @param newState The new state of the control.
	 */
	public void checkStateChanged(boolean newState)
	{
	}

	/**
	 * Is called when an entry of a list control gets selected.
	 * 
	 * @param entry The text of the selected entry.
	 */
	public void selectionChanged(String entry)
	{
	}

	/**
	 * Is called when a control (e.g. a window) is closed.
	 * 
	 * @param window The control that has been closed.
	 */
	public void closed(Window window)
	{
	}
}
