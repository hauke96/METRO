package metro.WindowControls;

import java.awt.Point;

/**
 * The interface with all the important methods every control element should have.
 * 
 * @author hauke
 *
 */

public interface ControlElement
{
	/**
	 * Draws the control.
	 */
	public void draw();

	/**
	 * Sets a new position
	 * 
	 * @param pos New position as Point.
	 */
	public void setPosition(Point pos);

	/**
	 * Updates the text of the control.
	 * 
	 * @param text The new text.
	 */
	public void setText(String text);

	/**
	 * Sets the enable state of the control. If it's disabled, the user can't interact with this control.
	 * 
	 * @param enable The new state of the control.
	 */
	public void setState(boolean enable);

	/**
	 * Returns the current position of this control.
	 * 
	 * @return Position as Point.
	 */
	public Point getPosition();

	/**
	 * Is called when mouse clicked with its left button.
	 * 
	 * @param screenX The x coordinate of the click.
	 * @param screenY The y coordinate of the click.
	 * @param button The button related to Buttons.xy.
	 * @return True when mouse clicked on control, false when not.
	 */
	public boolean mouseClicked(int screenX, int screenY, int button);

	/**
	 * Moves the element.
	 * 
	 * @param offset The amount of pixel to move. This is NOT the new position, only an offset!
	 */
	public void moveElement(Point offset);

	/**
	 * Fires when users scrolls.
	 * 
	 * @param amount Positive or negative amount of steps since last frame.
	 */
	public void mouseScrolled(int amount);

	/**
	 * Fires, when a key has been pressed.
	 * 
	 * @param keyCode The code of the key as int.
	 */
	public void keyPressed(int keyCode);

	/**
	 * Fires when a key has been released.
	 * 
	 * @param keyCode The code of the key from Input.Keys
	 */
	public void keyUp(int keyCode);
}
