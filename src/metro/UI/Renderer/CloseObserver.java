package metro.UI.Renderer;

import metro.UI.Renderable.CloseObservable;

/**
 * A close observer is able to react to "close" events of close observables (e.g. a window).
 * 
 * @author hauke
 *
 */
public interface CloseObserver
{
	/**
	 * Will be calls when an observable closes itself or has been closed.
	 * 
	 * @param container The container that has been closed.
	 */
	public void reactToClosedControlElement(CloseObservable container);
}
