package metro;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.*;
import java.util.*;

/**
 * Holds all necessary information that are stored in the settings.cfg file.
 * This class can also load and parse this file by calling {@code read()}.
 * By calling {@code set(String, Object)}, the settings will automagically saved into the settings.cfg, so no manual call is necessary.
 * 
 * This class differes between {@code _settings} and {@code _newSettings}. The {@code _newSettings} fiel holds all settings including the
 * changed ones whereas {@code _settings} holds only the old ones. To get the old settings just call {@code getOld(String)} and to get
 * the new ones call {@code get(String)}.
 * 
 * @author hauke
 *
 */
public class Settings
{
	private static Map<String, Object> _settings,
		_newSettings;

	private static String __praeambel = "* comments begin with *\n* Settings: [name]=[value]\n";

	/**
	 * Creates a plain default config file for this system.
	 */
	public static void create()
	{
		if(_newSettings == null) _newSettings = new HashMap<String, Object>();

		set("fullscreen.on", true);
		GraphicsEnvironment gEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = gEnviroment.getScreenDevices();
		set("screen.width", devices[0].getDisplayMode().getWidth());
		set("screen.height", devices[0].getDisplayMode().getHeight());
		set("use.opengl30", true);
		set("use.hdpi", false);
		set("use.vsync", true);
		set("amount.samples", 8);
		set("amount.segments", 64);

		save();
	}

	/**
	 * Reads everything from the settings.cfg
	 */
	public static void read()
	{
		File file = new File("./settings.cfg");
		if(!file.exists()) create(); // create default values is no settings file exist

		_settings = new HashMap<String, Object>();
		_newSettings = new HashMap<String, Object>();

		try(BufferedReader reader = new BufferedReader(new FileReader("./settings.cfg")))
		{
			String str = null, settingsName = null, settingsValue = null;
			str = getNextEntry(reader);

			while(!str.equals("[eol]")) // end of a line
			{
				if(!str.contains("=") || str.equals("")) continue;

				String[] split = str.split("=");
				settingsName = split[0];
				settingsValue = split[1];

				_settings.put(settingsName, settingsValue);
				_newSettings.put(settingsName, settingsValue);

				str = getNextEntry(reader);
			}
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns the next entry in the settings file.
	 * 
	 * @param reader The reader of the file.
	 * @return The next entry of the file.
	 */
	private static String getNextEntry(BufferedReader reader)
	{
		String str = null;
		try
		{
			str = reader.readLine();
		}
		catch(IOException e)
		{
		}

		if(str == null) str = "[eol]";
		else if(str.charAt(0) == '*' || str.charAt(0) == ' ') return getNextEntry(reader);

		return str;
	}

	/**
	 * Saves everything to the settings file. A flag is set, when something changed, so there won't be unnecessary writing.
	 */
	public static void save()
	{
		File oldFile = new File("./settings.cfg");
		if(oldFile.exists()) oldFile.delete();

		if(Boolean.parseBoolean(_newSettings.get("fullscreen.on").toString())) // if fullscreen is true, the resolution has to be the full-screen resolution
		{
			GraphicsEnvironment gEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = gEnviroment.getScreenDevices();

			_newSettings.put("screen.width", devices[0].getDisplayMode().getWidth());
			_newSettings.put("screen.height", devices[0].getDisplayMode().getHeight());
		}

		try
		{
			FileWriter writer = new FileWriter("./settings.cfg", true);
			writer.write(__praeambel);
			Object[] keys = _newSettings.keySet().toArray();
			Object[] values = _newSettings.values().toArray();
			for(int i = 0; i < keys.length; ++i)
			{
				writer.write(keys[i] + "=" + values[i] + "\n");
			}
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Returns the original value (the value after starting the game with no changes) of the settings.
	 * 
	 * @param settingsKey The key which value you want to have.
	 * @return The value of the key. "" when key does not exist.
	 */
	public static Object getOld(String settingsKey)
	{
		return _settings.containsKey(settingsKey) ? _settings.get(settingsKey) : "";
	}

	/**
	 * Returns the value of the given setting.
	 * 
	 * @param settingsKey The key which value you want to have.
	 * @return The value of the key. "" when key does not exist.
	 */
	public static Object get(String settingsKey)
	{
		return _newSettings.containsKey(settingsKey) ? _newSettings.get(settingsKey) : "";
	}

	/**
	 * Sets a setting to the given value. This method will automagically save the settings, you don't have to call {@code save()} manually.
	 * 
	 * @param key The the key of the setting.
	 * @param value The new value of the setting.
	 */
	public static void set(String key, Object value)
	{
		_newSettings.put(key, value);
		save();
	}
}
