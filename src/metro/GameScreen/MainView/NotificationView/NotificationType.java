package metro.GameScreen.MainView.NotificationView;

/**
 * A notification type defines the kind of notification and the way it's displayed, processed, ...
 * 
 * @author hauke
 *
 */
public enum NotificationType
{
	/**
	 * An error of the game (e.g. "invalid train line")
	 */
	GAME_ERROR,
	/**
	 * A system error (caught exceptions or other problems)
	 */
	SYSTEM_ERROR,
	/**
	 * An ingame warning (e.g. "There are more trains on this line than are able to drive at the same time...")
	 */
	GAME_WARNING,
	/**
	 * A system warning (e.g. "Train xyz can not be loaded and therefore does not appear in the game")
	 */
	SYSTEM_WARNING,
	/**
	 * An ingame information (e.g. "Baught train xyz")
	 */
	GAME_INFO,
	/**
	 * A system information (e.g. "Changed amount of segments to xyz")
	 */
	SYSTEM_INFO;
}
