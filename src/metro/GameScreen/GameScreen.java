package metro.GameScreen;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Settings;
import metro.Graphics.Draw;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;
import metro.WindowControls.Checkbox;
import metro.WindowControls.ControlActionManager;
import metro.WindowControls.ControlElement;
import metro.WindowControls.InputField;
import metro.WindowControls.Label;
import metro.WindowControls.List;
import metro.WindowControls.Window;

/**
 * Every Menu or Game Sreen has to implement this interface for start() and update(). This will make the creation process more easy.
 * 
 * @author Hauke
 * 
 */

public abstract class GameScreen extends Observable
{
	/**
	 * Makes it possible to create a settings-window from every(!) game screen.
	 */
	public SettingsWindow _settingsWindow;
	/**
	 * Makes it possible to create a ingame-menu-window from every(!) game screen.
	 */
	public InGameMenuWindow _inGameMenuWindow;
	private static InputField _selectedInput = null;
	private static ControlActionManager _controlActionManager;
	/**
	 * A list of all control. Seems senseless due to the list in the ActionControlManager, but this is a list with control that only belongs to this game screen.
	 */
	private ArrayList<ControlElement> _allControls = new ArrayList<>();

	/**
	 * Sets the new control action manager.
	 * 
	 * @param manager An instance of the ControlActionManager.
	 */
	public static void setActionManager(ControlActionManager manager)
	{
		_controlActionManager = manager;
	}

	/**
	 * Will be executed as fast as possible ;)
	 * This method will actually not directly update the game screen, but fires the {@code updateGameScreen(g)} method to do this.
	 * 
	 * @param g SpriteBatch to use.
	 */
	public void update(SpriteBatch g)
	{
		updateGameScreen(g);
	}

	/**
	 * Updates the actual game screen.
	 * 
	 * @param g SpriteBatch to draw on.
	 */
	public abstract void updateGameScreen(SpriteBatch g);

	/**
	 * When mouse has clicked
	 * 
	 * @param screenX The x-position on the screen
	 * @param screenY The y-position on the screen
	 * @param mouseButton The number of the button like Buttons.LEFT
	 */
	public abstract void mouseClicked(int screenX, int screenY, int mouseButton);

	/**
	 * When mouse has been released.
	 * 
	 * @param mouseButton The number of the button like Buttons.LEFT
	 */
	public abstract void mouseReleased(int mouseButton);

	/**
	 * When a key was pressed.
	 * 
	 * @param keyCode Key number from Gdx.Input
	 */
	public void keyPressed(int keyCode)
	{
		if(_selectedInput == null)
		{
			if(keyCode == Keys.ESCAPE && _inGameMenuWindow == null) // Show in game window if no input control and no other window is focused/open.
			{
				createMenuWindow();
			}
			keyDown(keyCode);
		} // Window-class will do not-null-stuff, and Input-class will check if the input is selected -> nothing to do here
		else
		{
			_selectedInput.keyPressed(keyCode);
		}
	}

	/**
	 * Forwards the key up event to the active input control.
	 * 
	 * @param keyCode Key number from Gdx.Input
	 */
	public void keyUp(int keyCode)
	{
		if(_selectedInput != null) _selectedInput.keyUp(keyCode);
	}

	/**
	 * When a key was pressed AND it has been checked weather the ESC key for the ingame menu window has been pressed.
	 * 
	 * @param keyCode Key number from Gdx.Input
	 */
	public abstract void keyDown(int keyCode);

	/**
	 * Fires when user scrolls.
	 * 
	 * @param amount Positive or negative amount of steps since last frame.
	 */
	public abstract void mouseScrolled(int amount);

	/**
	 * Creates the in-game menu window with the yes/no option for exiting the game but provides a settings button as well.
	 */
	public void createMenuWindow()
	{
		if(_inGameMenuWindow == null) _inGameMenuWindow = new InGameMenuWindow();
	}

	/**
	 * Create a settings menu with some options to configure METRO.
	 */
	public void createSettingsWindow()
	{
		if(_settingsWindow == null) _settingsWindow = new SettingsWindow();
	}

	/**
	 * Sets the selected field to a new one. Reset selected field with null as parameter. The Input-component will be informed about the change.
	 * 
	 * @param field The selected input component.
	 */
	public void setSelectedInput(InputField field)
	{
		if(field != null) field.select();
		else if(_selectedInput != null) _selectedInput.disselect();
		_selectedInput = field;
	}

	/**
	 * Gets the current/selected input field. Can be null -> no input selected.
	 * 
	 * @return Selected input field.
	 */
	public InputField getSelectedInput()
	{
		return _selectedInput;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj.getClass().equals(this.getClass());
	}

	/**
	 * When a game screen is active it can be used, otherwise it'll be deleted, ignored, ...
	 * 
	 * @return True when active and usable, false when inactive, closed, ...
	 */
	public abstract boolean isActive();

	/**
	 * Resets the game screen to its default values.
	 */
	public abstract void reset();

