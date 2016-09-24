package metro.GameUI.Screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.Common.Technical.Contract;
import metro.UI.Renderable.Controls.InputField;

/**
 * This manager has the current game screen and manages input and switching.
 * 
 * @author hauke
 *
 */
public class CurrentGameScreenManager implements GameScreenSwitchedObserver, InputProcessor
{
	private GameScreen _currentGameScreen;
	private static InputField __selectedInput = null;

	@Override
	public void reactToGameScreenSwitch(GameScreen newGameScreen)
	{
		switchToGameScreen(newGameScreen);
	}

	/**
	 * Switches to the given screen. If there's an old screen, the close method will be called.
	 * 
	 * @param newGameScreen The new game screen which will be displayed.
	 */
	public void switchToGameScreen(GameScreen newGameScreen)
	{
		if(_currentGameScreen != null)
		{
			_currentGameScreen.close();
		}
		_currentGameScreen = newGameScreen;
		newGameScreen.registerSwitchedObserver(this);
	}

	/**
	 * Will render the current game screen.
	 * 
	 * @param sp The sprite batch to draw on.
	 */
	public void renderCurrentGameScreen(SpriteBatch sp)
	{
		Contract.RequireNotNull(_currentGameScreen);

		_currentGameScreen.updateGameScreen(sp);
	}

	@Override
	public boolean keyDown(int keyCode)
	{
		Contract.RequireNotNull(_currentGameScreen);
		_currentGameScreen.keyDown(keyCode);
		return false;
	}

	@Override
	public boolean keyUp(int keyCode)
	{
		Contract.RequireNotNull(_currentGameScreen);
		_currentGameScreen.keyUp(keyCode);
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int mouseButton)
	{
		Contract.RequireNotNull(_currentGameScreen);
		_currentGameScreen.mouseClicked(screenX, screenY, mouseButton);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int mouseButton)
	{
		Contract.RequireNotNull(_currentGameScreen);
		_currentGameScreen.mouseReleased(mouseButton);
		return false;
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
		Contract.RequireNotNull(_currentGameScreen);
		_currentGameScreen.mouseScrolled(amount);
		return false;
	}

	/**
	 * Sets the selected field to a new one. Reset selected field with null as parameter. The Input-component will be informed about the change.
	 * 
	 * @param field The selected input component.
	 */
	public void setSelectedInput(InputField field)
	{
		if(field != null) field.select();
		else if(__selectedInput != null) __selectedInput.disselect();
		__selectedInput = field;
	}
}
