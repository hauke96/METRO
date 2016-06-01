package metro.WindowControls;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * The interface with all the important methods every control element should have.
 * 
 * @author hauke
 *
 */

abstract class ControlElement extends ActionObservable
{
	protected boolean _state;
	protected String _text;
	protected Rectangle _area;

	public ControlElement()
	{
		_state = true;
		_text = "";
	}

	/**
	 * Draws the control.
	 */
	abstract void draw();

	/**
	 * Is called when mouse clicked with its left button.
	 * 
	 * @param screenX The x coordinate of the click.
	 * @param screenY The y coordinate of the click.
	 * @param button The button related to Buttons.xy.
	 * @return True when mouse clicked on control, false when not.
	 */
	abstract boolean mouseClicked(int screenX, int screenY, int button);

	/**
	 * Moves the element.
	 * 
	 * @param offset The amount of pixel to move. This is NOT the new position, only an offset!
	 */
	public abstract void moveElement(Point offset);

	/**
	 * Fires when users scrolls.
	 * 
	 * @param amount Positive or negative amount of steps since last frame.
	 */
	abstract void mouseScrolled(int amount);

	/**
	 * Fires, when a key has been pressed.
	 * 
	 * @param keyCode The code of the key as int.
	 */
	abstract void keyPressed(int keyCode);

	/**
	 * Fires when a key has been released.
	 * 
	 * @param keyCode The code of the key from Input.Keys
	 */
	abstract void keyUp(int keyCode);

	/**
	 * Gets an rectangle which covers the whole area of this control.
	 * 
	 * @return The area of this control as rectangle.
	 */
	public Rectangle getArea()
	{
		return (Rectangle)_area.clone();
	}

	/**
	 * Sets the new area of this control. The control will be displayed in this area on next frame.
	 * 
	 * @param newArea The new area of the control.
	 */
	public void setArea(Rectangle newArea)
	{
		_area = newArea;
	}

	/**
	 * Sets a new position
	 * 
	 * @param pos New position as Point.
	 */
	public void setPosition(Point pos)
	{
		_area.setLocation(pos);
	}

	/**
	 * Returns the current position of this control.
	 * 
	 * @return Position as Point.
	 */
	public Point getPosition()
	{
		return (Point)_area.getLocation().clone();
	}

	/**
	 * Updates the text of the control.
	 * 
	 * @param text The new text.
	 */
	public void setText(String text)
	{
		_text = text;
	}

	/**
	 * @return The text of the control.
	 */
	public String getText()
	{
		return _text;
	}

	/**
	 * Sets the enable state of the control. If it's disabled, the user can't interact with this control.
	 * 
	 * @param newState The new state of the control.
	 */
	public void setState(boolean newState)
	{
		_state = newState;
	}

	/**
	 * Gets the current state of this control.
	 * 
	 * @return True when control is active and the user can interact with it, false if not.
	 */
	public boolean getState()
	{
		return _state;
	}
}
