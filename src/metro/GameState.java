package metro;

import metro.Exceptions.NotEnoughMoneyException;

/**
 * A game state is the place where all game relevant information are stored.
 * There can only be one instance of the game state accessible by instance().
 * This class uses the singleton pattern to be sure that there's only be one instance at all.
 * 
 * @author hauke
 *
 */
public class GameState
{
	private static final GameState __INSTANCE = new GameState();

	private int _money;
	private int _baseNetSpacing; // amount of pixel between lines of the base net

	/**
	 * Creates a new game state. This can't be done by an external class.
	 */
	private GameState()
	{
		_money = 500000000;
		_baseNetSpacing = 20;
	}

	/**
	 * @return The instance of the game state. There can only be one instance per game.
	 */
	public static GameState getInstance()
	{
		return __INSTANCE;
	}

	/**
	 * Gets the money of the player.
	 * 
	 * @return The players money.
	 */
	public int getMoney()
	{
		return _money;
	}

	/**
	 * Add an specific amount of money to the players account. Adding a negative amount of money to the account is not permitted.
	 * May throw an IllegalArgumentException when the amount of money is less than 0.
	 * 
	 * @param moreMoney The money to add/subtract. Throw an IllegalArgumentException when the amount of money is less than 0.
	 */
	public void addMoney(int moreMoney)
	{
		if(moreMoney < 0)
		{
			throw new IllegalArgumentException("The amount of money needs to be greater 0. Use withdrawMoney(int) to remove an amount of money from the bank account.");
		}
		_money += moreMoney;
	}

	/**
	 * Removes the given amount of money from the bank account of the player.
	 * Passing a negative amount will add the value of it to the account (e.g. -100 will add 100$ to the account).
	 * 
	 * @param moreMoney The amount of money.
	 * @throws NotEnoughMoneyException An exception describing that there's to less money on the bank account to withdraw the given amount from it.
	 */
	public void withdrawMoney(int moreMoney) throws NotEnoughMoneyException
	{
		if(moreMoney >= 0 && moreMoney <= _money)
		{
			_money -= moreMoney;
		}
		else if(moreMoney < 0 && moreMoney * -1 <= _money)
		{
			addMoney(-moreMoney);
		}
		else
		{
			throw new NotEnoughMoneyException(_money, moreMoney);
		}
	}

	/**
	 * @return Gets the size of the base net spacing.
	 */
	public int getBaseNetSpacing()
	{
		return _baseNetSpacing;
	}

	/**
	 * Zooms in (makes things greater :D ) or out in by in-/decreasing the {@code _baseNetSpacing}.
	 * The minimal spacing is 20 pixel, the maximum is 70.
	 * 
	 * @param amount The amount of steps. Each step in-/decreases the {@code _baseNetSpacing} by 5 pixel.
	 */
	public void zoom(int amount)
	{
		_baseNetSpacing += -5 * amount;
		if(_baseNetSpacing < 20) _baseNetSpacing = 20;
		else if(_baseNetSpacing > 70) _baseNetSpacing = 70;
	}
}
