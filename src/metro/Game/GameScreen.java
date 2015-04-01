package metro.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Every Menu or Game Sreen has to implement this interface for start() and update().
 */

/**
 * @author Hauke
 * 
 */
public interface GameScreen
{
	/**
	 * Will be executed as fast as possible ;)
	 */
	public void update(SpriteBatch g);

	/**
	 * When mouse has clicked
	 * @param screenX The x-position on the screen
	 * @param screenY The y-position on the screen
	 * @param mouseButton The number of the button like Buttons.LEFT
	 */
	public void mouseClicked(int screenX, int screenY, int mouseButton);
	/**
	 * When mouse has been released.
	 * @param mouseButton The number of the button like Buttons.LEFT
	 */
	public void mouseReleased(int mouseButton);
	/**
	 * When a key was pressed.
	 * @param keyCode Key number from Gdx.Input
	 */
	public void keyPressed(int keyCode);
	/**
	 * Fires when user scrolls.
	 * @param amount Positive or negative amount of steps since last frame. 
	 */
	public void mouseScrolled(int amount);
}
