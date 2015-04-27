package metro.Game;

import java.io.*;

public class Settings
{
	public static boolean __fullscreen;
	public static int __screenWidth, __screenHeight;
	private static String __praeambel = "* comments begin with *\n* Settings: [name]=[value]\n"; 
	
	public static void read()
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("./settings.cfg"));
			String str = null,
				settingsName = null,
				settingsValue = null;
			str = getNextEntry(reader);
			while(!str.equals("[eol]"))
			{
				String[] split = str.split("=");
				settingsName = split[0];
				settingsValue = split[1];
				
				switch(settingsName)
				{
					case "fullscreen":
						__fullscreen = Boolean.parseBoolean(settingsValue);
						break;
					case "screen_width":
						__screenWidth = Integer.parseInt(settingsValue);
						break;
					case "screen_height":
						__screenHeight = Integer.parseInt(settingsValue);
						break;
					case "":
					default:
						break;
				}
				str = getNextEntry(reader);
			}
			
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
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
	
	public static void save()
	{
		File oldFile = new File("./settings.cfg");
		if(oldFile.exists()) oldFile.delete();
		
		try
		{
			FileWriter writer = new FileWriter("./settings.cfg", true);
			writer.write(__praeambel);
			writer.write("fullscreen=" + __fullscreen + "\n");
			writer.write("screen_width=" + __screenWidth + "\n");
			writer.write("screen_height=" + __screenHeight + "\n");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
	}
}
