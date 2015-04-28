package metro.WindowControls;

import java.awt.Rectangle;

public abstract class Input implements ControlElement
{
	protected String _text;
	protected Rectangle _position;
	protected Window _windowHandle;
	
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
	public void setText(String text)
	{
		_text = text;
	}
}
