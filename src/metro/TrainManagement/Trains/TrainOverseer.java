package metro.TrainManagement.Trains;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import metro.METRO;
import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Lines.TrainLineOverseer;

/**
 * The TrainOverseer holds all trains and provides an interface to interact with and modify them.
 * All the template trains are stored here as well. Constants of Trains (like their price) aren't
 * stored in this class but in the {@code metro.TrainManagement.Trains.Train} class.
 * 
 * @author hauke
 *
 */
public class TrainOverseer
{
	private ArrayList<Train> _trainList = new ArrayList<>();
	private LinkedHashMap<String, TrainTemplate> _templateTrains = new LinkedHashMap<>();
	private final static TrainOverseer __INSTANCE = new TrainOverseer();

	private TrainOverseer()
	{
	}

	/**
	 * @return The instance of the train observer. There can only be one instance per game.
	 */
	public static TrainOverseer getInstance()
	{
		return __INSTANCE;
	}
	
	/**
	 * @return A list of all trains.
	 */
	public ArrayList<Train> getTrains()
	{
		return _trainList;
	}

	/**
	 * Adds a train to the list of bought trains. This method does not check for any valid trains (not null, etc.)!
	 * 
	 * @param train The new train to add.
	 */
	public void addTrain(Train train)
	{
		if(METRO.__gameState.addMoney(-train.getPrice()))
		{
			_trainList.add(train);
		}
		else
		{
			// TODO add notification (s. #40)
		}
	}

	/**
	 * Removes a train from the list of bought trains. This method does not check for any valid trains (not null, etc.)!
	 * This method will also add the price of the train to the players account.
	 * 
	 * @param trainName The name of the train that should be removed.
	 */
	public void removeTrain(String trainName)
	{
		Train train = getTrainByName(trainName);
		if(train != null)
		{
			_trainList.remove(train);
			METRO.__gameState.addMoney(train.getPrice());
		}
	}

	/**
	 * Gets a train template by giving its model name.
	 * 
	 * @param name The model name of the train.
	 * @return A new train template object.
	 */
	public TrainTemplate getTemplateTrain(String name)
	{
		return _templateTrains.get(name);
	}

	/**
	 * @return A list with all available trains. This does NOT mean that the player already bought them, they are just available for him.
	 */
	public ArrayList<TrainTemplate> getTemplateTrains()
	{
		return new ArrayList<TrainTemplate>(_templateTrains.values());
	}

	/**
	 * Adds a template train to the list of them.
	 * 
	 * @param train The template train that should be added.
	 */
	public void addTemplateTrain(TrainTemplate train)
	{
		_templateTrains.put(train.getName(), train);
	}

	/**
	 * Searches for a train with the given name. When there's no train with this name, this method will return {@code null}.
	 * 
	 * @param trainName The name of the train.
	 * @return The train or {@code null} when this train doesn't exist.
	 */
	public Train getTrainByName(String trainName)
	{
		for(Train train : _trainList)
		{
			if(train.getName().equals(trainName))
			{
				return train;
			}
		}
		return null;
	}

	/**
	 * Removes all trains from the list of trains and adds the money to the players account.
	 * 
	 * @param lineName The name of the trains that should be removed from the list.
	 */
	public void sellTrainFromLine(String lineName)
	{
		LinkedList<Train> trains = new LinkedList<Train>(); // because add() is in O(1)
		TrainLine line = TrainLineOverseer.getInstance().getLine(lineName); // FIXME BAAAAAD (circle dependency)

		for(Train train : _trainList)
		{
			if(train.getLine().equals(line))
			{
				trains.add(train);
				METRO.__gameState.addMoney(train.getPrice());
			}
		}

		_trainList.removeAll(trains);
	}
}
