package metro.UI.Renderable;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * The abstract class with all the important methods every control element should have.
 * Every control element has got a state (enables/disabled), a visible flag (visible/invisible), a text and an are the control is in.
 * 
 * @author hauke
 *
 */

public abstract class ControlElement extends ActionObservable
{
	protected boolean _state;
	protected String _text;
	protected Rectangle _area;
	protected boolean _visible;

	/**
	 * Initiates the fields of the control element with default values.
	 * A new control is visible, enables and has no text. The area has to be set separately.
	 */
	protected ControlElement()
	{
		_visible = true;
		_state = true;
		_text = "";
		_area = new Rectangle();
	}

	/**
	 * Calls the {@link #draw()} method when the control is visible.
	 */
	public void drawControl()
	{
		if(_visible)
		{
			draw();
		}
	}

	/**
	 * Draws the control. This method is only called when the control is visible.
	 */
	protected abstract void draw();

	/**
	 * Is called when mouse clicked with its left button.
	 * 
	 * @param screenX The x coordinate of the click.
	 * @param screenY The y coordinate of the click.
	 * @param button The button related to Buttons.xy.
	 * @return True when mouse clicked on control, false when not.
	 */
	public abstract boolean mouseClicked(int screenX, int screenY, int button);

	/**
	 * Is called when the mouse is released.
	 * 
	 * @param screenX The x coordinate of the click.
	 * @param screenY The y coordinate of the click.
	 * @param button The button that had been clicke but has been released now.
	 */
	public abstract void mouseReleased(int screenX, int screenY, int button);

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
	public abstract void mouseScrolled(int amount);

	/**
	 * Fires, when a key has been pressed.
	 * 
	 * @param keyCode The code of the key as int.
	 */
	public abstract void keyPressed(int keyCode);

	/**
	 * Fires when a key has been released.
	 * 
	 * @param keyCode The code of the key from Input.Keys
	 */
	public abstract void keyUp(int keyCode);

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
	 * Checks if the given coordinates are in the area of this control.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return True when the coordinates are in the controls area, false if not.
	 */
	public boolean isInArea(int x, int y)
	{
		return _area.contains(x, y);
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

	/**
	 * Sets the visibility of the control. If it's set to {@code false} also the state of the control is set to {@code false}.
	 * Don't forget to set the state to true as well or use the {@link #enable()} method.
	 * 
	 * @param visibility The new visibility of the control.
	 */
	public void setVisibility(boolean visibility)
	{
		_visible = visibility;
		if(!_visible)
		{
			setState(false);
		}
	}

	/**
	 * Sets the visibility AND the state of the control to true, so it's visible and able to receive interaction events.
	 */
	public void enable()
	{
		setVisibility(true);
		setState(true);
	}

	/**
	 * @return The visibility status of the control. This has nothing directly to do with the {@link #getState()} method and the state property.
	 */
	public boolean isVisible()
	{
		return _visible;
	}
}
