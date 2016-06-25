package metro.WindowControls;

import metro.Exceptions.UninitiatedClassException;

/**
 * A {@link ContainerRegistrationService} is a class that can be used by the {@link ContainerRenderer} to render container and controls.
 * 
 * @author hauke
 */
public class ContainerRegistrationService
{
	private ContainerRenderer renderer = null;
	
	public void setRenderer(ContainerRenderer newRenderer)
	{
		renderer = newRenderer;
	}

	/**
	 * Registers a {@link ContainerRegistrationService} in the current renderer.
	 * 
	 * @param renderable A container renderable class.
	 */
	void register(StaticContainer renderable)
	{
		if(renderer == null)
		{
			throw new UninitiatedClassException("The container renderable is not initiated!");
		}
		renderer.registerStaticContainer(renderable);
	}

	/**
	 * Registers a {@link ContainerRegistrationService} in the current renderer.
	 * 
	 * @param renderable A floating container renderable class.
	 */
	void registerFloatingContainer(FloatingContainer renderable)
	{
		if(renderer == null)
		{
			throw new UninitiatedClassException("The container renderable is not initiated!");
		}
		renderer.registerFloatingContainer(renderable);
	}
}
