package metro.TrainManagement.Trains;

import metro.TrainManagement.Lines.TrainLine;

/**
 * A Train can carry passengers, costs a bit to buy and maintain it.
 * The costs of maintaining will also increase every month by a given factor (_costsFactor) and there is a maximum amount of passengers per train as well.
 * All data about the trains are stored in the ./data/trains.txt file and are read & parsed by the GameState class.
 * 
 * @author hauke
 *
 */

public class Train
{
	private String _name, _manufacturer;
	private int _price, _costs, _maxPassengers, _currPassengers;
	private float _costsFactor;
	private TrainLine _trainLine;

	/**
	 * Creates a new train with the following properties.
	 * Every data for the trains are in the ./data/trains.txt file and will be read & parsed by the GameState class.
	 * 
	 * @param name Name of the train.
	 * @param manufacturer Manufacturer of the train.
	 * @param price Price for buying the train.
	 * @param costs Costs per month.
	 * @param costsFactor Monthly increase of the costs.
	 * @param passengers Maximum amount of passengers per train.
	 */
	public Train(String name, String manufacturer, int price, int costs, float costsFactor, int passengers)
	{
		_name = name;
		_manufacturer = manufacturer;
		_price = price;
		_costs = costs;
		_costsFactor = costsFactor;
		_maxPassengers = passengers;
		_currPassengers = 0;
	}

	/**
	 * Creates a new Train with the properties of the given one.
	 * This constructor constructs a copy if you want so.
	 * 
	 * @param trainTemplate A train that should be copied.
	 */
	public Train(Train trainTemplate)
	{
		this(trainTemplate.getName(),
			trainTemplate.getManufacturer(),
			trainTemplate.getPrice(),
			trainTemplate.getCosts(),
			trainTemplate.getCostFactor(),
			trainTemplate.getMaxPassenger());
	}

	/**
	 * Sets the line of this train.
	 * 
	 * @param line The new line.
	 */
	public void setLine(TrainLine line)
	{
		_trainLine = line;
	}

	/**
	 * @return The model designation of the train.
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * @return Gets the name of the manufacturer of this train.
	 */
	public String getManufacturer()
	{
		return _manufacturer;
	}

	/**
	 * @return Gets the price this train costs.
	 */
	public int getPrice()
	{
		return _price;
	}

	/**
	 * @return Gets the monthly costs of this train without any consideration of the age.
	 */
	public int getCosts()
	{
		return _costs;
	}

	/**
	 * @return Gets the monthly increase of this train.
	 */
	public float getCostFactor()
	{
		return _costsFactor;
	}

	/**
	 * @return Gets the maximum amount of passengers that can travel with this train.
	 */
	public int getMaxPassenger()
	{
		return _maxPassengers;
	}

	/**
	 * @return Gets the current amount of passenger that are travelling with this train.
	 */
	public int getCurrPassenger()
	{
		return _currPassengers;
	}

	/**
	 * @return The train line this train is driving on.
	 */
	public TrainLine getLine()
	{
		return _trainLine;
	}
}
