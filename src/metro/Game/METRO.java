package metro.Game;
/**
 * METRO - Master of established transport railway operators
 * 
 * Code convention:
 *  
 * 	final: 						_WITH_BIG_NAME
 * 	normal instance variables: 	_variable_someName
 * 	METRO instance variables:	__variable
 * 	METRO finals:				__FINAL
 * 	local stuff: 				chooseAnyName
 * 
 * 	final and local with small beginning char and capitol for every following word ( e.g.: int myVariableIsCool = 42; ).
 * 
 * 	Method names has to be clear ( not a(){...} but createCharacter(){...} )
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import metro.Graphics.Draw;
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
 * @version 0.0.3
 */
public class METRO extends Frame implements ApplicationListener, InputProcessor
{
	private static final long serialVersionUID = 1L;
	
	public static Dimension __SCREEN_SIZE;// = Toolkit.getDefaultToolkit().getScreenSize();
	public static final String __TITLE = "METRO",
		__VERSION = "0.1.0";
	
	public static BitmapFont __stdFont;
	public static GameScreen __currentGameScreen,
		__controlDrawer; // draws all the important controls and infos after rendering the scene
	public static TextureRegion __viewPortButton_Texture,
		__mainMenu_Buttons,
		__mainMenu_TitleImage,
		__iconSet;
	public static metro.WindowControls.Button __viewPortButton_City, 
		__viewPortButton_Train;
	public static Color __metroRed,
		__metroBlue;
	public static int __money = 50000,
		__baseNetSpacing = 25; // amount of pixel between lines of the base net
	public static ArrayList<Window> __windowList = new ArrayList<Window>();
	public static Point __mousePosition;
	public static OrthographicCamera __camera;
	public static SpriteBatch __spriteBatch;
	public static LwjglApplication __application; 
	
	private Point _oldMousePosition = new Point(0,0);

	public static void main(String[] args) 
	{
		new METRO();
	}
	private METRO()
	{
	    GraphicsEnvironment gEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice[] devices = gEnviroment.getScreenDevices();
		__SCREEN_SIZE = new Dimension(devices[0].getDisplayMode().getWidth(), devices[0].getDisplayMode().getHeight()); 
		
		Settings.read();
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = __TITLE + "  " + __VERSION;
		config.width = Settings.__screenwidth;//__SCREEN_SIZE.width;
		config.height = Settings.__screenheight;//__SCREEN_SIZE.height;
		config.useGL30 = Settings.__useopengl30;
		config.resizable = false;
		config.fullscreen = Settings.__fullscreen;
//		config.foregroundFPS = -1; // max frames
		config.samples = Settings.__amountsamples;
		config.vSyncEnabled = Settings.__usevsync;
		config.useHDPI = Settings.__usehdpi;
		
		__application = new LwjglApplication(this, config);
	}
	@Override
	public void create()
	{
		__spriteBatch = new SpriteBatch();
		
		__camera = new OrthographicCamera();
		__camera.setToOrtho(true, __SCREEN_SIZE.width / 2, __SCREEN_SIZE.height / 2);
		__camera.update();
		
		// Set up the input event handling
		Gdx.input.setInputProcessor(this);
		
		try
		{
			Pixmap pixmap = new Pixmap(Gdx.files.internal("textures/Cursor.png")); // has to be a width of 2^x (2, 4, 8, 16, 32, ...)
			Gdx.input.setCursorImage(pixmap, 16, 13); // sets cursor to correct position
			pixmap.dispose();
		}
		catch(GdxRuntimeException ex)
		{
			System.out.println(ex.getMessage());
		}
		
		//Load important stuff
		__viewPortButton_Texture = new TextureRegion(new Texture(Gdx.files.internal("textures/ViewPortButtons.png")));
		__viewPortButton_Texture.flip(false, true);

		__viewPortButton_City = new metro.WindowControls.Button(new Rectangle(__SCREEN_SIZE.width / 2 - 200, -15, 201, 60), 
				new Rectangle(0, 0, 201, 60), 
				__viewPortButton_Texture);
		__viewPortButton_Train = new metro.WindowControls.Button(new Rectangle(__SCREEN_SIZE.width / 2, -5, 200, 60), 
				new Rectangle(200, 0, 200, 60), 
				__viewPortButton_Texture);
		
		__mainMenu_Buttons = new TextureRegion(new Texture(Gdx.files.internal("textures/MainMenu_Buttons.png")));
		__mainMenu_Buttons.flip(false, true);
		
		__mainMenu_TitleImage = new TextureRegion(new Texture(Gdx.files.internal("textures/MainMenu_TitleImage.png")));
		__mainMenu_TitleImage.flip(false, true);
		
		__iconSet = new TextureRegion(new Texture(Gdx.files.internal("textures/IconSet.png")));
		__iconSet.flip(false, true);
		
		//Load fonts
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GatsbyFLF-Bold.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.magFilter = TextureFilter.Nearest;
		parameter.size = 21;
		parameter.flip = true;
		__stdFont = generator.generateFont(parameter); // font size 20 pixels
		generator.dispose();
		
		//Create MainMenu Screen
	    __currentGameScreen = new MainMenu();
	    
	    // Create special colors
	    __metroBlue = new Color(100, 180, 255);
	    __metroRed = new Color(255, 100, 100);
	}
	@Override
	public void resize (int width, int height) 
	{
		__camera.setToOrtho(true, width, height);
		__spriteBatch.setProjectionMatrix(__camera.combined);
	}
	@Override
	public void render()
	{
		__mousePosition = MouseInfo.getPointerInfo().getLocation();
		boolean mouseInWindow = false;
		for(Window win : __windowList)
		{
			mouseInWindow |= win.isMouseOnWindow(__mousePosition.x, __mousePosition.y);
		}
		if(mouseInWindow) __mousePosition = _oldMousePosition;
		else _oldMousePosition = __mousePosition;
		
		//Clear screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		__spriteBatch.begin();
		
		__currentGameScreen.update(__spriteBatch);
		if(__controlDrawer != null) __controlDrawer.update(__spriteBatch);
		//Draw every window with its controls
		for(Window win : __windowList)
		{
			win.draw(__spriteBatch);
		}
		
		///
		/// END   DRAW GAME-SCREEN
		///
		
		Draw.setColor(__metroBlue);
		Draw.String("FPS: " + Gdx.graphics.getFramesPerSecond(), __SCREEN_SIZE.width - (Draw.getStringSize("FPS: " + Gdx.graphics.getFramesPerSecond()).width + 20), 20);
		
		__spriteBatch.end();
	}
	@Override
	public void pause(){}
	@Override
	public void resume(){}
	@Override
	public boolean keyDown(int keycode) 
	{
//		System.out.println("KeyEvent::keyDown::" + keycode);
		__currentGameScreen.keyPressed(keycode);
		return false;
	}
	@Override
	public boolean keyUp(int keycode) 
	{
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
		Point mPos = MouseInfo.getPointerInfo().getLocation();
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
	 * @param window The window that should be closed.
	 */
	public static void __closeWindow(Window window)
	{
		__windowList.remove(window);
	}
}