package metro.WindowControls;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
		void notifyAllContainer(List<? extends Container> listOfContainer);
	}

	interface ContainerNotifier
	{
		void notifyContainer(Container container);
	}

	interface Notifier
	{
		void executeFunctions();
	}

	private List<FloatingContainer> _listOfFloatingContainer;
	private List<StaticContainer> _listOfStaticContainer;

	/**
	 * Creates a basic renderer with empty fields.
	 */
	public BasicContainerRenderer()
	{
		_listOfStaticContainer = new CopyOnWriteArrayList<StaticContainer>();
		_listOfFloatingContainer = new CopyOnWriteArrayList<FloatingContainer>();

		ContainerRegistrationService registrationService = new ContainerRegistrationService();
		registrationService.setRenderer(this);
		Container.setContainerRegistrationService(registrationService);
	}

	@Override
	public void registerRenderable(StaticContainer renderable)
	{
		_listOfStaticContainer.add(renderable);
		renderable.registerCloseObserver(this);
	}

	@Override
	public void registerFloatingRenderable(FloatingContainer renderable)
	{
		_listOfFloatingContainer.add(renderable);
		renderable.registerCloseObserver(this);
	}

	@Override
	public void notifyDraw()
	{
		normalNotifying((Container container) -> container.draw());
	}

	@Override
	public boolean notifyMouseClick(int screenX, int screenY, int button)
	{
		// TODO create observer for the click on a not-focused window and bring it to the front

		class ModifiableBoolean
		{
			private boolean value = false;
		}
		ModifiableBoolean isClickedValue = new ModifiableBoolean();

		Notifier notifier = () -> {
			ContainerCollectionNotifier containerNotifier = (List<? extends Container> l) -> {
				Container currentContainer;
				for(int i = l.size() - 1; i >= 0; i--)
				{
					currentContainer = l.get(i);
					if(currentContainer.isInArea(screenX, screenY))
					{
						currentContainer.mouseClicked(screenX, screenY, button);
						isClickedValue.value = true;
						break;
					}
				}
			};

			containerNotifier.notifyAllContainer(_listOfStaticContainer);// TODO determine on which control the focus is

			containerNotifier.notifyAllContainer(_listOfFloatingContainer);
		};

		generalNotifying(notifier);

		return isClickedValue.value;
	}

	@Override
	public void notifyMouseScrolled(int amount)
	{
		normalNotifying((Container container) -> container.mouseScrolled(amount));
	}

	@Override
	public void notifyKeyPressed(int keyCode)
	{
		normalNotifying((Container container) -> container.keyPressed(keyCode));
	}

	@Override
	public void notifyKeyUp(int keyCode)
	{
		normalNotifying((Container container) -> container.keyUp(keyCode));
	}

	private void normalNotifying(ContainerNotifier notifyFunction)
	{
		// TODO determine on which control the focus is
		Notifier notifier = () -> {
			for(StaticContainer container : _listOfStaticContainer)
			{
				notifyFunction.notifyContainer(container);
			}

			for(FloatingContainer window : _listOfFloatingContainer)
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
	}
}