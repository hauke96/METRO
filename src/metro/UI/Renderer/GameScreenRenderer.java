package metro.UI.Renderer;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
	 * Updates the actual game screen.
	 * 
	 * @param sp SpriteBatch to draw on.
	 */
	void updateGameScreen(SpriteBatch sp);

	/**
	 * Switches the current game screen to the given one.
	 * 
	 * @param gameScreen
	 */
	void switchGameScreen(GameScreenContainer gameScreen);

	/**
	 * Renderes the controls specified by the renderer passed to the game screen.
	 */
	void renderUI();
}
