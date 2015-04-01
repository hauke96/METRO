package metro.Game;


import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import metro.WindowControls.Button;
import metro.WindowControls.Window;
import metro.graphics.Draw;

import com.badlogic.gdx.Input.Keys;
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
    		new Point(50, 
    			METRO.__SCREEN_SIZE.height / 2 - METRO.__mainMenu_TitleImage.getRegionHeight() / 2 - 400), // same y-pos as title image
    		new Point(500, 800));
	    new metro.WindowControls.Button(new Rectangle((500 - (int)(METRO.__mainMenu_TitleImage.getRegionWidth() * 0.4f)) / 2, 
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
    		+ "\n\nCITY-VIEW - In this mode you'll see where the most passengers are.", new Point(20, 100), 450, _welcomeWindow);
	    ArrayList<String> list = new ArrayList<String>();
	    list.add("Hallo world!");
	    list.add("Wie gehts euch?");
	    list.add("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.");
	    list.add("Hallo world!");
	    list.add("Wie gehts euch?");
	    list.add("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.");
	    list.add("Hallo world!");
	    list.add("Wie gehts euch?");
	    list.add("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.");
	    list.add("Hallo world!");
	    list.add("Wie gehts euch?");
	    list.add("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.");
	    new metro.WindowControls.List(new Rectangle(50, 300, 400, 450), list, _welcomeWindow);
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
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(_button_startGame.isPressed(screenX, screenY))
		{
			_welcomeWindow.close();
			_welcomeWindow = null;
			METRO.__currentGameScreen = new GameScreen_TrainView();
			GameScreen_TrainView._cityGameScreen = new GameScreen_CityView();
			GameScreen_CityView._trainGameScreen = METRO.__currentGameScreen;
			METRO.__controlDrawer = new ScreenInfoDrawer();
		}
		else if(_button_exitGame.isPressed(screenX, screenY))
		{
			METRO.__close();
		}
	}
	public void mouseReleased(int mouseButton){}
	public void keyPressed(int keyCode)
	{
		if(keyCode == Keys.ESCAPE)
		{
			METRO.__close();
		}
	}
	@Override
	public void mouseScrolled(int amount) 
	{
	}
}
