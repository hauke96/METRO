package metro.Common.Technical.Exceptions;

/**
 * This exception will be fired if the Locator cannot resolve a type. This usually happens when this type hasn't been registered.
 * 
 * @author hauke
 *
 */
public class ResolutionFailedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a resolution failed exception for the given class.
	 * 
	 * @param clazz
	 *            The class that couldn't be resolved.
	 */
	public ResolutionFailedException(Class<?> clazz)
	{
		super("Failed to resolve class " + clazz.getSimpleName() + " (" + clazz.getName() + "). The type is not registered in the Locator.");
	}
}
