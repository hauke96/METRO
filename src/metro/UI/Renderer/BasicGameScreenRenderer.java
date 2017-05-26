package metro.UI.Renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import juard.Contract;
import metro.AppContext.Locator;
import metro.UI.ContainerRegistrationService;
import metro.UI.Renderable.Container.GameScreen.GameScreenContainer;
import metro.UI.Renderable.Container.GameScreen.GameScreenSwitchedObserver;

/**
 * @author hauke
 * 
 *         A Basic
 */
public class BasicGameScreenRenderer implements GameScreenRenderer, GameScreenSwitchedObserver
{
	private GameScreenContainer _currentGameScreen;
	
	@Override
	public void switchGameScreen(GameScreenContainer gameScreen)
	{
		reactToGameScreenSwitch(gameScreen);
	}
	
	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		_currentGameScreen.updateGameScreen(sp);
	}
	
	@Override
	public void renderUI()
	{
		_currentGameScreen.renderUI();
	}
	
	@Override
	public void reactToGameScreenSwitch(GameScreenContainer newGameScreen)
	{
		Contract.RequireNotNull(newGameScreen);
		
		ContainerRegistrationService registrationService;
		
		// old game screen
		if (_currentGameScreen != null)
		{
			_currentGameScreen.unregisterSwitchedObserver();
			registrationService = _currentGameScreen.getContainerRegistrationService();
		}
		else
		{
			registrationService = Locator.get(ContainerRegistrationService.class);
		}
		
		// new gamescreen
		_currentGameScreen = newGameScreen;
		_currentGameScreen.registerSwitchedObserver(this);
		_currentGameScreen.init(new BasicContainerRenderer(registrationService));
		
		Contract.EnsureNotNull(newGameScreen);
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		return _currentGameScreen.keyDownEvent(keycode);
	}
	
	@Override
	public boolean keyUp(int keycode)
	{
		return _currentGameScreen.keyUpEvent(keycode);
	}
	
	@Override
	public boolean keyTyped(char character)
	{
		return _currentGameScreen.keyTypedEvent(character);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return _currentGameScreen.touchDownEvent(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return _currentGameScreen.touchUpEvent(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean scrolled(int amount)
	{
		return _currentGameScreen.scrolledEvent(amount);
	}
}
