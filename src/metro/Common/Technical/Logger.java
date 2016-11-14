package metro.Common.Technical;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Logger
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
	public static void __debug(String wholeMessage, int stackCounter)
	{
		if(wholeMessage.length() > 0)
		{
//			if(message.contains("\n"))
//			{
				String[] splittedMessages = wholeMessage.split("\\n");
				
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				// get current time with Date()
				Date date = new Date();

				System.out.println("\n" + dateFormat.format(date) + " " + Thread.currentThread().getStackTrace()[stackCounter]);
				
				for(String message : splittedMessages)
				{
//					__debug(s, stackCounter + 1);
//				}
//			}
//			else
//			{
//				if(message.charAt(0) == '[')
//				{
//				}
//				else
//				{
					message = "         " + message;
//				}
				System.out.println(message);
			}
		}
	}

}
