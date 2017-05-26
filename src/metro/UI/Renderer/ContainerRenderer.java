package metro.UI.Renderer;

import metro.UI.ContainerRegistrationService;
import metro.UI.Renderable.Container.FloatingContainer;
import metro.UI.Renderable.Container.StaticContainer;

/**
 * A {@link ContainerRenderer} is able to render container and controls.
 * 
 * @author hauke
 *
 */
public interface ContainerRenderer
{
	/**
	 * Gets the used registration service.
	 * 
	 * @return The currently used registration service.
	 */
	public ContainerRegistrationService getRegistrationService();
	
	/**
	 * Registers a new static container.
	 * 
	 * @param newStaticContainer
	 *            The new static container that should be rendered.
	 */
	public void registerStaticContainer(StaticContainer newStaticContainer);
	
	/**
	 * Registers a new floating container.
	 * 
	 * @param newFloatingContainer
	 *            The new floating container that should be rendered.
	 */
	public void registerFloatingContainer(FloatingContainer newFloatingContainer);
	
	/**
	 * Notifies all registered renderables about a draw call.
	 */
	public void notifyDraw();
	
	/**
	 * Notifies all registered renderables about a mouse click.
	 * 
	 * @param screenX
	 *            The x-coordinate.
	 * @param screenY
	 *            The y-coordinate.
	 * @param button
	 *            The pressed button.
	 * @return True when a control in a container got the click event.
	 */
	public boolean notifyMouseClick(int screenX, int screenY, int button);
	
	/**
	 * Notifies all registered renderables about a released mouse button.
	 * 
	 * @param screenX
	 *            The x-coordinate.
	 * @param screenY
	 *            The y-coordinate.
	 * @param button
	 *            The released button.
	 */
	public void notifyMouseReleased(int screenX, int screenY, int button);
	
	/**
	 * Notifies all registered renderables about a mouse scroll event.
	 *
	 * @param amount
	 *            The amount of scrolled steps.
	 */
	public void notifyMouseScrolled(int amount);
	
	/**
	 * Notifies all registered renderables about a pressed key.
	 * 
	 * @param keyCode
	 *            The key that's pressed.
	 */
	public void notifyKeyPressed(int keyCode);
	
	/**
	 * Notifies all registered renderables about a released key.
	 * 
	 * @param keyCode
	 *            The key that's released.
	 */
	public void notifyKeyUp(int keyCode);
}
