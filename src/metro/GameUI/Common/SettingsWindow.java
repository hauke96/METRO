package metro.GameUI.Common;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.MessageFormat;

import metro.METRO;
import metro.Common.Game.Settings;
import metro.Common.Technical.Logger;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.CloseObservable;
import metro.UI.Renderable.Container.Window;
import metro.UI.Renderable.Controls.Button;
import metro.UI.Renderable.Controls.Checkbox;
import metro.UI.Renderable.Controls.Label;
import metro.UI.Renderable.Controls.List;
import metro.UI.Renderer.CloseObserver;

/**
 * This is the settings window, that provides some options to configure.
 * These options are: Resolution, fullscreen, OpenGL settings, amount of segments and amount of samples.
 * 
 * @author hauke
 *
 */
public class SettingsWindow
{
	private static SettingsWindow __INSTANCE;
	
	private Window _window;
	private Button _okButton;
	private Checkbox _fullscreenOn,
		_useOpenGL30,
		_useVSync,
		_useHDPI;
	private List _resolutionList,
		_sampleList,
		_segmentList;
	private Settings _settings = Settings.getInstance();
	
	/**
	 * Shows the settings window. There'll be only one instance.
	 */
	public static void show()
	{
		if(__INSTANCE == null)
		{
			__INSTANCE = new SettingsWindow();
		}
		
		__INSTANCE._window.setVisibility(true);
	}

	/**
	 * Creates a settings window.
	 */
	private SettingsWindow()
	{
		_window = new Window("METRO settings",
			new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 50, METRO.__SCREEN_SIZE.height / 2 - 225, 500, 450),
			METRO.__metroBlue);
		registerWindowCloseObserver();

		Label label = new Label("To make things easier, you don't need to click on \"save\". Everything will be saved in realtime by just changing settings.",
			new Point(20, 20),
			460);
		_window.add(label);

		_okButton = new Button(new Rectangle(200, 420, 100, 20),
			"OK");
		_window.add(_okButton);
		addButtonObserver();

		_fullscreenOn = new Checkbox(new Point(20, 70), "Fullscreen", Boolean.parseBoolean(_settings.get("fullscreen.on").toString()), true);
		_useOpenGL30 = new Checkbox(new Point(20, 90), "Use OpenGL 3.0", Boolean.parseBoolean(_settings.get("use.opengl30").toString()), true);
		_useVSync = new Checkbox(new Point(20, 110), "Enable VSync", Boolean.parseBoolean(_settings.get("use.vsync").toString()), true);
		_useHDPI = new Checkbox(new Point(20, 130), "Enable HDPI", Boolean.parseBoolean(_settings.get("use.hdpi").toString()), true);
		_window.add(_fullscreenOn);
		_window.add(_useOpenGL30);
		_window.add(_useVSync);
		_window.add(_useHDPI);
		addCheckboxObserver();

		label = new Label("Screen Resolution:", new Point(20, 180));
		_window.add(label);
		_resolutionList = new List(new Rectangle(20, 200, 190, 150), true);
		_resolutionList.addElement("1920x1200");
		_resolutionList.addElement("1920x1080");
		_resolutionList.addElement("1600x900");
		_resolutionList.addElement("1440x900");
		_resolutionList.addElement("1366x768");
		_resolutionList.addElement("1360x768");
		_resolutionList.addElement("1280x768");
		_resolutionList.addElement("1280x720");
		_resolutionList.addElement("1024x768");
		if(Boolean.parseBoolean(_settings.get("fullscreen.on").toString())) _resolutionList.setState(false);
		int index = _resolutionList.getIndex(Integer.parseInt(_settings.get("screen.width").toString()) + "x" + Integer.parseInt(_settings.get("screen.width").toString())); // get the entry with the current resolution
		_resolutionList.setSelectedEntry(index);
		_window.add(_resolutionList);

		label = new Label("Amount Samples:", new Point(240, 180));
		_window.add(label);
		_sampleList = new List(new Rectangle(240, 200, 90, 150), true);
		_sampleList.addElement("0");
		_sampleList.addElement("2");
		_sampleList.addElement("4");
		_sampleList.addElement("8");
		_sampleList.addElement("16");
		index = _sampleList.getIndex(_settings.get("amount.samples") + ""); // get the entry with the current resolution
		_sampleList.setSelectedEntry(index);
		_window.add(_sampleList);

		label = new Label("Amount Segments:", new Point(360, 180));
		_window.add(label);
		_segmentList = new List(new Rectangle(360, 200, 90, 150), true);
		_segmentList.addElement("6");
		_segmentList.addElement("16");
		_segmentList.addElement("24");
		_segmentList.addElement("32");
		_segmentList.addElement("64");
		_segmentList.addElement("128");
		_segmentList.addElement("256");
		index = _segmentList.getIndex(_settings.get("amount.segments") + ""); // get the entry with the current resolution
		_segmentList.setSelectedEntry(index);
		_window.add(_segmentList);

		addListObserver();
	}

	/**
	 * Adds the observer for the "ok" button that closes the window.
	 */
	private void addButtonObserver()
	{
		_okButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				_window.close();
			}
		});
	}

	/**
	 * Creates the observer with sub-classes for all the check-boxes of this class.
	 */
	private void addCheckboxObserver()
	{
		_fullscreenOn.register(new ActionObserver()
		{
			@Override
			public void checkStateChanged(boolean newState)
			{
				_settings.set("fullscreen.on", _fullscreenOn.isChecked());
				_resolutionList.setState(!(Boolean.parseBoolean(_settings.get("fullscreen.on").toString())));
			}
		});
		_useOpenGL30.register(new ActionObserver()
		{
			@Override
			public void checkStateChanged(boolean newState)
			{
				_settings.set("use.opengl30", _useOpenGL30.isChecked());
			}
		});
		_useOpenGL30.register(new ActionObserver()
		{
			@Override
			public void checkStateChanged(boolean newState)
			{
				_settings.set("use.vsync", _useVSync.isChecked());
			}
		});
		_useOpenGL30.register(new ActionObserver()
		{
			@Override
			public void checkStateChanged(boolean newState)
			{
				_settings.set("use.hdpi", _useHDPI.isChecked());
			}
		});
	}

	/**
	 * Add observer for all lists (resolution, samples and segments)
	 */
	private void addListObserver()
	{
		_resolutionList.register(new ActionObserver()
		{
			@Override
			public void selectionChanged(String entry)
			{
				if(entry != null && !Boolean.parseBoolean(_settings.get("fullscreen.on").toString())) // ... and fullscreen-mode is off
				{
					Logger.__debug(MessageFormat.format("Old res.: {0}x{1} -- New res.: {2}",
						_settings.get("screen.width"),
						_settings.get("screen.height"),
						_resolutionList.getText()));

					String splitted[] = entry.split("x");
					if(splitted.length == 2)
					{
						_settings.set("screen.width", Integer.parseInt(splitted[0]));
						_settings.set("screen.height", Integer.parseInt(splitted[1]));
					}
				}
			}
		});
		_sampleList.register(new ActionObserver()
		{
			@Override
			public void selectionChanged(String entry)
			{
				if(entry != null && !entry.equals("")) _settings.set("amount.samples", Integer.parseInt(entry));
			}
		});
		_segmentList.register(new ActionObserver()
		{
			@Override
			public void selectionChanged(String entry)
			{
				if(entry != null && !entry.equals("")) _settings.set("amount.segments", Integer.parseInt(entry));
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