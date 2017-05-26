package metro.GameUI.MainMenu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.AppContext.Locator;
import metro.Common.Game.Settings;
import metro.GameUI.Common.SettingsWindow;
import metro.GameUI.MainView.MainView;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.Container.GameScreen.GameScreenContainer;

/**
 * The main menu is the first menu you'll see after starting the game. It provides some basic options like start, exit and settings.
 * 
 * @author Hauke
 * 
 */

public class MainMenuTool extends GameScreenContainer
{
	private MainMenuView	_mainMenuView;
	private Settings		_settings;
	
	/**
	 * Creates a main menu with the welcome-window, and the three buttons "Play", "Settings" and "Exit".
	 * 
	 * @param settings
	 *            The settings object with game settings.
	 */
	public MainMenuTool(Settings settings)
	{
		_settings = settings;
		
		_mainMenuView = new MainMenuView();
		_mainMenuView.loadVisuals();
	}
	
	@Override
	protected void initializeUi()
	{
		_mainMenuView.initializeUi();
		addActionObservations();
	}
	
	/**
	 * Creates anonymous inner classes for all buttons (using ActionObserver system) to do their specific action.
	 */
	private void addActionObservations()
	{
		_mainMenuView.getExitButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				METRO.__exit();
			}
		});
		_mainMenuView.getSettingsButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				SettingsWindow.show(_settings);
			}
		});
		_mainMenuView.getStartGameButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				exitGameScreen(Locator.get(MainView.class));
			}
		});
	}
	
	protected void exitGameScreen(GameScreenContainer newContainer)
	{
		_mainMenuView.close();
		
		notifyAllAboutSwitch(newContainer);
	}
	
	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
	}
}
