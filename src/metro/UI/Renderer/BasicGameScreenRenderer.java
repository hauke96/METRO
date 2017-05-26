package metro.UI.Renderer;

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
	public void switchTo(GameScreenContainer gameScreen)
	{
		reactToGameScreenSwitch(gameScreen);
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
		
		if (_currentGameScreen != null)
		{
			_currentGameScreen.unregisterSwitchedObserver();
			
			newGameScreen.init(_currentGameScreen.getContainerRenderer());
		}
		else
		{
			ContainerRegistrationService registrationService = Locator.get(ContainerRegistrationService.class);
			
			// TODO resolve the BasicContainerRenderer, this also ensures that there'll be only one instance
			newGameScreen.init(new BasicContainerRenderer(registrationService));
		}
		
		newGameScreen.registerSwitchedObserver(this);
		
		_currentGameScreen = newGameScreen;
		
		Contract.EnsureNotNull(_currentGameScreen);
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
