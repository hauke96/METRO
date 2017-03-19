package metro.GameUI.MainMenu;

import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.UI.Renderable.ActionObservable;
import metro.UI.Renderable.Container.Panel;
import metro.UI.Renderable.Container.Window;
import metro.UI.Renderable.Controls.Button;
import metro.UI.Renderable.Controls.Canvas;
import metro.UI.Renderable.Controls.Canvas.CanvasPainter;

public class MainMenuView
{
	private Button _button_startGame,
		_button_settings,
		_button_exitGame;
	private Window _welcomeWindow;
	private static TextureRegion __buttonTextures,
		_titleImageTexture;
	private Panel _panel;
	private Canvas _titleImageCanvas;

	void initializeUi()
	{
		// Create MainMenuTool buttons:
		_button_startGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 - 25, 200, 50),
			new Rectangle(0, 0, 200, 50), __buttonTextures);

		_button_settings = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 + 35, 200, 50),
			new Rectangle(0, 50, 200, 50), __buttonTextures);

		_button_exitGame = new Button(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 + 95, 200, 50),
			new Rectangle(0, 100, 200, 50), __buttonTextures);

		_titleImageCanvas = new Canvas(new Point(
			METRO.__SCREEN_SIZE.width / 2 - _titleImageTexture.getRegionWidth() / 2,
			METRO.__SCREEN_SIZE.height / 2 - _titleImageTexture.getRegionHeight() / 2 - 200));
		_titleImageCanvas.setPainter(new CanvasPainter()
		{
			@Override
			public void paint()
			{
				Draw.Image(_titleImageTexture, 0, 0);
			}
		});

		// Create welcome-window:
		_welcomeWindow = new WelcomeWindow(_titleImageTexture);

		_panel = new Panel(new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 100, METRO.__SCREEN_SIZE.height / 2 - 25, 200, 170));
		_panel.add(_button_startGame);
		_panel.add(_button_settings);
		_panel.add(_button_exitGame);
	}

	void loadVisuals()
	{
		__buttonTextures = new TextureRegion(new Texture(Gdx.files.internal("textures/MainMenu_Buttons.png")));
		__buttonTextures.flip(false, true);

		_titleImageTexture = new TextureRegion(new Texture(Gdx.files.internal("textures/MainMenu_TitleImage.png")));
		_titleImageTexture.flip(false, true);
	}

	void close()
	{
		_panel.close();
		if(_welcomeWindow != null)
		{
			_welcomeWindow.close();
		}
	}

	ActionObservable getExitButton()
	{
		return _button_exitGame;
	}

	ActionObservable getSettingsButton()
	{
		return _button_settings;
	}

	ActionObservable getStartGameButton()
	{
		return _button_startGame;
	}
}
