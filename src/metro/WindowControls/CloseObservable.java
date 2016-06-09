package metro.WindowControls;

import java.util.LinkedList;
import java.util.List;

abstract class CloseObservable extends ControlElement implements Closable
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
	
	void registerCloseObserver(CloseObserver observer)
	{
		_listOfCloseObserver.add(observer);
	}
	
	void removeCloseObserver(CloseObserver observer)
	{
		_listOfCloseObserver.remove(observer);
	}
}
