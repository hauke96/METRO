package metro;

import java.util.ArrayList;
import java.util.HashMap;

import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Trains.Train;
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
	private ArrayList<TrainLine> _lineList;
	private ArrayList<Train> _trainList;
	private HashMap<String, Train> _availableTrains;

	/**
	 * Creates a new game state. This can't be done by an external class.
	 */
	private GameState()
	{
		_money = 500000;
		_stationList = new ArrayList<>();
		_lineList = new ArrayList<>();
		_trainList = new ArrayList<>();
		_availableTrains = new HashMap<>();
		// TODO parsing method for the ./data/trains.txt
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
	 */
	public void addMoney(int moreMoney)
	{
		_money += moreMoney;
	}

	/**
	 * @return A list of all train stations.
	 */
	public ArrayList<TrainStation> getStations()
	{
		return _stationList;
	}

	/**
	 * @return A list of all train lines.
	 */
	public ArrayList<TrainLine> getLines()
	{
		return _lineList;
	}

	/**
	 * @return A list of all trains.
	 */
	public ArrayList<Train> getTrains()
	{
		return _trainList;
	}

	/**
	 * Gets a train by giving the name of the train.
	 * 
	 * @param name The name of the train.
	 * @return A new train object.
	 */
	public Train getTrain(String name)
	{
		return _availableTrains.get(name);
	}
}
