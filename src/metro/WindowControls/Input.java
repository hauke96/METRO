package metro.WindowControls;

import java.awt.Rectangle;

public abstract class Input implements ControlElement
{
	protected String _text;
	protected Rectangle _position;
	protected Window _windowHandle;
	protected boolean _shift,
		_selected;
	
	/**
	 * Returns the current text of the control.
	 * @return The current input.
	 */
	public String getText()
	{
		return _text;
	}
	
	/**
	 * Sets the whole text and deletes the old one.
	 * @param text The new text of the input.
	 */
	public abstract void setText(String text);
	
	/**
	 * Sets this input as selected which enables inputs.
	 */
	public void select()
	{
		_selected = true;
	}
	
	/**
	 * Sets this input as non-selected which disables inputs.
	 */
	public void disselect()
	{
		_selected = false;
	}
}
