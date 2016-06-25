package metro.UI.Renderable;

import java.util.LinkedList;
import java.util.List;

import metro.UI.Renderer.CloseObserver;

public abstract class CloseObservable extends ControlElement implements Closable
{
	private List<CloseObserver> _listOfCloseObserver;
	
	protected CloseObservable()
	{
		_listOfCloseObserver = new LinkedList<CloseObserver>();
	}
	
	protected void notifyAboutClose()
	{
		for(CloseObserver closeObserver:_listOfCloseObserver)
		{
			closeObserver.reactToClosedControlElement(this);
		}
	}
	
	public void registerCloseObserver(CloseObserver observer)
	{
		_listOfCloseObserver.add(observer);
	}
	
	public void removeCloseObserver(CloseObserver observer)
	{
		_listOfCloseObserver.remove(observer);
	}
}
