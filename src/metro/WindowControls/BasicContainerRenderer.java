package metro.WindowControls;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The {@link BasicContainerRenderer} is a renderer with no special features, it just forwards every call to all {@link ContainerRenderable} classes.
 * 
 * @author hauke
 *
 */
public class BasicContainerRenderer implements CloseObserver, ContainerRenderer
{
	interface Notifier
	{
		void notifyRenderable(ContainerRenderable container);
	}

	private List<FloatingContainer> _listOfFloatingContainer;
	private List<StaticContainer> _listOfRenderables;

	/**
	 * Creates a basic renderer with empty fields.
	 */
	public BasicContainerRenderer()
	{
		_listOfRenderables = new CopyOnWriteArrayList<StaticContainer>();
		_listOfFloatingContainer = new CopyOnWriteArrayList<FloatingContainer>();
		ContainerRenderable.setRenderer(this);
	}

	@Override
	public void registerRenderable(StaticContainer renderable)
	{
		_listOfRenderables.add(renderable);
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
		generalNotifying((ContainerRenderable container) -> container.draw());
	}

	@Override
	public void notifyMouseClick(int screenX, int screenY, int button)
	{
		generalNotifying((ContainerRenderable container) -> container.mouseClicked(screenX, screenY, button));
	}

	@Override
	public void notifyMouseScrolled(int amount)
	{
		generalNotifying((ContainerRenderable container) -> container.mouseScrolled(amount));
	}

	@Override
	public void notifyKeyPressed(int keyCode)
	{
		generalNotifying((ContainerRenderable container) -> container.keyPressed(keyCode));
	}

	@Override
	public void notifyKeyUp(int keyCode)
	{
		generalNotifying((ContainerRenderable container) -> container.keyUp(keyCode));
	}

	private void generalNotifying(Notifier notifyFunction)
	{
		// TODO determine on which control the focus is
		for(ContainerRenderable container : _listOfRenderables)
		{
			notifyFunction.notifyRenderable(container);
		}

		for(FloatingContainer window : _listOfFloatingContainer)
		{
			notifyFunction.notifyRenderable(window);
		}
	}

	@Override
	public void closed(Closeable container)
	{
		_listOfRenderables.remove(container);
		_listOfFloatingContainer.remove(container);
	}
}