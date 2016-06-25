package metro.UI;

import metro.Exceptions.UninitiatedClassException;
import metro.UI.Renderable.Container.FloatingContainer;
import metro.UI.Renderable.Container.StaticContainer;
import metro.UI.Renderer.ContainerRenderer;

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
	 * @param container A container renderable class.
	 */
	public void register(StaticContainer container)
	{
		if(renderer == null)
		{
			throw new UninitiatedClassException("The container renderable is not initiated!");
		}
		renderer.registerStaticContainer(container);
	}

	/**
	 * Registers a {@link ContainerRegistrationService} in the current renderer.
	 * 
	 * @param container A floating container renderable class.
	 */
	public void register(FloatingContainer container)
	{
		if(renderer == null)
		{
			throw new UninitiatedClassException("The container renderable is not initiated!");
		}
		renderer.registerFloatingContainer(container);
	}
}
