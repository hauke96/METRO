package metro.Exceptions.Contract;

/**
 * @author hauke
 */
public class PostconditionNotNullFailed extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("javadoc")
	public PostconditionNotNullFailed()
	{
		super("The given argument must not be null but is was.");
	}
}
