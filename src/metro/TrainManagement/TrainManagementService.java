package metro.TrainManagement;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.Common.Technical.Logger;
import metro.Exceptions.NotEnoughMoneyException;
import metro.GameUI.MainView.NotificationView.NotificationServer;
import metro.GameUI.MainView.NotificationView.NotificationType;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;
import metro.TrainManagement.Trains.Train;
import metro.TrainManagement.Trains.TrainLine;
import metro.TrainManagement.Trains.TrainStation;
import metro.TrainManagement.Trains.TrainTemplate;
import metro.TrainManagement.Trains.TravelerSpot;

/**
 * The train management service combines the former TrainOverseer and TrainLineOverseer into one class without any circle dependencies anymore.
 * This service knows all Trains and all lines and is able to edit the lists and the train/line itself. You can add, remove or change these.
 * There're no constants or any value belonging to a train or line, these are stored in the {@code Train} or {@code Line} class.
 * 
 * @author hauke
 */
public class TrainManagementService implements Observer
{
	private List<TrainLine> _trainLineList;
	private List<Train> _trainList;
	private List<TrainStation> _stationList;
	private List<TravelerSpot> _travelerSpotList;
	private HashMap<String, TrainTemplate> _templateTrains;
	private float _lastRenderTime;
	private TrainLineDrawingService _trainLineDrawingService;

	private final static TrainManagementService __INSTANCE = new TrainManagementService();

	private TrainManagementService()
	{
		init();
	}
	
	/**
	 * Resets the whole service. Be careful with this method, it removes everything!
	 */
	public void init()
	{
		_trainLineList = new ArrayList<>();
		_trainList = new ArrayList<>();
		_stationList = new ArrayList<>();
		_travelerSpotList = new ArrayList<>();

		_templateTrains = new HashMap<>();
		_lastRenderTime = System.nanoTime();

		_trainLineDrawingService = new TrainLineDrawingService();

		try
		{
			createTrains();
		}
		catch(IOException e)
		{
			Logger.__fatal("Can't read train.txt due to an error: ", e);
		}
		catch(IllegalArgumentException e)
		{
			Logger.__fatal("", e);
		}

		_travelerSpotList.add(new TravelerSpot(new Point(15, 10), 5));
		_travelerSpotList.add(new TravelerSpot(new Point(8, 5), 3));
		_travelerSpotList.add(new TravelerSpot(new Point(23, 9), 4));
		_travelerSpotList.add(new TravelerSpot(new Point(14, 24), 3));
		_travelerSpotList.add(new TravelerSpot(new Point(15, 31), 4));

		_travelerSpotList.add(new TravelerSpot(new Point(45, 35), 6));
		_travelerSpotList.add(new TravelerSpot(new Point(53, 32), 4));
		_travelerSpotList.add(new TravelerSpot(new Point(55, 37), 4));
		_travelerSpotList.add(new TravelerSpot(new Point(56, 49), 3));
		// TODO Auto-generated method stub
		
	}

