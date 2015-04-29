package metro.WindowControls;

import java.awt.Point;

import com.badlogic.gdx.Input.Keys;

/**
 * The interface with all the important methods every control element should have.
 * @author hauke
 *
 */

public interface ControlElement 
{
	/**
	 * Draws the control.
	 * @param g Graphics handle.
	 */
	public void draw();
	
	/**
	 * When mouse clicked on control.
	 * @return
	 */
	public boolean clickOnControlElement();
	
	/**
	 * Sets a new position
	 * @param pos New position as Point.
	 */
	public void setPosition(Point pos);
	
	/**
	 * Returns the current position of this control.
	 * @return Position as Point.
	 */
	public Point getPosition();
	
	/**
	 * Moves the element.
	 * @param offset The amount of pixel to move. This is NOT the new position, only an offset!
	 */
	public void moveElement(Point offset);
	
	/**
	 * Fires when users scrolls.
	 * @param amount Positive or negative amount of steps since last frame.
	 */
	public void mouseScrolled(int amount);
	
	/**
	 * Fires, when a key has been pressed.
	 * @param keyCode The code of the key as int.
	 */
	public void keyPressed(int keyCode);

	/**
	 * Fires when a key has been released.
	 * @param keyCode The code of the key from Input.Keys
	 */
	public void keyUp(int keyCode);
}
