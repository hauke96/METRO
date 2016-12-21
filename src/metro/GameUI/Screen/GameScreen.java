package metro.GameUI.Screen;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.MessageFormat;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Common.Game.Settings;
import metro.Common.Technical.Logger;
import metro.GameUI.Common.InGameMenuWindow;
import metro.GameUI.Common.SettingsWindow;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.CloseObservable;
import metro.UI.Renderable.Container.Window;
import metro.UI.Renderable.Controls.Button;
import metro.UI.Renderable.Controls.Checkbox;
import metro.UI.Renderable.Controls.Label;
import metro.UI.Renderable.Controls.List;
import metro.UI.Renderer.CloseObserver;

/**
 * Every Menu or Game Sreen has to implement this interface for start() and update(). This will make the creation process more easy.
 * 
 * @author Hauke
 * 
 */

public abstract class GameScreen extends GameScreenSwitchedObservable
{
	/**
	 * Updates the actual game screen.
	 * 
	 * @param sp SpriteBatch to draw on.
	 */
	public abstract void updateGameScreen(SpriteBatch sp);

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

	/**
	 * When a key was pressed.
	 * 
	 * @param keyCode Key number from Gdx.Input
	 */
	public void keyPressed(int keyCode)
	{
		if(keyCode == Keys.ESCAPE) // Show in game window if no input control and no other window is focused/open.
		{
			createMenuWindow();
		}
		else
		{
			keyDown(keyCode);
		}
	}

	/**
	 * Forwards the key up event to the active input control.
	 * 
	 * @param keyCode Key number from Gdx.Input
	 */
	public void keyUp(int keyCode)
	{
	}

	/**
	 * When a key was pressed AND it has been checked weather the ESC key for the ingame menu window has been pressed.
	 * 
	 * @param keyCode Key number from Gdx.Input
	 */
	public void keyDown(int keyCode)
	{
	}

	/**
	 * Fires when user scrolls.
	 * 
	 * @param amount Positive or negative amount of steps since last frame.
	 */
	public void mouseScrolled(int amount)
	{
	}

	/**
	 * Resets the game screen to its default values.
	 */
	public void reset()
	{
	}

	/**
	 * Creates the in-game menu window with the yes/no option for exiting the game but provides a settings button as well.
	 */
	public void createMenuWindow()
	{
		InGameMenuWindow.show();
		// if(_inGameMenuWindow == null) _inGameMenuWindow = new InGameMenuWindow();
	}

	/**
	 * Create a settings menu with some options to configure METRO.
	 */
	public void createSettingsWindow()
	{
		SettingsWindow.show();
		// if(_settingsWindow == null) _settingsWindow = new SettingsWindow();
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
