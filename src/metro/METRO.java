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

import metro.GameScreen.GameScreen;
import metro.GameScreen.MainMenu;
import metro.Graphics.Draw;
import metro.WindowControls.Button;
import metro.WindowControls.Window;

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

/**
 * @author Hauke
 * @version 0.1.0
 */
public class METRO extends Frame implements ApplicationListener, InputProcessor
{
	private static final long serialVersionUID = 1L;

	public static Dimension __SCREEN_SIZE;// = Toolkit.getDefaultToolkit().getScreenSize();
	public static final String __TITLE = "METRO",
		__VERSION = "0.1.0";

	private static OSType _detected_OS = OSType.UNKNOWN;

	public static BitmapFont __stdFont;
	public static GameScreen __currentGameScreen,
		__controlDrawer; // draws all the important controls and infos after rendering the scene
	public static TextureRegion __mainMenu_Buttons,
		__mainMenu_TitleImage,
		__iconSet,
		__mouseCursorImage;
	public static Button __viewPortButton_City,
		__viewPortButton_Train;
	public static Color __metroRed,
		__metroBlue;
	public static int __money = 50000,
		__baseNetSpacing = 25; // amount of pixel between lines of the base net
	public static ArrayList<Window> __windowList = new ArrayList<Window>();
	public static Point __mousePosition,
		__originalMousePosition;
	public static OrthographicCamera __camera;
	public static SpriteBatch __spriteBatch;
	public static LwjglApplication __application;

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

		// __CURRENT_OS
		__spriteBatch = new SpriteBatch();

		__camera = new OrthographicCamera();
		__camera.setToOrtho(true, __SCREEN_SIZE.width / 2, __SCREEN_SIZE.height / 2);
		__camera.update();

		// Set up the input event handling
		Gdx.input.setInputProcessor(this);

		// load new cursor image
		try
		{
			if(_detected_OS == OSType.LINUX || _detected_OS == OSType.MAC)
			{
				Pixmap pixmap = new Pixmap(Gdx.files.internal("textures/Cursor.png")); // has to be a width of 2^x (2, 4, 8, 16, 32, ...)
				Gdx.input.setCursorImage(pixmap, 16, 13); // sets cursor to correct position
				pixmap.dispose();
			}
			else if(_detected_OS == OSType.WIN) // setCursorImage doesn't work on Windows :(
			{
				__mouseCursorImage = new TextureRegion(new Texture(Gdx.files.internal("textures/Cursor.png")));
			}
		}
		catch(GdxRuntimeException ex)
		{
			System.out.println(ex.getMessage());
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
		__currentGameScreen = new MainMenu();

		// Create special colors
		__metroBlue = new Color(100, 180, 255);
		__metroRed = new Color(255, 100, 100);
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
		__mousePosition = MouseInfo.getPointerInfo().getLocation();

		if(!Boolean.parseBoolean(Settings.get("fullscreen.on").toString()))
		{
			__mousePosition.translate(-_config.x, -_config.y - 25);
		}
		__originalMousePosition = (Point)__mousePosition.clone();

		boolean mouseInWindow = false;
		for(Window win : __windowList)
		{
			mouseInWindow |= win.isMouseOnWindow(__mousePosition.x, __mousePosition.y);
		}
		if(mouseInWindow) __mousePosition = _oldMousePosition;
		else _oldMousePosition = __mousePosition;

		// Clear screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		__spriteBatch.begin();

		__currentGameScreen.update(__spriteBatch);
		if(__controlDrawer != null) __controlDrawer.update(__spriteBatch);

		// Draw every window with its controls
		for(Window win : __windowList)
		{
			win.draw(__spriteBatch);
		}

		// /
		// / END DRAW GAME-SCREEN
		// /

		Draw.setColor(__metroBlue);
		Draw.String("FPS: " + Gdx.graphics.getFramesPerSecond(), __SCREEN_SIZE.width - (Draw.getStringSize("FPS: " + Gdx.graphics.getFramesPerSecond()).width + 20), 20);

		if(_detected_OS == OSType.WIN) // setCursorImage doesn't work on Windows :(
		{
			if(__mouseCursorImage != null) Draw.Image(__mouseCursorImage, __originalMousePosition.x - 16, __originalMousePosition.y - 16);
		}

		__spriteBatch.end();
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
	public boolean keyDown(int keycode)
	{
		__currentGameScreen.keyPressed(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		for(Window win : __windowList)
		{
			win.keyUp(keycode);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		Window clickedWindow = null;

		for(int i = __windowList.size() - 1; i >= 0; i--) // from last to first window
		{
			__windowList.get(i).mousePressed(screenX, screenY, button);
			if(__windowList.get(i).isMouseOnWindow(screenX, screenY)) // if mouse is just on the window area
			{
				clickedWindow = __windowList.get(i);
				break;
			}
		}
		if(clickedWindow == null) // if no window has been clicked
		{
			__currentGameScreen.mouseClicked(screenX, screenY, button); // forward click to game screen
			if(__controlDrawer != null) __controlDrawer.mouseClicked(screenX, screenY, button);
		}
		else
		{
			clickedWindow.closeIfNeeded(screenX, screenY, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		__currentGameScreen.mouseReleased(button);

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
		__currentGameScreen.mouseScrolled(amount);
		Point mPos = METRO.__originalMousePosition;
		for(int i = __windowList.size() - 1; i >= 0; i--) // from last to first window
		{
			if(__windowList.get(i).isMouseOnWindow(mPos.x, mPos.y)) // if mouse is just on the window area
			{
				__windowList.get(i).mouseScrolled(amount);
				break;
			}
		}
		return false;
	}

	/**
	 * Closes a specific window (please do window.close() instead).
	 * 
	 * @param window The window that should be closed.
	 */
	public static void __closeWindow(Window window)
	{
		__windowList.remove(window);
	}

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
					System.out.println("No backup of settings.cfg has been created.");
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