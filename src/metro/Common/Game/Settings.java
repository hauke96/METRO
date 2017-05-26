package metro.Common.Game;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import juard.Contract;
import metro.Common.Technical.Logger;

/**
 * Holds all necessary information that are stored in the settings.cfg file.
 * This class can also load and parse this file by calling {@link #read()}.
 * By calling {@link #set(String, Object)}, the settings will automagically saved into the settings.cfg, so no manual call is necessary.
 * 
 * This class differs between {@code _settings} and {@code _newSettings}. The {@code _newSettings} field holds all settings including the
 * changed ones whereas {@code _settings} holds only the old ones. To get the old settings just call {@link #getOiginal(String)} and to get
 * the new ones call {@link #get(String)}.
 * 
 * @author hauke
 *
 */
public class Settings
{
	public static final String	FULLSCREEN_ON	= "fullscreen.on";
	public static final String	SCREEN_WIDTH	= "screen.width";
	public static final String	SCREEN_HEIGHT	= "screen.height";
	public static final String	USE_OPENGL30	= "use.opengl30";
	public static final String	USE_HDPI		= "use.hdpi";
	public static final String	USE_VSYNC		= "use.vsync";
	public static final String	AMOUNT_SAMPLES	= "amount.samples";
	public static final String	AMOUNT_SEGMENTS	= "amount.segments";
	
	private static final String EOL = "[eol]";
	
	private Map<String, Object> _settings,
			_newSettings;
	
	private String __praeambel;
	
	public Settings()
	{
		__praeambel = "* comments begin with *\n* Settings: [name]=[value]\n";
		_newSettings = new HashMap<String, Object>();
	}
	
	/**
	 * Creates a plain default config file for this system with quite low settings.
	 */
	public void create()
	{
		Contract.RequireNotNull(_newSettings);
		
		set(FULLSCREEN_ON, false);
		set(SCREEN_WIDTH, 1024);
		set(SCREEN_HEIGHT, 768);
		set(USE_OPENGL30, false);
		set(USE_HDPI, false);
		set(USE_VSYNC, true);
		set(AMOUNT_SAMPLES, 4);
		set(AMOUNT_SEGMENTS, 32);
		
		save();
	}
	
	/**
	 * Reads everything from the settings.cfg.
	 */
	public void read()
	{
		File file = new File("./settings.cfg");
		if (!file.exists()) create(); // create default values is no settings file exist
		
		_settings = new HashMap<String, Object>();
		_newSettings = new HashMap<String, Object>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader("./settings.cfg")))
		{
			String str = null, settingsName = null, settingsValue = null;
			str = getNextEntry(reader);
			
			while (!str.equals(EOL)) // end of a line
			{
				if (!str.contains("=") || str.equals("")) continue;
				
				String[] split = str.split("=");
				settingsName = split[0];
				settingsValue = split[1];
				
				_settings.put(settingsName, settingsValue);
				_newSettings.put(settingsName, settingsValue);
				
				str = getNextEntry(reader);
			}
			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the next entry in the settings file.
	 * 
	 * @param reader
	 *            The reader of the file.
	 * @return The next entry of the file.
	 */
	private String getNextEntry(BufferedReader reader)
	{
		String str = null;
		try
		{
			str = reader.readLine();
		}
		catch (IOException e)
		{
			Logger.__error(e.getMessage(), e);
			return EOL;
		}
		
		if (str == null) str = EOL;
		else if (str.charAt(0) == '*' || str.charAt(0) == ' ') return getNextEntry(reader);
		
		return str;
	}
	
	/**
	 * Saves everything to the settings file. A flag is set, when something changed, so there won't be unnecessary writing.
	 */
	public void save()
	{
		File oldFile = new File("./settings.cfg");
		if (oldFile.exists()) oldFile.delete();
		
		if (Boolean.parseBoolean(_newSettings.get(FULLSCREEN_ON).toString())) // if fullscreen is true, the resolution has to be the full-screen resolution
		{
			GraphicsEnvironment gEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = gEnviroment.getScreenDevices();
			
			_newSettings.put(SCREEN_WIDTH, devices[0].getDisplayMode().getWidth());
			_newSettings.put(SCREEN_HEIGHT, devices[0].getDisplayMode().getHeight());
		}
		
		try
		{
			FileWriter writer = new FileWriter("./settings.cfg", true);
			writer.write(__praeambel);
			Object[] keys = _newSettings.keySet().toArray();
			Object[] values = _newSettings.values().toArray();
			for (int i = 0; i < keys.length; ++i)
			{
				writer.write(keys[i] + "=" + values[i] + "\n");
			}
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Returns the original value (the value after starting the game with no changes) of the settings.
	 * 
	 * @param settingsKey
	 *            The key which value you want to have.
	 * @return The value of the key. "" when key does not exist.
	 */
	public Object getOiginal(String settingsKey)
	{
		return _settings.containsKey(settingsKey) ? _settings.get(settingsKey) : "";
	}
	
	/**
	 * Returns the value of the given setting.
	 * 
	 * @param settingsKey
	 *            The key which value you want to have.
	 * @return The value of the key. "" when key does not exist.
	 */
	public Object get(String settingsKey)
	{
		return _newSettings.containsKey(settingsKey) ? _newSettings.get(settingsKey) : "";
	}
	
	/**
	 * Sets a setting to the given value. This method will automagically save the settings, you don't have to call {@code save()} manually.
	 * 
	 * @param key
	 *            The the key of the setting.
	 * @param value
	 *            The new value of the setting.
	 */
	public void set(String key, Object value)
	{
		_newSettings.put(key, value);
		save();
	}
}
