package metro.UI.Renderable.Controls;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
import metro.UI.Renderable.ControlElement;

/**
 * Provides an input field control to make user inputs (in form of a string) possible.
 * There are no events triggered after typing something in but the method .getText() return the whole text.
 * 
 * @author hauke
 *
 */
public class InputField extends ControlElement
{
	private int _curserPos = 0,
		_xOffset = 0; // in pixel
	private boolean _shift,
		_selected;

	/**
	 * Creates a new InputField with one line to input text. The start-text is "" and the window is null
	 * 
	 * @param area The position.
	 */
	public InputField(Rectangle area)
	{
		this(area, "");
	}

	/**
	 * Creates a new InputField with one line to input text.
	 * 
	 * @param area The position on the window
	 * @param text The text, that's in the input box at the beginning.
	 */
	public InputField(Rectangle area, String text)
	{
		_area = area;
		_text = text;
	}

	@Override
	protected void draw()
	{
		METRO.__spriteBatch.end();
		METRO.__spriteBatch.begin();

		// Create scissor to draw only in the area of the list box.
		com.badlogic.gdx.math.Rectangle scissors = new com.badlogic.gdx.math.Rectangle();
		com.badlogic.gdx.math.Rectangle clipBounds = new com.badlogic.gdx.math.Rectangle(_area.x + METRO.__getXOffset(), _area.y + METRO.__getYOffset(), _area.width + 1,
			_area.height + 1);
		ScissorStack.calculateScissors((Camera)METRO.__camera, METRO.__spriteBatch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);

		// Fill background
		if(_state) Fill.setColor(Color.white);
		else Fill.setColor(Color.lightGray);
		Fill.Rect(_area);

		// draw text
		if(_state) Draw.setColor(Color.black);
		else Draw.setColor(Color.lightGray);
		Draw.String(_text, _area.x - _xOffset + 3, _area.y + 3);

		// Draw border
		if(_state) Draw.setColor(METRO.__metroBlue);
		else Draw.setColor(Color.lightGray);
		Draw.Rect(_area);

		// Draw cursor on right position
		String str = _text.substring(0, _curserPos);
		int width = Draw.getStringSize(str).width;

		if(_state) Draw.setColor(Color.gray);
		else Draw.setColor(Color.lightGray);
		Draw.Line(_area.x + width + 3, _area.y + 2, _area.x + width + 3, _area.y + _area.height - 4);

		ScissorStack.popScissors();
	}

	/**
	 * Does all actions when the mouse clicked on this control.
	 * 
	 * @return True when user clicked on control, false if not.
	 */
	public boolean clickOnControlElement()
	{
		if(!_state) return false;
		Point mPos = METRO.__originalMousePosition;
		boolean clickedOnControl = _area.contains(mPos);

		if(clickedOnControl) select();
		else disselect();

		return clickedOnControl;
	}

	@Override
	public boolean mouseClicked(int screenX, int screenY, int button)
	{
		if(clickOnControlElement())
		{
			notifyClickOnControl(null);
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(int screenX, int screenY, int button)
	{
	}

	@Override
	public void moveElement(Point offset)
	{
		_area.x += offset.x;
		_area.y += offset.y;
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public void keyPressed(int key)
	{
		// if this input box is NOT selected, just do nothing
		if(!_selected || !_state) return;

		switch(key)
		{
			case Keys.RIGHT:
				++_curserPos;
				_curserPos %= (_text.length() + 1);
				break;
			case Keys.LEFT:
				_curserPos--;
				if(_curserPos < 0) _curserPos = _text.length();
				break;
			case Keys.SHIFT_LEFT:
			case Keys.SHIFT_RIGHT:
				_shift = true;
				break;
			case Keys.SPACE:
				charTyped(' ');
				break;
			case Keys.PERIOD:
				charTyped('.');
				break;
			case Keys.COMMA:
				charTyped(',');
				break;
			case Keys.SEMICOLON:
				charTyped(';');
				break;
			case Keys.COLON:
				charTyped(':');
				break;
			case Keys.MINUS:
				charTyped('-');
				break;
			case Keys.BACKSPACE:
				if(_curserPos > 0)
				{
					_text = _text.substring(0, _curserPos - 1) + _text.substring(_curserPos, _text.length());
					_curserPos--;
				}
				break;
			case Keys.ENTER:
			case Keys.ESCAPE:
				METRO.__setSelectedInput(null);
				break;
		}
		// for ranges:
		if(key >= Keys.A && key <= Keys.Z)
		{
			charTyped(_shift ? (char)(36 + key) : (char)(68 + key));
		}
		else if(key >= Keys.NUM_0 && key <= Keys.NUM_9)
		{
			charTyped((char)(key + 41));
		}
		// if length of string in pixel is greater than the width of the field, delete last added character
		if(Draw.getStringSize(_text).width > _area.width)
		{
			_text = _text.substring(0, _curserPos - 1) + _text.substring(_curserPos, _text.length());
			_curserPos--;
		}
	}

	/**
	 * Adds a character at _cursor position to the current text.
	 * 
	 * @param c The character to add.
	 */
	private void charTyped(char c)
	{
		_text = new StringBuilder(_text).insert(_curserPos, c).toString();
		++_curserPos;
	}

	@Override
	public void keyUp(int key)
	{
		if(!_state) return;

		if(key == Keys.SHIFT_LEFT || key == Keys.SHIFT_RIGHT)
		{
			_shift = false;
		}
	}

	@Override
	public void setText(String text)
	{
		_text = text;
		_curserPos = _text.length();
	}

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
