package metro;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.*;

public class Settings
{
	private static boolean _fullscreen,
		_useopengl30,
		_usevsync,
		_usehdpi;
	private static int _screenwidth, 
		_screenheight,
		_amountsamples,
		_amountsegments;
	
	private static boolean _new_fullscreen,
		_new_useopengl30,
		_new_usevsync,
		_new_usehdpi;
	private static int _new_screenwidth, 
		_new_screenheight,
		_new_amountsamples,
		_new_amountsegments;
	
	//TODO create variables for "new" settings (used after restart)
	
	private static String __praeambel = "* comments begin with *\n* Settings: [name]=[value]\n"; 
	
	/**
	 * Reads everything from the settings.cfg
	 */
	public static void read()
	{
		try(BufferedReader reader = new BufferedReader(new FileReader("./settings.cfg")))
		{
			String str = null,
				settingsName = null,
				settingsValue = null;
			str = getNextEntry(reader);
			
			while(!str.equals("[eol]")) // end of a line
			{
				String[] split = str.split("=");
				settingsName = split[0];
				settingsValue = split[1];
				
				switch(settingsName)
				{
					case "fullscreen.on":
						_fullscreen = Boolean.parseBoolean(settingsValue);
						_new_fullscreen = _fullscreen;
						break;
					case "screen.width":
						_screenwidth = Integer.parseInt(settingsValue);
						_new_screenwidth = _screenwidth;
						break;
					case "screen.height":
						_screenheight = Integer.parseInt(settingsValue);
						_new_screenheight = _screenheight;
						break;
					case "use.opengl30":
						_useopengl30 = Boolean.parseBoolean(settingsValue);
						_new_useopengl30 = _useopengl30;
						break;
					case "use.vsync":
						_usevsync = Boolean.parseBoolean(settingsValue);
						_new_usevsync = _usevsync;
						break;
					case "use.hdpi":
						_usehdpi = Boolean.parseBoolean(settingsValue);
						_new_usehdpi = _usehdpi;
						break;
					case "amount.samples":
						_amountsamples = Integer.parseInt(settingsValue);
						_new_amountsamples = _amountsamples;
						break;
					case "amount.segments":
						_amountsegments = Integer.parseInt(settingsValue);
						_new_amountsegments = _amountsegments;
						break;
					case "":
					default:
						break;
				}
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
	 */
	public static void save()
	{
		File oldFile = new File("./settings.cfg");
		if(oldFile.exists()) oldFile.delete();
		
		if(_new_fullscreen) // if fullscreen is true, the resolution has to be the full-screen resolution
		{
			GraphicsEnvironment gEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = gEnviroment.getScreenDevices();

			_new_screenwidth = devices[0].getDisplayMode().getWidth();
			_new_screenheight = devices[0].getDisplayMode().getHeight();
		}
		
		try
		{
			FileWriter writer = new FileWriter("./settings.cfg", true);
			writer.write(__praeambel);
			writer.write("fullscreen.on=" + _new_fullscreen + "\n");
			writer.write("screen.width=" + _new_screenwidth + "\n");
			writer.write("screen.height=" + _new_screenheight + "\n");
			writer.write("use.opengl30=" + _new_useopengl30 + "\n");
			writer.write("use.vsync=" + _new_usevsync + "\n");
			writer.write("use.hdpi=" + _new_usehdpi + "\n");
			writer.write("amount.samples=" + _new_amountsamples + "\n");
			writer.write("amount.segments=" + _new_amountsegments + "\n");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	

	/*
	 * ALL GET-METHODS OF USED VALUES
	 */
	
	/**
	 * @return True if fullscreen is enabled.
	 */
	public static boolean fullscreen()
	{
		return _fullscreen;
	}
	
	/**
	 * @return True if OpenGL30 is used.
	 */
	public static boolean useOpenGL30()
	{
		return _useopengl30;
	}
	
	/**
	 * @return True if VSync is enabled.
	 */
	public static boolean useVSync()
	{
		return _usevsync;
	}
	
	/**
	 * @return True if HDPI is enabled.
	 */
	public static boolean useHDPI()
	{
		return _usehdpi;
	}
	
	/**
	 * @return Width of current game window.
	 */
	public static int screenWidth()
	{
		return _screenwidth;
	}
	
	/**
	 * @return Height of current game window.
	 */
	public static int screenHeight()
	{
		return _screenheight;
	}

	/**
	 * @return The amount of samples.
	 */
	public static int amountOfSamples()
	{
		return _amountsamples;
	}
	
	/**
	 * @return The amount of segments for circles.
	 */
	public static int amountOfSegments()
	{
		return _amountsegments;
	}
	
	/*
	 * ALL GET-METHODS FOR "NEW"-VALUES
	 */
	
	/**
	 * @return True if fullscreen is enabled.
	 */
	public static boolean new_fullscreen()
	{
		return _new_fullscreen;
	}
	
	/**
	 * @return True if OpenGL30 is used.
	 */
	public static boolean new_useOpenGL30()
	{
		return _new_useopengl30;
	}
	
	/**
	 * @return True if VSync is enabled.
	 */
	public static boolean new_useVSync()
	{
		return _new_usevsync;
	}
	
	/**
	 * @return True if HDPI is enabled.
	 */
	public static boolean new_useHDPI()
	{
		return _new_usehdpi;
	}
	
	/**
	 * @return Width of current game window.
	 */
	public static int new_screenWidth()
	{
		return _new_screenwidth;
	}
	
	/**
	 * @return Height of current game window.
	 */
	public static int new_screenHeight()
	{
		return _new_screenheight;
	}
	
	/**
	 * @return The amount of samples.
	 */
	public static int new_amountOfSamples()
	{
		return _new_amountsamples;
	}
	
	/**
	 * @return The amount of segments for circles.
	 */
	public static int new_amountOfSegments()
	{
		return _new_amountsegments;
	}
	
	/*
	 * ALL SET-METHODS:
	 */
	
	/**
	 * @param fullscreenMode The new fullscreen mode. Active after restart.
	 */
	public static void setFullscreen(boolean fullscreenMode)
	{
		_new_fullscreen = fullscreenMode;
		save();
	}
	
	/**
	 * @param opengl30on The new openGL 3.0 usage. Active after restart.
	 */
	public static void setOpenGL30Usage(boolean opengl30on)
	{
		_new_useopengl30 = opengl30on;
		save();
	}
	
	/**
	 * @param vsyncOn The new VSync usage. Active after restart.
	 */
	public static void setVSyncUsage(boolean vsyncOn)
	{
		_new_usevsync = vsyncOn;
		save();
	}
	
	/**
	 * @param hdpiOn The new HDPI usage. Active after restart.
	 */
	public static void setHDPIUsage(boolean hdpiOn)
	{
		_new_usehdpi = hdpiOn;
		save();
	}
	
	/**
	 * @param newWidth The new screen width. Active after restart.
	 */
	public static void setScreenWidth(int newWidth)
	{
		_new_screenwidth = newWidth;
		save();
	}
	
	/**
	 * @param newHeight The new screen height. Active after restart.
	 */
	public static void setScreenHeight(int newHeight)
	{
		_new_screenheight = newHeight;
		save();
	}
	
	/**
	 * @param newAmount The new amount of samples. Active after restart.
	 */
	public static void setAmountSamples(int newAmount)
	{
		_new_amountsamples = newAmount;
		save();
	}
	
	/**
	 * @param newAmount The new amount of segments for circles. Active after restart.
	 */
	public static void setAmountSegments(int newAmount)
	{
		_new_amountsegments = newAmount;
		save();
	}
}
