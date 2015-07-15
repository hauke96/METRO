package metro.TrainManagement.Lines;

import java.awt.Color;
import java.util.ArrayList;

import metro.TrainManagement.Nodes.RailwayNode;

/**
 * A RailwayConnection is the connection between two railway nodes. This connection is used by train lines to move trains.
 * All connections have a color.
 * 
 * @author hauke
 *
 */
public class RailwayConnection
{
	private ArrayList<Color> _colorList;
	private RailwayNode[] _coordinates;

	/**
	 * Creates a new connection of two railway nodes. The order (with regard to the equals-method) of the nodes is irrelevant!
	 * 
	 * @param node1 The first node.
	 * @param node2 The second node.
	 */
	public RailwayConnection(RailwayNode node1, RailwayNode node2)
	{
		_colorList = new ArrayList<Color>();
		_coordinates = new RailwayNode[2];
		_coordinates[0] = node1;
		_coordinates[1] = node2;
	}

	/**
	 * Sorts the colors based on their color index. NOT IMPLEMENTED YET!
	 */
	// FIXME: Implement me :(
	private void sortList()
	{
		// TODO: implement sort algorithm to sort by color
		// for(int i = 0; i < _colorList.size() - 1; i++)
		// {
		// boolean somethingChanged = false;
		// for(int k = 0; k < _colorList.size() - 1; k++)
		// {
		// if(getColorIndex(_colorList.get(k)) < getColorIndex(_colorList.get(k+1)))
		// {
		// //Swap element k and k+1
		// }
		// }
		// }
	}

	/**
	 * Adds a color to this railway connection.
	 * 
	 * @param color The color to add.
	 */
	public void addColor(Color color)
	{
		_colorList.add(color);
	}

	/**
	 * Gets an ArrayList of all colors from this connection.
	 * 
	 * @return A list of all colors.
	 */
	public ArrayList<Color> getColors()
	{
		return _colorList;
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean result = false;
		if(obj instanceof RailwayConnection)
		{
			RailwayConnection connection = (RailwayConnection)obj;

			// Check if both coordinated are equal
			result = connection._coordinates[0].equals(_coordinates[0])
				&& connection._coordinates[1].equals(_coordinates[1])
				|| connection._coordinates[0].equals(_coordinates[1])
				&& connection._coordinates[1].equals(_coordinates[0]);
		}

		return result;
	}

	/**
	 * Gets the HSV/HSB value of the RGP color as integer. This is a unique index of this color.
	 * 
	 * @param color The color which index you want to have.
	 * @return An unique index of this color.
	 */
	private int getColorIndex(Color color)
	{
		return (int)(Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0] * 255);
	}
}