	/**
	 * Registers a control in the control manager. If the control already is registered, it'll be deleted.
	 * 
	 * @param control The control to add.
	 */
	public void registerControl(ControlElement control)
	{
		_controlActionManager.registerElement(control);
		_allControls.add(control);
	}

	/**
	 * Unregistered an control element from the control manager to disable user interactions with it.
	 * Use this method from a game screen and NOT the METRO.__unregisterControl(ControlElement) method!
	 * 
	 * @param control The control to remove.
	 */
	public void unregisterControl(ControlElement control)
	{
		_controlActionManager.remove(control);
		_allControls.remove(control);
	}

	/**
	 * Closes the game screen by removing all controls from the game screen.
	 * Normally this method is called via METRO.closeGameScreen(GameScreen) to use the correct control manager.
	 */
	public void close()
	{
		if(METRO.__debug) System.out.println("[GameScreen]\nClosed game screen " + this + "\n");
		_controlActionManager.remove(_allControls);
		_allControls.clear();
	}

	/**
	 * This is the settings window, that provides some options to configure.
	 * These options are: Resolution, fullscreen, OpenGL settings, amount of segments and amount of samples.
	 * 
	 * @author hauke
	 *
	 */
	public class SettingsWindow
	{
		private Window _window;
		private Button _okButton;
		private Checkbox _fullscreenOn,
			_useOpenGL30,
			_useVSync,
			_useHDPI;
		private List _resolutionList,
			_sampleList,
			_segmentList;

		/**
		 * Creates a settings window.
		 */
		public SettingsWindow()
		{
			_window = new Window("METRO settings",
				new Point(METRO.__SCREEN_SIZE.width / 2 - 250, METRO.__SCREEN_SIZE.height / 2 - 225),
				new Point(500, 450),
				METRO.__metroBlue);

			Label label = new Label("To make things easier, you don't need to click on \"save\". Everything will be saved in realtime by just changing settings.",
				new Point(20, 20),
				460,
				_window);
			registerControl(label);

			_okButton = new Button(new Rectangle(200, 420, 100, 20),
				"OK",
				_window);
			registerControl(_okButton);
			addButtonObserver();

			_fullscreenOn = new Checkbox(new Point(20, 70), "Fullscreen", Boolean.parseBoolean(Settings.get("fullscreen.on").toString()), true, _window);
			registerControl(_fullscreenOn);
			_useOpenGL30 = new Checkbox(new Point(20, 90), "Use OpenGL 3.0", Boolean.parseBoolean(Settings.get("use.opengl30").toString()), true, _window);
			registerControl(_useOpenGL30);
			_useVSync = new Checkbox(new Point(20, 110), "Enable VSync", Boolean.parseBoolean(Settings.get("use.vsync").toString()), true, _window);
			registerControl(_useVSync);
			_useHDPI = new Checkbox(new Point(20, 130), "Enable HDPI", Boolean.parseBoolean(Settings.get("use.hdpi").toString()), true, _window);
			registerControl(_useHDPI);
			addCheckboxObserver();

			label = new Label("Screen Resolution:", new Point(20, 180), _window);
			registerControl(label);
			_resolutionList = new List(new Rectangle(20, 200, 190, 150), _window, true);
			_resolutionList.addElement("1920x1200");
			_resolutionList.addElement("1920x1080");
			_resolutionList.addElement("1600x900");
			_resolutionList.addElement("1440x900");
			_resolutionList.addElement("1366x768");
			_resolutionList.addElement("1360x768");
			_resolutionList.addElement("1280x768");
			_resolutionList.addElement("1280x720");
			_resolutionList.addElement("1024x768");
			if(Boolean.parseBoolean(Settings.get("fullscreen.on").toString())) _resolutionList.setState(false);
			int index = _resolutionList.getIndex(Integer.parseInt(Settings.get("screen.width").toString()) + "x" + Integer.parseInt(Settings.get("screen.width").toString())); // get the entry with the current resolution
			_resolutionList.setSelectedEntry(index);
			registerControl(_resolutionList);

			label = new Label("Amount Samples:", new Point(240, 180), _window);
			registerControl(label);
			_sampleList = new List(new Rectangle(240, 200, 90, 150), _window, true);
			_sampleList.addElement("0");
			_sampleList.addElement("2");
			_sampleList.addElement("4");
			_sampleList.addElement("8");
			_sampleList.addElement("16");
			index = _sampleList.getIndex(Settings.get("amount.samples") + ""); // get the entry with the current resolution
			_sampleList.setSelectedEntry(index);
			registerControl(_sampleList);

			label = new Label("Amount Segments:", new Point(360, 180), _window);
			registerControl(label);
			_segmentList = new List(new Rectangle(360, 200, 90, 150), _window, true);
			_segmentList.addElement("6");
			_segmentList.addElement("32");
			_segmentList.addElement("64");
			_segmentList.addElement("128");
			_segmentList.addElement("256");
			index = _segmentList.getIndex(Settings.get("amount.segments") + ""); // get the entry with the current resolution
			_segmentList.setSelectedEntry(index);
			registerControl(_segmentList);
			addListObserver();
		}

