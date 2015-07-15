package metro.TrainManagement.Lines;

import java.awt.Color;
import java.util.ArrayList;

/**
 * A Train line is a collection of several railway connections that touches each other (building one big line).
 * Trains can only move on one of this train lines.
 * To create this line, the TrainLineSelectTool is used to select different nodes, put them into connections and building this line.
 * 
 * @author hauke
 *
 */
public class TrainLine
{
	private ArrayList<RailwayConnection> _listOfConnections;
	private Color _lineColor;
	private String _name;

	/**
	 * Creates an empty new train line with a given title and a color.
	 * 
	 * @param name The title.
	 * @param lineColor The color.
	 */
	public TrainLine(String name, Color lineColor)
	{
		this(null, name, lineColor);
	}

	/**
	 * Creates a (partly) filled train line with a name and a color.
	 * 
	 * @param connections All connections that should be added.
	 * @param name The name.
	 * @param lineColor The color.
	 */
	public TrainLine(ArrayList<RailwayConnection> connections, String name, Color lineColor)
	{
		if(connections != null) _listOfConnections = connections;
		else _listOfConnections = new ArrayList<RailwayConnection>();
		_name = name;
		_lineColor = lineColor;
	}

	/**
	 * Gets the color of the train line.
	 * 
	 * @return The color.
	 */
	public Color getColor()
	{
		return _lineColor;
	}

	/**
	 * Checks if a connections is included in this train line.
	 * 
	 * @param connection The connection that's possibly in here.
	 * @return True if the connection is already included.
	 */
	public boolean contains(RailwayConnection connection)
	{
		return _listOfConnections.contains(connection);
	}

	/**
	 * Gets the name/title of this train line.
	 * 
	 * @return The name.
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * Updates the name of this train line.
	 * 
	 * @param newName The new name.
	 */
	public void setName(String newName)
	{
		_name = newName;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof TrainLine)
		{
			TrainLine line = (TrainLine)o;
			return line._name.equals(_name) && line._lineColor.equals(_lineColor); // equal when color and name are equal
		}
		return false;
	}
}
