package metro.UI;

import metro.Common.Technical.Exceptions.UninitiatedClassException;
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
	
	/**
	 * Sets the renderer of all controls. This will remove the old renderer.
	 * If no renderer is set at the beginning, a {@code UninitiatedClassException} will be thrown later.
	 * 
	 * @param newRenderer
	 *            The new renderer for controls.
	 */
	public void setRenderer(ContainerRenderer newRenderer)
	{
		renderer = newRenderer;
	}
	
	/**
	 * Registers a {@link ContainerRegistrationService} in the current renderer.
	 * 
	 * @param container
	 *            A container renderable class.
	 */
	public void register(StaticContainer container)
	{
		if (renderer == null)
		{
			throw new UninitiatedClassException("The container renderable is not initiated!");
		}
		renderer.registerStaticContainer(container);
	}
	
	/**
	 * Registers a {@link ContainerRegistrationService} in the current renderer.
	 * 
	 * @param container
	 *            A floating container renderable class.
	 */
	public void register(FloatingContainer container)
	{
		if (renderer == null)
		{
			throw new UninitiatedClassException("The container renderable is not initiated!");
		}
		renderer.registerFloatingContainer(container);
	}
}
