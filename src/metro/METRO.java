package metro;

/**
 * METRO - Master of established transport railway operators
 * 
 * 
 * 
 * Code convention:
 * 
 * final: _WITH_BIG_NAME
 * normal instance variables: _someCoolVariable
 * static instance variables: __evenCoolerVariable
 * final instance variables: __FINAL_VARIABLE
 * local stuff: chooseAnyName
 * 
 * Variable are written in camel-case, meaning they begin with lower case and having upper case letter for every sub-word
 * ( e.g.: int myVariableIsCool = 42; ).
 * final variables (due to their upper-case-only convention) are separated into words with an underscore.
 * 
 * Method names has to be clear ( not a(){...} but createCharacter(){...} )
 * 
 * 
 * 
 * Debug convention:
 * 
 * When you want to print something for debug on the console and you want to commit that, please be shure that you have the following output syntax:
 * 
 * [UniqueNameTag]
 * After 4 characters intention comes the message
 * with other correct intended information.
 * 
 * [AnotherTag]
 * Between to tags should be a blank line.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.Locale;

import org.lwjgl.opengl.Display;

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

import metro.Common.Game.GameState;
import metro.Common.Game.Settings;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
import metro.Common.Technical.Logger;
import metro.GameUI.MainMenu.MainMenu;
import metro.GameUI.Screen.CurrentGameScreenManager;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.Controls.InputField;
import metro.UI.Renderer.BasicContainerRenderer;

/**
 * @author Hauke
 * @version 0.1.4_indev
 */
public class METRO implements ApplicationListener, InputProcessor
{
	private Point _oldMousePosition;
	private LwjglApplicationConfiguration _config;

	private static OSType __detectedOS;
	private static BasicContainerRenderer __containerRenderer;
	private static CurrentGameScreenManager __currentGameScreenManager;
	private static ActionObserver __windowObserver;
	private static SpriteBatch __gameWindowSpriteBatch;
	private static int __xOffset,
		__yOffset,
		__titleBarBorderLineWidth;
	private static LwjglApplication __application;

	public static Dimension __SCREEN_SIZE;
	public static final String __VERSION = "0.1.4_indev";
	public static boolean __dragMode,
		__debug;
	public static Point __dragModeLastMousePosition;
	public static TextureRegion __iconSet,
		__mouseCursorImage;
	public static Color __metroRed,
		__metroBlue;
	public static Point __mousePosition,
		__originalMousePosition;
	public static OrthographicCamera __camera;
	public static SpriteBatch __spriteBatch;
	public static GameState __gameState;
	public static int __titleBarHeight;

	/**
	 * Creates a new METRO Game.
	 * 
	 * @param args The console arguments that have no function here.
	 */
	public static void main(String[] args)
	{
		new METRO();
	}

	private METRO()
	{
		setSettings();
	}

	/**
	 * Reads the settings from the settings.cfg and sets them to the variables of METRO.
	 */
	private void setSettings()
	{
		Settings settings = Settings.getInstance();

		settings.read();

		try
		{
			_config = new LwjglApplicationConfiguration();
			_config.title = "METRO  " + __VERSION;
			_config.width = Integer.parseInt(settings.getOld("screen.width").toString());
			_config.height = Integer.parseInt(settings.getOld("screen.height").toString());
			_config.useGL30 = Boolean.parseBoolean(settings.getOld("use.opengl30").toString());
			_config.resizable = false;
			_config.fullscreen = Boolean.parseBoolean(settings.getOld("fullscreen.on").toString());
			// _config.foregroundFPS = -1; // max frames
			_config.samples = Integer.parseInt(settings.getOld("amount.samples").toString());
			_config.vSyncEnabled = false;// Boolean.parseBoolean(Settings.get("use.vsync").toString());
			_config.useHDPI = Boolean.parseBoolean(settings.getOld("use.hdpi").toString());

			__application = new LwjglApplication(this, _config);

			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");

			__SCREEN_SIZE = new Dimension(Integer.parseInt(settings.getOld("screen.width").toString()),
				Integer.parseInt(settings.getOld("screen.height").toString()));
			__titleBarHeight = _config.fullscreen ? 0 : 22;
		}
		catch(Exception ex) // if something has gone wrong (e.g. "screen.width=19a20")
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
					Logger.__debug("No backup of settings.cfg has been created.\n");
				}

				System.err
					.println(
						"Something went wrong by reading the settings.\nThey have been renamed to \"settings.backup.{date}\".\nHere some more information:\n" + ex.getMessage());
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

	@Override
	public void create()
	{
		__containerRenderer = new BasicContainerRenderer();

		__debug = true;

		_oldMousePosition = new Point(0, 0);
		__titleBarBorderLineWidth = 1;
		__dragMode = false;

		detectOS();

		initGdx();

		loadVisuals();

		__currentGameScreenManager = new CurrentGameScreenManager();
		__currentGameScreenManager.switchToGameScreen(new MainMenu());

		__gameState = GameState.getInstance();
	}

