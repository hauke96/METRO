package Game;


import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;

import WindowControls.Button;


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
	
	public MainMenu()
	{
		_button_startGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width/2-100, METRO.__SCREEN_SIZE.height/2-25, 200, 50), 
				new Rectangle(0, 0, 200, 50), METRO.__mainMenu_Buttons);
		_button_exitGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width/2-100, METRO.__SCREEN_SIZE.height/2+35, 200, 50), 
				new Rectangle(0, 50, 200, 50), METRO.__mainMenu_Buttons);
	}

	/* (non-Javadoc)
	 * @see GameMode#update()
	 * @param buffer The graphics handle to the buffer to draw on.
	 */
	@Override
	public void update(Graphics2D g)
	{
		_button_startGame.draw(g);
		_button_exitGame.draw(g);
		
		g.drawImage(METRO.__mainMenu_TitleImage, 
				METRO.__SCREEN_SIZE.width / 2 - METRO.__mainMenu_TitleImage.getWidth() / 2,
				METRO.__SCREEN_SIZE.height / 2 - METRO.__mainMenu_TitleImage.getHeight() / 2 - 200,
				null);
	}
	/**
	 * Checks which button in the main menu has been clicked.
	 */
	public void mouseClicked(MouseEvent e)
	{
		if(_button_startGame.isPressed(e.getX(), e.getY()))
		{
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
