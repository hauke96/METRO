package metro.GameScreen;

import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.Settings;
import metro.Graphics.Draw;
import metro.WindowControls.Checkbox;
import metro.WindowControls.Input;
import metro.WindowControls.List;
import metro.WindowControls.Window;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;
import metro.WindowControls.Label;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Every Menu or Game Sreen has to implement this interface for start() and update(). This will make the creation process more easy.
 * 
 * @author Hauke
 * 
 */

public abstract class GameScreen
{
	public SettingsWindow _settingsWindow; // makes it possible to create a settings-window from every(!) game screen.
	public InGameMenuWindow _inGameMenuWindow; // makes it possible to create a ingame-menu-window from every(!) game screen.
	private static Input _selectedInput = null;

	/**
	 * Will be executed as fast as possible ;)
	 * This method will actually not directly update the game screen, but fires the {@code updateGameScreen(g)} method to do this.
	 * 
	 * @param g SpriteBatch to use.
	 */
	public void update(SpriteBatch g)
	{
		if(_inGameMenuWindow != null) _inGameMenuWindow.update();
		if(_settingsWindow != null) _settingsWindow.update();

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
		} // Window-class will do not-null-stuff, and Input-class will check if the input is selected -> nothing to do here
		keyDown(keyCode);
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
	public void setSelectedInput(Input field)
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
	public Input getSelectedInput()
	{
		return _selectedInput;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj.getClass().equals(this.getClass());
	}

	/**
	 * This is the settings window, that provides some options to configure.
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
			addWindowListener();

			new Label("To make things easier, you don't need to click on \"save\". Everything will be saved in realtime by just changing settings.",
				new Point(20, 20),
				460,
				_window);

			_okButton = new Button(new Rectangle(200, 420, 100, 20),
				"OK",
				_window);
			addButtonObserver();

			_fullscreenOn = new Checkbox(new Point(20, 70), "Fullscreen", Boolean.parseBoolean(Settings.getNew("fullscreen.on").toString()), true, _window);
			_useOpenGL30 = new Checkbox(new Point(20, 90), "Use OpenGL 3.0", Boolean.parseBoolean(Settings.getNew("use.opengl30").toString()), true, _window);
			_useVSync = new Checkbox(new Point(20, 110), "Enable VSync", Boolean.parseBoolean(Settings.getNew("use.vsync").toString()), true, _window);
			_useHDPI = new Checkbox(new Point(20, 130), "Enable HDPI", Boolean.parseBoolean(Settings.getNew("use.hdpi").toString()), true, _window);
			addCheckboxObserver();

			new Label("Screen Resolution:", new Point(20, 180), _window);
			_resolutionList = new List(new Rectangle(20, 200, 190, 150), _window, true);
			_resolutionList.addElement("1920x1200");
			_resolutionList.addElement("1920x1080");
			_resolutionList.addElement("1600x900");
			_resolutionList.addElement("1440x900");
			_resolutionList.addElement("1366x768");
			_resolutionList.addElement("1360x768");
			_resolutionList.addElement("1280x768");
			_resolutionList.addElement("1280x720");
			if(Boolean.parseBoolean(Settings.getNew("fullscreen.on").toString())) _resolutionList.setState(false);
			int index = _resolutionList.getIndex(Integer.parseInt(Settings.getNew("screen.width").toString()) + "x" + Integer.parseInt(Settings.getNew("screen.width").toString())); // get the entry with the current resolution
			_resolutionList.setSelectedEntry(index);

			new Label("Amount Samples:", new Point(240, 180), _window);
			_sampleList = new List(new Rectangle(240, 200, 90, 150), _window, true);
			_sampleList.addElement("0");
			_sampleList.addElement("2");
			_sampleList.addElement("4");
			_sampleList.addElement("8");
			_sampleList.addElement("16");
			index = _sampleList.getIndex(Settings.getNew("amount.samples") + ""); // get the entry with the current resolution
			_sampleList.setSelectedEntry(index);

			new Label("Amount Segments:", new Point(360, 180), _window);
			_segmentList = new List(new Rectangle(360, 200, 90, 150), _window, true);
			_segmentList.addElement("6");
			_segmentList.addElement("32");
			_segmentList.addElement("64");
			_segmentList.addElement("128");
			_segmentList.addElement("256");
			index = _segmentList.getIndex(Settings.getNew("amount.segments") + ""); // get the entry with the current resolution
			_segmentList.setSelectedEntry(index);
			addListObserver();

			Settings.save();
		}

		private void addWindowListener()
		{
			_window.register(new ActionObserver()
			{
				@Override
				public void closed()
				{
					close();
				}
			});
		}

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
					_resolutionList.setState(!(Boolean.parseBoolean(Settings.getNew("fullscreen.on").toString())));
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

		private void addListObserver()
		{
			_resolutionList.register(new ActionObserver()
			{
				@Override
				public void selectionChanged(String entry)
				{
					if(!Boolean.parseBoolean(Settings.getNew("fullscreen.on").toString())) // ... and fullscreen-mode is off
					{
						System.out
							.println(Settings.getNew("screen.width") + "x" + Settings.getNew("screen.height") + " -- " + _resolutionList.getText(_resolutionList.getSelected()));
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
					if(!entry.equals("")) Settings.set("amount.samples", Integer.parseInt(entry));
				}
			});
			_segmentList.register(new ActionObserver()
			{
				@Override
				public void selectionChanged(String entry)
				{
					if(!entry.equals("")) Settings.set("amount.segments", Integer.parseInt(entry));
				}
			});
		}

		/**
		 * Updates everything and handles clicks.
		 */
		public void update()
		{
			Settings.save(); // there will be no unnecessary writing, cause of boolean-variable
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
	 * This is the menu that'll appear when ESC is pressed during the game. It provides exit options and a settings button.
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
			_yesButton = new metro.WindowControls.Button(new Rectangle(10, 70, 120, 20), "Yes", _window);
			_settingsButton = new metro.WindowControls.Button(new Rectangle(140, 70, 120, 20), "Settings", _window);
			_noButton = new metro.WindowControls.Button(new Rectangle(270, 70, 120, 20), "No", _window);
			new metro.WindowControls.Label("Really quit METRO? Or go into settings?",
				new Point(200 - (Draw.getStringSize("Really quit METRO? Or go into settings?").width) / 2, 25), _window);
		}

		/**
		 * Updates everything and handles clicks.
		 */
		public void update()
		{
			if(_window.isClosed()) close();

			// TODO: ActionObserver implementation
			if(_yesButton.isPressed())
			{
				METRO.__application.exit();
			}
			// TODO: ActionObserver implementation
			else if(_settingsButton.isPressed())
			{
				if(_settingsWindow == null)
				{
					_settingsWindow = new SettingsWindow();
					close();
				}
				else
				{
					METRO.__windowList.remove(_settingsWindow._window);
					METRO.__windowList.add(_settingsWindow._window);
				}
			}
			// TODO: ActionObserver implementation
			else if(_noButton.isPressed())
			{
				close();
			}
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
