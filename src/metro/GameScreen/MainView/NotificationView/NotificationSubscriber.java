package metro.GameScreen.MainView.NotificationView;

/**
 * A subscriber for notifications receives all notifications that are send via the NotificationServer.
 * 
 * @author hauke
 *
 */
public interface NotificationSubscriber
{
	/**
	 * Is called when a new message has been published.
	 * 
	 * @param message The message that has been published.
	 * @param type 
	 */
	public void addMessage(String message, NotificationType type);
}
