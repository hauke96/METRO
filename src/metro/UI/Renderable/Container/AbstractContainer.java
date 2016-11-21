package metro.UI.Renderable.Container;

import java.awt.Point;
import java.util.Comparator;
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
public abstract class AbstractContainer extends CloseObservable implements Comparator<AbstractContainer>
{
	private static ContainerRegistrationService _containerRegistrationService;

	interface Notifier
	{
		void notifyControlElements(ControlElement control);
	}

	protected List<ControlElement> _listOfControlElements;
	private List<Observer> _listOfAboveChangedObserver;
	private AbstractContainer _aboveContainer;

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
		_listOfAboveChangedObserver = new LinkedList<>();
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
	 * Removes an observer for the "above of" property.
	 * 
	 * @param observer The observer to remove.
	 */
	public void removeAboveChangedObserver(Observer observer)
	{
		Contract.RequireNotNull(_listOfAboveChangedObserver);

		_listOfAboveChangedObserver.remove(observer);
	}

	/**
	 * Moves this control above the given one.
	 * 
	 * @param aboveContainer The container this is above of. When {@code null} is being passed, the current order will be removed.
	 * @throws ContainerPositioningConflict When the below-relation is in conflict with the one of another container (e.g. two container are below each other)
	 */
	public void setAboveOf(AbstractContainer aboveContainer)
	{
		// error-case: Both container are below each other -> exception
		if(aboveContainer != null && aboveContainer.isBelow(this) && isBelow(aboveContainer))
		{
			throw new ContainerPositioningConflict();
		}

		_aboveContainer = aboveContainer;

		notifyAboveOfChangedObserver();
	}

	/**
	 * Checks if the given container is below this one.
	 * 
	 * @param container A container to check.
	 * @return True when the given container is below this one.
	 */
	public boolean isBelow(AbstractContainer container)
	{
		return _aboveContainer != null && _aboveContainer.equals(container);
	}

	/**
	 * @return The container below this one.
	 */
	public AbstractContainer getContainerBelow()
	{
		return _aboveContainer;
	}

	/**
	 * Notifies the registers observers about the "above of" property changed.
	 */
	private void notifyAboveOfChangedObserver()
	{
		Contract.RequireNotNull(_listOfAboveChangedObserver);

		for(Observer o : _listOfAboveChangedObserver)
		{
			o.notify();
		}
	}

	/**
	 * Checks if this container is above the given one.
	 * 
	 * @param container2 The container to compare to.
	 * @return 1 when this is above, 0 if there's no relation between them and -1 when this is below the given container
	 * @throws ContainerPositioningConflict
	 */
	public int compareTo(AbstractContainer container2)
	{
		Contract.RequireNotNull(container2);

		// TODO write test for this

		// Just for better readings
		AbstractContainer container1 = this;

		AbstractContainer belowContainer1 = container1.getContainerBelow();
		AbstractContainer belowContainer2 = container2.getContainerBelow();
		/*
		@formatter:off
		
		    Question: Container1 above Container2?
		    
			1 is never null, because 1 is "this"
			
			return 0:
			    (1) 2 not under 1
			
			return 1: (1 over 2)
			    (2) 2 under 1
			
			return -1: (2 over 1)
			    (3) 1 under 2
			    
			1 is below 2 and 2 is below 1 -> exception
			
		@formatter:on
		*/

		// error-case: Both container are below each other
		if(belowContainer1 != null &&
			belowContainer2 != null &&
			equals(belowContainer2) &&
			container2.equals(belowContainer1))
		{
			throw new ContainerPositioningConflict();
		}

		// rule (1): This container (container1) has a container below it and it is container2
		if(belowContainer1 != null && container2.equals(belowContainer1))
		{
			return 1;
		}

		// rulse (2): The container2 has a container below and it is "this" (container1)
		if(belowContainer2 != null && equals(belowContainer2))
		{
			return -1;
		}

		// rule (0): This is not the container below 2
		return 0;
	}
}