		/**
		 * Adds the observer for the "ok" button that closes the window.
		 */
		private void addButtonObserver()
		{
			_okButton.register(new ActionObserver()
			{
				@Override
				public void clickedOnControl(Object arg)
				{
					close();
				}
			});
		}

		/**
		 * Creates the observer with sub-classes for all the check-boxes of this class.
		 */
		private void addCheckboxObserver()
		{
			_fullscreenOn.register(new ActionObserver()
			{
				@Override
				public void checkStateChanged(boolean newState)
				{
					Settings.set("fullscreen.on", _fullscreenOn.isChecked());
					_resolutionList.setState(!(Boolean.parseBoolean(Settings.get("fullscreen.on").toString())));
				}
			});
			_useOpenGL30.register(new ActionObserver()
			{
				@Override
				public void checkStateChanged(boolean newState)
				{
					Settings.set("use.opengl30", _useOpenGL30.isChecked());
				}
			});
			_useOpenGL30.register(new ActionObserver()
			{
				@Override
				public void checkStateChanged(boolean newState)
				{
					Settings.set("use.vsync", _useVSync.isChecked());
				}
			});
			_useOpenGL30.register(new ActionObserver()
			{
				@Override
				public void checkStateChanged(boolean newState)
				{
					Settings.set("use.hdpi", _useHDPI.isChecked());
				}
			});
		}

		/**
		 * Add observer for all lists (resolution, samples and segments)
		 */
		private void addListObserver()
		{
			_resolutionList.register(new ActionObserver()
			{
				@Override
				public void selectionChanged(String entry)
				{
					if(entry != null && !Boolean.parseBoolean(Settings.get("fullscreen.on").toString())) // ... and fullscreen-mode is off
					{
						System.out.println("[ResolutionChanged]\n" +
							"Old res.: " + Settings.get("screen.width") + "x" + Settings.get("screen.height")
							+ " -- " +
							"New res.: " + _resolutionList.getText() + "\n");
						String splitted[] = entry.split("x");
						if(splitted.length == 2)
						{
							Settings.set("screen.width", Integer.parseInt(splitted[0]));
							Settings.set("screen.height", Integer.parseInt(splitted[1]));
						}
					}
				}
			});
			_sampleList.register(new ActionObserver()
			{
				@Override
				public void selectionChanged(String entry)
				{
					if(entry != null && !entry.equals("")) Settings.set("amount.samples", Integer.parseInt(entry));
				}
			});
			_segmentList.register(new ActionObserver()
			{
				@Override
				public void selectionChanged(String entry)
				{
					if(entry != null && !entry.equals("")) Settings.set("amount.segments", Integer.parseInt(entry));
				}
			});
		}

		/**
		 * Just closes the window and sets the _settingsWindow to null.
		 */
		private void close()
		{
			_window.close();
			_settingsWindow = null;
		}
	}

	/**
	 * This is the menu that'll appear after pressing ESC during the game.
	 * It provides exit options and a settings button.
	 * This window will ask the user if he wants to quit and the user can choose between "yes" and "no".
	 * There's also a "settings" button that will open the settings window.
	 * 
	 * @author hauke
	 *
	 */
	public class InGameMenuWindow
	{
		private Window _window;
		private Button _yesButton,
			_noButton,
			_settingsButton;

		/**
		 * Creates a new exit game window with a settings button.
		 */
		public InGameMenuWindow()
		{
			_window = new Window("Really quit?", new Point(METRO.__SCREEN_SIZE.width / 2 - 200,
				METRO.__SCREEN_SIZE.height / 2 - 50), new Point(400, 100), METRO.__metroRed);

			_yesButton = new Button(new Rectangle(10, 70, 120, 20), "Yes", _window);
			registerControl(_yesButton);
			_settingsButton = new Button(new Rectangle(140, 70, 120, 20), "Settings", _window);
			registerControl(_settingsButton);
			_noButton = new Button(new Rectangle(270, 70, 120, 20), "No", _window);
			registerControl(_noButton);
			addButtonObserver();

			Label label = new Label("Really quit METRO? Or go into settings?",
				new Point(200 - (Draw.getStringSize("Really quit METRO? Or go into settings?").width) / 2, 25), _window);
			registerControl(label);
		}

		/**
		 * Creates observer for all buttons ("yes", "no" and "settings").
		 */
		private void addButtonObserver()
		{
			_yesButton.register(new ActionObserver()
			{
				@Override
				public void clickedOnControl(Object arg)
				{
					METRO.__application.exit();
				}
			});
			_settingsButton.register(new ActionObserver()
			{
				@Override
				public void clickedOnControl(Object arg)
				{
					_settingsWindow = new SettingsWindow();
					close();
				}
			});
			_noButton.register(new ActionObserver()
			{
				@Override
				public void clickedOnControl(Object arg)
				{
					close();
				}
			});
		}

		/**
		 * Just closes the window and sets the _inGameMenuWindow to null.
		 */
		private void close()
		{
			_window.close();
			_inGameMenuWindow = null;
		}
	}
}
