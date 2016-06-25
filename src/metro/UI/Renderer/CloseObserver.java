package metro.UI.Renderer;

import metro.UI.Renderable.CloseObservable;

public interface CloseObserver
{
	public void reactToClosedControlElement(CloseObservable container);
}
