package metro.Common.Technical;

/**
 * This is a modifiable boolean class to extract values from e.g. a lambda expression.
 * 
 * @author hauke
 *
 */
public class ModifiableBoolean
{
	private boolean _value;
	
	/**
	 * Creates a with {@code false} initialized boolean.
	 */
	public ModifiableBoolean()
	{
		_value = false;
	}
	
	/**
	 * @return The current value (so {@code true} or {@code false}).
	 */
	public boolean value()
	{
		return _value;
	}
	
	/**
	 * @param newValue
	 *            The new value.
	 */
	public void set(boolean newValue)
	{
		_value = newValue;
	}
}
