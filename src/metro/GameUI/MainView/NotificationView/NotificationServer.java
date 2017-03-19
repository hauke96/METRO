package metro.GameUI.MainView.NotificationView;

import java.util.ArrayList;
import java.util.List;

/**
 * The notification server is just a abstraction layer between a publisher of notifications and a service thats displays, stores, analyses, ... messages.
 * The Server uses a private Singleton pattern
 * 
 * @author hauke
 *
 */
// TOOD make un-static but as service (in serviceConfiguration)
public class NotificationServer
{
	private static List<NotificationSubscriber> __listOfSubscribers = new ArrayList<>();

	/**
	 * Adds the subscriber to the list of subscribers for notifications.
	 * Every time a message comes in, the {@link NotificationSubscriber#addMessage(String, NotificationType)} method of the subscriber will be called.
	 * 
	 * @param subscriber The subscriber to add to the server. He will now be informed.
	 */
	public static void subscribe(NotificationSubscriber subscriber)
	{
		__listOfSubscribers.add(subscriber);
	}

	/**
	 * Use this method to publish a message.
	 * 
	 * @param message The message to publish.
	 * @param type The type of the message. This may influence the way it'll be processed.
	 */
	public static void publishNotification(String message, NotificationType type)
	{
		for(NotificationSubscriber sub : __listOfSubscribers)
		{
			sub.addMessage(message, type);
		}
	}

	/**
	 * @return The list with all subscribers.
	 */
	public static List<NotificationSubscriber> getSubscriber()
	{
		return __listOfSubscribers;
	}
}
