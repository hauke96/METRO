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
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import javax.imageio.ImageIO;

import WindowControls.Window;

/**
 * @author Hauke
 * @version 0.0.1
 */
public class METRO extends Frame implements MouseListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	
	public static final Dimension __SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	//public static final Dimension __SCREEN_SIZE = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/2,Toolkit.getDefaultToolkit().getScreenSize().height/2);
	public static final String __TITLE = "METRO",
		__VERSION = "0.0.2";
	
	public static Font __stdFont;// = new Font("Huxley Titling", Font.PLAIN, 20);
	public static GameScreen __currentGameScreen,
		__controlDrawer; // draws all the important controls and infos after rendering the scene
	public static BufferedImage __viewPortButton_Texture,
		__mainMenu_Buttons,
		__mainMenu_TitleImage,
		__iconSet;
	public static WindowControls.Button __viewPortButton_City, 
		__viewPortButton_Train;
	public static Color __metroRed,
		__metroBlue;
	public static int __money = 12345678;
	public static ArrayList<Window> __windowList = new ArrayList<Window>();
	public static CloseWindow __closeWindow;
	
	private BufferedImage _cursor,
		__bufferedImage;
	private long _oldSystemTime = System.currentTimeMillis();
 
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		new METRO();
	}
	private METRO(){
		super(__TITLE + "  " + __VERSION);
		
	    setBackground(Color.white);
	    setSize(__SCREEN_SIZE.width, __SCREEN_SIZE.height);
	    addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	        	System.exit(0);
	        }
	    });
	    addMouseListener(this);
	    addKeyListener(this);
	    setLocationRelativeTo(null);
	    setUndecorated(true);
	    
	    Locale.setDefault(new Locale("de", "DE"));
	    
	    // Create special colors
	    __metroBlue = new Color(100, 180, 255);
	    __metroRed = new Color(255, 100, 100);
	    
	    
		try
		{
		    __stdFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/GatsbyFLF-Bold.ttf")).deriveFont(20f);
		    
			_cursor = ImageIO.read(new File("textures/Cursor.png"));
			
			// make Cursor invisible
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			    cursorImg, new Point(0, 0), "blank cursor");
			METRO.getWindows()[0].setCursor(blankCursor);
			
			__viewPortButton_Texture = ImageIO.read(new File("textures/ViewPortButtons.png"));
			__viewPortButton_City = new WindowControls.Button(new Rectangle(__SCREEN_SIZE.width / 2 - 200, -15, 201, 60), 
					new Rectangle(0, 0, 201, 60), 
					__viewPortButton_Texture);
			__viewPortButton_Train = new WindowControls.Button(new Rectangle(__SCREEN_SIZE.width / 2, -5, 200, 60), 
					new Rectangle(200, 0, 200, 60), 
					__viewPortButton_Texture);
			__mainMenu_Buttons = ImageIO.read(new File("textures/MainMenu_Buttons.png"));
			__mainMenu_TitleImage = ImageIO.read(new File("textures/MainMenu_TitleImage.png"));
			__iconSet = ImageIO.read(new File("textures/IconSet.png"));
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	    
	    Window win = new Window("This is a test :)", new Point(200, 200), new Point(350, 300));
	    __windowList.add(win);
	    new WindowControls.Button(new Rectangle(100, 100, 201, 60), 
				new Rectangle(0, 0, 201, 60), 
				__viewPortButton_Texture, win);
		
	    // Create screen NOW (because of setVisible and repaint and NullPointerExceptions ;) )
	    __currentGameScreen = new MainMenu();
	    
	    setVisible(true);
	    setResizable(false);
	    
	    
	}
	
	@Override
    public void paint(Graphics g) 
	{
		//update very important and basic things:
		if(__closeWindow != null)
		{
			__closeWindow.update(g);
		}
		
		__bufferedImage = new BufferedImage(__SCREEN_SIZE.width, __SCREEN_SIZE.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = __bufferedImage.createGraphics();
		
		g2d.setFont(__stdFont);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Clear screen with white:
		g2d.setColor(Color.white);
		g2d.fillRect(0,  0, __SCREEN_SIZE.width, __SCREEN_SIZE.height);
		
		///
		/// BEGIN DRAW GAME-SCREEN
		///
		
		__currentGameScreen.update(g2d);
		if(__controlDrawer != null) __controlDrawer.update(g2d);
		//Draw every window with its controls
		for(Window win : __windowList)
		{
			win.draw(g2d);
		}
		
		///
		/// END   DRAW GAME-SCREEN
		///
		
		// Draw cursor icon/image
		g2d.drawImage(_cursor, MouseInfo.getPointerInfo().getLocation().x - 10, MouseInfo.getPointerInfo().getLocation().y - 10, null);
		
		//Draw FPS on screen
		g2d.setColor(__metroBlue);
		g2d.drawString((int)(1000 / (float)(System.currentTimeMillis() - _oldSystemTime)) + " FPS", __SCREEN_SIZE.width - 50, 20);
		_oldSystemTime = System.currentTimeMillis();
		
		//finalize drawing stuff
	    Graphics2D g2dComponent = (Graphics2D) g;
	    g2dComponent.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    		RenderingHints.VALUE_ANTIALIAS_ON);
	    g2dComponent.drawImage(__bufferedImage,0, 0, __SCREEN_SIZE.width, __SCREEN_SIZE.height, null);
	    g2dComponent.finalize();

//	    try{Thread.sleep(20);}
//	    catch(Exception e){}
	    repaint(); // max ~60fps  (1000/60=16.6666...)
	}
	public void update(Graphics g)
	{
        paint(g);
    }
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		Window clickedWindow = null;
		
//		for(Window win : __windowList)
		for(int i = __windowList.size() - 1; i >= 0; i--)
		{
			__windowList.get(i).mousePressed(e);
			if(__windowList.get(i).isMouseOnWindow()) // if mouse is just on the window area (not on a Button, etc.)
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
		//if(__controlDrawer != null) __controlDrawer.mouseClicked(e);
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
	
	/**
	 * Closes a specific window.
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
		__closeWindow = new CloseWindow();
	}
	private static class CloseWindow 
	{
		private WindowControls.Button _yesButton, 
			_noButton;
		private Window _closeWindow;
		
		public CloseWindow()
		{
			_closeWindow = new Window("Really quit?",new Point(__SCREEN_SIZE.width / 2 - 200, 
					__SCREEN_SIZE.height / 2 - 50), new Point(400, 100), __metroRed);
			_yesButton = new WindowControls.Button(new Rectangle(10, 75, 185, 20), "Yes", _closeWindow);
			_noButton = new WindowControls.Button(new Rectangle(205, 75, 185, 20), "No", _closeWindow);
			new WindowControls.Label("Really quit METRO?", new Point(140, 55), _closeWindow);
			
			__windowList.add(_closeWindow);
		}
		public void update(Graphics g)
		{
			if(_yesButton.isPressed())
			{
				System.exit(0);
			}
			else if(_noButton.isPressed())
			{
				METRO.__closeWindow = null;
//				_closeWindow = null;
//				_yesButton = null;
//				_noButton = null;
				METRO.__windowList.remove(_closeWindow);
			}
		}
	}
}
