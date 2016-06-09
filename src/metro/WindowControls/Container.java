package metro.WindowControls;

import java.util.LinkedList;
import java.util.List;

import metro.Exceptions.UninitiatedClassException;

abstract class Container extends CloseObservable
{
	private static ContainerRegistrationService _containerRegistrationService;
	
	interface Notifier
	{
		void notifyControlElements(ControlElement control);
	}
	
	protected List<ControlElement> _listOfControlElements;

	public Container()
	{
		if(_containerRegistrationService == null)
		{
			throw new UninitiatedClassException("There's no container registration service available. Set it before creating a container.");
		}
		
		_listOfControlElements= new LinkedList<ControlElement>();
		registerContainerInRenderer(_containerRegistrationService);
	}
	
	public static void setContainerRegistrationService(ContainerRegistrationService newContainerRegistrationService)
	{
		_containerRegistrationService = newContainerRegistrationService;
	}
	
	protected abstract void registerContainerInRenderer(ContainerRegistrationService registrationService);
	
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
	
	public void add(ControlElement control)
	{
		_listOfControlElements.add(control);
	}
	
	public void remove(ControlElement control)
	{
		_listOfControlElements.remove(control);
	}
}
