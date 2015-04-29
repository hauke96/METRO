package metro.WindowControls;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import metro.Game.METRO;
import metro.Graphics.Draw;
import metro.Graphics.Fill;

public class InputField extends Input
{
	private int _curserPos = 0,
		_xOffset = 0;
	
	/**
	 * Creates a new InputField with one line to input text. The start-text is "" and the window is null
	 * @param position The position.
	 */
	public InputField(Rectangle position)
	{
		this(position, null, "");
	}
	
	/**
	 * Creates a new InputField with one line to input text. The start-text is ""
	 * @param position The position on the window
	 * @param win The window handle.
	 */
	public InputField(Rectangle position, Window win)
	{
		this(position, win, "");
	}
	
	/**
	 * Creates a new InputField with one line to input text.
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
		
		//Create scissor to draw only in the area of the list box.
		com.badlogic.gdx.math.Rectangle scissors = new com.badlogic.gdx.math.Rectangle();
		com.badlogic.gdx.math.Rectangle clipBounds = new com.badlogic.gdx.math.Rectangle(_position.x, _position.y, _position.width + 1, _position.height + 1);
		ScissorStack.calculateScissors((Camera)METRO.__camera, METRO.__spriteBatch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);
		
		// Fill background
		Fill.setColor(Color.white);
		Fill.Rect(_position);
		
		//draw text
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
		//TODO: Check if clicked and set _selectedInput to this.
		return false;
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
	public void mouseScrolled(int amount){}

	@Override
	public void keyPressed(int key)
	{
		if(key == Keys.RIGHT)
		{
			_curserPos++;
			_curserPos %= (_text.length() + 1);
		}
		else if(key == Keys.LEFT)
		{
			_curserPos--;
			if(_curserPos < 0) _curserPos = _text.length();
		}
		else if(key >= Keys.A && key <= Keys.Z)
		{
			_text = new StringBuilder(_text).insert(_curserPos, (char)(36 + key)).toString();
			_curserPos++;
		}
		//TODO: ENTER -> set _selectedInput to null
	}
}
