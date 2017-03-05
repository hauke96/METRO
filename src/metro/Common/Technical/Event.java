package metro.Common.Technical;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a general class for C#-like events.
 * 
 * @author hauke
 *
 */
public class Event
{
	/**
	 * Provides a method that will be fired a certain events this handler is registered to.
	 * 
	 * @author hauke
	 *
	 */
	public interface EventHandler
	{
		/**
		 * This method will be called when event is fired.
		 */
		public void handleEvent();
	}

	private List<EventHandler> _handlerList;

	/**
	 * Creates a new Event.
	 */
	public Event()
	{
		_handlerList = new ArrayList<>();
	}

	/**
	 * Registers the given event handler.
	 * 
	 * Duplicates are allowed.
	 * 
	 * @param eventHandler The handler to register.
	 */
	public void add(EventHandler eventHandler)
	{
		_handlerList.add(eventHandler);
	}

	/**
	 * Notified all registered event handler.
	 */
	public void fireEvent()
	{
		for(EventHandler eventHandler : _handlerList)
		{
			eventHandler.handleEvent();
		}
	}
}
