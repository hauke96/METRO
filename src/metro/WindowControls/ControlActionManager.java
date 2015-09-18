package metro.WindowControls;

import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.Input.Buttons;

/**
 * The ControlActionManager knows every ControlElement and handles the user input events.
 * Every ControlElement that has been created and uses the ActionObserver system is registered in this manager.
 * 
 * @author hauke
 *
 */
public class ControlActionManager
{
	ArrayList<ControlElement> _listOfControlElements,
		_bufferListOfControlElements;
	boolean _currentlyIterating;

	public ControlActionManager()
	{
		_listOfControlElements = new ArrayList<ControlElement>();
		_bufferListOfControlElements = new ArrayList<ControlElement>();
		_currentlyIterating = false;
	}

	/**
	 * Registeres a new ControlElement and deleted one if it's already registered.
	 * If this all happens during another button click (or other ControlAction method), every control is written
	 * into a buffer list and later (und the updateList() method) added to the real list.
	 * 
	 * @param control The control to register/remove.
	 */
	public void registerElement(ControlElement control)
	{
		if(_currentlyIterating)
		{
			_bufferListOfControlElements.add(control);
		}
		else
		{
			if(_listOfControlElements.contains(control)) _listOfControlElements.remove(control);
			else _listOfControlElements.add(control);
		}
	}

	public void moveElement(Point offset)
	{
	}

	public void mouseScrolled(int amount)
	{
	}

	/**
	 * This method forwards a user click by calling its mouseLeftClick() method.
	 * It also updates the list of control afterwards (to prevent unexpected exceptions).
	 * 
	 * @param screenX The x coordinate of the mouse.
	 * @param screenY The y coordinate of the mouse.
	 * @param button The mouse button (Buttons.xy).
	 */
	public boolean mouseClicked(int screenX, int screenY, int button)
	{
		boolean controlClicked = false;
		if(button == Buttons.LEFT)
		{
			_currentlyIterating = true;
			for(ControlElement control : _listOfControlElements)
			{
				controlClicked |= control.mouseLeftClicked(screenX, screenY, button);
			}
			_currentlyIterating = false;
			updateList();
		}
		return controlClicked;
	}

	public void keyPressed(int keyCode)
	{
	}

	public void keyUp(int keyCode)
	{
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
}
