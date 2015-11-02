package metro;

/**
 * METRO - Master of established transport railway operators
 * 
 * Code convention:
 * 
 * final: _WITH_BIG_NAME
 * normal instance variables: _variable_someName
 * METRO instance variables: __variable
 * METRO finals: __FINAL
 * local stuff: chooseAnyName
 * 
 * final and local with small beginning char and capitol for every following word ( e.g.: int myVariableIsCool = 42; ).
 * 
 * Method names has to be clear ( not a(){...} but createCharacter(){...} )
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.GdxRuntimeException;

import metro.GameScreen.GameScreen;
import metro.GameScreen.MainMenu;
import metro.Graphics.Draw;
import metro.WindowControls.Button;
import metro.WindowControls.ControlActionManager;
import metro.WindowControls.ControlElement;
import metro.WindowControls.InputField;
import metro.WindowControls.Window;

/**
 * @author Hauke
 * @version 0.1.3_indev
 */
public class METRO extends Frame implements ApplicationListener, InputProcessor
{
	private static final long serialVersionUID = 1L;

	public static Dimension __SCREEN_SIZE;
	public static final String __TITLE = "METRO",
		__VERSION = "0.1.3_indev";

	private static OSType _detected_OS = OSType.UNKNOWN;
	private static ControlActionManager _controlActionManager;
	private static GameScreen _currentGameScreen;

	public static BitmapFont __stdFont;
	public static TextureRegion __mainMenu_Buttons,
		__mainMenu_TitleImage,
		__iconSet,
		__mouseCursorImage;
	public static Button __viewPortButton_City,
		__viewPortButton_Train;
	public static Color __metroRed,
		__metroBlue;
	public static int __baseNetSpacing = 25; // amount of pixel between lines of the base net
	public static ArrayList<Window> __windowList = new ArrayList<Window>();
	public static Point __mousePosition,
		__originalMousePosition;
	public static OrthographicCamera __camera;
	public static SpriteBatch __spriteBatch;
	public static LwjglApplication __application;
	public static GameState __gameState;

	private Point _oldMousePosition = new Point(0, 0);
	private LwjglApplicationConfiguration _config;

	public static void main(String[] args)
	{
		new METRO();
	}

	private METRO()
	{
		setSettings();
	}

	@Override
	public void create()
	{
		_controlActionManager = new ControlActionManager();
		GameScreen.setActionManager(_controlActionManager);

		// __CURRENT_OS
		String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
		if(os.contains("win"))
		{
			_detected_OS = OSType.WIN;
		}
		else if(os.contains("nux"))
		{
			_detected_OS = OSType.LINUX;
		}
		else if(os.contains("mac") || os.contains("darwin"))
		{
			_detected_OS = OSType.MAC;
		}

		__spriteBatch = new SpriteBatch();

		__camera = new OrthographicCamera();
		__camera.setToOrtho(true, __SCREEN_SIZE.width / 2, __SCREEN_SIZE.height / 2);
		__camera.update();

		// Set up the input event handling
		Gdx.input.setInputProcessor(this);

		// load new cursor image
		try
		{
			Pixmap pixmap = new Pixmap(Gdx.files.internal("textures/Cursor.png")); // has to be a width of 2^x (2, 4, 8, 16, 32, ...)
			Gdx.input.setCursorImage(pixmap, 16, 13); // sets cursor to correct position
			pixmap.dispose();
			if(_detected_OS == OSType.WIN) // setCursorImage doesn't work on Windows :(
			{
				__mouseCursorImage = new TextureRegion(new Texture(Gdx.files.internal("textures/Cursor.png")));
			}
		}
		catch(GdxRuntimeException ex)
		{
			System.out.println(ex.getMessage()); // TODO: Put into popup-message-box
		}

		__mainMenu_Buttons = new TextureRegion(new Texture(Gdx.files.internal("textures/MainMenu_Buttons.png")));
		__mainMenu_Buttons.flip(false, true);

		__mainMenu_TitleImage = new TextureRegion(new Texture(Gdx.files.internal("textures/MainMenu_TitleImage.png")));
		__mainMenu_TitleImage.flip(false, true);

		__iconSet = new TextureRegion(new Texture(Gdx.files.internal("textures/IconSet.png")));
		__iconSet.flip(false, true);

		// Load fonts
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GatsbyFLF-Bold.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.magFilter = TextureFilter.Linear;
		parameter.size = 21;
		parameter.flip = true;
		__stdFont = generator.generateFont(parameter); // font size 20 pixels
		generator.dispose();

		// Create MainMenu Screen
		_currentGameScreen = new MainMenu();

		// Create special colors
		__metroBlue = new Color(100, 180, 255);
		__metroRed = new Color(255, 100, 100);

		__gameState = GameState.getInstance();
	}