	/**
	 * Detects the current operating system and saves it.
	 */
	private void detectOS()
	{
		String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
		if(os.contains("win"))
		{
			__detectedOS = OSType.WIN;
		}
		else if(os.contains("nux"))
		{
			__detectedOS = OSType.LINUX;
		}
		else if(os.contains("mac") || os.contains("darwin"))
		{
			__detectedOS = OSType.MAC;
		}
		else
		{
			__detectedOS = OSType.UNKNOWN;
		}
	}

	/**
	 * Initializes the gdx system by creating a sprite batch, a camera and an input processor.
	 */
	private void initGdx()
	{
		__spriteBatch = new SpriteBatch();
		__gameWindowSpriteBatch = new SpriteBatch();
		if(_config.fullscreen)
		{
			__xOffset = 0;
			__yOffset = 0;
		}
		else
		{
			__xOffset = 1;
			__yOffset = __titleBarHeight;
		}

		__camera = new OrthographicCamera();
		__camera.setToOrtho(true, __SCREEN_SIZE.width / 2, __SCREEN_SIZE.height / 2);
		__camera.update();

		// Set up the input event handling
		Gdx.input.setInputProcessor(this);
	}

	/**
	 * Loads everything that has to do with visual things like textures and also the fonts.
	 * This method loads all textures including main menu button, title image, icon set and cursor image.
	 * It also creates the two default colors {@code __metroBlue} and {@code __metroRed}.
	 */
	private void loadVisuals()
	{
		__iconSet = new TextureRegion(new Texture(Gdx.files.internal("textures/IconSet.png")));
		__iconSet.flip(false, true);

		loadCursorImage();
		Draw.setFont(loadFonts());

		// Create special colors
		__metroBlue = new Color(100, 180, 255);
		__metroRed = new Color(255, 100, 100);
	}

	/**
	 * Loads the cursor image based on the operating system (setting default cursor doesn't work in windows -> loading the cursor as texture).
	 */
	private void loadCursorImage()
	{
		try
		{
			Pixmap pixmap = new Pixmap(Gdx.files.internal("textures/Cursor.png")); // has to be a width of 2^x (2, 4, 8, 16, 32, ...)
			Gdx.input.setCursorImage(pixmap, 15, 12); // sets cursor to correct position
			pixmap.dispose();
			if(__detectedOS == OSType.WIN) // setCursorImage doesn't work on Windows :(
			{
				__mouseCursorImage = new TextureRegion(new Texture(Gdx.files.internal("textures/Cursor.png")));
			}
		}
		catch(GdxRuntimeException ex)
		{
			Logger.__debug("" + ex.getMessage());
		}
	}

	/**
	 * Load the font {@code GatsbyFLF-Bold} in size 21.
	 */
	private BitmapFont loadFonts()
	{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GatsbyFLF-Bold.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.magFilter = TextureFilter.Linear;
		parameter.size = 21;
		parameter.flip = true;
		BitmapFont font = generator.generateFont(parameter); // font size 20 pixels
		generator.dispose();

		return font;
	}

	@Override
	public void resize(int width, int height)
	{
		__camera.setToOrtho(true, width, height);
		__spriteBatch.setProjectionMatrix(__camera.combined);
		__gameWindowSpriteBatch.setProjectionMatrix(__camera.combined);
	}

	@Override
	public void render()
	{
		calculateMousePosition();

		Fill.setOffset(__xOffset, __yOffset);
		Draw.setOffset(__xOffset, __yOffset);

		__spriteBatch.begin();

		renderInit();
		__currentGameScreenManager.renderCurrentGameScreen(__spriteBatch);
		__containerRenderer.notifyDraw();
		renderFPSDisplay();
		renderCursor();

		__spriteBatch.end();

		Fill.setOffset(0, 0);
		Draw.setOffset(0, 0);

		__gameWindowSpriteBatch.begin();

		renderWindowTitleBar();

		__gameWindowSpriteBatch.end();

	}

	/**
	 * Draws the title bar including the close cross and the blue border around the window.
	 * This method uses the _titleBarHeight and _titleBarBorderLineWidth variables.
	 */
	private void renderWindowTitleBar()
	{
		if(_config.fullscreen) return;
		// white title bar background
		Fill.setColor(Color.white);
		Fill.Rect(new Rectangle(0, 0, __SCREEN_SIZE.width, __titleBarHeight), __gameWindowSpriteBatch);

		// blue title bar background
		Fill.setColor(__metroBlue);
		Fill.Rect(
			new Rectangle(__titleBarBorderLineWidth * 2,
				__titleBarBorderLineWidth * 2,
				__SCREEN_SIZE.width - 4 * __titleBarBorderLineWidth - __titleBarHeight + 1,
				__titleBarHeight - 4 * __titleBarBorderLineWidth),
			__gameWindowSpriteBatch);

		// title of title bar
		Draw.setColor(Color.white);
		String title = "METRO - Master of Established Transport Railway Operators - v" + __VERSION;
		Dimension size = Draw.getStringSize(title);
		Draw.String(title, (__SCREEN_SIZE.width - __titleBarHeight) / 2 - size.width / 2, __titleBarBorderLineWidth + 4, __gameWindowSpriteBatch);

		// border aroung window
		Draw.setColor(__metroBlue);
		Draw.Rect(0, 0, __SCREEN_SIZE.width, __SCREEN_SIZE.height, __titleBarBorderLineWidth, __gameWindowSpriteBatch);

		// border around title bar
		Draw.Rect(0, 0, __SCREEN_SIZE.width, __titleBarHeight, __titleBarBorderLineWidth, __gameWindowSpriteBatch);

		// vertical line of close-cross
		Draw.Line(__SCREEN_SIZE.width - __titleBarHeight, 0, __SCREEN_SIZE.width - __titleBarHeight, __titleBarHeight, __titleBarBorderLineWidth, __gameWindowSpriteBatch);

		// close-cross
		Draw.setColor(__metroRed);
		Draw.Line(__SCREEN_SIZE.width - (__titleBarHeight - 6), 6, __SCREEN_SIZE.width - 6, __titleBarHeight - 6, 2, __gameWindowSpriteBatch);
		Draw.Line(__SCREEN_SIZE.width - (__titleBarHeight - 6), __titleBarHeight - 6, __SCREEN_SIZE.width - 6, 6, 2, __gameWindowSpriteBatch);
	}

