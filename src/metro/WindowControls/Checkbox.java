package metro.WindowControls;

import java.awt.Color;
import java.awt.Point;

import metro.METRO;
import metro.Graphics.Draw;

public class Checkbox extends ActionObservable implements ControlElement
{
	private boolean _checked,
		_enabled, // other name: isCheckable (true - box can be used; false - box can't be used)
		_oldState; // used by hasChanged() method to detemine if the state has changed since last call
	private String _text;
	private Label _label;
	private Point _position;
	private Window _windowHandle;
	private int _textWidth = 0;

	/**
	 * Creates a new checkbox with a certain text.
	 * 
	 * @param position The position of the checkbox.
	 * @param text The text of the checkbox
	 */
	public Checkbox(Point position, String text)
	{
		this(position, text, false, true, null);
	}

	/**
	 * Creates a new checbox with a certain text and a start-state.
	 * 
	 * @param position The position of the checkbox.
	 * @param text The text of the checkbox
	 * @param checked The start state of the checkbox.
	 */
	public Checkbox(Point position, String text, boolean checked)
	{
		this(position, text, checked, true, null);
	}

	/**
	 * Creates a new checbox with a certain text, a start-state and an enable flag.
	 * 
	 * @param position The position of the checkbox.
	 * @param text The text of the checkbox
	 * @param checked The start state of the checkbox.
	 * @param enabled The enable-flag.
	 */
	public Checkbox(Point position, String text, boolean checked, boolean enabled)
	{
		this(position, text, checked, enabled, null);
	}

	/**
	 * Creates a new checbox with a certain text, a start-state and an enable flag.
	 * 
	 * @param position The position of the checkbox.
	 * @param text The text of the checkbox
	 * @param checked The start state of the checkbox.
	 * @param enabled The enable-flag.
	 * @param window The window this control is attached to.
	 */
	public Checkbox(Point position, String text, boolean checked, boolean enabled, Window window)
	{
		this(position, text, 0, checked, enabled, window);
	}

	/**
	 * Creates a new checbox with a certain text, a start-state and an enable flag.
	 * 
	 * @param position The position of the checkbox.
	 * @param text The text of the checkbox
	 * @param textWidth The maximum width of the text. This enables automatic linebreaks
	 * @param checked The start state of the checkbox.
	 * @param enabled The enable-flag.
	 * @param window The window this control is attached to.
	 */
	public Checkbox(Point position, String text, int textWidth, boolean checked, boolean enabled, Window window)
	{
		_position = position;
		_text = text;
		_textWidth = textWidth;
		_label = new Label(_text, new Point(_position.x + 25, _position.y), _textWidth, window);
		_checked = checked;
		_oldState = _checked;
		_enabled = enabled;
		_windowHandle = window;
		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
//		METRO.__registerControl(this);
	}

	/**
	 * Returns the current state of the checkbox.
	 * 
	 * @return True if checked, false if not.
	 */
	public boolean isChecked()
	{
		return _checked;
	}

	/**
	 * Returns true, when the state (true/false) has changes since last call of this method.
	 * 
	 * @return True if state has changed.
	 */
	public boolean hasChanged()
	{
		boolean hasChanged = _oldState != _checked;
		if(hasChanged) _oldState = _checked;
		return hasChanged;
	}

	public boolean isPressed()
	{
		Point mPos = METRO.__originalMousePosition;

		return mPos.x >= _position.x
			&& mPos.x <= _position.x + 15
			&& mPos.y >= _position.y
			&& mPos.y <= _position.y + 15;
	}

	@Override
	public void draw()
	{
		// set the color depending on _enabled
		Draw.setColor(_enabled
			? METRO.__metroBlue
			: new Color(METRO.__metroBlue.getRed(), METRO.__metroBlue.getGreen(), METRO.__metroBlue.getBlue(), 125));
		_label.setColor(_enabled
			? Color.black
			: Color.lightGray);

		Draw.Rect(_position.x, _position.y, 15, 15);
		if(_checked) // draw cross if checked
		{
			Draw.Line(_position.x + 3, _position.y + 3, _position.x + 13, _position.y + 13);
			Draw.Line(_position.x + 3, _position.y + 13, _position.x + 13, _position.y + 3);
		}
	}

	@Override
	public boolean clickOnControlElement()
	{
		Point mPos = METRO.__originalMousePosition;

		if(mPos.x >= _position.x
			&& mPos.x <= _position.x + 15
			&& mPos.y >= _position.y
			&& mPos.y <= _position.y + 15)
		{
			_checked = (false == _checked) && _enabled;
			return true;
		}
		return false;
	}

	@Override
	public void setPosition(Point pos)
	{
		_position = pos;
	}

	@Override
	public Point getPosition()
	{
		return _position;
	}
	
	@Override
	public boolean mouseClicked(int screenX, int screenY, int button)
	{
		if(clickOnControlElement())
		{
			notifyStateChanged(_checked);
			return true;
		}
		return false;
	}

	@Override
	public void moveElement(Point offset)
	{
		_position.setLocation(_position.x + offset.x, _position.y + offset.y);
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public void keyPressed(int key)
	{
	}

	@Override
	public void keyUp(int keyCode)
	{
	}

	@Override
	public void setText(String text)
	{
		_text = text;
	}

	@Override
	public void setState(boolean enable)
	{
		_enabled = enable;
	}
}
