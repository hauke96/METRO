package metro.WindowControls;

/**
 * A {@link ContainerRenderer} is able to render container and controls.
 * 
 * @author hauke
 *
 */
public interface ContainerRenderer
{
	/**
	 * Registers a new renderable class.
	 * 
	 * @param containerRenderable The {@link ContainerRenderable} that should be rendered.
	 */
	void registerRenderable(ContainerRenderable containerRenderable);

	void registerFloatingRenderable(FloatingContainer renderer);

	/**
	 * Notifies all registered renderables about a draw call.
	 */
	public void notifyDraw();

	/**
	 * Notifies all registered renderables about a mouse click.
	 * 
	 * @param screenX The x-coordinate.
	 * @param screenY The y-coordinate.
	 * @param button The pressed button.
	 */
	public void notifyMouseClick(int screenX, int screenY, int button);

	/**
	 * Notifies all registered renderables about a mouse scroll event.
	 *
	 * @param amount The amount of scrolled steps.
	 */
	public void notifyMouseScrolled(int amount);

	/**
	 * Notifies all registered renderables about a pressed key.
	 * 
	 * @param keyCode The key that's pressed.
	 */
	public void notifyKeyPressed(int keyCode);

	/**
	 * Notifies all registered renderables about a released key.
	 * 
	 * @param keyCode The key that's released.
	 */
	public void notifyKeyUp(int keyCode);
}