	/**
	 * Calculates the mouse position concerning fullscreen, window borders, windows, etc.
	 * When the mouse is in a window, the position does not change (to prevent wrong clicks on other controls/tools).
	 */
	private void calculateMousePosition()
	{
		__mousePosition = MouseInfo.getPointerInfo().getLocation();
		__mousePosition.translate(-_config.x - __xOffset, -_config.y - __yOffset);
		__originalMousePosition = (Point)__mousePosition.clone();

		boolean mouseInWindow = false;

		// TODO check if mouse is in window

		if(mouseInWindow) __mousePosition = _oldMousePosition;
		else _oldMousePosition = __mousePosition;
	}

	/**
	 * Initializes the rendering by clearing the screen and updating the control list.
	 */
	private void renderInit()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

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
		if(__detectedOS == OSType.WIN) // setCursorImage doesn't work on Windows :(
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
		__containerRenderer.notifyKeyPressed(keyCode);
		// TODO check if the gamescreen is allowed to handle input now (if an input field has focus, no other control is allowed to)
		__currentGameScreenManager.keyDown(keyCode);
		return false;
	}

	@Override
	public boolean keyUp(int keyCode)
	{
		__containerRenderer.notifyKeyUp(keyCode);
		__currentGameScreenManager.keyUp(keyCode);
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
	 * If yes: Forward click to this window and remind it
	 * If no : Go to next window.
	 * screenX -= 1;
	 * screenY -= 20;
	 * 
	 * 2.) Forward click to ControlActionManager with the clicked window as parameter
	 * 3.) Check if the clicked window is null (=there has no window been clicked)
	 * If yes: Close the window if needed
	 * If no : Forward click to game screen
	 */
	public boolean touchDown(int screenX, int screenY, int pointer, int mouseButton)
	{
		if(screenY <= __titleBarHeight)
		{
			if(screenX >= __SCREEN_SIZE.width - __titleBarHeight)
			{
				__application.exit();
			}
			else
			{
				__dragMode = true;
				__dragModeLastMousePosition = new Point(screenX, screenY);
			}

			return false;
		}

		screenX -= __xOffset;
		screenY -= __yOffset;

		boolean controlGotClickEvent = __containerRenderer.notifyMouseClick(screenX, screenY, mouseButton);

		if(!controlGotClickEvent)
		{
			__currentGameScreenManager.touchDown(screenX, screenY, pointer, mouseButton);
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int mouseButton)
	{
		screenX -= __xOffset;
		screenY -= __yOffset;
		__dragMode = false;
		__containerRenderer.notifyMouseReleased(screenX, screenY, mouseButton);
		__currentGameScreenManager.touchUp(screenX, screenY, pointer, mouseButton);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		if(__dragMode)
		{
			Display.setLocation((screenX - __dragModeLastMousePosition.x) + Display.getX(),
				(screenY - __dragModeLastMousePosition.y) + Display.getY());
		}
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
		__containerRenderer.notifyMouseScrolled(amount);
		// TODO check if game screen is allowed to get the scroll event or if other controls get this before
		__currentGameScreenManager.scrolled(amount);
		return false;
	}

	@Override
	public void dispose()
	{
	}

	/**
	 * Sets the selected input of the current game screen. This enables to change the focus of the input fields.
	 * 
	 * @param field The input field that should get the focus.
	 */
	public static void __setSelectedInput(InputField field)
	{
		__currentGameScreenManager.setSelectedInput(field);
	}

	/**
	 * @return The x-offset of the normal drawing canvas (everything excluding the window border)
	 */
	public static int __getXOffset()
	{
		return __xOffset;
	}

	/**
	 * @return The y-offset of the normal drawing canvas (everything excluding the window border)
	 */
	public static int __getYOffset()
	{
		return __yOffset;
	}

	/**
	 * Closes the game window and exits the application. This method wont ask the user to confirm or anything like that.
	 */
	public static void __exit()
	{
		__application.exit();
	}

	private enum OSType
	{
		WIN,
		LINUX,
		MAC,
		UNKNOWN;
	}
}