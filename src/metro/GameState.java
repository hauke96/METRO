package metro;

import java.util.ArrayList;

import metro.TrainManagement.Trains.TrainStation;

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
	private ArrayList<TrainStation> _stationList;
	private int _baseNetSpacing; // amount of pixel between lines of the base net

	/**
	 * Creates a new game state. This can't be done by an external class.
	 */
	private GameState()
	{
		_money = 500000000;
		_stationList = new ArrayList<>();
		_baseNetSpacing = 50;
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
	 * Add an specific amount of money to the players account. Giving a negative amount will subtract money from the account.
	 * 
	 * @param moreMoney The money to add/subtract.
	 * @return True when transaction was successful, false when not (e.g. not enough money).
	 */
	public boolean addMoney(int moreMoney)
	{
		if(moreMoney <= 0 && moreMoney * -1 < _money // not enough money
			|| moreMoney >= 0)
		{
			_money += moreMoney;
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @return A list of all train stations.
	 */
	public ArrayList<TrainStation> getStations()
	{
		return _stationList;
	}

	/**
	 * @return Gets the size of the base net spacing.
	 */
	public int getBaseNetSpacing()
	{
		return _baseNetSpacing;
	}
}