	private void createTrains() throws IOException, IllegalArgumentException
	{
		List<String> lines = Files.readAllLines((new File("data/trains.txt")).toPath(), Charset.defaultCharset());

		Pattern comment = Pattern.compile("\\s#\\w"),
			empty = Pattern.compile("\\s");

		String name = "",
			manufacturer = "",
			price = "",
			costs = "",
			costsfactor = "",
			passenger = "",
			speed = "";

		for(String line : lines)
		{
			if(!comment.matcher(line).matches() && !empty.matcher(line).matches())
			{
				String[] lineSplit = line.split(":");
				if(lineSplit.length > 2) throw new IllegalArgumentException("Each property must contain exactly one \":\" symbol!");
				//TODO simplify this whole method!
				switch(lineSplit[0])
				{
					case "NAME":
						if(!name.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"NAME\" exists multiple times in the property " + name + "!");
						name = lineSplit[1];
						break;
					case "MANUFACTURER":
						if(!manufacturer.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"MANUFACTURER\" exists multiple times in the property " + name + "!");
						manufacturer = lineSplit[1];
						break;
					case "PRICE":
						if(!price.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"PRICE\" exists multiple times in the property " + name + "!");
						price = lineSplit[1];
						break;
					case "COSTS":
						if(!costs.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"COSTS\" exists multiple times in the property " + name + "!");
						costs = lineSplit[1];
						break;
					case "COSTSFACTOR":
						if(!costsfactor.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"COSTFACTOR\" exists multiple times in the property " + name + "!");
						costsfactor = lineSplit[1];
						break;
					case "PASSENGER":
						if(!passenger.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"PASSENGER\" exists multiple times in the property " + name + "!");
						passenger = lineSplit[1];
						break;
					case "SPEED":
						if(!speed.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"SPEED\" exists multiple times in the property " + name + "!");
						speed = lineSplit[1];
						break;
				}

				// when the read data is complete, create train and put it into the map and then reset all variables
				if(!name.equals("")
					&& !manufacturer.equals("")
					&& !price.equals("")
					&& !costs.equals("")
					&& !costsfactor.equals("")
					&& !passenger.equals("")
					&& !speed.equals(""))
				{
					Logger.__debug(
						"Name: " + name + "\n" +
						"Manufacturer: " + manufacturer + "\n" +
						"Price: " + Integer.parseInt(price) + "\n" +
						"Costs: " + Integer.parseInt(costs) + "\n" +
						"CostFactor: " + Float.parseFloat(costsfactor) + "\n" +
						"Passenger: " + Integer.parseInt(passenger) + "\n" +
						"Speed: " + Float.parseFloat(speed));
					addTemplateTrain(new TrainTemplate(name,
						name,
						manufacturer,
						Integer.parseInt(price),
						Integer.parseInt(costs),
						Float.parseFloat(costsfactor),
						Integer.parseInt(passenger),
						Float.parseFloat(speed)));
					name = "";
					manufacturer = "";
					price = "";
					costs = "";
					costsfactor = "";
					passenger = "";
					speed = "";
				}
			}
		}
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
	public List<Train> getTrains()
	{
		return _trainList;
	}

	/**
	 * Adds a train to the list of bought trains and adds the management service as observer.
	 * This method does not check for any valid trains (not null, etc.)!
	 * 
	 * @param train The new train to add.
	 */
	public void addTrain(Train train)
	{
		try
		{
			METRO.__gameState.withdrawMoney(train.getPrice());
			_trainList.add(train);
			train.addObserver(this);
		}
		catch(NotEnoughMoneyException e)
		{
			NotificationServer.publishNotification("You have not enough money to buy a train of " + train.getModelName(), NotificationType.GAME_ERROR);
			Logger.__info("There's not enough money to add a " + train.getModelName() + " train.\n" + e.getMessage());
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
	public List<TrainTemplate> getTemplateTrains()
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
		_trainLineList.remove(line); // remove old line, because maybe line.equals(old-line) == true
		_trainLineList.add(line); // adds the new line to the list
		_trainLineDrawingService.calcLinePositions(_trainLineList);
	}

	/**
	 * Adds a train line to the list of lines. An older version of this line will be removed.
	 * 
	 * @param lineName The name of the line to add.
	 */
	public void addLine(String lineName)
	{
		addLine(getLine(lineName));
	}

	/**
	 * Removes a line from the list.
	 * 
	 * @param line The line to remove.
	 */
	public void removeLine(TrainLine line)
	{
		if(line == null) return;
		_trainLineList.remove(line);
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
		for(TrainLine line : _trainLineList)
		{
			if(line.getName().equals(lineName))
			{
				return line.getColor();
			}
		}
		return null;
	}

	/**
	 * List of train lines. This is only a copy of the original!
	 * 
	 * @return A list with all train lines.
	 */
	public List<TrainLine> getLines()
	{
		return new ArrayList<TrainLine>(_trainLineList);
	}

	/**
	 * Searches for a special line.
	 * 
	 * @param lineName The name of the line.
	 * @return The line. null if line does not exist.
	 */
	public TrainLine getLine(String lineName)
	{
		for(TrainLine line : _trainLineList)
		{
			if(line.getName().equals(lineName))
			{
				return line;
			}
		}
		return null;
	}

	/**
	 * Adds a station to the list of stations. If this fails, due to a low balance, nothing changes.
	 * 
	 * @param trainStation The station to add.
	 */
	public void addStation(TrainStation trainStation)
	{
		try
		{
			GameState.getInstance().withdrawMoney(TrainStation.__price);
			_stationList.add(trainStation);
		}
		catch(NotEnoughMoneyException e)
		{
			NotificationServer.publishNotification("You have not enough money for a station.", NotificationType.GAME_ERROR);
			Logger.__info("Not enough Money to buy station.\n" + e.getMessage());
		}
	}

	/**
	 * @return A list of all train stations. This is only a copy of the original!
	 */
	public List<TrainStation> getStations()
	{
		return new ArrayList<>(_stationList);
	}

	/**
	 * Draws all train lines.
	 * 
	 * @param offset The map offset in pixel.
	 */
	public void drawLines(Point offset)
	{
		_trainLineDrawingService.drawLines(offset, _trainLineList);
	}

	/**
	 * Draws all stations.
	 * 
	 * @param offset The map offset in pixel.
	 */
	public void drawStations(Point offset)
	{
		for(TrainStation station : _stationList)
		{
			station.draw(offset);
			station.addRandomPassenger(getStrength(station.getPosition()));
			station.handlePassenger();
		}
	}

	/**
	 * Gets the maximum density of the citizens at the given point.
	 * 
	 * @param pos The position to check.
	 * @return The maximum density at the given position.
	 */
	private int getStrength(Point pos)
	{
		int maxStrength = 0;

		for(TravelerSpot spot : _travelerSpotList)
		{
			// if(spot.contains(pos)) // is given point in circle
			// {
			int strength = spot.getStrength(pos);
			if(strength > maxStrength)
			{
				maxStrength = strength;
			}
			// }
		}

		return maxStrength;
	}

	/**
	 * Draws all trains.
	 * 
	 * @param offset The map offset in pixel.
	 */
	public void drawTrains(Point offset)
	{
		float deltaTime = System.nanoTime() - _lastRenderTime;
		_lastRenderTime = System.nanoTime();

		lockNodes();

		for(Train train : _trainList)
		{
			train.draw(offset);
			train.drive(canMove(train, deltaTime), deltaTime);
		}

		RailwayNodeOverseer.unlockAllSignals();
	}

	/**
	 * Locks all nodes for the train movement, so that there won't be any errors.
	 */
	private void lockNodes()
	{
		for(Train train : getTrains())
		{
			Point nextNode = train.getNextNode();
			Point currentNode = train.getCurrentNode();

			// set signal for train
			RailwayNodeOverseer.getNodeByPosition(currentNode).setSignalValue(
				RailwayNodeOverseer.getNodeByPosition(nextNode),
				train);

			// find train thats nearer on the nextNode and set therefore the signal to this node
			for(Train t : getTrains())
			{
				if(!t.equals(train))
				{
					if(nextNode.equals(t.getNextNode()) // driving to same node
						&& currentNode.equals(t.getCurrentNode()) // same last node. In addition to first condition --> trains driving in same direction
						&& train.calcPosition().distance(nextNode) >= t.calcPosition().distance(nextNode)) // train behind t --> train can't move, t can
					{
						RailwayNodeOverseer.getNodeByPosition(currentNode).setSignalValue(
							RailwayNodeOverseer.getNodeByPosition(nextNode),
							t);
					}
				}
			}
		}
	}

	/**
	 * Checks if the train can move by having a specific distance to the train ahead.
	 * 
	 * @param train The train which may can move.
	 * @return True when the train can move, false when not.
	 */
	private boolean canMove(Train train, float deltaTime)
	{
		if(train.getLine() == null) return false;
		
		Point currentNode = train.getCurrentNode();
		Point currentNodeAfterMove = train.getCurrentNode(deltaTime);
		Point nextNodeAfterMove = train.getNextNode(deltaTime);

		if(currentNode.equals(currentNodeAfterMove)) // train does not pass a signal
		{
			return RailwayNodeOverseer.getNodeByPosition(currentNode).getSignalValue(
				RailwayNodeOverseer.getNodeByPosition(train.getNextNode()),
				train);
		}

		// when the train will pass a node/signal, check if any other train is in this area or if this train can move forward.
		// When there's any other node in the next area, the signal at "currentNode" will change to "green" which is wrong, because
		// then other train might rush into this train.
		return RailwayNodeOverseer.getNodeByPosition(currentNodeAfterMove).getSignalValue(
			RailwayNodeOverseer.getNodeByPosition(nextNodeAfterMove),
			train);
	}

	/**
	 * Checks if a color is already in use.
	 * 
	 * @param color The color that might be used.
	 * @return True when color is used, false when color is free.
	 */
	public boolean isLineColorUsed(Color color)
	{
		for(TrainLine line : _trainLineList)
		{
			if(line.getColor().equals(color)) return true;
		}
		return false;
	}

	@Override
	public void update(Observable o, Object arg)
	{
		if(o instanceof Train)
		{
			Train t = (Train)o;
			Point currNode = t.calcCurrentNode(); // don't use the current node thats buffered in the Train class, it's outdated! Calculate actual current node.
			boolean stationFound = false;

			for(TrainStation station : _stationList)
			{
				if(station.getPosition().equals(currNode))
				{
					t.waitFor(3000);
					stationFound = true;
					//FIXME all stations will lose passengers when the train stops at one station
					TrainStation s = getStation(currNode);
					s.movePassenger(t.getMaxPassenger() - t.getCurrPassenger(), 3000 * (long)1e6);
					break;
				}
			}

			if(!stationFound) // not on a station --> normally update the position
			{
				t.updatePositionNodes();
			}
		}
	}

	private TrainStation getStation(Point currNode)
	{
		for(TrainStation station : _stationList)
		{
			if(station.equals(currNode))
			{
				return station;
			}
		}

		return null;
	}

	/**
	 * @return A list of all traveler spots
	 */
	public List<TravelerSpot> getTravelerSpots()
	{
		return new ArrayList<>(_travelerSpotList);
	}
}
