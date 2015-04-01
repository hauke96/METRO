package metro.WindowControls;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import metro.Game.METRO;
import metro.graphics.Draw;
import metro.graphics.Fill;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

public class List implements ControlElement 
{
	private ArrayList<String> _entries = new ArrayList<String>();
	private Rectangle _position;
	private Window _windowHandle;
	private int _offset = 0;
	private float _maxOffset = 0,
		_scrollHeight = 20; // height of one scroll

	public List(Rectangle position, Window win)
	{
		this(position, null, win);
	}
	public List(Rectangle position, ArrayList<String> entries, Window win)
	{
		if(entries != null) _entries = entries;
		_position = position;
		
		int yOffset = 30;
		for(String e : _entries)
		{
			int amountRows = Draw.getStringLines(e, 
					_position.width - 6);

			yOffset += 60 + // 2 * 30px space above and below string
				Draw.getStringSize(e).height * amountRows + // rows * height of string 
				(amountRows - 1) * 8; // space between lines
		}
		if(yOffset > _position.height) _maxOffset = yOffset - _position.height - _scrollHeight - 3;
		
		_windowHandle = win;
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
		
		//Clear Background
		Fill.setColor(Color.white);
		Fill.Rect(_position.x, _position.y, _position.width, _position.height);
		
		int yOffset = 30;
		Point mPos = MouseInfo.getPointerInfo().getLocation();
		for(int i = 0; i < _entries.size(); i++)
		{
			int amountRows = Draw.getStringLines(_entries.get(i), _position.width - 6);
			Draw.setColor(Color.lightGray);
			
			// Calculate rect for the border = rect of entry
			Rectangle entryRect = new Rectangle(_position.x + 3, 
				_position.y - 25 + _offset + yOffset, 
				_position.width - 9, 
				60 + // 2 * 30px space above and below string
					Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string 
					(amountRows - 1) * 8 // space between lines
					 - 3);
			
			if(entryRect.contains(mPos)) // when mouse is in an entry, make background light-light-gray
			{
				Fill.setColor(new Color(240, 240, 240));
				Fill.Rect(entryRect);
			}
			
			//Draw border around entry
			Draw.Rect(_position.x + 3, 
				_position.y - 25 + _offset + yOffset, 
				_position.width - 9, 
				60 + // 2 * 30px space above and below string
					Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string 
					(amountRows - 1) * 8 // space between lines
					 - 3); // gap between rects
			
			// Draw the string
			Draw.setColor(Color.black);
			Draw.String(_entries.get(i), _position.x + 20, _position.y + _offset + yOffset + 5, _position.width - 40);
			

			yOffset += 60 + // 2 * 30px space above and below string
				Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string 
				(amountRows - 1) * 8; // space between lines
		}
		
		//Fill the little gabs ob the two top and bottom lines
		Fill.Rect(_position.x, _position.y, _position.width, 3);
		Fill.Rect(_position.x, _position.y + _position.height - 2, _position.width, 3);
		
		//Draw all the border lines. The two top and bottom lines are just looking cool ;)
		Draw.setColor(METRO.__metroBlue);
		Draw.Rect(_position);
		Draw.Line(_position.x, _position.y + _position.height - 2, _position.x + _position.width - 3, _position.y + _position.height - 2); // bottom 2
		Draw.Line(_position.x + _position.width - 3, _position.y, _position.x + _position.width - 3, _position.y + _position.height); // right for scroll bar
		Draw.Line(_position.x, _position.y + 2, _position.x + _position.width - 3, _position.y + 2); // top 2
		
		//Draw scroll bar
		int height = (int)(_position.height * (_position.height / (_maxOffset * _scrollHeight))), // gets the height of the scrollbar. E.g.: The area is 2*_pos.height, then is _max/_scrollHeight = 0.5
			yPos = (int)((_position.height - height) * -(_offset / _maxOffset));
		
		Fill.setColor(METRO.__metroBlue);
		Fill.Rect(_position.x + _position.width - 3, 
			_position.y + yPos, 
			_position.x + _position.width - 2, 
			height); // right for scroll bar
		
		ScissorStack.popScissors();
	}
	@Override
	public boolean clickOnControlElement() 
	{
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
		return new Point(_position.x, _position.y);
	}
	@Override
	public void moveElement(Point offset) 
	{
		_position.x += offset.x;
		_position.y += offset.y;
	}
	/**
	 * Adds an element to the list control.
	 * @param element The element to add.
	 */
	public void addElement(String element)
	{
		_entries.add(element);
	}
	@Override
	public void mouseScrolled(int amount) 
	{
		if(_position.contains(MouseInfo.getPointerInfo().getLocation()))
		{
			_offset += -1 * amount * _scrollHeight;
			if(_offset > 0) _offset = 0;
			if(_offset < -_maxOffset) _offset = (int)-_maxOffset;
		}
	}
}
