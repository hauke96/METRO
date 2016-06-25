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

public class List extends ControlElement
{
	private ArrayList<String> _entries = new ArrayList<String>();
	private int _offset,
		_defaultYSpace, // space between text and border in the normal mode
		_compactYSpace, // space between text and border in the compact mode
		_selectedEntry; // the entry, that was clicked
	private int _maxOffset,
		_scrollHeight; // height of one scroll step
	private boolean _compact = false, // less space between text and top/bottom edge
		_decorated = true,
		_sticky = false;
	private Color _backgroundColor,
		_hoverColor;

	/**
	 * Creates a new list control element on a window.
	 * 
	 * @param position The position on the window.
	 */
	public List(Rectangle position)
	{
		this(position, null, false);
	}

	/**
	 * Creates a new list control element on a window.
	 * 
	 * @param position The position on the window.
	 * @param compact Set to true if list should be a compact list (entries are not that high)
	 */
	public List(Rectangle position, boolean compact)
	{
		this(position, null, compact);
	}

	/**
	 * Creates a new list control element on a window with some entries in it.
	 * 
	 * @param position The position on the window.
	 * @param entries The entries that are in the list after creating it.
	 */
	public List(Rectangle position, ArrayList<String> entries)
	{
		this(position, entries, false);
	}

	/**
	 * Creates a new list control element on a window with some entries in it.
	 * 
	 * @param position The position on the window.
	 * @param entries The entries that are in the list after creating it.
	 * @param compact Set to true if list should be a compact list (entries are not that high)
	 */
	public List(Rectangle position, ArrayList<String> entries, boolean compact)
	{
		if(entries != null) _entries = entries;

		_area = position;
		_compact = compact;
		_offset = 0;
		_defaultYSpace = 30;
		_compactYSpace = 8;
		_maxOffset = 0;
		_scrollHeight = 20;

		_backgroundColor = new Color(255, 255, 255, 255);
		_hoverColor = new Color(240, 240, 240, 255);

		setSelectedEntry(-1);
		calcMaxOffset();

	}

	/**
	 * Calculates the maximum offset for the list to scroll. If there're to less elements for scrolling the maximum offset is 0.
	 */
	private void calcMaxOffset()
	{
		int ySpace = _defaultYSpace;
		if(_compact) ySpace = _compactYSpace;
		int yOffset = ySpace;

		for(String e : _entries)
		{
			int amountRows = Draw.getStringLines(e, _area.width - 6);

			yOffset += 2 * ySpace + // space above and below string
				Draw.getStringSize(e).height * amountRows + // rows * height of string
				(amountRows - 1) * 8; // space between lines
		}
		yOffset += ySpace;
		int magicFactor = (23 / 20) * (ySpace - 10) + 17; // fine-tuning for the maximum offset for scrolling (with this factor, the space between last element and bottom of list are matching automagically)
		if(yOffset - _scrollHeight - 3 > _area.height) _maxOffset = yOffset - _area.height - _scrollHeight + magicFactor;
		else _maxOffset = 0;
	}

	/**
	 * Adds an element to the list control.
	 * 
	 * @param element The element to add.
	 */
	public void addElement(String element)
	{
		_entries.add(element);
		int oldOffset = _offset;
		int oldMaxOffset = _maxOffset;

		calcMaxOffset();

		if(_sticky && oldOffset == -oldMaxOffset)
		{
			_offset = -_maxOffset;
		}
	}

	/**
	 * Removes the entry at this position.
	 * 
	 * @param entryIndex The position of the entry.
	 */
	public void removeElement(int entryIndex)
	{
		if(entryIndex < 0 || entryIndex >= _entries.size()) return;
		_entries.remove(entryIndex);
		setSelectedEntry(-1);
		calcMaxOffset();
		if(-_offset > _maxOffset) _offset = -_maxOffset;
	}

	/**
	 * Gets the text of a given entry.
	 * 
	 * @param entryIndex The number of the entry.
	 * @return String The text of the entry. It's "" if the entry doesn't exist.
	 */
	public String getText(int entryIndex)
	{
		if(entryIndex >= _entries.size() || entryIndex < 0) return "";
		return _entries.get(entryIndex);
	}

	/**
	 * Returns the text of the selected entry of this list.
	 * When the entry does not exists or nothing is selected an empty string will be returned.
	 * 
	 * @return The text of the selecting entry. {@code ""} when selection is invalid.
	 */
	public String getText()
	{
		return getText(_selectedEntry);
	}

