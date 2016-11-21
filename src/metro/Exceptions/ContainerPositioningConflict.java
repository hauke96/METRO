package metro.Exceptions;

public class ContainerPositioningConflict extends Exception
{
	/**
	 * ID needed for the serialization process.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an exception that says, that the "is below of" property of two container are in conflict.
	 */
	public ContainerPositioningConflict()
	{
		super("The \"is below of\" property of two containers are in conflict.");
	}
}
