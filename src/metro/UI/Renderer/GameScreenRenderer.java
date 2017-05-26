package metro.UI.Renderer;

import com.badlogic.gdx.InputProcessor;

import metro.UI.Renderable.Container.GameScreen.GameScreenContainer;
import metro.UI.Renderable.Container.GameScreen.GameScreenSwitchedObserver;

/**
 * @author hauke
 *
 *         Renders the current game screen.
 */
public interface GameScreenRenderer extends InputProcessor, GameScreenSwitchedObserver
{
	/**
	 * Renders the controls specified by the renderer passed to the game screen.
	 */
	void renderUI();
	
	/**
	 * Switches the current game screen to the given one.
	 * 
	 * @param gameScreen
	 *            The new game screen.
	 */
	void switchTo(GameScreenContainer gameScreen);
}
