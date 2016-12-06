package metro.UI.Renderable.Container;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import metro.Common.Technical.Contract;
import metro.Exceptions.ContainerPositioningConflict;
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

	private List<Observer> _listOfAboveChangedObserver;
	private List<AbstractContainer> _theContainerBelow;

	/**
	 * Creates a new container. Will throw an {@code UninitiatedClassException} when the registration service is not initiated via {@link #setContainerRegistrationService(ContainerRegistrationService)}.
	 */
	public AbstractContainer()
	{
		if(_containerRegistrationService == null)
		{
			throw new UninitiatedClassException("There's no container registration service available. Set it before creating a container.");
		}

		// TODO use array list
		_listOfControlElements = new LinkedList<ControlElement>();
		_listOfAboveChangedObserver = new LinkedList<>();
		_theContainerBelow = new ArrayList<>();

		registerContainerInRenderer(_containerRegistrationService);
	}

	/**
	 * Initiates the {@code #_containerRegistrationService} of this {@code AbstractContainer}.
	 * 
	 * @param newContainerRegistrationService The new {@link #_containerRegistrationService} for this container.
	 */
	public static void setContainerRegistrationService(ContainerRegistrationService newContainerRegistrationService)
	{
		_containerRegistrationService = newContainerRegistrationService;
	}

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

	/**
	 * Moves this control above the given one.
	 * 
	 * @param aboveContainer The container this is above of. When {@code null} is being passed, the current order will be removed.
	 * @throws ContainerPositioningConflict When the below-relation is in conflict with the one of another container (e.g. two container are below each other)
	 */
	public void setAboveOf(AbstractContainer aboveContainer)
	{
		if(!getContainerBelow().contains(aboveContainer))
		{
			// error-case: Both container are below each other -> exception
			if(aboveContainer != null && aboveContainer.isAbove(this) && isAbove(aboveContainer))
			{
				throw new ContainerPositioningConflict();
			}

			getContainerBelow().add(aboveContainer);

			notifyAboveOfChangedObserver();
		}
	}

	/**
	 * Checks if the given container is above this one.
	 * 
	 * @param container A container to check.
	 * @return True when the given container is above this one.
	 */
	public boolean isAbove(AbstractContainer container)
	{
		Contract.RequireNotNull(_theContainerBelow);

		return container != null && compareTo(container) == 1;
	}

	/**
	 * @return All the container below this one.
	 */
	public List<AbstractContainer> getContainerBelow()
	{
		Contract.EnsureNotNull(_theContainerBelow);
		return _theContainerBelow;
	}

	/**
	 * Registers the container in the renderer so that the renderer knows that kind of container this is. This is very important for the correct rendering and input processing of container.
	 * 
	 * @param registrationService The registration service which is able to register container in the renderer.
	 */
	protected abstract void registerContainerInRenderer(ContainerRegistrationService registrationService);

	/**
	 * Adds an observer for the event that the "above of" property changed.
	 * The observer will be notices when this happens.
	 * 
	 * @param observer The new observer.
	 */
	public void registerAboveChangedObserver(Observer observer)
	{
		Contract.RequireNotNull(_listOfAboveChangedObserver);

		_listOfAboveChangedObserver.add(observer);
	}

	/**
	 * Removes all above-changed observer from this control.
	 */
	public void removeAboveChangedObserver()
	{
		_listOfAboveChangedObserver.clear();
	}

	/**
	 * Removes an observer for the "above of" property.
	 * 
	 * @param observer The observer to remove.
	 */
	public void removeAboveChangedObserver(Observer observer)
	{
		Contract.RequireNotNull(_listOfAboveChangedObserver);

		_listOfAboveChangedObserver.remove(observer);
	}

	private void generalNotifying(Notifier notifyFunction)
	{
		for(ControlElement control : _listOfControlElements)
		{
			notifyFunction.notifyControlElements(control);
		}
	}

	/**
	 * Notifies the registers observers about the "above of" property changed.
	 */
	private void notifyAboveOfChangedObserver()
	{
		Contract.RequireNotNull(_listOfAboveChangedObserver);

		for(Observer o : _listOfAboveChangedObserver)
		{
			o.update(null, null);
		}
	}

	/**
	 * Checks if this container is above the given one.
	 * 
	 * @param otherContainer The container to compare to.
	 * @return 1 when this is above, 0 if there's no relation between them and -1 when this is below the given container
	 */
	public int compareTo(AbstractContainer otherContainer)
	{
		Contract.RequireNotNull(otherContainer);

		// use this. just for better readings
		List<AbstractContainer> allContainerBelowThis = this.getContainerBelow();
		List<AbstractContainer> allContainerBelowOther = otherContainer.getContainerBelow();

		boolean otherIsBelowThis = allContainerBelowThis.contains(otherContainer);
		boolean thisIsBelowOther = allContainerBelowOther.contains(this);

		if(thisIsBelowOther && otherIsBelowThis)
		{
			// Both are above each other -> conflict
			throw new ContainerPositioningConflict();
		}

		if(!thisIsBelowOther && !otherIsBelowThis)
		{
			return 0;
		}

		if(thisIsBelowOther)
		{
			return -1;
		}

		return 1;
	}
}
