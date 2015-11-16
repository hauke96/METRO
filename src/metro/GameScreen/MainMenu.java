package metro.GameScreen;

import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.MainView.MainView;
import metro.Graphics.Draw;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;
import metro.WindowControls.Label;
import metro.WindowControls.Window;

/**
 * The main menu is the first menu you'll see after starting the game. It provides some basic options like start, exit and settings.
 * 
 * @author Hauke
 * 
 */

public class MainMenu extends GameScreen
{
	private Button _button_startGame,
		_button_settings,
		_button_exitGame;
	private Window _welcomeWindow;

	/**
	 * Creates a main menu with the welcome-window, and the three buttons "Play", "Settings" and "Exit".
	 */
	public MainMenu()
	{
		// Create MainMenu buttons:
		_button_startGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 - 25, 200, 50),
			new Rectangle(0, 0, 200, 50), METRO.__mainMenu_Buttons);
		registerControl(_button_startGame);
		
		_button_settings = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 + 35, 200, 50),
			new Rectangle(0, 50, 200, 50), METRO.__mainMenu_Buttons);
		registerControl(_button_settings);
		
		_button_exitGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 + 95, 200, 50),
			new Rectangle(0, 100, 200, 50), METRO.__mainMenu_Buttons);
		registerControl(_button_exitGame);
		
		addActionObservations();

		// Create welcome-window:
		_welcomeWindow = new Window("Welcome to METRO - ver.:" + METRO.__VERSION,
			new Point(50, METRO.__SCREEN_SIZE.height / 2 - METRO.__mainMenu_TitleImage.getRegionHeight() / 2 - 300), // same y-pos as title image
			new Point(500, 360));
		
		Button button = new Button(
			new Rectangle((500 - (int)(METRO.__mainMenu_TitleImage.getRegionWidth() * 0.4f)) / 2,
				(260 - (int)(METRO.__mainMenu_TitleImage.getRegionWidth() * 0.4f)) / 2,
				(int)(METRO.__mainMenu_TitleImage.getRegionWidth() * 0.4f),
				(int)(METRO.__mainMenu_TitleImage.getRegionHeight() * 0.4f)),
			new Rectangle(0,
				0,
				METRO.__mainMenu_TitleImage.getRegionWidth(),
				METRO.__mainMenu_TitleImage.getRegionHeight()),
			METRO.__mainMenu_TitleImage, _welcomeWindow);
		registerControl(button);

		Label label = new Label("METRO stands for \"Master of established transport railway operators\" and is a simple Subway/Rapid-Transit and economic simulator."
			+ "\n\nFor all changes take a look into the 'changelog.txt'"
			+ "\nNew main-features of ver." + METRO.__VERSION + ":"
			+ "\n\n   * Dialog for creating a train line"
			+ "\n   * Create, edit and remove lines"
			+ "\n   * Lines are drawn within their color"
			+ "\n\nAnd now: Have fun and earn money ;)",
			new Point(20, 100), 450, _welcomeWindow);
		registerControl(label);
	}

	/**
	 * Creates anonymous inner classes for all buttons (using ActionObserver system) to do their specific action.
	 */
	private void addActionObservations()
	{
		_button_exitGame.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				METRO.__application.exit();
			}
		});
		_button_settings.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				createSettingsWindow();
			}
		});
		_button_startGame.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				_welcomeWindow.close();
				METRO.__changeGameScreen(new MainView());
			}
		});
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		_button_startGame.draw();
		_button_settings.draw();
		_button_exitGame.draw();

		Draw.Image(METRO.__mainMenu_TitleImage,
			METRO.__SCREEN_SIZE.width / 2 - METRO.__mainMenu_TitleImage.getRegionWidth() / 2,
			METRO.__SCREEN_SIZE.height / 2 - METRO.__mainMenu_TitleImage.getRegionHeight() / 2 - 200);
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
	}

	@Override
	public void keyDown(int keyCode)
	{
		if(_welcomeWindow != null) _welcomeWindow.keyPressed(keyCode);
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void reset()
	{
	}
}
