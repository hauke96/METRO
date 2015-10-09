package metro.WindowControls;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import metro.METRO;
import metro.Graphics.Draw;
import metro.Graphics.Fill;

/**
 * Creates a list control element that can have some entries in it. An entry is highlighted when the mouse is over it.
 * 
 * @author hauke
 *
 */

public class List extends ActionObservable implements ControlElement
{
	private ArrayList<String> _entries = new ArrayList<String>();
	private Rectangle _position;
	private Window _windowHandle;
	private int _offset,
		_defaultYSpace,
		_compactYSpace,
		_selectedEntry; // the entry, that was clicked
	private float _maxOffset,
		_scrollHeight; // height of one scroll
	private boolean _compact = false, // less space between text and top/bottom edge
		_enabled = true;

	/**
	 * Creates a new list control element on a window.
	 * 
	 * @param position The position on the window.
	 * @param win The window with which this list of connected.
	 */
	public List(Rectangle position, Window win)
	{
		this(position, null, win, false);
	}

	/**
	 * Creates a new list control element on a window.
	 * 
	 * @param position The position on the window.
	 * @param win The window with which this list of connected.
	 * @param compact Set to true if list should be a compact list (entries are not that high)
	 */
	public List(Rectangle position, Window win, boolean compact)
	{
		this(position, null, win, compact);
	}

	/**
	 * Creates a new list control element on a window with some entries in it.
	 * 
	 * @param position The position on the window.
	 * @param entries The entries that are in the list after creating it.
	 * @param win The window with which this list of connected.
	 */
	public List(Rectangle position, ArrayList<String> entries, Window win)
	{
		this(position, entries, win, false);
	}

	/**
	 * Creates a new list control element on a window with some entries in it.
	 * 
	 * @param position The position on the window.
	 * @param entries The entries that are in the list after creating it.
	 * @param win The window with which this list of connected.
	 * @param compact Set to true if list should be a compact list (entries are not that high)
	 */
	public List(Rectangle position, ArrayList<String> entries, Window win, boolean compact)
	{
		if(entries != null) _entries = entries;

		_position = position;
		_offset = 0;
		_defaultYSpace = 30;
		_compactYSpace = 8;
		_selectedEntry = -1;
		_maxOffset = 0;
		_scrollHeight = 20;

		int ySpace = _defaultYSpace;
		if(_compact) ySpace = _compactYSpace;
		int yOffset = ySpace;

		for(String e : _entries)
		{
			int amountRows = Draw.getStringLines(e, _position.width - 6);

			yOffset += 2 * ySpace + // 2 * 30px space above and below string
				Draw.getStringSize(e).height * amountRows + // rows * height of string
				(amountRows - 1) * 8; // space between lines
		}
		if(yOffset > _position.height) _maxOffset = yOffset - _position.height - _scrollHeight - 3;

		_windowHandle = win;
		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
		METRO.__registerControl(this);
		
		_compact = compact;
	}

	/**
	 * Adds an element to the list control.
	 * 
	 * @param element The element to add.
	 */
	public void addElement(String element)
	{
		_entries.add(element);
		int ySpace = _defaultYSpace;
		if(_compact) ySpace = _compactYSpace;
		int yOffset = ySpace;
		for(String e : _entries)
		{
			int amountRows = Draw.getStringLines(e,
				_position.width - 6);

			yOffset += 2 * ySpace + // space above and below string
				Draw.getStringSize(e).height * amountRows + // rows * height of string
				(amountRows - 1) * 8; // space between lines
		}
		int magicFactor = (0 - (23 / 20)) * (ySpace - 10) + 17; // fine-tuning for the maximum offset for scrolling (with this factor, the space between last element and bottom of list are matching automagically)
		if(yOffset - _scrollHeight - 3 > _position.height) _maxOffset = yOffset - _position.height - _scrollHeight + magicFactor;
	}

	@Override
	public void setState(boolean enableState)
	{
		_enabled = enableState;
	}

	/**
	 * Gets the text of a given entry.
	 * 
	 * @param entryIndex The number of the entry.
	 */
	public String getText(int entryIndex)
	{
		if(entryIndex >= _entries.size() || entryIndex < 0) return "";
		return _entries.get(entryIndex);
	}

	/**
	 * Sets the selected Entry to a specific.
	 * 
	 * @param entryIndex The entry number.
	 */
	public void setSelectedEntry(int entryIndex)
	{
		_selectedEntry = entryIndex;
		notifySelectionChanged(_selectedEntry != -1 ? _entries.get(_selectedEntry): null);
	}

	/**
	 * Return the index of the selected entry.
	 * 
	 * @return Index of selected entry. Returns -1 if nothing is selected.
	 */
	public int getSelected()
	{
		return _selectedEntry;
	}

