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
import metro.WindowControls.Button;
import metro.WindowControls.Label;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Every Menu or Game Sreen has to implement this interface for start() and update(). This will make the creation process more easy.
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
	 * @param g SpriteBatch to draw on.
	 */
	public abstract void updateGameScreen(SpriteBatch g);

	/**
	 * When mouse has clicked
	 * @param screenX The x-position on the screen
	 * @param screenY The y-position on the screen
	 * @param mouseButton The number of the button like Buttons.LEFT
	 */
	public abstract void mouseClicked(int screenX, int screenY, int mouseButton);
	
	/**
	 * When mouse has been released.
	 * @param mouseButton The number of the button like Buttons.LEFT
	 */
	public abstract void mouseReleased(int mouseButton);
	
	/**
	 * When a key was pressed.
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
	 * @param keyCode Key number from Gdx.Input
	 */
	public abstract void keyDown(int keyCode);
	
	/**
	 * Fires when user scrolls.
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
	 * @return Selected input field.
	 */
	public Input getSelectedInput()
	{
		return _selectedInput;
	}

	/**
	 * This is the settings window, that provides some options to configure.
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
			
			new Label("To make things easier, you don't need to click on \"save\". Everything will be saved in realtime by just changing settings.", 
				new Point(20, 20), 
				460, 
				_window);
			
			_okButton = new Button(new Rectangle(200, 420, 100, 20), 
				"OK", 
				_window);
			
			_fullscreenOn = new Checkbox(new Point(20, 70), "Fullscreen", Settings.new_fullscreen(), true, _window);
			_useOpenGL30 = new Checkbox(new Point(20, 90), "Use OpenGL 3.0", Settings.new_useOpenGL30(), true, _window);
			_useVSync = new Checkbox(new Point(20, 110), "Enable VSync", Settings.new_useVSync(), true, _window);
			_useHDPI = new Checkbox(new Point(20, 130), "Enable HDPI", Settings.new_useHDPI(), true, _window);

			new Label("Screen Resolution:", new Point(20, 180), _window);
			_resolutionList = new List(new Rectangle(20, 200, 190, 150), _window, true);
			_resolutionList.addElement("1920x1200");
			_resolutionList.addElement("1920x1080");
			_resolutionList.addElement("1600x900");
			_resolutionList.addElement("1440x900");
			_resolutionList.addElement("1366x768");
			_resolutionList.addElement("1360x768x");
			_resolutionList.addElement("1280x768");
			_resolutionList.addElement("1280x720");
			if(!Settings.new_fullscreen()) _resolutionList.setState(false);
			int index = _resolutionList.getIndex(Settings.new_screenWidth() + "x" + Settings.new_screenHeight()); // get the entry with the current resolution
			_resolutionList.setSelectedEntry(index);

			new Label("Amount Samples:", new Point(240, 180), _window);
			_sampleList = new List(new Rectangle(240, 200, 90, 150), _window, true);
			_sampleList.addElement("0");
			_sampleList.addElement("2");
			_sampleList.addElement("4");
			_sampleList.addElement("8");
			_sampleList.addElement("16");
			index = _sampleList.getIndex(Settings.amountOfSamples() + ""); // get the entry with the current resolution
			_sampleList.setSelectedEntry(index);
			
			new Label("Amount Segments:", new Point(360, 180), _window);
			_segmentList = new List(new Rectangle(360, 200, 90, 150), _window, true);
			_segmentList.addElement("6");
			_segmentList.addElement("32");
			_segmentList.addElement("64");
			_segmentList.addElement("128");
			_segmentList.addElement("256");
			index = _segmentList.getIndex(Settings.amountOfSegments() + ""); // get the entry with the current resolution
			_segmentList.setSelectedEntry(index);
			
			Settings.save();
		}
		
		/**
		 * Updates everything and handles clicks.
		 */
		public void update()
		{
			if(_window.isClosed()) close();
			
			if(_okButton.isPressed())
			{
				close();
			}
			else if(_fullscreenOn.hasChanged()
				|| _useOpenGL30.hasChanged()
				|| _useVSync.hasChanged()
				|| _useHDPI.hasChanged())
			{
				Settings.setFullscreen(_fullscreenOn.isChecked());
				Settings.setOpenGL30Usage(_useOpenGL30.isChecked());
				Settings.setVSyncUsage(_useVSync.isChecked());
				Settings.setHDPIUsage(_useHDPI.isChecked());

				_resolutionList.setState(!Settings.new_fullscreen());
			}
			else if(!_resolutionList.getText(_resolutionList.getSelected()).equals(Settings.new_screenWidth() + "x" + Settings.new_screenHeight())) // if selection has changed
			{
				String entry = _resolutionList.getText(_resolutionList.getSelected());
				String splitted[] = entry.split("x");
				if(splitted.length == 2)
				{
					Settings.setScreenWidth(Integer.parseInt(splitted[0]));
					Settings.setScreenHeight(Integer.parseInt(splitted[1]));
				}
			}
			else if(!_sampleList.getText(_sampleList.getSelected()).equals(Settings.new_amountOfSamples() + "")) // if selection has changed
			{
				String entry = _sampleList.getText(_sampleList.getSelected());
				if(!entry.equals("")) Settings.setAmountSamples(Integer.parseInt(entry));
			}
			else if(!_segmentList.getText(_segmentList.getSelected()).equals(Settings.new_amountOfSegments() + "")) // if selection has changed
			{
				String entry = _segmentList.getText(_segmentList.getSelected());
				if(!entry.equals("")) Settings.setAmountSegments(Integer.parseInt(entry));
			}
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
			_window = new Window("Really quit?",new Point(METRO.__SCREEN_SIZE.width / 2 - 200, 
					METRO.__SCREEN_SIZE.height / 2 - 50), new Point(400, 100), METRO.__metroRed);
			_yesButton = new metro.WindowControls.Button(new Rectangle(10, 70, 120, 20), "Yes", _window);
			_settingsButton = new metro.WindowControls.Button(new Rectangle(140, 70, 120, 20), "Settings", _window);
			_noButton = new metro.WindowControls.Button(new Rectangle(270, 70, 120, 20), "No", _window);
			new metro.WindowControls.Label("Really quit METRO? Or go into settings?", new Point(200 - (Draw.getStringSize("Really quit METRO? Or go into settings?").width)/2, 25), _window);
		}
		
		/**
		 * Updates everything and handles clicks.
		 */
		public void update()
		{
			if(_window.isClosed()) close();
			
			if(_yesButton.isPressed())
			{
				METRO.__application.exit();
			}
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
