package metro.Common.Technical;

public class ModifiableBoolean
{
	private boolean _value;

	public ModifiableBoolean()
	{
		_value = false;
	}

	public boolean value()
	{
		return _value;
	}

	public void set(boolean newValue)
	{
		_value = newValue;
	}
}
