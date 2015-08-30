package metro.TrainManagement.Lines;

import java.awt.Color;
import java.util.ArrayList;

import metro.TrainManagement.Nodes.RailwayNode;

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
	private ArrayList<RailwayNode> _listOfNodes;
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
	public TrainLine(ArrayList<RailwayNode> connections, String name, Color lineColor)
	{
		if(connections != null) _listOfNodes = connections;
		else _listOfNodes = new ArrayList<RailwayNode>();
		_name = name;
		_lineColor = lineColor;
	}

	/**
	 * Checks if a node is included in this train line.
	 * 
	 * @param node The node that's possibly in here.
	 * @return True if the node is already included.
	 */
	public boolean contains(RailwayNode node)
	{
		return _listOfNodes.contains(node);
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

	/**
	 * Clears this train line, which means that the color of all nodes will be removed.
	 */
	public void clear()
	{
		for(RailwayNode node : _listOfNodes)
		{
			node.removeColor(_lineColor);
		}
	}

	/**
	 * Check if this train line is a valid line. A line is valid when both of the following conditions apply:
	 * 1.) There're more than 2 nodes in this line.
	 * 2.) There're exactly 2 end/start nodes in this line. These nodes only have one neighbor.
	 * 
	 * @return True when valid.
	 */
	public boolean isValid()
	{
		int amountEndNodes = 0;

		for(RailwayNode node : _listOfNodes)
		{
			if(node.isEndOfLine(_lineColor)) amountEndNodes++;
		}

		return _listOfNodes.size() >= 2 && amountEndNodes == 2;
	}

	/**
	 * Returns the color of this line.
	 * 
	 * @return The color.
	 */
	public Color getColor()
	{
		return _lineColor;
	}

	/**
	 * Returns the whole list with all nodes of this line.
	 * 
	 * @return The list with all nodes.
	 */
	public ArrayList<RailwayNode> getNodes()
	{
		return _listOfNodes;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof TrainLine)
		{
			TrainLine line = (TrainLine)o;
			return _listOfNodes.equals(line._listOfNodes);// old: line._name.equals(_name) && line._lineColor.equals(_lineColor); // equal when color and name are equal
		}
		return false;
	}
}
