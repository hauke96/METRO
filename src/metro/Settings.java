package metro;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.*;
import java.util.*;

public class Settings
{
	private static java.util.Map<String, Object> _settings,
		_newSettings;
	private static boolean _newSettingsHaveChanged = false; // true when something changed and has to be saved
	
	private static String __praeambel = "* comments begin with *\n* Settings: [name]=[value]\n"; 
	
	/**
	 * Reads everything from the settings.cfg
	 */
	public static void read()
	{
		_settings = new HashMap<String, Object>();
		_newSettings = new HashMap<String, Object>();
		
		try(BufferedReader reader = new BufferedReader(new FileReader("./settings.cfg")))
		{
			String str = null,
				settingsName = null,
				settingsValue = null;
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
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the next entry in the settings file.
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
		catch (IOException e){}
		
		if(str == null) return "[eol]";
		if(str.charAt(0) == '*' || str.charAt(0) == ' ') return getNextEntry(reader);
		return str;
	}
	
	/**
	 * Saves everything to the settings file.
	 * A flag is set, when something changed, so there won't be unnecessary writing.
	 */
	public static void save()
	{
		if(!_newSettingsHaveChanged) return; // when nothing changed
		
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
			for(int i = 0; i < keys.length; i++)
			{
				writer.write(keys[i] + "=" + values[i] + "\n");
				System.out.println(keys[i] + "=" + values[i]);
			}
			System.out.println("\n");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Returns the value of the key.
	 * @param settingsKey The key which value you want to have.
	 * @return The value of the key. "" when key does not exist
	 */
	public static Object get(String settingsKey)
	{
		return _settings.containsKey(settingsKey) ? _settings.get(settingsKey) : "";
	}
	
	public static Object getNew(String settingsKey)
	{
		return _newSettings.containsKey(settingsKey) ? _newSettings.get(settingsKey) : "";
	}

	public static void set(String key, Object value)
	{
		_newSettings.put(key, value);
		_newSettingsHaveChanged = true;
	}
}
