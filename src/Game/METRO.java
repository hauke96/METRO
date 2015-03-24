package Game;
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
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import WindowControls.Window;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * @author Hauke
 * @version 0.0.2
 */
public class METRO extends Frame implements MouseListener, KeyListener, ApplicationListener
{
	private static final long serialVersionUID = 1L;
	
	public static Dimension __SCREEN_SIZE;// = Toolkit.getDefaultToolkit().getScreenSize();
	//public static final Dimension __SCREEN_SIZE = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/2,Toolkit.getDefaultToolkit().getScreenSize().height/2);
	public static final String __TITLE = "METRO",
		__VERSION = "0.0.2";
	
	public static BitmapFont __stdFont;// = new Font("Huxley Titling", Font.PLAIN, 20);
	public static GameScreen __currentGameScreen,
		__controlDrawer; // draws all the important controls and infos after rendering the scene
	public static TextureRegion __viewPortButton_Texture,
		__mainMenu_Buttons,
		__mainMenu_TitleImage,
		__iconSet;
	public static WindowControls.Button __viewPortButton_City, 
		__viewPortButton_Train;
	public static Color __metroRed,
		__metroBlue;
	public static int __money = 50000,
		__baseNetSpacing = 25; // amount of pixel between lines of the base net
	public static ArrayList<Window> __windowList = new ArrayList<Window>();
	public static ExitGameWindow __exitGameWindow;
	public static Point __mousePosition;
	public static OrthographicCamera __camera;
	public static SpriteBatch __spriteBatch;
	
	private TextureRegion _cursor;
//		__bufferedImage;
	private long _oldSystemTime = System.currentTimeMillis();
	private Point _oldMousePosition = new Point(0,0);
	private LwjglApplication _application; 
 
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		new METRO();
	}
	private METRO()
	{
	    GraphicsEnvironment gEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice[] devices = gEnviroment.getScreenDevices();
		__SCREEN_SIZE = new Dimension(devices[0].getDisplayMode().getWidth(), devices[0].getDisplayMode().getHeight()); 
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "METRO";
		config.width = __SCREEN_SIZE.width;
		config.height = __SCREEN_SIZE.height;
		config.useGL30 = true;
		config.resizable = false;
		config.fullscreen = true;
		
		_application = new LwjglApplication(this, config);
		
//		super(__TITLE + "  " + __VERSION);
		
//	    setBackground(Color.white);
//	    setSize(__SCREEN_SIZE.width, __SCREEN_SIZE.height);
//	    addWindowListener(new WindowAdapter() {
//	        public void windowClosing(WindowEvent e) {
//	        	System.exit(0);
//	        }
//	    });
	    //TODO: Create Key and Mouse listener to get input stuff
//	    addMouseListener(this);
//	    addKeyListener(this);
//	    setLocationRelativeTo(null);
//	    setUndecorated(true);
	    
//	    GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
//	    GraphicsDevice[] devices = g.getScreenDevices();
//	    __SCREEN_SIZE = new Dimension(devices[0].getDisplayMode().getWidth(), devices[0].getDisplayMode().getHeight()); 
	    
//		setBounds(0, 0, __SCREEN_SIZE.width, __SCREEN_SIZE.height);
		
//	    Locale.setDefault(new Locale("de", "DE"));
	    
	    
//		try
//		{
//		    __stdFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/GatsbyFLF-Bold.ttf")).deriveFont(20f);
//			
//			// make Cursor invisible
//			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
//			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//			    cursorImg, new Point(0, 0), "blank cursor");
//			METRO.getWindows()[0].setCursor(blankCursor);
//			
//		}
//		catch (Exception e)
//		{
//			System.out.println(e.getMessage());
//		}
		
	    // Create screen NOW (because of setVisible and repaint and NullPointerExceptions ;) )
	    
//	    setVisible(true);
//	    setResizable(false);
	}
	
