package metro.Common.Game;

import metro.Common.Technical.Exceptions.NotEnoughMoneyException;

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
	private int _money,
		_baseNetSpacing, // amount of pixel between lines of the base net
		_toolViewWidth;

	/**
	 * Creates a new game state. This can't be done by an external class.
	 */
	public GameState()
	{
		_money = 500000000;
		_baseNetSpacing = 20;
		_toolViewWidth = 400;
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
	 * Sets the amount of money. This will overwrite the old amount ({@link #addMoney(int)} does not).
	 * 
	 * @param money The new amount of money.
	 */
	public void setMoney(int money)
	{
		_money = money;
	}

	/**
	 * Add an specific amount of money to the players account. Adding a negative amount of money to the account is not permitted.
	 * May throw an IllegalArgumentException when the amount of money is less than 0.
	 * 
	 * @param moreMoney The money to add/subtract. Throw an IllegalArgumentException when the amount of money is less than 0.
	 * @throws IllegalArgumentException
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
	 * @param moneyToWithdraw The amount of money.
	 * @throws NotEnoughMoneyException An exception describing that there's to less money on the bank account to withdraw the given amount from it.
	 */
	public void withdrawMoney(int moneyToWithdraw) throws NotEnoughMoneyException
	{
		if(moneyToWithdraw >= 0 && moneyToWithdraw <= _money)
		{
			_money -= moneyToWithdraw;
		}
		else if(moneyToWithdraw < 0)
		{
			throw new IllegalArgumentException("The amount of money needs to be greater 0. Use addMoney(int) to add an amount of money to the bank account.");
		}
		else if(moneyToWithdraw > _money)
		{
			throw new NotEnoughMoneyException(_money, moneyToWithdraw);
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
	 * Overwrites the old base net spacing. This will update no UI or something!
	 * 
	 * @param spacing The new spacing.
	 */
	public void setBaseNetSpacing(int spacing)
	{
		_baseNetSpacing = spacing;
	}

	/**
	 * @return The width of tool views (e.g. line- and trainView).
	 */
	public int getToolViewWidth()
	{
		return _toolViewWidth;
	}

	/**
	 * Overwrites the width of tools. This will not update any UIs.
	 * 
	 * @param width The new width.
	 */
	public void setToolViewWidth(int width)
	{
		_toolViewWidth = width;
	}

	/**
	 * Zooms in (makes things greater :D ) or out in by in-/decreasing the {@code _baseNetSpacing}.
	 * The minimal spacing is 20 pixel, the maximum is 70.
	 * 
	 * Passing a positive amount of steps will decrease the {@code _baseNetSpacing} (zooming out), a negative amount will increase.
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
