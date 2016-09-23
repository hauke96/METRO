package metro.Exceptions.Contact;

/**
 * @author hauke
 */
public class PreconditionNotEmptyFailed extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("javadoc")
	public PreconditionNotEmptyFailed()
	{
		super("The given argument must not be an empty string.");
	}
}
