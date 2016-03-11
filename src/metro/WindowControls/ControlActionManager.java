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
	private ArrayList<ControlElement> _controlElements,
		_bufferedControlElements,
		_removedControlElements;
	boolean _currentlyIterating;

	/**
	 * Creates a new action manager by initializing all lists.
	 */
	public ControlActionManager()
	{
		_controlElements = new ArrayList<ControlElement>();
		_bufferedControlElements = new ArrayList<ControlElement>();
		_removedControlElements = new ArrayList<ControlElement>();
		_currentlyIterating = false;
	}

	/**
	 * Registers a new ControlElement by adding the control into a buffer list.
	 * The METRO class will update the real list of control before starting a new render cycle.
	 * 
	 * @param control The control to register/remove.
	 */
	public void registerElement(ControlElement control)
	{
		if(!_bufferedControlElements.contains(control)) _bufferedControlElements.add(control);
	}

	/**
	 * Registers a new ControlElement.
	 * If this all happens during another button click (or other ControlAction method), every control is written
	 * into a buffer list and later (in the updateList() method) added to the real list.
	 * 
	 * @param control The control to register/remove.
	 */
	private void registerElementIntern(ControlElement control)
	{
		if(_currentlyIterating)
		{
			_bufferedControlElements.add(control);
		}
		else
		{
			_controlElements.add(control);
		}
	}

	/**
	 * Removes a control from the lists of control elements.
	 * In comparison to the {@link #registerElementIntern(ControlElement)} method,
	 * this one won't change the buffer list.
	 * 
	 * @param control The control that should be removed.
	 */
	private void removeElementIntern(ControlElement control)
	{
		if(!_currentlyIterating)
		{
			_controlElements.remove(control);
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
	public boolean mouseClicked(int screenX, int screenY, int button, Window clickedWindow)
	{
		ArrayList<ControlElement> listOfControls = clickedWindow != null
			? clickedWindow.getElements()
			: _controlElements;
		boolean controlClicked = false;

		if(button == Buttons.LEFT)
		{
			_currentlyIterating = true;
			boolean inputClicked = false;
			for(ControlElement control : listOfControls)
			{
				if(control.getState())
				{
					boolean b = control.mouseClicked(screenX, screenY, button);
					if(b && control instanceof InputField) // if clicked element is an input field, set this as selected field
					{
						inputClicked = true;
						METRO.__setSelectedInput((InputField)control);
					}

					controlClicked |= b;
				}
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
		for(int i = _controlElements.size() - 1; i > 0; i--)
		{
			ControlElement control = _controlElements.get(i);
			if(control.getState())
			{
				if(control.getArea().contains(mPos))
				{
					control.mouseScrolled(amount);
					break;
				}
			}
		}
	}

	/**
	 * Updates the list of all controls by copying the buffered list into the normal list.
	 * This method uses the registerElement(Control) method, to there're normal deletions as well.
	 */
	public void updateList()
	{
		for(ControlElement control : _removedControlElements)
		{
			_bufferedControlElements.remove(control);
			removeElementIntern(control);
		}
		_removedControlElements.clear();

		for(ControlElement control : _bufferedControlElements)
		{
			registerElementIntern(control);
		}
		_bufferedControlElements.clear();
	}

	/**
	 * Removes a control from the list of controls of the action manager.
	 * 
	 * @param elements The control to remove.
	 */
	public void remove(ArrayList<ControlElement> elements)
	{
		/*
		 * I know that there's the addAll() method but i don't
		 * want to have doubles and remove(ControlElement)
		 * ensures this fact.
		 */
		for(ControlElement control : elements)
		{
			remove(control);
		}
	}

	/**
	 * Removes the given control element from the list of all controls.
	 * You can also remove a whole list by using remove(ArrayList\<ControlElement\>);
	 * 
	 * @param control The control to remove.
	 */
	public void remove(ControlElement control)
	{
		if(!_removedControlElements.contains(control)) _removedControlElements.add(control);
	}
}
