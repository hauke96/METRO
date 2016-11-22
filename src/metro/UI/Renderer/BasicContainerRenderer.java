package metro.UI.Renderer;

import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;

import metro.UI.ContainerRegistrationService;
import metro.UI.Renderable.CloseObservable;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Container.FloatingContainer;
import metro.UI.Renderable.Container.StaticContainer;

/**
 * The {@link BasicContainerRenderer} is a renderer with no special features, it just forwards every call to all {@link ContainerRegistrationService} classes.
 * 
 * @author hauke
 *
 */
public class BasicContainerRenderer implements CloseObserver, ContainerRenderer
{
	interface ContainerCollectionNotifier
	{
		void notifyAllContainer(List<AbstractContainer> listOfContainer);
	}

	interface ContainerNotifier
	{
		void notifyContainer(AbstractContainer container);
	}

	interface Notifier
	{
		void executeFunctions();
	}

	private Comparator<AbstractContainer> _controlsAboveComparator;

	private Observer _staticContainerAboveChangedObserver;
	private Observer _floatingContainerAboveChangedObserver;

	private List<AbstractContainer> _listOfFloatingContainer;
	private List<AbstractContainer> _listOfStaticContainer;

	/**
	 * Creates a basic renderer with empty fields.
	 */
	public BasicContainerRenderer()
	{
		_listOfStaticContainer = new CopyOnWriteArrayList<AbstractContainer>();
		_listOfFloatingContainer = new CopyOnWriteArrayList<AbstractContainer>();

		_controlsAboveComparator = new Comparator<AbstractContainer>()
		{
			@Override
			public int compare(AbstractContainer container1, AbstractContainer container2)
			{
				return container1.compareTo(container2);
			}
		};

		// FIXME sorting does not work :(
		_staticContainerAboveChangedObserver = (o, arg) -> _listOfStaticContainer.sort(_controlsAboveComparator);
		_floatingContainerAboveChangedObserver = (o, arg) -> _listOfFloatingContainer.sort(_controlsAboveComparator);

		ContainerRegistrationService registrationService = new ContainerRegistrationService();
		registrationService.setRenderer(this);
		AbstractContainer.setContainerRegistrationService(registrationService);
	}

	@Override
	public void registerStaticContainer(StaticContainer renderable)
	{
		_listOfStaticContainer.add(renderable);
		renderable.registerCloseObserver(this);
		renderable.registerAboveChangedObserver(_staticContainerAboveChangedObserver);
		_staticContainerAboveChangedObserver.update(null, null);
	}

	@Override
	public void registerFloatingContainer(FloatingContainer renderable)
	{
		_listOfFloatingContainer.add(renderable);
		renderable.registerCloseObserver(this);
		renderable.registerAboveChangedObserver(_floatingContainerAboveChangedObserver);
		_floatingContainerAboveChangedObserver.update(null, null);
	}

	@Override
	public void notifyDraw()
	{
		normalNotifying((AbstractContainer container) -> container.drawControl());
	}

	@Override
	public boolean notifyMouseClick(int screenX, int screenY, int button)
	{
		class ModifiableBoolean
		{
			private boolean value = false;
		}
		ModifiableBoolean isClickedValue = new ModifiableBoolean();

		Notifier notifier = () -> {
			ContainerCollectionNotifier containerNotifier = (List<AbstractContainer> l) -> {
				AbstractContainer clickedContainer = null;

				for(int i = l.size() - 1; i >= 0; i--)
				{
					clickedContainer = l.get(i);
					if(clickedContainer.isInArea(screenX, screenY))
					{
						break;
					}
				}

				// // Move window to the top if no control on this window has been clicked
				if(clickedContainer != null)
				{
					l.remove(clickedContainer);
					l.add(clickedContainer);

					isClickedValue.value = clickedContainer.mouseClicked(screenX, screenY, button);
				}
			};

			containerNotifier.notifyAllContainer(_listOfFloatingContainer);
			if(!isClickedValue.value)
			{
				containerNotifier.notifyAllContainer(_listOfStaticContainer);// TODO determine on which control the focus is
			}
		};

		generalNotifying(notifier);

		return isClickedValue.value;
	}

	@Override
	public void notifyMouseReleased(int screenX, int screenY, int button)
	{
		normalNotifying((AbstractContainer container) -> container.mouseReleased(screenX, screenY, button));
	}

	@Override
	public void notifyMouseScrolled(int amount)
	{
		normalNotifying((AbstractContainer container) -> container.mouseScrolled(amount));
	}

	@Override
	public void notifyKeyPressed(int keyCode)
	{
		normalNotifying((AbstractContainer container) -> container.keyPressed(keyCode));
	}

	@Override
	public void notifyKeyUp(int keyCode)
	{
		normalNotifying((AbstractContainer container) -> container.keyUp(keyCode));
	}

	private void normalNotifying(ContainerNotifier notifyFunction)
	{
		// TODO determine on which control the focus is
		Notifier notifier = () -> {
			for(AbstractContainer container : _listOfStaticContainer)
			{
				notifyFunction.notifyContainer(container);
			}

			for(AbstractContainer window : _listOfFloatingContainer)
			{
				notifyFunction.notifyContainer(window);
			}
		};
		generalNotifying(notifier);
	}

	private void generalNotifying(Notifier notifier)
	{
		notifier.executeFunctions();
	}

	@Override
	public void reactToClosedControlElement(CloseObservable container)
	{
		_listOfStaticContainer.remove(container);
		_listOfFloatingContainer.remove(container);

		_staticContainerAboveChangedObserver.update(null, null);
		_floatingContainerAboveChangedObserver.update(null, null);

		if(container instanceof AbstractContainer)
		{
			AbstractContainer abstractContainer = (AbstractContainer)container;
			abstractContainer.removeAboveChangedObserver(_staticContainerAboveChangedObserver);
			abstractContainer.removeAboveChangedObserver(_floatingContainerAboveChangedObserver);
		}
	}
}