package metro.UI.Renderable;

/**
 * 
 * Closable controls or container can be closed and might then notify their observer about this event.
 * 
 * @author hauke
 *
 */
public interface Closable
{
	/**
	 * This is the close event. After this the control or container will be closed and removed from the renderer.
	 */
	public void close();
}