	@Override
	public void resize(int width, int height)
	{
		__camera.setToOrtho(true, width, height);
		__spriteBatch.setProjectionMatrix(__camera.combined);
	}

	@Override
	public void render()
	{
		calculateMousePosition();

		__spriteBatch.begin();

		renderInit();
		renderGameScreen();
		renderWindows();
		renderFPSDisplay();
		renderCursor();

		__spriteBatch.end();
	}

	/**
	 * Calculates the mouse position concerning fullscreen, window borders, windows, etc.
	 * When the mouse is in a window, the position does not change (to prevent wrong clicks on other controls/tools).
	 */
	private void calculateMousePosition()
	{
		__mousePosition = MouseInfo.getPointerInfo().getLocation();

		if(!Boolean.parseBoolean(Settings.get("fullscreen.on").toString()))
		{
			__mousePosition.translate(-_config.x - 3, -_config.y - 24);
			// __mousePosition.translate(-3, 1); // correction because the normal cursor is NOT in the center
		}
		__originalMousePosition = (Point)__mousePosition.clone();

		boolean mouseInWindow = false;
		for(Window win : __windowList)
		{
			mouseInWindow |= win.isMouseOnWindowArea(__mousePosition.x, __mousePosition.y);
		}
		if(mouseInWindow) __mousePosition = _oldMousePosition;
		else _oldMousePosition = __mousePosition;
	}

