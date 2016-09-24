package metro.GameUI.MainView.NotificationView;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import metro.GameUI.MainView.NotificationView.NotificationServer;
import metro.GameUI.MainView.NotificationView.NotificationSubscriber;
import metro.GameUI.MainView.NotificationView.NotificationType;

/**
 * @author hauke
 */
public class NotificationServerTest //implements NotificationSubscriber
{
	private static String messageToSend = "The answer is 42";
	private static String receivedMessage = "";
	
	/**
	 * Removes all subscribers before every test due to static saving.
	 */
	@Before
	public void removeAllSubscriber()
	{
		NotificationServer.getSubscriber().clear();
	}
	
	/**
	 * Test if the server add a new subscriber in the correct way.
	 */
	@Test
	public void testAddingCorrect()
	{
		assertEquals(0, NotificationServer.getSubscriber().size());

		// simply add empty subscriber:
		NotificationServer.subscribe(null);

		assertEquals(1, NotificationServer.getSubscriber().size());

		// simply add another empty subscriber:
		NotificationServer.subscribe(null);

		assertEquals(2, NotificationServer.getSubscriber().size());
	}

	/**
	 * Tests if the subscriber receives the correct message.
	 */
	@Test
	public void testSubscriberWorks()
	{
		NotificationServer.subscribe(new NotificationSubscriber()
		{
			@Override
			public void addMessage(String message, NotificationType type)
			{
				assertEquals(messageToSend, message);
				assertEquals(NotificationType.SYSTEM_INFO, type);
				receivedMessage = message;
			}
		});
		NotificationServer.publishNotification(messageToSend, NotificationType.SYSTEM_INFO);
		
		assertEquals(messageToSend, receivedMessage);
	}
}
