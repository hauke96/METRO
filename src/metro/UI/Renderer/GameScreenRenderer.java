package metro.UI.Renderer;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.GameUI.Screen.GameScreen;
import metro.GameUI.Screen.GameScreenSwitchedObserver;

/**
 * @author hauke
 *
 * Renders the current game screen.
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
	void switchGameScreen(GameScreen gameScreen);
}