	/**
	 * Initializes the rendering and clears the screen.
	 */
	private void renderInit()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
	}

	/**
	 * Updates the game screen and draws the control drawer.
	 */
	private void renderGameScreen()
	{
		_currentGameScreen.update(__spriteBatch);
	}

	/**
	 * Draws all windows that are in the __windowList.
	 */
	private void renderWindows()
	{
		for(Window win : __windowList)
		{
			win.draw(__spriteBatch);
		}
	}

	/**
	 * Draws the FPS-display with the frames per second given by Gdx.
	 */
	private void renderFPSDisplay()
	{
		Draw.setColor(__metroBlue);
		Draw.String("FPS: " + Gdx.graphics.getFramesPerSecond(), __SCREEN_SIZE.width - (Draw.getStringSize("FPS: " + Gdx.graphics.getFramesPerSecond()).width + 30), 25);
	}

	/**
	 * Draws the cursor based on the operating system (if windows: cursor is drawn manually).
	 */
	private void renderCursor()
	{
		if(_detected_OS == OSType.WIN) // setCursorImage doesn't work on Windows :(
		{
			if(__mouseCursorImage != null) Draw.Image(__mouseCursorImage, __originalMousePosition.x - 16, __originalMousePosition.y - 16);
		}
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}

	@Override
	public boolean keyDown(int keyCode)
	{
		_currentGameScreen.keyPressed(keyCode);
		for(Window win : __windowList)
		{
			win.keyPressed(keyCode);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keyCode)
	{
		_currentGameScreen.keyUp(keyCode);
		for(Window win : __windowList)
		{
			win.keyUp(keyCode);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	/**
	 * Is executed when the user clicks with the mouse.
	 * This method will forward the click event to the right control, window or game screen.
	 * The exact hierarchy is shown below.
	 * 
	 * The click-hierarchy:
	 * 1.) Check if the mouse is in a window
	 *     If yes: Forward click to this window and remind it
	 *     If no : Go to next window.
	 * 2.) Forward click to ControlActionManager with the clicked window as parameter
	 * 3.) Check if the clicked window is null (=there has no window been clicked)
	 *     If yes: Close the window if needed
	 *     If no : Forward click to game screen
	 */
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		Window clickedWindow = null;

		/*
		 * Go from the last to first window when no window has been clicked yet.
		 * This will consider the "depth-position" of the window (windows are behind/before others).
		 * Thus only the frontmost window will receive a click, all others don't to prevent weird behaviour.
		 */
		for(int i = __windowList.size() - 1; i >= 0 && clickedWindow == null; i--)
		{
			if(__windowList.get(i).isMouseOnWindowArea(screenX, screenY)) // if mouse is just on the window area
			{
				__windowList.get(i).mousePressed(screenX, screenY, button);
				clickedWindow = __windowList.get(i);
			}
		}
		
		boolean controlHasBeenClicked = !_controlActionManager.mouseClicked(screenX, screenY, button, clickedWindow);
		
		/* 
		 * Decide: Close window (if needed) when clickedWindow is null 
		 * XOR 
		 * forward click to game screen when no control has been clicked AND the clicked window is null
		 *     
		 */
		if(clickedWindow != null)
		{
			// close the clicked window after handling the click
			boolean closed = clickedWindow.closeIfNeeded(screenX, screenY, button) || clickedWindow.isClosed();
			if(closed) __windowList.remove(clickedWindow);
		}
		else if(controlHasBeenClicked)
		{
			// forward click to game screen
			_currentGameScreen.mouseClicked(screenX, screenY, button);
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		_currentGameScreen.mouseReleased(button);

		for(Window win : __windowList)
		{
			win.mouseReleased();
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		_currentGameScreen.mouseScrolled(amount);

		boolean mouseOnWindow = false;
		/*
		 * Go from the last to first window when no window has been clicked yet.
		 * This will consider the "depth-position" of the window (windows are behind/before others).
		 * Thus only the frontmost window will receive a scroll event, all others don't to prevent weird behaviour.
		 */
		for(int i = __windowList.size() - 1; i >= 0 && !mouseOnWindow; i--)
		{
			if(__windowList.get(i).isMouseOnWindow(__mousePosition.x, __mousePosition.y)) // if mouse is on window area but not on a control
			{
				__windowList.get(i).mouseScrolled(amount);
				mouseOnWindow = true;
			}
		}
		if(!mouseOnWindow) _controlActionManager.mouseScroll(amount);

		return false;
	}

	/**
	 * Removes a control from the control action manager to disable user interactions with it.
	 * Notice: Don't use this from a game screen there's the method "unregisterControl(ControlElement control)" in the GameScreen class!
	 * 
	 * @param control The control to remove.
	 */
	public static void __unregisterControl(ControlElement control)
	{
		_controlActionManager.remove(control);
	}

	/**
	 * Closes the current game screen and makes the given screen to the new one.
	 * 
	 * @param newScreen The new game screen.
	 */
	public static void __changeGameScreen(GameScreen newScreen)
	{
		_currentGameScreen.close();
		_currentGameScreen = newScreen;
	}

	/**
	 * Sets the selected input of the current game screen. This enables to change the focus of the input fields.
	 * 
	 * @param field The input field that should get the focus.
	 */
	public static void __setSelectedInput(InputField field)
	{
		_currentGameScreen.setSelectedInput(field);
	}

	/**
	 * Reads the settings from the settings.cfg and sets them to the variables of METRO.
	 */
	private void setSettings()
	{
		Settings.read();

		try
		{
			__SCREEN_SIZE = new Dimension(Integer.parseInt(Settings.get("screen.width").toString()),
				Integer.parseInt(Settings.get("screen.height").toString()));

			_config = new LwjglApplicationConfiguration();
			_config.title = __TITLE + "  " + __VERSION;
			_config.width = Integer.parseInt(Settings.get("screen.width").toString());
			_config.height = Integer.parseInt(Settings.get("screen.height").toString());
			_config.useGL30 = Boolean.parseBoolean(Settings.get("use.opengl30").toString());
			_config.resizable = false;
			_config.fullscreen = Boolean.parseBoolean(Settings.get("fullscreen.on").toString());
			// config.foregroundFPS = -1; // max frames
			_config.samples = Integer.parseInt(Settings.get("amount.samples").toString());
			_config.vSyncEnabled = Boolean.parseBoolean(Settings.get("use.vsync").toString());
			_config.useHDPI = Boolean.parseBoolean(Settings.get("use.hdpi").toString());

			__application = new LwjglApplication(this, _config);
		}
		catch(Exception ex) // if something gone wrong (e.g. "screen.width=19a20"
		{
			File file = new File("./settings.cfg");
			if(file.exists()) // try to use default values by deleting config file
			{
				// Create date time for backup name
				java.util.Date date = new java.util.Date();
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy.h:mm:ss");
				sdf.setTimeZone(java.util.TimeZone.getTimeZone("CET"));
				String formattedDate = sdf.format(date);

				// try to rename settings.cfg
				if(!file.renameTo(new File("./settings.backup." + formattedDate + ".cfg")))
				{
					System.out.println("No backup of settings.cfg has been created."); // TODO: Put into popup-message-box
				}

				// no
				setSettings();
			}
			else
			{
				System.err.println("Could NOT create configuration."
					+ "Using default values by deleting the ./settings.cfg is not working."
					+ "\nHere some technical information :" + ex.getMessage() + "\n");
				for(StackTraceElement str : ex.getStackTrace())
				{
					System.err.println(str.toString());
				}
			}
		}
	}

	private enum OSType
	{
		WIN,
		LINUX,
		MAC,
		UNKNOWN;
	}
}