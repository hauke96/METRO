package metro.WindowControls;

import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.Input.Buttons;

import metro.METRO;

/**
 * The ControlActionManager knows every ControlElement and handles the user input events.
 * Every ControlElement, that has been created and uses the ActionObserver system, is registered in this manager.
 * 
 * @author hauke
 *
 */
public class ControlActionManager
{
	ArrayList<ControlElement> _listOfControlElements,
		_bufferListOfControlElements;
	boolean _currentlyIterating;

	/**
	 * Creates a new action manager by initializing all lists.
	 */
	public ControlActionManager()
	{
		_listOfControlElements = new ArrayList<ControlElement>();
		_bufferListOfControlElements = new ArrayList<ControlElement>();
		_currentlyIterating = false;
	}

	/**
	 * Registers a new ControlElement and deleted one if it's already registered.
	 * If this all happens during another button click (or other ControlAction method), every control is written
	 * into a buffer list and later (in the updateList() method) added to the real list.
	 * 
	 * @param control The control to register/remove.
	 */
	public void registerElement(ControlElement control)
	{
		if(_currentlyIterating)
		{
			if(_bufferListOfControlElements.contains(control)) _bufferListOfControlElements.remove(control);
			else _bufferListOfControlElements.add(control);
		}
		else
		{
			if(_listOfControlElements.contains(control)) _listOfControlElements.remove(control);
			else _listOfControlElements.add(control);
		}
	}

	/**
	 * This method forwards a user click by calling its mouseLeftClick() method.
	 * It also updates the list of control afterwards (to prevent unexpected exceptions).
	 * 
	 * @param screenX The x coordinate of the mouse.
	 * @param screenY The y coordinate of the mouse.
	 * @param button The mouse button (Buttons.xy).
	 * @param clickedWindow The clicked window. Hand over {@code null} when no window has been clicked.
	 * @return boolean True when the mouse clicked on one control, false when not.
	 */
	public boolean mouseClicked(int screenX, int screenY, int button)
	{
		boolean controlClicked = false;
<<<<<<< HEAD

=======
>>>>>>> 56bf8a70a7f574f91b53f3e3a9ceb9529982f89d
		if(button == Buttons.LEFT)
		{
			_currentlyIterating = true;
			boolean inputClicked = false;
			for(ControlElement control : _listOfControlElements)
			{
				boolean b = control.mouseClicked(screenX, screenY, button);
				if(b && control instanceof InputField) // if clicked element is an input field, set this as selected field
				{
					inputClicked = true;
					METRO.__setSelectedInput((InputField)control);
				}

				controlClicked |= b;
			}
			if(!inputClicked) METRO.__setSelectedInput(null);
			_currentlyIterating = false;
			updateList();
		}
		return controlClicked;
	}

	/**
	 * Calls the scroll method on the control the mouse hovers currently.
	 * The checking if the mouse hovers an object or not goes from back to front.
	 * 
	 * @param amount The amount of scroll steps.
	 */
	public void mouseScroll(int amount)
	{
		Point mPos = METRO.__originalMousePosition;
		for(int i = _listOfControlElements.size() - 1; i > 0; i--)
		{
			ControlElement control = _listOfControlElements.get(i);
			if(control.getArea().contains(mPos))
			{
				control.mouseScrolled(amount);
				break;
			}
		}
	}

	/**
	 * Updates the list of all controls by copying the buffered list into the normal list.
	 * This method uses the registerElement(Control) method, to there're normal deletions as well.
	 */
	private void updateList()
	{
		for(ControlElement control : _bufferListOfControlElements)
		{
			registerElement(control);
		}
		_bufferListOfControlElements.clear();
	}

	/**
	 * Removes a control from the list of controls of the action manager.
	 * 
	 * @param elements The control to remove.
	 */
	public void remove(ArrayList<ControlElement> elements)
	{
		_bufferListOfControlElements.addAll(elements);
	}

	/**
	 * Removes the given control element from the list of all controls.
	 * You can also remove a whole list by using remove(ArrayList\<ControlElement\>);
	 * 
	 * @param control The control to remove.
	 */
	public void remove(ControlElement control)
	{
		_bufferListOfControlElements.add(control);
	}
}
