package metro.WindowControls;

import java.util.LinkedList;
import java.util.List;

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

	private List<Window> _listOfWindows;
	private List<ContainerRenderable> _listOfRenderables;

	/**
	 * Creates a basic renderer with empty fields.
	 */
	public BasicContainerRenderer()
	{
		_listOfRenderables = new LinkedList<ContainerRenderable>();
		_listOfWindows = new LinkedList<Window>();
	}

	@Override
	public void registerRenderable(ContainerRenderable renderer)
	{
		_listOfRenderables.add(renderer);
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

		for(Window window : _listOfWindows)
		{
			notifyFunction.notifyRenderable(window);
		}
	}

	@Override
	public void closed(Container container)
	{
		_listOfRenderables.remove(container);
	}
}