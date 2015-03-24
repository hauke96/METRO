package Game;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
	 * @param e MouseEvent with infos (pos etc.)
	 */
	public void mouseClicked(MouseEvent e);
	/**
	 * When mouse has been released.
	 * @param e MouseEvent with infos (pos etc.)
	 */
	public void mouseReleased(MouseEvent e);
	/**
	 * When a key was pressed.
	 * @param e KeyEvent with infos (which key?)
	 */
	public void keyPressed(KeyEvent e);
}
