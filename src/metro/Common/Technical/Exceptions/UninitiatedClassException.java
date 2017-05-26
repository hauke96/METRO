package metro.Common.Technical.Exceptions;

/**
 * @author hauke
 */
public class UninitiatedClassException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new UninitiatedClassException with the given message.
	 * 
	 * @param message
	 *            The message of this exception.
	 */
	public UninitiatedClassException(String message)
	{
		super(message);
	}
}
