package metro.Exceptions.Contract;

/**
 * A general exception for a failed precondition.
 * There're more specified exceptions then this one ;)
 * 
 * @author hauke
 *
 */
public class PreconditionFailedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("javadoc")
	public PreconditionFailedException()
	{
		super("The expression was not true.");
	}
}
