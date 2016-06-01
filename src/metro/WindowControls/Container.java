package metro.WindowControls;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import metro.WindowControls.ContainerRenderer.Notifier;

abstract class Container extends ControlElement implements Closeable
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
	}

	public void onDraw()
	{
		generalNotifying((ControlElement control)->control.draw());
	}
	
	public void onMouseClick(int screenX, int screenY, int button)
	{
		generalNotifying((ControlElement control)->control.mouseClicked(screenX, screenY, button));
	}
	
	public void onMouseScrolled(int amount)
	{
		generalNotifying((ControlElement control)->control.mouseScrolled(amount));
	}
	
	public void onKeyPressed(int keyCode)
	{
		generalNotifying((ControlElement control)->control.keyPressed(keyCode));
	}
	
	public void onKeyUp(int keyCode)
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
	
	// TODO add methods like add(), remove(x), ...
}
