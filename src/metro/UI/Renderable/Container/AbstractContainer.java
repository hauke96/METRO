package metro.UI.Renderable.Container;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import metro.Exceptions.UninitiatedClassException;
import metro.UI.ContainerRegistrationService;
import metro.UI.Renderable.CloseObservable;
import metro.UI.Renderable.ControlElement;

/**
 * A AbstractContainer is a closable and close observable control element which can itself store control elements.
 * This control has to draw the controls at its own and also pass input event to them.
 * To be able to react to events which are suitable for this kind of container, the {@code AbstractContainer} works closely together with the {@code ContainerRegistrationService}.
 * 
 * @author hauke
 *
 */
public abstract class AbstractContainer extends CloseObservable
{
	private static ContainerRegistrationService _containerRegistrationService;

	interface Notifier
	{
		void notifyControlElements(ControlElement control);
	}

	protected List<ControlElement> _listOfControlElements;

	/**
	 * Creates a new container. Will throw an {@code UninitiatedClassException} when the registration service is not initiated via {@link #setContainerRegistrationService(ContainerRegistrationService)}.
	 */
	public AbstractContainer()
	{
		if(_containerRegistrationService == null)
		{
			throw new UninitiatedClassException("There's no container registration service available. Set it before creating a container.");
		}

		_listOfControlElements = new LinkedList<ControlElement>();
		registerContainerInRenderer(_containerRegistrationService);
	}

	/**
	 * Initiates the {@code #_containerRegistrationService} of this {@code AbstractContainer}.
	 * 
	 * @param newContainerRegistrationService The new {@link #_containerRegistrationService} for this container.
	 */
	@SuppressWarnings("javadoc")
	public static void setContainerRegistrationService(ContainerRegistrationService newContainerRegistrationService)
	{
		_containerRegistrationService = newContainerRegistrationService;
	}

	/**
	 * Registers the container in the renderer so that the renderer knows that kind of container this is. This is very important for the correct rendering and input processing of container.
	 * 
	 * @param registrationService The registration service which is able to register container in the renderer.
	 */
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

	/**
	 * Adds a control to the container which will display it and process input events.
	 * 
	 * @param control The new control to add.
	 */
	public void add(ControlElement control)
	{
		_listOfControlElements.add(control);
	}

	/**
	 * Will remove a control from the container. The control will not be visible after this and will not get any input anymore.
	 * 
	 * @param control The control that should be removed.
	 */
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