//	@Override
//    public void paint(Graphics g) 
//	{
//		__mousePosition = MouseInfo.getPointerInfo().getLocation();
//		boolean mouseInWindow = false;
//		for(Window win : __windowList)
//		{
//			mouseInWindow |= win.isMouseOnWindow();
//		}
//		if(mouseInWindow) __mousePosition = _oldMousePosition;
//		else _oldMousePosition = __mousePosition;
//		
//		//update exitGameScreen, which should appear over all game screens
//		if(__exitGameWindow != null)
//		{
//			__exitGameWindow.update(g);
//		}
//		
//		__bufferedImage = new BufferedImage(__SCREEN_SIZE.width, __SCREEN_SIZE.height, BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g2d = __bufferedImage.createGraphics();
//		
//		g2d.setFont(__stdFont);
//
//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		
//		//Clear screen with white:
//		g2d.setColor(Color.white);
//		g2d.fillRect(0,  0, __SCREEN_SIZE.width, __SCREEN_SIZE.height);
//		
//		///
//		/// BEGIN DRAW GAME-SCREEN
//		///
//		
//		__currentGameScreen.update(g2d);
//		if(__controlDrawer != null) __controlDrawer.update(g2d);
//		//Draw every window with its controls
//		for(Window win : __windowList)
//		{
//			win.draw(g2d);
//		}
//		
//		///
//		/// END   DRAW GAME-SCREEN
//		///
//		
//		// Draw cursor icon/image
//		g2d.drawImage(_cursor, MouseInfo.getPointerInfo().getLocation().x - 10, MouseInfo.getPointerInfo().getLocation().y - 10, null);
//		
//		//Draw FPS on screen
//		g2d.setColor(__metroBlue);
//		g2d.drawString((int)(1000 / (float)(System.currentTimeMillis() - _oldSystemTime)) + " FPS", __SCREEN_SIZE.width - 50, 20);
//		_oldSystemTime = System.currentTimeMillis();
//		
//		//finalize drawing stuff
//	    Graphics2D g2dComponent = (Graphics2D) g;
//	    g2dComponent.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//	    		RenderingHints.VALUE_ANTIALIAS_ON);
//	    g2dComponent.drawImage(__bufferedImage,0, 0, __SCREEN_SIZE.width, __SCREEN_SIZE.height, null);
//	    g2dComponent.finalize();
//
////	    try{Thread.sleep(20);}
////	    catch(Exception e){}
//	    repaint(); // max ~60fps  (1000/60=16.6666...)
//	}
//	public void update(Graphics g)
//	{
//        paint(g);
//    }
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		Window clickedWindow = null;
		
		for(int i = __windowList.size() - 1; i >= 0; i--) // from last to first window
		{
			__windowList.get(i).mousePressed(e);
			if(__windowList.get(i).isMouseOnWindow()) // if mouse is just on the window area
			{
				clickedWindow = __windowList.get(i);
				break;
			}
		}
		if(clickedWindow == null) // if no window has been clicked
		{
			__currentGameScreen.mouseClicked(e); // forward click to game screen
			if(__controlDrawer != null) __controlDrawer.mouseClicked(e);
		}
		else
		{
			clickedWindow.closeIfNeeded(e);
		}
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{
		__currentGameScreen.mouseReleased(e);
		
		for(Window win : __windowList)
		{
			win.mouseReleased();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e){}
	@Override
	public void mouseEntered(MouseEvent e){}
	@Override
	public void mouseExited(MouseEvent e){}
	@Override
	public void keyPressed(KeyEvent e)
	{
		__currentGameScreen.keyPressed(e);
	}
	@Override
	public void keyReleased(KeyEvent e)
	{}
	@Override
	public void keyTyped(KeyEvent e)
	{}

	@Override
	public void create()
	{
		__spriteBatch = new SpriteBatch();
		
		__camera = new OrthographicCamera();
		__camera.setToOrtho(true, __SCREEN_SIZE.width / 2, __SCREEN_SIZE.height / 2);
		__camera.update();

		
		//Load important stuff
		__viewPortButton_Texture = new TextureRegion(new Texture(Gdx.files.internal("textures/ViewPortButtons.png")));
		__viewPortButton_Texture.flip(false, true);

		__viewPortButton_City = new WindowControls.Button(new Rectangle(__SCREEN_SIZE.width / 2 - 200, -15, 201, 60), 
				new Rectangle(0, 0, 201, 60), 
				__viewPortButton_Texture);
		__viewPortButton_Train = new WindowControls.Button(new Rectangle(__SCREEN_SIZE.width / 2, -5, 200, 60), 
				new Rectangle(200, 0, 200, 60), 
				__viewPortButton_Texture);
		
		__mainMenu_Buttons = new TextureRegion(new Texture(Gdx.files.internal("textures/MainMenu_Buttons.png")));
		__mainMenu_Buttons.flip(false, true);
		
		__mainMenu_TitleImage = new TextureRegion(new Texture(Gdx.files.internal("textures/MainMenu_TitleImage.png")));
		__mainMenu_TitleImage.flip(false, true);
		
		__iconSet = new TextureRegion(new Texture(Gdx.files.internal("textures/IconSet.png")));
		__iconSet.flip(false, true);
		
		_cursor = new TextureRegion(new Texture(Gdx.files.internal("textures/Cursor.png")));
		_cursor.flip(false, true);
		
		//Load fonts
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GatsbyFLF-Bold.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 20;
		BitmapFont _stdFont = generator.generateFont(parameter); // font size 20 pixels
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
			mouseInWindow |= win.isMouseOnWindow();
		}
		if(mouseInWindow) __mousePosition = _oldMousePosition;
		else _oldMousePosition = __mousePosition;
		
		//update exitGameScreen, which should appear over all game screens
		if(__exitGameWindow != null)
		{
			__exitGameWindow.update();
		}
		
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
		
		// Draw cursor icon/image
		__spriteBatch.draw(_cursor, MouseInfo.getPointerInfo().getLocation().x - 10, MouseInfo.getPointerInfo().getLocation().y - 10);
		
		//TODO: Draw FPS on screen
//		g2d.setColor(__metroBlue);
//		g2d.drawString((int)(1000 / (float)(System.currentTimeMillis() - _oldSystemTime)) + " FPS", __SCREEN_SIZE.width - 50, 20);
		_oldSystemTime = System.currentTimeMillis();
		
		//finalize drawing stuff
//	    Graphics2D g2dComponent = (Graphics2D) g;
//	    g2dComponent.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//	    		RenderingHints.VALUE_ANTIALIAS_ON);
//	    g2dComponent.drawImage(__bufferedImage,0, 0, __SCREEN_SIZE.width, __SCREEN_SIZE.height, null);
//	    g2dComponent.finalize();
		
		
		
		
		
		
		
		
		
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
	
	/**
	 * Closes a specific window (please do window.close() instead).
	 * @param window The window that should be closed.
	 */
	public static void __closeWindow(Window window)
	{
		__windowList.remove(window);
	}
	/**
	 * Creates a "really quit"-dialog window. This method does NOT QUIT the game.
	 */
	public static void __close()
	{
		__exitGameWindow = new ExitGameWindow();
	}
	
	
	
	private static class ExitGameWindow 
	{
		private WindowControls.Button _yesButton, 
			_noButton;
		private Window _closeWindow;
		
		public ExitGameWindow()
		{
			_closeWindow = new Window("Really quit?",new Point(__SCREEN_SIZE.width / 2 - 200, 
					__SCREEN_SIZE.height / 2 - 50), new Point(400, 100), __metroRed);
			_yesButton = new WindowControls.Button(new Rectangle(10, 75, 185, 20), "Yes", _closeWindow);
			_noButton = new WindowControls.Button(new Rectangle(205, 75, 185, 20), "No", _closeWindow);
			new WindowControls.Label("Really quit METRO?", new Point(140, 55), _closeWindow);
		}
		public void update()
		{
			if(_yesButton.isPressed())
			{
				System.exit(0);
			}
			else if(_noButton.isPressed())
			{
				_closeWindow.close();
				METRO.__exitGameWindow = null;
			}
		}
	}
}