	/**
	 * Sets the selected Entry to a specific.
	 * 
	 * @param entryIndex The entry number.
	 */
	public void setSelectedEntry(int entryIndex)
	{
		_selectedEntry = entryIndex;
		if(0 <= _selectedEntry && _selectedEntry < _entries.size())
		{
			notifySelectionChanged(_entries.get(_selectedEntry));
		}
		else
		{
			notifySelectionChanged(null);
		}
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
		for(int i = 0; i < _entries.size(); ++i)
		{
			if(_entries.get(i).equals(entryText))
			{
				return i;
			}
		}
		return -1;
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
	protected void draw()
	{
		METRO.__spriteBatch.end();
		METRO.__spriteBatch.begin();

		// Create scissor to draw only in the area of the list box.
		com.badlogic.gdx.math.Rectangle scissors = new com.badlogic.gdx.math.Rectangle();
		com.badlogic.gdx.math.Rectangle clipBounds = new com.badlogic.gdx.math.Rectangle(_area.x + METRO.__getXOffset(), _area.y + METRO.__getYOffset(),
			_area.width + 1,
			_area.height + 1);
		ScissorStack.calculateScissors((Camera)METRO.__camera, METRO.__spriteBatch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);

		if(_decorated) clearBackground();

		drawEntries();

		if(_decorated) drawBorders();

		drawScrollbar();

		ScissorStack.popScissors();
	}

	/**
	 * Makes the background white.
	 */
	private void clearBackground()
	{
		Fill.setColor(_backgroundColor);
		Fill.Rect(_area.x, _area.y, _area.width, _area.height);
	}

	/**
	 * Draws all list entries.
	 */
	private void drawEntries()
	{
		int ySpace = _defaultYSpace;
		if(_compact) ySpace = _compactYSpace;
		int yOffset = ySpace;
		Point mPos = METRO.__originalMousePosition;
		for(int i = 0; i < _entries.size(); ++i)
		{
			int amountRows = Draw.getStringLines(_entries.get(i), _area.width - 6);
			Draw.setColor(Color.lightGray);

			// Calculate rect for the border = rect of entry
			Rectangle entryRect = new Rectangle(_area.x + 3,
				_area.y + _offset + (yOffset - ySpace) + 5,
				_area.width - 9,
				2 * ySpace + // space above and below string
					Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string
					(amountRows - 1) * 8 // space between lines
					- 1);

			// Hover effect: when mouse is in an entry, make background light-light-gray
			if(_state && entryRect.contains(mPos))
			{
				Fill.setColor(_hoverColor);
				Fill.Rect(entryRect);
			}

			// Draw border for selected entry
			if(_state && _decorated)
			{
				if(_selectedEntry == i)
				{
					Draw.setColor(Color.gray);
				}
				Draw.Rect(_area.x + 3,
					_area.y + _offset + (yOffset - ySpace) + 5,
					_area.width - 9,
					2 * ySpace + // space above and below string
						Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string
						(amountRows - 1) * 8 // space between lines
						- 3); // gap between rects
			}
			else if(_state) // enabled but not decoryted --> just a line between entries
			{
				Draw.Line(_area.x + 8,
					_area.y + _offset + (yOffset - ySpace) + 4,
					_area.x + _area.width - 11,
					_area.y + _offset + (yOffset - ySpace) + 4);
			}

			// Draw the string
			Draw.setColor((_state ? Color.black : Color.gray)); // gray when disabled
			Draw.String(_entries.get(i), _area.x + 20, _area.y + _offset + yOffset + 4, _area.width - 40);

			yOffset += 2 * ySpace + // 2 * 30px space above and below string
				Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string
				(amountRows - 1) * 8; // space between lines
		}
	}

	/**
	 * Draws the borders and some fancy lines around the list.
	 */
	private void drawBorders()
	{
		// Fill the little gabs of the two top and bottom lines
		Fill.Rect(_area.x, _area.y, _area.width, 3);
		Fill.Rect(_area.x, _area.y + _area.height - 2, _area.width, 3);

		// Draw all the border lines. The two top and bottom lines are just looking cool ;)
		Draw.setColor(METRO.__metroBlue);
		Draw.Rect(_area);
		Draw.Line(_area.x, _area.y + _area.height - 2, _area.x + _area.width - 3, _area.y + _area.height - 2); // bottom 2
		Draw.Line(_area.x + _area.width - 3, _area.y, _area.x + _area.width - 3, _area.y + _area.height); // right for scroll bar
		Draw.Line(_area.x, _area.y + 2, _area.x + _area.width - 3, _area.y + 2); // top 2
	}

	/**
	 * Draws the scrollbar line.
	 */
	private void drawScrollbar()
	{
		int height = _maxOffset == 0 ? _area.height : (int)((_area.height / (float)(_area.height + _maxOffset)) * _area.height);
		int yPos = (int)((_area.height - height) * -(_offset / (float)_maxOffset));

		Fill.setColor(METRO.__metroBlue);
		Fill.Rect(_area.x + _area.width - 3,
			_area.y + yPos,
			_area.x + _area.width - 2,
			height); // right for scroll bar
	}

	/**
	 * Does all actions when the mouse clicked on this control.
	 * 
	 * @return True when user clicked on control, false if not.
	 */
	public boolean clickOnControlElement()
	{
		Point mPos = METRO.__originalMousePosition;
		if(!_area.contains(mPos)) return false;

		int ySpace = _defaultYSpace;
		if(_compact) ySpace = _compactYSpace;
		int yOffset = ySpace;

		for(int i = 0; i < _entries.size(); ++i)
		{
			int amountRows = Draw.getStringLines(_entries.get(i), _area.width - 6);
			Draw.setColor(Color.lightGray);

			// Calculate rect for the border = rect of entry
			Rectangle entryRect = new Rectangle(_area.x + 3,
				_area.y + _offset + (yOffset - ySpace) + 5,
				_area.width - 9,
				2 * ySpace + // space above and below string
					Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string
					(amountRows - 1) * 8 // space between lines
					- 3);

			if(_state && entryRect.contains(mPos)) // when mouse is in an entry, make background light-light-gray
			{
				setSelectedEntry(i);
				return true;
			}

			yOffset += 2 * ySpace + // 2 * 30px space above and below string
				Draw.getStringSize(_entries.get(i)).height * amountRows + // rows * height of string
				(amountRows - 1) * 8; // space between lines
		}

		return _area.contains(mPos);
	}

	/**
	 * Removes all entries from this list control including the selected entry.
	 */
	public void clear()
	{
		_entries.clear();
		_offset = 0;
		_defaultYSpace = 30;
		_compactYSpace = 8;
		setSelectedEntry(-1);
		_maxOffset = 0;
		_scrollHeight = 20;
	}

	@Override
	boolean mouseClicked(int screenX, int screenY, int button)
	{
		if(clickOnControlElement())
		{
			notifyClickOnControl(null);
			return true;
		}
		return false;
	}

	@Override
	void mouseReleased(int screenX, int screenY, int button)
	{
	}

	@Override
	public void moveElement(Point offset)
	{
		_area.x += offset.x;
		_area.y += offset.y;
	}

	@Override
	void mouseScrolled(int amount)
	{
		// when the _offset is 0, the scroll bar is at the very top.
		// when the _offset is -_maxOffset, the scroll bar is at the very bottom.
		if(_state && _area.contains(METRO.__originalMousePosition))
		{
			_offset += -1 * amount * _scrollHeight;
			if(_offset > 0) _offset = 0;
			if(_offset < -_maxOffset) _offset = (int)-_maxOffset;
		}
	}

	@Override
	void keyPressed(int key)
	{
	}

	@Override
	void keyUp(int keyCode)
	{
	}

	/**
	 * Sets the selected entry of this list.
	 * When the entry does not exist, nothing changes.
	 * Beware: When there's more than one entry with this value, it can happen that an entry becomes selected you don't want to have selected.
	 */
	public void setText(String entry)
	{
		if(_entries.contains(entry))
		{
			_selectedEntry = getIndex(entry);
			if(_selectedEntry != -1)
			{
				setSelectedEntry(_selectedEntry);
			}
		}
	}

	/**
	 * Sets the style of the list. An undecorated list has no background, no borders, only the entries and the scroll bar.
	 * 
	 * @param decorated True to have normal decoration (borders etc.), false to have entries and scroll bar only.
	 */
	public void setDecoration(boolean decorated)
	{
		_decorated = decorated;
	}

	/**
	 * Sets the transparency of the background color and the color of hovered entries. Does not influence line colors (borders etc.) and text colors.
	 * 
	 * @param transparency The transparency from 0 (fully transparent) to 255 (not at all).
	 */
	public void setTransparency(int transparency)
	{
		_backgroundColor = new Color(255, 255, 255, transparency);
		int c = 240 - (255 - transparency) / 4;
		_hoverColor = new Color(c, c, c, transparency);
	}

	/**
	 * Sets the stickiness of the list.
	 * When the list is sticky, the scroll bar is at the very top/bottom it stays at the top/bottom when a new entry is added to the list.
	 * When the list is sticky but the scroll bar is somewhere in the middle, the list acts normal.
	 * 
	 * @param sticky True to set the list sticky, false to not set it sticky.
	 */
	public void setStickiness(boolean sticky)
	{
		_sticky = sticky;
	}
}
