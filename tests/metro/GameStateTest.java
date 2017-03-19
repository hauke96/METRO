package metro;

import static org.junit.Assert.*;

import org.junit.Test;

import metro.AppContext.Locator;
import metro.Common.Game.GameState;
import metro.Common.Technical.Exceptions.NotEnoughMoneyException;

/**
 * @author hauke
 */
public class GameStateTest
{
	private GameState gameState;

	/**
	 * Simply gets the instance of the game state.
	 */
	public GameStateTest()
	{
		gameState = Locator.get(GameState.class);
	}

	/**
	 * Check if everything is sets properly (meaning, everythin's != 0).
	 */
	@Test
	public void testEverythingIsSet()
	{
		assertNotEquals(0, gameState.getMoney());
		assertNotEquals(0, gameState.getBaseNetSpacing());
		assertNotEquals(0, gameState.getToolViewWidth());
	}

	/**
	 * Set something to the fields of the game state and checks if the values have been set correctly.
	 */
	@Test
	public void testGetterSetter()
	{
		gameState.setMoney(5789);
		gameState.setBaseNetSpacing(245);
		gameState.setToolViewWidth(825);

		assertEquals(5789, gameState.getMoney());
		assertEquals(245, gameState.getBaseNetSpacing());
		assertEquals(825, gameState.getToolViewWidth());
	}

	/**
	 * Sets the amount of money to a valid number, adds something valid and checks if the calculation was correct.
	 */
	@Test
	public void testAddMoneyCorrect()
	{
		gameState.setMoney(368);
		gameState.addMoney(246);

		assertEquals(368 + 246, gameState.getMoney());
	}

	/**
	 * Takes a negative amount of money on the account and add something valid.
	 */
	@Test
	public void testAddMoneyToNegativeAccountCorrect()
	{
		gameState.setMoney(-368);
		gameState.addMoney(246);

		assertEquals(-368 + 246, gameState.getMoney());
	}

	/**
	 * Takes a valid amount of money and adds something invalid. An exception should be the result.
	 * 
	 * @throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddMoneyWrong() throws IllegalArgumentException
	{
		gameState.setMoney(368);
		gameState.addMoney(-246);
	}

	/**
	 * Subtracts something from the game state account.
	 * When more money is withdrawn then there actually exists, an exception should be the result.
	 * 
	 * @throws NotEnoughMoneyException
	 */
	@Test
	public void testWithdrawMoney() throws NotEnoughMoneyException
	{
		gameState.setMoney(100);
		gameState.withdrawMoney(54);
		assertEquals(46, gameState.getMoney());
	}

	/**
	 * Checks if the game state withdraws money when a negative amount is passed.
	 * A negative amount of money is not valid, so an IllegalArgumentException should be throwed.
	 * 
	 * @throws NotEnoughMoneyException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithdrawMoneyNegative() throws NotEnoughMoneyException
	{
		gameState.setMoney(100);
		gameState.withdrawMoney(-54);
		assertEquals(154, gameState.getMoney());
	}

	/**
	 * Check if the game state allows the withdraw of more money then the account currently has.
	 * 
	 * @throws NotEnoughMoneyException
	 */
	@Test(expected = NotEnoughMoneyException.class)
	public void testWithdrawMoneyTooMuch() throws NotEnoughMoneyException
	{
		gameState.setMoney(100);
		gameState.withdrawMoney(154);
		assertEquals(100, gameState.getMoney());
	}

	/**
	 * Tests is the zooming works correctly.
	 */
	@Test
	public void testZoom()
	{
		gameState.setBaseNetSpacing(40);
		int spacing = gameState.getBaseNetSpacing();

		gameState.zoom(1);
		assertEquals(spacing - 5, gameState.getBaseNetSpacing());

		gameState.zoom(-1);
		assertEquals(spacing, gameState.getBaseNetSpacing());

		gameState.zoom(100);
		assertEquals(20, gameState.getBaseNetSpacing());

		gameState.zoom(-100);
		assertEquals(70, gameState.getBaseNetSpacing());
	}
}
