package metro.GameUI.Common;

import java.text.MessageFormat;

import metro.Common.Technical.Event;
import metro.Common.Technical.Logger;

/**
 * Every Menu or game screen has to implement this interface for start() and update(). This will make the creation process more easy.
 * 
 * @author Hauke
 * 
 */
public abstract class ToolView
{
	/**
	 * This event indicates, that this tool view will be closed. Unload everything on this event call.
	 */
	public final Event CloseEvent;

	/**
	 * Creates an empty tool view with usable close-event.
	 */
	public ToolView()
	{
		CloseEvent = new Event();
	}

	/**
	 * Handles the click event.
	 * 
	 * @param screenX The x-position on the screen
	 * @param screenY The y-position on the screen
	 * @param mouseButton The number of the button like Buttons.LEFT
	 * @return True when view successfully processed click.
	 */
	public abstract boolean mouseClicked(int screenX, int screenY, int mouseButton);

	/**
	 * @return True when mouse is in sensible/important area of the game screen.
	 */
	public abstract boolean isHovered();

	/**
	 * Closes the tool view by removing all controls from it.
	 * This also removes all observers to the close event in order to make garbage-collection easier/possible.
	 */
	public void close()
	{
		Logger.__debug("Closed tool view " + this);
		Logger.__debug(MessageFormat.format("Unregister {0} observer.", CloseEvent.countObservers()));

		CloseEvent.fireEvent();
		CloseEvent.clean();
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj.getClass().equals(this.getClass());
	}
}
