package metro.UI.Renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.Common.Technical.Contract;
import metro.GameUI.Screen.GameScreen;

/**
 * @author hauke
 * 
 * A Basic 
 */
public class BasicGameScreenRenderer implements GameScreenRenderer
{
	private GameScreen _currentGameScreen;
	
	@Override
	public void switchGameScreen(GameScreen gameScreen)
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
	public void reactToGameScreenSwitch(GameScreen newGameScreen)
	{
		Contract.RequireNotNull(newGameScreen);
		_currentGameScreen = newGameScreen;
		Contract.EnsureNotNull(_currentGameScreen);
	}

	@Override
	public boolean keyDown(int keycode)
	{
		_currentGameScreen.keyDown(keycode);
		return true;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		_currentGameScreen.keyUp(keycode);
		return true;
	}

	@Override
	public boolean keyTyped(char character)
	{
		_currentGameScreen.keyPressed(character);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		_currentGameScreen.mouseClicked(screenX, screenY, button);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		_currentGameScreen.mouseReleased(button);
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
		_currentGameScreen.mouseScrolled(amount);
		return true;
	}
}
