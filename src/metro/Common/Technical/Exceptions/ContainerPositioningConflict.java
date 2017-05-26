package metro.Common.Technical.Exceptions;

/**
 * This exception indicates, that two container have a conflict in there "is below of" relation.
 * That means that two containers are probably below each other which can't be.
 * 
 * @author hauke
 *
 */
public class ContainerPositioningConflict extends RuntimeException
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
