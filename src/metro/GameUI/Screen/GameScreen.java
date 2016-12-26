package metro.GameUI.Screen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.Common.Technical.Logger;
import metro.GameUI.Common.InGameMenuWindow;
import metro.GameUI.Common.SettingsWindow;

/**
 * Every Menu or Game Sreen has to implement this interface for start() and update(). This will make the creation process more easy.
 * 
 * @author Hauke
 * 
 */

public abstract class GameScreen extends GameScreenSwitchedObservable
{
	/**
	 * When mouse has clicked
	 * 
	 * @param screenX The x-position on the screen
	 * @param screenY The y-position on the screen
	 * @param mouseButton The number of the button like Buttons.LEFT
	 */
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
	}

	/**
	 * When mouse has been released.
	 * 
	 * @param mouseButton The number of the button like Buttons.LEFT
	 */
	public void mouseReleased(int mouseButton)
	{
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj.getClass().equals(this.getClass());
	}

	/**
	 * When a game screen is active it can be used, otherwise it'll be deleted, ignored, ...
	 * 
	 * @return True when active and usable, false when inactive, closed, ...
	 */
	public abstract boolean isActive();

	/**
	 * @return True when mouse is in sensible/important area of the game screen.
	 */
	public abstract boolean isHovered();

	/**
	 * Closes the game screen by removing all controls from the game screen.
	 * Normally this method is called via METRO.closeGameScreen(GameScreen) to use the correct control manager.
	 */
	public void close()
	{
		Logger.__debug("Closed game screen " + this);
		Logger.__debug("Amount observer: " + countObservers());
		deleteObservers();
	}

	/**
	 * Switches to the given screen.
	 * 
	 * @param newGameScreen The new game screen which will then be displayed.
	 */
	public void switchToGameScreen(GameScreen newGameScreen)
	{
		notifyAllAboutSwitch(newGameScreen);
	}
}
