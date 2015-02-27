import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	}

	public void mouseClicked(MouseEvent e)
	{
		if(_button_startGame.isPressed(e.getX(), e.getY()))
		{
			METRO.__currentGameScreen = new GameScreen_TrainView();
			GameScreen_TrainView._cityGameScreen = new GameScreen_CityView();
			GameScreen_CityView._trainGameScreen = METRO.__currentGameScreen;
		}
		else if(_button_exitGame.isPressed(e.getX(), e.getY()))
		{
			System.exit(0);
		}
	}
	public void mouseReleased(MouseEvent e){}
}
