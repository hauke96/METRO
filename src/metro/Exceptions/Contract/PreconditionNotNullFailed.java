package metro.Exceptions.Contract;

/**
 * @author hauke
 */
public class PreconditionNotNullFailed extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("javadoc")
	public PreconditionNotNullFailed()
	{
		super("The given argument must not be null but is was.");
	}
}
