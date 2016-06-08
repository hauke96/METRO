package metro.WindowControls;

import metro.Exceptions.UninitiatedClassException;

/**
 * A {@link ContainerRenderable} is a class that can be used by the {@link ContainerRenderer} to render container and controls.
 * 
 * @author hauke
 */
public abstract class ContainerRenderable extends ControlElement
{
	private static ContainerRenderer renderer = null;
	
	public static void setRenderer(ContainerRenderer newRenderer)
	{
		renderer = newRenderer;
	}

	/**
	 * Registers a {@link ContainerRenderable} in the current renderer.
	 * 
	 * @param renderable A container renderable class.
	 */
	protected void register(ContainerRenderable renderable)
	{
		if(renderer == null)
		{
			throw new UninitiatedClassException("The container renderable is not initiated!");
		}
		renderer.registerRenderable(renderable);
	}

	/**
	 * Registers a {@link ContainerRenderable} in the current renderer.
	 * 
	 * @param renderable A floating container renderable class.
	 */
	protected void registerFloatingContainer(FloatingContainer renderable)
	{
		if(renderer == null)
		{
			throw new UninitiatedClassException("The container renderable is not initiated!");
		}
		renderer.registerFloatingRenderable(renderable);
	}
}
