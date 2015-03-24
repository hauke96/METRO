package metro.Game;


import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import metro.WindowControls.Button;
import metro.WindowControls.Window;
import metro.graphics.Draw;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * 
 */

/**
 * @author Hauke
 *
 */
public class MainMenu implements GameScreen
{
	private Button _button_startGame, 
		_button_exitGame;
	private Window _welcomeWindow;
	
	public MainMenu()
	{
		// Create MainMenu buttons:
		_button_startGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width/2-100, METRO.__SCREEN_SIZE.height/2-25, 200, 50), 
			new Rectangle(0, 0, 200, 50), METRO.__mainMenu_Buttons);
		_button_exitGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width/2-100, METRO.__SCREEN_SIZE.height/2+35, 200, 50), 
			new Rectangle(0, 50, 200, 50), METRO.__mainMenu_Buttons);

		// Create welcome-window:
	    _welcomeWindow = new Window("Welcome to METRO", 
    		new Point(200, 
    			METRO.__SCREEN_SIZE.height / 2 - METRO.__mainMenu_TitleImage.getRegionHeight() / 2 - 200), // same y-pos as title image
    		new Point(350, 350));
	    new metro.WindowControls.Button(new Rectangle((350 - (int)(METRO.__mainMenu_TitleImage.getRegionWidth() * 0.4f)) / 2, 
	    		(260 - (int)(METRO.__mainMenu_TitleImage.getRegionWidth() * 0.4f)) / 2, 
	    		(int)(METRO.__mainMenu_TitleImage.getRegionWidth() * 0.4f), 
	    		(int)(METRO.__mainMenu_TitleImage.getRegionHeight() * 0.4f)),
			new Rectangle(0, 
				0, 
				METRO.__mainMenu_TitleImage.getRegionWidth(),
				METRO.__mainMenu_TitleImage.getRegionHeight()),
				METRO.__mainMenu_TitleImage, _welcomeWindow);
	    new metro.WindowControls.Label("METRO stands for \"Master of established transport railway operators\" and is a simple Subway/Rapid-Transit and economic simulator."
    		+ "\n\nTRAIN-VIEW - In this mode you'll be able to build tracks and stations."
    		+ "\n\nCITY-VIEW - In this mode you'll see where the most passengers are.", new Point(20, 100), 330, _welcomeWindow);
	}

	/* (non-Javadoc)
	 * @see GameMode#update()
	 * @param buffer The graphics handle to the buffer to draw on.
	 */
	@Override
	public void update(SpriteBatch sp)
	{
		_button_startGame.draw();
		_button_exitGame.draw();
		
		Draw.Image(METRO.__mainMenu_TitleImage, 
				METRO.__SCREEN_SIZE.width / 2 - METRO.__mainMenu_TitleImage.getRegionWidth() / 2,
				METRO.__SCREEN_SIZE.height / 2 - METRO.__mainMenu_TitleImage.getRegionHeight() / 2 - 200);
	}
	/**
	 * Checks which button in the main menu has been clicked.
	 */
	public void mouseClicked(MouseEvent e)
	{
		if(_button_startGame.isPressed(e.getX(), e.getY()))
		{
			_welcomeWindow.close();
			_welcomeWindow = null;
			METRO.__currentGameScreen = new GameScreen_TrainView();
			GameScreen_TrainView._cityGameScreen = new GameScreen_CityView();
			GameScreen_CityView._trainGameScreen = METRO.__currentGameScreen;
			METRO.__controlDrawer = new ScreenInfoDrawer();
		}
		else if(_button_exitGame.isPressed(e.getX(), e.getY()))
		{
			METRO.__close();
		}
	}
	public void mouseReleased(MouseEvent e){}
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			METRO.__close();
		}
	}
}
