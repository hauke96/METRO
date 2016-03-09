package metro.GameScreen;

import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
	private static TextureRegion __buttonTextures,
		_titleImageTexture;

	/**
	 * Creates a main menu with the welcome-window, and the three buttons "Play", "Settings" and "Exit".
	 */
	public MainMenu()
	{
		loadVisuals();

		// Create MainMenu buttons:
		_button_startGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 - 25, 200, 50),
			new Rectangle(0, 0, 200, 50), __buttonTextures);
		registerControl(_button_startGame);

		_button_settings = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 + 35, 200, 50),
			new Rectangle(0, 50, 200, 50), __buttonTextures);
		registerControl(_button_settings);

		_button_exitGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 + 95, 200, 50),
			new Rectangle(0, 100, 200, 50), __buttonTextures);
		registerControl(_button_exitGame);

		addActionObservations();

		// Create welcome-window:
		_welcomeWindow = new Window("Welcome to METRO - v" + METRO.__VERSION,
			new Point(50, METRO.__SCREEN_SIZE.height / 2 - _titleImageTexture.getRegionHeight() / 2 - 300), // same y-pos as title image
			new Point(500, 530));
		registerControl(_welcomeWindow);

		Button button = new Button(
			new Rectangle((500 - (int)(_titleImageTexture.getRegionWidth() * 0.4f)) / 2,
				(260 - (int)(_titleImageTexture.getRegionWidth() * 0.4f)) / 2,
				(int)(_titleImageTexture.getRegionWidth() * 0.4f),
				(int)(_titleImageTexture.getRegionHeight() * 0.4f)),
			new Rectangle(0,
				0,
				_titleImageTexture.getRegionWidth(),
				_titleImageTexture.getRegionHeight()),
			_titleImageTexture, _welcomeWindow);
		registerControl(button);

		Label label = new Label("METRO stands for \"Master of established transport railway operators\" and is a simple Subway/Rapid-Transit and economic simulator.\n\n"
			+ "For all changes take a look into the 'changelog.txt'\n"
			+ "New main-features of v" + METRO.__VERSION + ":\n\n"
			+ " * Trains\n"
			+ "     - Have own speed\n"
			+ "     - Changing direction when reaching the end of a line\n\n" 
			+ "And now: Have fun and earn money ;)",
			new Point(20, 100), 450, _welcomeWindow);
		registerControl(label);
	}

	private void loadVisuals()
	{
		__buttonTextures = new TextureRegion(new Texture(Gdx.files.internal("textures/MainMenu_Buttons.png")));
		__buttonTextures.flip(false, true);

		_titleImageTexture = new TextureRegion(new Texture(Gdx.files.internal("textures/MainMenu_TitleImage.png")));
		_titleImageTexture.flip(false, true);
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
				METRO.__exit();
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

		Draw.Image(_titleImageTexture,
			METRO.__SCREEN_SIZE.width / 2 - _titleImageTexture.getRegionWidth() / 2,
			METRO.__SCREEN_SIZE.height / 2 - _titleImageTexture.getRegionHeight() / 2 - 200);
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

	@Override
	public boolean isHovered()
	{
		Point mPos = METRO.__mousePosition;
		return _button_exitGame.getArea().contains(mPos)
			|| _button_settings.getArea().contains(mPos)
			|| _button_startGame.getArea().contains(mPos);
	}
}
