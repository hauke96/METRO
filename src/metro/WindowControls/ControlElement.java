package metro.WindowControls;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
}
