package metro.UI.Renderable.Container.GameScreen;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Common.Technical.Contract;
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
	private ArrayList<GameScreenSwitchedObservable> _observerList;
	private InputProcessor _gameScreenInputProcessor;
	// TODO setter for input processor where a gameScreenContainer can register itself (or a controller)

	/**
	 * Creates a new GameScreenPanel which is fullscreen and always on top
	 * 
	 * @param renderer The renderer for the controls
	 */
	public GameScreenContainer(ContainerRenderer renderer)
	{
		Contract.RequireNotNull(renderer);

		_area = new Rectangle(0, 0, METRO.__SCREEN_SIZE.width, METRO.__SCREEN_SIZE.height);
		_observerList = new ArrayList<>();
		_containerRenderer = renderer;

		Contract.EnsureNotNull(_area);
		Contract.EnsureNotNull(_observerList);
		Contract.EnsureNotNull(_containerRenderer);
	}

	/**
	 * Renders the UI via the renderer specified in the constructor.
	 */
	public void renderUI()
	{
		_containerRenderer.notifyDraw();
	}

	public abstract void updateGameScreen(SpriteBatch sp);

	public void close()
	{
		throw new UnsupportedOperationException("close() in GameScreenContainer Not Implemented!");
	}

	/**
	 * Adds the given observer to the list. It will be notified when a game screen will be switched.
	 * 
	 * @param observer The observer to add.
	 */
	public void registerSwitchedObserver(GameScreenSwitchedObservable observer)
	{
		Contract.RequireNotNull(observer);

		_observerList.add(observer);
	}

	@Override
	public void reactToGameScreenSwitch(GameScreenContainer newGameScreen)
	{
		Contract.RequireNotNull(newGameScreen);

		notifyAllAboutSwitch(newGameScreen);
	}

	public boolean keyDownEvent(int keycode)
	{
		_containerRenderer.notifyKeyPressed(keycode);
		if(_gameScreenInputProcessor != null)
		{
			_gameScreenInputProcessor.keyDown(keycode);
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
		if(clickProcessed && _gameScreenInputProcessor != null)
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
