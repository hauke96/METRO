package metro.Exceptions;

/**
 * This exception is for any transaction or action with money where not enough money exists.
 * 
 * @author hauke
 *
 */
public class NotEnoughMoneyException extends Exception
{
	/**
	 * ID needed for the serializable process.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an exception that says, that there's not enough money for this transaction.
	 */
	public NotEnoughMoneyException()
	{
		super("There's not enough money to do this transaction.");
	}

	/**
	 * Creates an exception for a bad money transfer with the given values of the current state and the amount of money involved in the transaction.
	 * 
	 * @param currentBalance The current amount of money on the bank account.
	 * @param transactionMoney The amount of money involved in the transaction.
	 */
	public NotEnoughMoneyException(int currentBalance, int transactionMoney)
	{
		super("There's not enough money to do this transaction. There are " + currentBalance + "$ on the bank account but " + transactionMoney + "$ should be tranfered.");
	}

	/**
	 * Creates an exception for anything where not enough money exists with a custom message.
	 * 
	 * @param message The message for this exception.
	 */
	public NotEnoughMoneyException(String message)
	{
		super(message);
	}
}
