package metro.WindowControls;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import metro.METRO;
import metro.Graphics.Draw;
import metro.Graphics.Fill;

public class InputField extends Input
{
	private int _curserPos = 0,
		_xOffset = 0; // in pixel

	/**
	 * Creates a new InputField with one line to input text. The start-text is "" and the window is null
	 * 
	 * @param position The position.
	 */
	public InputField(Rectangle position)
	{
		this(position, null, "");
	}

	/**
	 * Creates a new InputField with one line to input text. The start-text is ""
	 * 
	 * @param position The position on the window
	 * @param win The window handle.
	 */
	public InputField(Rectangle position, Window win)
	{
		this(position, win, "");
	}

	/**
	 * Creates a new InputField with one line to input text.
	 * 
	 * @param position The position on the window
	 * @param win The window handle.
	 * @param text The text, that's in the input box at the beginning.
	 */
	public InputField(Rectangle position, Window win, String text)
	{
		_position = position;
		_windowHandle = win;
		_text = text;

		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
	}

	@Override
	public void draw()
	{
		METRO.__spriteBatch.end();
		METRO.__spriteBatch.begin();

		// Create scissor to draw only in the area of the list box.
		com.badlogic.gdx.math.Rectangle scissors = new com.badlogic.gdx.math.Rectangle();
		com.badlogic.gdx.math.Rectangle clipBounds = new com.badlogic.gdx.math.Rectangle(_position.x, _position.y, _position.width + 1, _position.height + 1);
		ScissorStack.calculateScissors((Camera)METRO.__camera, METRO.__spriteBatch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);

		// Fill background
		Fill.setColor(Color.white);
		Fill.Rect(_position);

		// draw text
		Draw.String(_text, _position.x - _xOffset + 3, _position.y + 3);

		// Draw border
		Draw.setColor(METRO.__metroBlue);
		Draw.Rect(_position);

		// Draw cursor on right position
		String str = _text.substring(0, _curserPos);
		int width = Draw.getStringSize(str).width;
		Draw.setColor(Color.gray);
		Draw.Line(_position.x + width + 3, _position.y + 2, _position.x + width + 3, _position.y + _position.height - 4);

		ScissorStack.popScissors();
	}

	@Override
	public boolean clickOnControlElement()
	{
		Point mPos = METRO.__originalMousePosition;
		return _position.contains(mPos);
	}

	@Override
	public void setPosition(Point pos)
	{
		_position = new Rectangle(pos.x, pos.y, _position.width, _position.height);
	}

	@Override
	public Point getPosition()
	{
		return _position.getLocation();
	}

	@Override
	public void moveElement(Point offset)
	{
		_position.x += offset.x;
		_position.y += offset.y;
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public void keyPressed(int key)
	{
		// if this input box is NOT selected, just do nothing
		if(!_selected) return;

		switch(key)
		{
			case Keys.RIGHT:
				_curserPos++;
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
				METRO.__currentGameScreen.setSelectedInput(null);
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
		if(Draw.getStringSize(_text).width > _position.width)
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
		_curserPos++;
	}

	@Override
	public void keyUp(int key)
	{
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
}
