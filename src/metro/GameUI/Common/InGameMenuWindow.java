package metro.GameUI.Common;

import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.CloseObservable;
import metro.UI.Renderable.Container.Window;
import metro.UI.Renderable.Controls.Button;
import metro.UI.Renderable.Controls.Label;
import metro.UI.Renderer.CloseObserver;

/**
 * This is the menu that'll appear after pressing ESC during the game.
 * It provides exit options and a settings button.
 * This window will ask the user if he wants to quit and the user can choose between "yes" and "no".
 * There's also a "settings" button that will open the settings window.
 * 
 * @author hauke
 *
 */
public class InGameMenuWindow
{
	private static InGameMenuWindow __INSTANCE;
	
	private Window _window;
	private Button _yesButton,
		_noButton,
		_settingsButton;
	
	/**
	 * Shows the InGameMenuWindow. There'll be only one instance.
	 */
	public static void show()
	{
		if(__INSTANCE == null)
		{
			__INSTANCE = new InGameMenuWindow();
		}
		
		__INSTANCE._window.setVisibility(true);
	}

	/**
	 * Creates a new exit game window with a settings button.
	 */
	private InGameMenuWindow()
	{
		_window = new Window("Really quit?",
			new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 200,
				METRO.__SCREEN_SIZE.height / 2 - 50,
				400,
				100),
			METRO.__metroRed);
		registerWindowCloseObserver();

		_yesButton = new Button(new Rectangle(10, 70, 120, 20), "Yes");
		_settingsButton = new Button(new Rectangle(140, 70, 120, 20), "Settings");
		_noButton = new Button(new Rectangle(270, 70, 120, 20), "No");
		addButtonObserver();

		Label label = new Label("Really quit METRO? Or go into settings?",
			new Point(200 - (Draw.getStringSize("Really quit METRO? Or go into settings?").width) / 2, 25));

		_window.add(label);
		_window.add(_yesButton);
		_window.add(_settingsButton);
		_window.add(_noButton);
	}

	/**
	 * Creates observer for all buttons ("yes", "no" and "settings").
	 */
	private void addButtonObserver()
	{
		_yesButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				METRO.__exit();
			}
		});
		_settingsButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				SettingsWindow.show();
				_window.close();
			}
		});
		_noButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				_window.close();
			}
		});
	}

	/**
	 * Just closes the window and sets the _settingsWindow to null.
	 */
	private void registerWindowCloseObserver()
	{
		_window.registerCloseObserver(new CloseObserver()
		{
			@Override
			public void reactToClosedControlElement(CloseObservable container)
			{
				__INSTANCE = null;
			}
		});
	}
}