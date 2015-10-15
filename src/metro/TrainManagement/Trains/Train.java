package metro.TrainManagement.Trains;

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
	private int _price, _costs, _passengers;
	private float _costsFactor;

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
		_passengers = passengers;
	}

	/**
	 * @return The model designation of the train.
	 */
	public String getName()
	{
		return _name;
	}
}
