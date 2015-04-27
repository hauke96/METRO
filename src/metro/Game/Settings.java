package metro.Game;

import java.io.*;

public class Settings
{
	public static boolean __fullscreen,
		__useopengl30,
		__usevsync,
		__usehdpi;
	public static int __screenwidth, 
		__screenheight,
		__amountsamples;
	
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
					case "fullscreen.on":
						__fullscreen = Boolean.parseBoolean(settingsValue);
						break;
					case "screen.width":
						__screenwidth = Integer.parseInt(settingsValue);
						break;
					case "screen.height":
						__screenheight = Integer.parseInt(settingsValue);
						break;
					case "use.opengl30":
						__useopengl30 = Boolean.parseBoolean(settingsValue);
						break;
					//TODO weiter machen
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
			writer.write("fullscreen.on=" + __fullscreen + "\n");
			writer.write("screen.width=" + __screenwidth + "\n");
			writer.write("screen.height=" + __screenheight + "\n");
			writer.write("use.opengl30=" + __useopengl30 + "\n");
			writer.write("use.vsync=" + __usevsync + "\n");
			writer.write("use.hdpi=" + __usehdpi + "\n");
			writer.write("amount.samples=" + __amountsamples + "\n");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
	}
}
