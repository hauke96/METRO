package metro.UI.Renderable.Container;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import metro.Exceptions.UninitiatedClassException;
import metro.UI.ContainerRegistrationService;
import metro.UI.Renderable.CloseObservable;
import metro.UI.Renderable.ControlElement;

public abstract class AbstractContainer extends CloseObservable
{
	private static ContainerRegistrationService _containerRegistrationService;

	interface Notifier
	{
		void notifyControlElements(ControlElement control);
	}

	protected List<ControlElement> _listOfControlElements;

	public AbstractContainer()
	{
		if(_containerRegistrationService == null)
		{
			throw new UninitiatedClassException("There's no container registration service available. Set it before creating a container.");
		}

		_listOfControlElements = new LinkedList<ControlElement>();
		registerContainerInRenderer(_containerRegistrationService);
	}

	public static void setContainerRegistrationService(ContainerRegistrationService newContainerRegistrationService)
	{
		_containerRegistrationService = newContainerRegistrationService;
	}

	protected abstract void registerContainerInRenderer(ContainerRegistrationService registrationService);

	@Override
	protected void draw()
	{
		generalNotifying((ControlElement control) -> control.drawControl());
	}

	@Override
	public boolean mouseClicked(int screenX, int screenY, int button)
	{
		generalNotifying((ControlElement control) -> control.mouseClicked(screenX, screenY, button));
		return false;
	}

	@Override
	public void mouseReleased(int screenX, int screenY, int button)
	{
		generalNotifying((ControlElement control) -> control.mouseReleased(screenX, screenY, button));
	}

	@Override
	public void mouseScrolled(int amount)
	{
		generalNotifying((ControlElement control) -> control.mouseScrolled(amount));
	}

	@Override
	public void keyPressed(int keyCode)
	{
		generalNotifying((ControlElement control) -> control.keyPressed(keyCode));
	}

	@Override
	public void keyUp(int keyCode)
	{
		generalNotifying((ControlElement control) -> control.keyUp(keyCode));
	}

	private void generalNotifying(Notifier notifyFunction)
	{
		for(ControlElement control : _listOfControlElements)
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

	@Override
	public void setPosition(Point pos)
	{
		Point offset = new Point(pos.x - getPosition().x, pos.y - getPosition().y);
		for(ControlElement element : _listOfControlElements)
		{
			element.moveElement(offset);
		}
		super.setPosition(pos);
	}
}
