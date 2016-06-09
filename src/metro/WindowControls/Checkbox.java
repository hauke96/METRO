package metro.WindowControls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.Graphics.Draw;

/**
 * A checkbox is a simple control to (de)activate things. This class provides lots of features to do this and draws the control.
 * 
 * @author hauke
 *
 */
public class Checkbox extends ControlElement
{
	private boolean _checked,
		_oldState; // used by hasChanged() method to determine if the state has changed since last call
	private Label _label;
	private int _textWidth;

	/**
	 * Creates a new checkbox with a certain text.
	 * 
	 * @param position The position of the checkbox.
	 * @param text The text of the checkbox
	 */
	public Checkbox(Point position, String text)
	{
		this(position, text, 0, false, true);
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
		this(position, text, 0, checked, true);
	}

	/**
	 * Creates a new checbox with a certain text and a start-state.
	 * 
	 * @param position The position of the checkbox.
	 * @param text The text of the checkbox
	 * @param checked The start state of the checkbox.
	 * @param enabled The enable-flag.
	 */
	public Checkbox(Point position, String text, boolean checked, boolean enabled)
	{
		this(position, text, 0, checked, enabled);
	}

	/**
	 * Creates a new checkbox with a certain text, a start-state and an enable flag.
	 * 
	 * @param position The position of the checkbox.
	 * @param text The text of the checkbox
	 * @param textWidth The maximum width of the text. This enables automatic linebreaks
	 * @param checked The start state of the checkbox.
	 * @param enabled The enable-flag.
	 */
	public Checkbox(Point position, String text, int textWidth, boolean checked, boolean enabled)
	{
		_text = text;
		_textWidth = textWidth;
		_label = new Label(_text, new Point(position.x + 25, position.y), _textWidth);
		Dimension size = calcSize();
		_area = new Rectangle(position.x, position.y, size.width, size.height);
		_checked = checked;
		_oldState = _checked;
		_state = enabled;
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

	/**
	 * This method checks if the mouse is in the area of the box.
	 * Notice: The mouse does NOT need to be clicked to return true!
	 * 
	 * @return True when the mouse is in the area of the box, false when not.
	 */
	public boolean isPressed()
	{
		Point mPos = METRO.__originalMousePosition;

		return mPos.x >= _area.x
			&& mPos.x <= _area.x + 15
			&& mPos.y >= _area.y
			&& mPos.y <= _area.y + 15;
	}

	@Override
	void draw()
	{
		// set the color depending on _enabled
		Draw.setColor(_state
			? METRO.__metroBlue
			: new Color(METRO.__metroBlue.getRed(), METRO.__metroBlue.getGreen(), METRO.__metroBlue.getBlue(), 125));
		_label.setColor(_state
			? Color.black
			: Color.lightGray);

		Draw.Rect(_area.x, _area.y, 15, 15);
		if(_checked) // draw cross if checked
		{
			Draw.Line(_area.x + 3, _area.y + 3, _area.x + 13, _area.y + 13);
			Draw.Line(_area.x + 3, _area.y + 13, _area.x + 13, _area.y + 3);
		}
	}

	/**
	 * Does all actions when the mouse clicked on this control.
	 * 
	 * @return True when user clicked on control, false if not.
	 */
	public boolean clickOnControlElement()
	{
		Point mPos = METRO.__originalMousePosition;

		if(mPos.x >= _area.x
			&& mPos.x <= _area.x + 15
			&& mPos.y >= _area.y
			&& mPos.y <= _area.y + 15)
		{
			_checked = (false == _checked) && _state;
			return true;
		}
		return false;
	}

	@Override
	boolean mouseClicked(int screenX, int screenY, int button)
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
		_area.setLocation(_area.x + offset.x, _area.y + offset.y);
	}

	@Override
	void mouseScrolled(int amount)
	{
	}

	@Override
	void keyPressed(int key)
	{
	}

	@Override
	void keyUp(int keyCode)
	{
	}

	@Override
	public void setText(String text)
	{
		_text = text;
		_label.setText(text);
	}

	@Override
	public Rectangle getArea()
	{
		Dimension size = calcSize();
		return new Rectangle(_area.x, _area.y, size.width, size.height);
	}

	private Dimension calcSize()
	{
		return new Dimension(25 + _label.getPosition().x, 20);
	}
}
