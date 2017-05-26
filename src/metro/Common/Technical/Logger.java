package metro.Common.Technical;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Displays nice log entries.
 * 
 * @author hauke
 *
 */
public class Logger
{
	private static final String	DEBUG	= "[debug]";
	private static final String	ERROR	= "[error]";
	private static final String	INFO	= "[info]  ";
	private static final String	FATAL	= "[fatal]";
	
	/**
	 * True to create an empty line between each output.
	 */
	public static boolean	__separatingEmptyLine	= false;
	/**
	 * True to also show debug output.
	 */
	public static boolean	__showDebug				= true;
	
	/**
	 * Prints the given information including
	 * 
	 * @param infoMessage
	 *            The message to print as normal information.
	 */
	public static void __info(String infoMessage)
	{
		__printOutput(infoMessage, INFO, System.out);
	}
	
	/**
	 * Prints a debug message into the console but only if the debug mode is on.
	 * 
	 * @param debugMessage
	 *            The message to print in debug mode.
	 */
	public static void __debug(String debugMessage)
	{
		if (!__showDebug) return;
		// The 3 will print the path to the line of code that calls __debug(String)
		__printOutput(debugMessage, DEBUG, System.out, 3);
	}
	
	/**
	 * Prints the error message to STD_ERR.
	 * 
	 * @param errorMessage
	 *            The message to show as error.
	 */
	public static void __error(String errorMessage)
	{
		__printOutput(errorMessage, ERROR, System.err, 3);
	}
	
	/**
	 * Prints the error message to STD_ERR.
	 * 
	 * @param errorMessage
	 *            The message to show as error.
	 * @param throwable
	 *            An throwable object to show even more information.
	 */
	public static void __error(String errorMessage, Throwable throwable)
	{
		__printOutput(errorMessage, ERROR, System.err, 3);
		throwable.printStackTrace();
	}
	
	/**
	 * Shows the error message and exits the application with exit-code 1.
	 * 
	 * @param errorMessage
	 *            The error message to show as fatal error.
	 */
	public static void __fatal(String errorMessage)
	{
		System.err.println("\nMETRO will be closed due to the following fatal error:\n");
		__printOutput(errorMessage, FATAL, System.err, 3);
		System.exit(1);
	}
	
	/**
	 * Shows the error message and exits the application with exit-code 1.
	 * 
	 * @param errorMessage
	 *            The error message to show.
	 * @param throwable
	 *            An throwable object to show even more information.
	 */
	public static void __fatal(String errorMessage, Throwable throwable)
	{
		__printOutput(errorMessage, FATAL, System.err, 3);
		System.err.println("\nMETRO will be closed due to the following exception:\n");
		throwable.printStackTrace();
		System.exit(1);
	}
	
	/**
	 * Prints the given information.
	 * 
	 * @param message
	 *            The general message to display.
	 * @param tag
	 *            The tag specified by the fields of the Logger class.
	 * @param stream
	 *            The output stream (STD_ERR or STD_OUT)
	 */
	private static void __printOutput(String message, String tag, PrintStream stream)
	{
		__printOutput(message, tag, stream, -1);
	}
	
	/**
	 * Prints the given information.
	 * 
	 * @param message
	 *            The general message to display.
	 * @param tag
	 *            The tag specified by the fields of the Logger class.
	 * @param stream
	 *            The output stream (std_err or std_in
	 * @param stackCounter
	 *            The stack counter. Set this to -1 to not display the method that called uses the Logger.
	 */
	private static void __printOutput(String message, String tag, PrintStream stream, int stackCounter)
	{
		if (message.length() > 0)
		{
			String[] splittedMessages = message.split("\\n");
			
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			// get current time with Date()
			Date date = new Date();
			
			String formattedOutput;
			if (stackCounter == -1)
			{
				formattedOutput = MessageFormat.format("{0} {1}", dateFormat.format(date), tag);
			}
			else
			{
				formattedOutput = MessageFormat.format("{0} {1} {2}\n                 ", dateFormat.format(date), tag, Thread.currentThread().getStackTrace()[stackCounter]);
			}
			stream.print(formattedOutput);
			
			if (splittedMessages.length >= 1)
			{
				// print first line separate to also be able to do one-line-outputs without unwanted intention.
				String line = splittedMessages[0];
				stream.println(line);
				
				// print rest of lines
				for (int i = 1; i < splittedMessages.length; i++)
				{
					line = splittedMessages[i];
					line = "                 " + line;
					stream.println(line);
				}
			}
			
			if (__separatingEmptyLine)
			{
				System.out.println();
			}
		}
	}
}