	/**
	 * Returns the index of the first entry with the given text.
	 * 
	 * @param entryText The text to search.
	 * @return The index. Returns -1 if the entry doesn't exist.
	 */
	public int getIndex(String entryText)
	{
		for(int i = 0; i < _entries.size(); i++)
		{
			if(_entries.get(i).equals(entryText))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Removes the entry at this position.
	 * 
	 * @param entryIndex The position of the entry.
	 */
	public void remove(int entryIndex)
	{
		if(entryIndex < 0 || entryIndex >= _entries.size()) return;
		_entries.remove(entryIndex);
		_selectedEntry = -1;
		notifySelectionChanged(null);
	}

	/**
	 * Checks if the given text is in the list.
	 * 
	 * @param text The entry to search.
	 * @return True when the text is in the list, false if not.
	 */
	public boolean contains(String text)
	{
		return _entries.contains(text);
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

		// Clear Background
		Fill.setColor(Color.white);
		Fill.Rect(_position.x, _position.y, _position.width, _position.height);

		int ySpace = _defaultYSpace;
		if(_compact) ySpace = _compactYSpace;
		int yOffset = ySpace;
		Point mPos = METRO.__originalMousePosition;
		for(int i = 0; i < _entries.size(); i++)
		{
			int amountRows = Draw.getStringLines(_entries.get(i), _position.width - 6);
			Draw.setColor(Color.lightGray);

			// Calculate rect for the border = rect of entry
			Rectangle entryRect = new Rectangle(_position.x + 3,
				_position.y + _offset + (yOffset - ySpace) + 5,
				_position.width - 9,
				2 * ySpace + // space above and below string
					Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string
					(amountRows - 1) * 8 // space between lines
					- 3);

			if(_enabled && entryRect.contains(mPos)) // when mouse is in an entry, make background light-light-gray
			{
				Fill.setColor(new Color(240, 240, 240));
				Fill.Rect(entryRect);
			}

			// Draw border around entry
			if(_enabled && _selectedEntry == i)
			{
				Draw.setColor(Color.gray);
			}
			Draw.Rect(_position.x + 3,
				_position.y + _offset + (yOffset - ySpace) + 5,
				_position.width - 9,
				2 * ySpace + // space above and below string
					Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string
					(amountRows - 1) * 8 // space between lines
					- 3); // gap between rects

			// Draw the string
			Draw.setColor((_enabled ? Color.black : Color.gray)); // gray when disabled
			Draw.String(_entries.get(i), _position.x + 20, _position.y + _offset + yOffset + 4
				, _position.width - 40);

			yOffset += 2 * ySpace + // 2 * 30px space above and below string
				Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string
				(amountRows - 1) * 8; // space between lines
		}

		// Fill the little gabs ob the two top and bottom lines
		Fill.Rect(_position.x, _position.y, _position.width, 3);
		Fill.Rect(_position.x, _position.y + _position.height - 2, _position.width, 3);

		// Draw all the border lines. The two top and bottom lines are just looking cool ;)
		Draw.setColor(METRO.__metroBlue);
		Draw.Rect(_position);
		Draw.Line(_position.x, _position.y + _position.height - 2, _position.x + _position.width - 3, _position.y + _position.height - 2); // bottom 2
		Draw.Line(_position.x + _position.width - 3, _position.y, _position.x + _position.width - 3, _position.y + _position.height); // right for scroll bar
		Draw.Line(_position.x, _position.y + 2, _position.x + _position.width - 3, _position.y + 2); // top 2

		// Draw scroll bar
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
		Point mPos = METRO.__originalMousePosition;
		if(!_position.contains(mPos)) return false;

		int ySpace = _defaultYSpace;
		if(_compact) ySpace = _compactYSpace;
		int yOffset = ySpace;

		for(int i = 0; i < _entries.size(); i++)
		{
			int amountRows = Draw.getStringLines(_entries.get(i), _position.width - 6);
			Draw.setColor(Color.lightGray);

			// Calculate rect for the border = rect of entry
			Rectangle entryRect = new Rectangle(_position.x + 3,
				_position.y + _offset + (yOffset - ySpace) + 5,
				_position.width - 9,
				2 * ySpace + // space above and below string
					Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string
					(amountRows - 1) * 8 // space between lines
					- 3);

			if(_enabled && entryRect.contains(mPos)) // when mouse is in an entry, make background light-light-gray
			{
				_selectedEntry = i;
				notifySelectionChanged(_entries.get(_selectedEntry));
				return true;
			}

			yOffset += 2 * ySpace + // 2 * 30px space above and below string
				Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string
				(amountRows - 1) * 8; // space between lines
		}

		_selectedEntry = -1;
		notifySelectionChanged(null);

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
		return new Point(_position.x, _position.y);
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
	public void moveElement(Point offset)
	{
		_position.x += offset.x;
		_position.y += offset.y;
	}

	@Override
	public void mouseScrolled(int amount)
	{
		if(_enabled && _position.contains(METRO.__originalMousePosition))
		{
			_offset += -1 * amount * _scrollHeight;
			if(_offset > 0) _offset = 0;
			if(_offset < -_maxOffset) _offset = (int)-_maxOffset;
		}
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
	}
}
