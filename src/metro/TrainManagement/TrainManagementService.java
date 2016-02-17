package metro.TrainManagement;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Nodes.RailwayNode;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;
import metro.TrainManagement.Trains.Train;
import metro.TrainManagement.Trains.TrainTemplate;

/**
 * The train management service combines the former TrainOverseer and TrainLineOverseer into one class without any circle dependencies anymore.
 * This service knows all Trains and all lines and is able to edit the lists and the train/line itself. You can add, remove or change these.
 * There're no constants or any value belonging to a train or line, these are stored in the {@code Train} or {@code Line} class.
 * 
 * @author hauke
 */
public class TrainManagementService
{
	private static ArrayList<TrainLine> _listOfTrainLines = new ArrayList<TrainLine>();
	private ArrayList<Train> _trainList = new ArrayList<>();
	private LinkedHashMap<String, TrainTemplate> _templateTrains = new LinkedHashMap<>();
	private final static TrainManagementService __INSTANCE = new TrainManagementService();

	private TrainManagementService()
	{
	}

	/**
	 * @return The instance of the train observer. There can only be one instance per game.
	 */
	public static TrainManagementService getInstance()
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
	public void sellTrainsFromLine(String lineName)
	{
		LinkedList<Train> trains = new LinkedList<Train>(); // because add() is in O(1)
		TrainLine line = getLine(lineName);

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

	/**
	 * Adds a train line to the list of lines. An older version of this line will be removed.
	 * 
	 * @param line The line to add.
	 */
	public void addLine(TrainLine line)
	{
		if(line == null) return;
		_listOfTrainLines.remove(line); // remove old line, because maybe line.equals(old-line) == true
		_listOfTrainLines.add(line); // adds the new line to the list
	}

	/**
	 * Removes a line from the list.
	 * 
	 * @param line The line to remove.
	 */
	public void removeLine(TrainLine line)
	{
		if(line == null) return;
		_listOfTrainLines.remove(line);
		removeTrain(line.getName()); 
	}

	/**
	 * Removes a line including all containing colors and settings.
	 * 
	 * @param lineName The name of the line.
	 */
	public void removeLine(String lineName)
	{
		removeLine(getLine(lineName));
	}

	/**
	 * Returns the color of a given train line.
	 * 
	 * @param lineName The name of the line.
	 * @return The color of the given line. null if line does not exist.
	 */
	public Color getLineColor(String lineName)
	{
		for(TrainLine line : _listOfTrainLines)
		{
			if(line.getName().equals(lineName))
			{
				return line.getColor();
			}
		}
		return null;
	}

	/**
	 * Creates a copy of the list train lines and returns this copy.
	 * 
	 * @return A copy of the list of all lines.
	 */
	@SuppressWarnings("unchecked") // cast will always succeed, because the list only hold TrainLine objects
	public ArrayList<TrainLine> getLines()
	{
		return (ArrayList<TrainLine>)_listOfTrainLines.clone();
	}

	/**
	 * Searches for a special line.
	 * 
	 * @param lineName The name of the line.
	 * @return The line. null if line does not exist.
	 */
	public TrainLine getLine(String lineName)
	{
		for(TrainLine line : _listOfTrainLines)
		{
			if(line.getName().equals(lineName))
			{
				return line;
			}
		}
		return null;
	}

	/**
	 * Draws all train lines.
	 * 
	 * @param offset The map offset in pixel.
	 * @param sp The sprite batch to draw on.
	 */
	public void drawLines(Point offset, SpriteBatch sp)
	{
		HashMap<RailwayNode, Integer> map = new HashMap<RailwayNode, Integer>();
		for(RailwayNode node : RailwayNodeOverseer._nodeMap.values())
		{
			map.put(node, new Integer(0));
		}

		for(TrainLine line : _listOfTrainLines)
		{
			line.draw(offset, sp, map);
		}
	}

	/**
	 * Checks if a color is already in use.
	 * 
	 * @param color The color that might be used.
	 * @return True when color is used, false when color is free.
	 */
	public boolean isLineColorUsed(Color color)
	{
		for(TrainLine line : _listOfTrainLines)
		{
			if(line.getColor().equals(color)) return true;
		}
		return false;
	}
}
