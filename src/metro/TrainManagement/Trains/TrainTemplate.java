package metro.TrainManagement.Trains;

/**
 * A train template is a final train. There are also less available properties e.g. has a train template no line and no current passengers.
 * 
 * Info: For more information about lines just look into the {@code line} class ;)
 * 
 * @author hauke
 *
 */

public class TrainTemplate
{
	protected String _name, _manufacturer;
	protected int _price, _costs, _maxPassengers;
	protected float _costsFactor, _speed;

	/**
	 * Creates a new train template with the following properties.
	 * Every data for trains are in the ./data/trains.txt file and will be read & parsed by the GameState class.
	 * This is not a actual train, for more information about trains look into the {@code train} class.
	 * 
	 * @param name The model name of the train.
	 * @param manufacturer Manufacturer of the train.
	 * @param price Price for buying the train.
	 * @param costs Costs per month.
	 * @param costsFactor Monthly increase of the costs.
	 * @param passengers Maximum amount of passengers per train.
	 * @param speed The speed in nodes per second.
	 */
	public TrainTemplate(String name, String manufacturer, int price, int costs, float costsFactor, int passengers, float speed)
	{
		_name = name;
		_manufacturer = manufacturer;
		_price = price;
		_costs = costs;
		_costsFactor = costsFactor;
		_maxPassengers = passengers;
		_speed = speed;
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
	 * @return Gets the speed in nodes per second.
	 */
	public float getSpeed()
	{
		return _speed;
	}
}
