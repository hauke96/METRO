package metro.WindowControls;

import java.util.LinkedList;
import java.util.List;

abstract class Container extends ContainerRenderable
{
	interface Notifier
	{
		void notifyControlElements(ControlElement control);
	}
	
	protected List<ControlElement> _listOfControlElements;
	private List<CloseObserver> _listOfCloseObserver;

	public Container()
	{
		_listOfControlElements= new LinkedList<ControlElement>();
		_listOfCloseObserver = new LinkedList<CloseObserver>();
		registerContainerInRenderer();
	}
	
	protected abstract void registerContainerInRenderer();
	
	@Override
	void draw()
	{
		generalNotifying((ControlElement control)->control.draw());
	}
	
	@Override
	boolean mouseClicked(int screenX, int screenY, int button)
	{
		generalNotifying((ControlElement control)->control.mouseClicked(screenX, screenY, button));
		return false;
	}

	@Override
	void mouseScrolled(int amount)
	{
		generalNotifying((ControlElement control)->control.mouseScrolled(amount));
	}

	@Override
	void keyPressed(int keyCode)
	{
		generalNotifying((ControlElement control)->control.keyPressed(keyCode));
	}

	@Override
	void keyUp(int keyCode)
	{
		generalNotifying((ControlElement control)->control.keyUp(keyCode));
	}
	
	private void generalNotifying(Notifier notifyFunction)
	{
		for(ControlElement control: _listOfControlElements)
		{
			notifyFunction.notifyControlElements(control);
		}
	}
	
	public void notifyAboutClose()
	{
		for(CloseObserver closeObserver:_listOfCloseObserver)
		{
			closeObserver.closed(this);
		}
	}
	
	public void add(ControlElement control)
	{
		_listOfControlElements.add(control);
	}
	
	public void remove(ControlElement control)
	{
		_listOfControlElements.remove(control);
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
