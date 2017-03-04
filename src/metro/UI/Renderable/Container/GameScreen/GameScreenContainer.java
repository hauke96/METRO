package metro.UI.Renderable.Container.GameScreen;

import java.awt.Rectangle;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.AppContext.ServiceLocator;
import metro.Common.Game.Settings;
import metro.Common.Technical.Contract;
import metro.GameUI.Common.InGameMenuWindow;
import metro.UI.ContainerRegistrationService;
import metro.UI.Renderer.ContainerRenderer;

/**
 * A GameScreenContainer is a fullscreen always-on-top container that contains all controls that are usable right now.
 * 
 * @author hauke
 *
 */
public abstract class GameScreenContainer extends GameScreenSwitchedObservable implements GameScreenSwitchedObserver
{
	private ContainerRenderer _containerRenderer;
	private Rectangle _area;
	private InputProcessor _gameScreenInputProcessor;
	private Settings _settings;

	// TODO setter for input processor where a gameScreenContainer can register itself (or a controller)

	/**
	 * Creates a new GameScreenPanel which is full screen and always on top
	 */
	public GameScreenContainer()
	{
		 // directly use ServiceLocator for convenience (always calling "super(settings);" in sub-classes is unhandy.
		_settings = ServiceLocator.get(Settings.class);
		_area = new Rectangle(0, 0, METRO.__SCREEN_SIZE.width, METRO.__SCREEN_SIZE.height);

		Contract.EnsureNotNull(_area);
	}

	/**
	 * Initializes this class with the container renderer.
	 * 
	 * @param renderer The container renderer.
	 */
	public void init(ContainerRenderer renderer)
	{
		Contract.RequireNotNull(renderer);

		_containerRenderer = renderer;
		
		initializeUi();

		Contract.EnsureNotNull(_containerRenderer);
	}

	/**
	 * This method will create the UI of the game screen. Creating the UI in the constructor won't work (renderer not set there).
	 */
	protected abstract void initializeUi();

	public void setInputProcessor(InputProcessor processor)
	{
		_gameScreenInputProcessor = processor;
	}
	
	public ContainerRegistrationService getContainerRegistrationService()
	{
		return _containerRenderer.getRegistrationService();
	}

	/**
	 * Renders the UI via the renderer specified in the constructor.
	 */
	public void renderUI()
	{
		Contract.RequireNotNull(_containerRenderer);

		_containerRenderer.notifyDraw();
	}

	public abstract void updateGameScreen(SpriteBatch sp);

	@Override
	public void reactToGameScreenSwitch(GameScreenContainer newGameScreen)
	{
		Contract.RequireNotNull(newGameScreen);

		notifyAllAboutSwitch(newGameScreen);
	}

	public boolean keyDownEvent(int keycode)
	{
		if(keycode == Keys.ESCAPE) // Show in game window if no input control and no other window is focused/open.
		{
			InGameMenuWindow.show(_settings);
		}
		else
		{
			_containerRenderer.notifyKeyPressed(keycode);
			if(_gameScreenInputProcessor != null)
			{
				_gameScreenInputProcessor.keyDown(keycode);
			}
		}
		return true;
	}

	public boolean keyUpEvent(int keycode)
	{
		_containerRenderer.notifyKeyUp(keycode);
		if(_gameScreenInputProcessor != null)
		{
			_gameScreenInputProcessor.keyUp(keycode);
		}
		return true;
	}

	public boolean keyTypedEvent(char character)
	{
		if(_gameScreenInputProcessor != null)
		{
			_gameScreenInputProcessor.keyTyped(character);
		}
		return true;
	}

	public boolean touchDownEvent(int screenX, int screenY, int pointer, int button)
	{
		boolean clickProcessed = _containerRenderer.notifyMouseClick(screenX, screenY, button);
		if(!clickProcessed && _gameScreenInputProcessor != null)
		{
			_gameScreenInputProcessor.touchDown(screenX, screenY, pointer, button);
		}
		return true;
	}

	public boolean touchUpEvent(int screenX, int screenY, int pointer, int button)
	{
		_containerRenderer.notifyMouseReleased(screenX, screenY, button);
		if(_gameScreenInputProcessor != null)
		{
			_gameScreenInputProcessor.touchUp(screenX, screenY, pointer, button);
		}
		return true;
	}

	public boolean touchDraggedEvent(int screenX, int screenY, int pointer)
	{
		if(_gameScreenInputProcessor != null)
		{
			_gameScreenInputProcessor.touchDragged(screenX, screenY, pointer);
		}
		return true;
	}

	public boolean mouseMovedEvent(int screenX, int screenY)
	{
		if(_gameScreenInputProcessor != null)
		{
			_gameScreenInputProcessor.mouseMoved(screenX, screenY);
		}
		return true;
	}

	public boolean scrolledEvent(int amount)
	{
		_containerRenderer.notifyMouseScrolled(amount);
		if(_gameScreenInputProcessor != null)
		{
			_gameScreenInputProcessor.scrolled(amount);
		}
		return true;
	}
}
