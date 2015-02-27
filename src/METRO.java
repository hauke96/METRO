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
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

/**
 * @author Hauke
 * @version 0.0.1
 */
public class METRO extends Frame implements MouseListener
{
	private static final long serialVersionUID = 1L;
	public static final Dimension __SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	//public static final Dimension __SCREEN_SIZE = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/2,Toolkit.getDefaultToolkit().getScreenSize().height/2);
	public static final String __TITLE = "METRO",
		__VERSION = "0.0.1";
	public static final Font __STDFONT = new Font("Huxley Titling", Font.PLAIN, 20);
	
	public static GameScreen __currentGameScreen;
	public static BufferedImage __bufferedImage,
		__viewPortButton_Texture,
		__mainMenu_Buttons;
	public static WindowControls.Button __viewPortButton_City, 
		__viewPortButton_Train;
	public static Color __metroRed,
		__metroBlue;
	public static int __money = 12345678;
	 
	private BufferedImage _cursor;
 
	 
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
	    setSize(__SCREEN_SIZE.width, __SCREEN_SIZE.height);
	    addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	        	System.exit(0);
	        }
	    });
	    addMouseListener(this);
	    setLocationRelativeTo(null);
	    setUndecorated(true);

	    Locale.setDefault(new Locale("de", "DE"));
	    
	    // Create special colors
	    __metroBlue = new Color(100, 180, 255);
	    __metroRed = new Color(255, 100, 100);
	    
		try
		{
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
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		
	    // Create screen NOW (because of setVisible and repaint and NullPointerExceptions ;) )
	    __currentGameScreen = new MainMenu();
	    
	    setVisible(true);
	    setResizable(false);
	}
	
	@Override
    public void paint(Graphics g) 
	{
		__bufferedImage = new BufferedImage(__SCREEN_SIZE.width, __SCREEN_SIZE.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = __bufferedImage.createGraphics();
		
		g2d.setFont(__STDFONT);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//Clear screen with white:
		g2d.setColor(Color.white);
		g2d.fillRect(0,  0, __SCREEN_SIZE.width, __SCREEN_SIZE.height);
		
		__currentGameScreen.update(g2d);
		
		// Draw cursor icon/image
		g2d.drawImage(_cursor, MouseInfo.getPointerInfo().getLocation().x - 10, MouseInfo.getPointerInfo().getLocation().y - 10, null);
		
	    Graphics2D g2dComponent = (Graphics2D) g;
	    g2dComponent.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    		RenderingHints.VALUE_ANTIALIAS_ON);
	    g2dComponent.drawImage(__bufferedImage,0, 0, __SCREEN_SIZE.width, __SCREEN_SIZE.height, null);
	    g2dComponent.finalize();

	    try{Thread.sleep(20);}
	    catch(Exception e){}
	    repaint(); // max ~60fps  (1000/60=16.6666...)
	}
	public void update(Graphics g){
        paint(g);
    }
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		__currentGameScreen.mouseClicked(e);
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{
		__currentGameScreen.mouseReleased(e);
	}
	@Override
	public void mouseClicked(MouseEvent e){}
	@Override
	public void mouseEntered(MouseEvent e){}
	@Override
	public void mouseExited(MouseEvent e){}
}
