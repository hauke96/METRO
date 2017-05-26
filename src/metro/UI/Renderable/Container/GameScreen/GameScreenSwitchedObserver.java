package metro.UI.Renderable.Container.GameScreen;

/**
 * A {@link GameScreenSwitchedObserver} gets a notification when the current gamescreen should be switched.
 * 
 * @author hauke
 */
public interface GameScreenSwitchedObserver
{
	/**
	 * Reacts to the notification that the current gamescreen should be switched.
	 * 
	 * @param newGameScreen
	 *            The gamescreen that should be switched to.
	 */
	void reactToGameScreenSwitch(GameScreenContainer newGameScreen);
}
