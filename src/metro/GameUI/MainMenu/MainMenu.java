package metro.GameUI.MainMenu;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import metro.METRO;
import metro.GameUI.MainView.MainView;
import metro.GameUI.Screen.GameScreen;
import metro.Graphics.Draw;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.Container.Panel;
import metro.UI.Renderable.Container.Window;
import metro.UI.Renderable.Controls.Button;
import metro.UI.Renderable.Controls.ColorFormattedLabel;
import metro.UI.Renderable.Controls.Label;

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
	private Window _welcomeWindow; // TODO extract window into seperate class
	private static TextureRegion __buttonTextures,
		_titleImageTexture;
	private Panel _panel;

	/**
	 * Creates a main menu with the welcome-window, and the three buttons "Play", "Settings" and "Exit".
	 */
	public MainMenu()
	{
		loadVisuals();

		// Create MainMenu buttons:
		_button_startGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 - 25, 200, 50),
			new Rectangle(0, 0, 200, 50), __buttonTextures);

		_button_settings = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 + 35, 200, 50),
			new Rectangle(0, 50, 200, 50), __buttonTextures);

		_button_exitGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 + 95, 200, 50),
			new Rectangle(0, 100, 200, 50), __buttonTextures);

		addActionObservations();

		// Create welcome-window:
		new WelcomeWindow(_titleImageTexture);

		_panel = new Panel(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 - 25, 200, 170));
		_panel.add(_button_startGame);
		_panel.add(_button_settings);
		_panel.add(_button_exitGame);
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
				exitGameScreen(new MainView());
//				METRO.__changeGameScreen(new MainView());
			}
		});
	}

	protected void exitGameScreen(MainView mainView)
	{
		_panel.close();
		if(_welcomeWindow != null)
		{
			_welcomeWindow.close();
		}
		switchToGameScreen(mainView);
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		Draw.Image(_titleImageTexture,
			METRO.__SCREEN_SIZE.width / 2 - _titleImageTexture.getRegionWidth() / 2,
			METRO.__SCREEN_SIZE.height / 2 - _titleImageTexture.getRegionHeight() / 2 - 200);
	}

	@Override
	public boolean isActive()
	{
		return true;
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
