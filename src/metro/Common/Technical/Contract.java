package metro.Common.Technical;

import metro.Exceptions.Contact.PostconditionNotNullFailed;
import metro.Exceptions.Contact.PreconditionNotEmptyFailed;
import metro.Exceptions.Contact.PreconditionNotNullFailed;

/**
 * This class offers many contact methods to ensure pre- and postconditions.
 * 
 * @author hauke
 *
 */
public class Contract
{
	/**
	 * Checks if the given variable is not null. If so, an exception will be thrown.
	 *
	 * @param variable The variable to check.
	 */
	public static void RequireNotNull(Object variable)
	{
		if(variable == null)
		{
			throw new PreconditionNotNullFailed();
		}
	}

	/**
	 * Checks if the given variable is not null and if it also is not an empty string.
	 *
	 * @param variable The variable to check.
	 */
	public static void RequireNotNullOrEmpty(String variable)
	{
		Contract.RequireNotNull(variable);
		if(variable.isEmpty())
		{
			throw new PreconditionNotEmptyFailed();
		}
	}

	/**
	 * Checks if the result of a method is not null.
	 * Place this method call just before the return statement in your method.
	 * 
	 * @param variable The variable to check.
	 */
	public static void EnsureNotNull(Object variable)
	{
		if(variable == null)
		{
			throw new PostconditionNotNullFailed();
		}
	}
}
