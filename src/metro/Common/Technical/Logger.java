package metro.Common.Technical;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Sorgt für eine schöne Log-Ausgabe.
 * 
 * @author hauke
 *
 */
public class Logger
{
	/**
	 * Prints a debug message into the console but only if the debug mode is on.
	 * 
	 * @param message The message to print.
	 */
	public static void __debug(String message)
	{
		__debug(message, 3);
	}

	/**
	 * Prints a debug message into the console but only if the debug mode is on.
	 * 
	 * @param message The message to print.
	 * @param stackCounter The counter for the stack trace to print the right line number (and not something like {@code metro.Logger.__debug(METRO.java:650)} :/
	 */
	public static void __debug(String message, int stackCounter)
	{
		if(message.length() > 0)
		{
			String[] splittedMessages = message.split("\\n");

			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			// get current time with Date()
			Date date = new Date();

			System.out.println("\n" + dateFormat.format(date) + " " + Thread.currentThread().getStackTrace()[stackCounter]);

			for(String line : splittedMessages)
			{
				line = "         " + line;
				System.out.println(line);
			}
		}
	}

}
