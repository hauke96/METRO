package metro.UI.Renderer;

import java.util.List;
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

	private List<AbstractContainer> _listOfFloatingContainer;
	private List<AbstractContainer> _listOfStaticContainer;

	/**
	 * Creates a basic renderer with empty fields.
	 */
	public BasicContainerRenderer()
	{
		_listOfStaticContainer = new CopyOnWriteArrayList<AbstractContainer>();
		_listOfFloatingContainer = new CopyOnWriteArrayList<AbstractContainer>();

		ContainerRegistrationService registrationService = new ContainerRegistrationService();
		registrationService.setRenderer(this);
		AbstractContainer.setContainerRegistrationService(registrationService);
	}

	@Override
	public void registerStaticContainer(StaticContainer renderable)
	{
		_listOfStaticContainer.add(renderable);
		renderable.registerCloseObserver(this);
	}

	@Override
	public void registerFloatingContainer(FloatingContainer renderable)
	{
		_listOfFloatingContainer.add(renderable);
		renderable.registerCloseObserver(this);
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
						clickedContainer.mouseClicked(screenX, screenY, button);
						isClickedValue.value = true;
						break;
					}
				}

				// Move window to the top if no control on this window has been clicked
				if(clickedContainer != null)
				{
					l.remove(clickedContainer);
					l.add(clickedContainer);
				}
			};

			containerNotifier.notifyAllContainer(_listOfStaticContainer);// TODO determine on which control the focus is

			containerNotifier.notifyAllContainer(_listOfFloatingContainer);
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
	}
}