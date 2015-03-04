package WindowControls;

import java.awt.Graphics;
import java.awt.Point;

public interface ControlElement 
{
	/**
	 * The update method for stuff.
	 */
	public void update();
	/**
	 * Draws the control.
	 * @param g Graphics handle.
	 */
	public void draw(Graphics g);
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
}
