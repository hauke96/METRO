package metro.Common.Technical;

import org.junit.Test;

import metro.Exceptions.Contract.PreconditionNotEmptyFailed;
import metro.Exceptions.Contract.PreconditionNotNullFailed;

public class ContactTest
{
	/**
	 * Test if an exception occurs when null has been passed.
	 */
	@Test(expected = PreconditionNotNullFailed.class)
	public void testContractNotNullExpectedFail()
	{
		Object obj = null;

		Contract.RequireNotNull(obj);
	}

	/**
	 * Test if no exception occurs when an object has been passed.
	 */
	@Test
	public void testContractNotNullExpectedCorrect()
	{
		Object obj = new Object();

		Contract.RequireNotNull(obj);
	}

	/**
	 * Test if an exception occurs when an empty string has been passed.
	 */
	@Test(expected = PreconditionNotEmptyFailed.class)
	public void testContractNotEmptyExpectedFail()
	{
		String s = "";

		Contract.RequireNotNullOrEmpty(s);
	}

	/**
	 * Test if an exception occurs when null has been passed.
	 */
	@Test(expected = PreconditionNotNullFailed.class)
	public void testContractNotEmptyNotNullExpectedFail()
	{
		String s = null;

		Contract.RequireNotNullOrEmpty(s);
	}

	/**
	 * Test if no exception occurs when a valid string (! null && !empty) has been passed.
	 */
	@Test
	public void testContractNotEmptyExpectedCorrect()
	{
		String s = "foo";

		Contract.RequireNotNullOrEmpty(s);
	}
}
