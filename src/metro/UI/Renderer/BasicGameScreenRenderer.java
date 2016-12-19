package metro.UI.Renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.Common.Technical.Contract;
import metro.UI.Renderable.Container.GameScreen.GameScreenContainer;

/**
 * @author hauke
 * 
 * A Basic 
 */
public class BasicGameScreenRenderer implements GameScreenRenderer
{
	private GameScreenContainer _currentGameScreen;
	
	@Override
	public void switchGameScreen(GameScreenContainer gameScreen)
	{
		Contract.RequireNotNull(gameScreen);
		_currentGameScreen = gameScreen;
		Contract.EnsureNotNull(_currentGameScreen);
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
		_currentGameScreen = newGameScreen;
		Contract.EnsureNotNull(_currentGameScreen);
	}

	@Override
	public boolean keyDown(int keycode)
	{
		_currentGameScreen.keyDownEvent(keycode);
		return true;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		_currentGameScreen.keyUpEvent(keycode);
		return true;
	}

	@Override
	public boolean keyTyped(char character)
	{
		_currentGameScreen.keyTypedEvent(character);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		_currentGameScreen.touchDownEvent(screenX, screenY, pointer, button);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		_currentGameScreen.touchUpEvent(screenX, screenY, pointer, button);
		return true;
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
		_currentGameScreen.scrolledEvent(amount);
		return true;
	}
}
