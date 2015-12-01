package metro.TrainManagement.Lines;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import metro.METRO;
import metro.TrainManagement.Nodes.RailwayNode;

/**
 * A Train line is a collection of several railway connections that touches each other (building one big line).
 * Trains can only move on one of this train lines.
 * To create this line, the TrainLineSelectTool is used to select different nodes, put them into connections and building this line.
 * A train line in immutable which means that the line stays sorted as it is. To change something you have to create a new line.
 * 
 * @author hauke
 *
 */
public class TrainLine
{
	private final ArrayList<RailwayNode> _listOfNodes;
	private Color _lineColor;
	private String _name;
	private final double _length;

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
		METRO.__debug("[CalcTrainLineLength]");
		_length = calcLength();
		METRO.__debug("Length: " + _length + "\n");
	}

	/**
	 * Calculates the length of the train line.
	 * The length is scaled by the {@code METRO.__baseNetSpacing} and is NOT given in pixel.
	 * 
	 * @return The length of the train line.
	 */
	private double calcLength()
	{
		return calcPartLength(0, _listOfNodes.size() - 1);
	}

	/**
	 * calculates the distance between two nodes.
	 * 
	 * @param startNode The index of the start node.
	 * @param endNode The index of the end node.
	 * @return The distance between them.
	 */
	private double calcPartLength(int startNode, int endNode)
	{
		double length = 0.0;

		for(int i = startNode; i < endNode; ++i) // don't cycle over the last element of the list because of (i + 1) index usages
		{
			int xDiff = Math.abs(_listOfNodes.get(i).getPosition().x - _listOfNodes.get(i + 1).getPosition().x);
			int yDiff = Math.abs(_listOfNodes.get(i).getPosition().y - _listOfNodes.get(i + 1).getPosition().y);

			METRO.__debug("    i: " + i + " :: " +_listOfNodes.get(i).getPosition() + " --> " + _listOfNodes.get(i + 1).getPosition());
			
			// distance between two nodes
			double v = Math.sqrt(Math.hypot(xDiff, yDiff));
			length += v;
		}

		return length;
	}

	/**
	 * Calculates the position of a train that traveled a specific distance from the start node.
	 * The idea of searching the position is to find two nodes that are around the distance (E.g. the distance is 5 and one node is at 4.5 and one at 5.5).
	 * 
	 * @param distance The traveled distance of a train.
	 * @param startNode The node the train started.
	 * @return The position of the train.
	 */
	public Point2D getPositionOfTrain(double distance, RailwayNode startNode)
	{
		// to be able to use one algorithm that starts at the first node, convert the distance in
		if(_listOfNodes.get(_listOfNodes.size() - 1).equals(startNode)) distance = _length - distance;
		distance *= _length;

		double length = 0.0,
			lastLength = 0.0;
		Point nodePre = null,
			nodeSuc = null;

		// iterate over the nodes and calculate the distance until it's over the distance or at the end of the list
		for(int i = 0; i < _listOfNodes.size() - 1 && nodePre == null; ++i)
		{
			lastLength = calcPartLength(i, i + 1);
			length += lastLength;
			if(length >= distance)
			{
				nodePre = _listOfNodes.get(i).getPosition();
				nodeSuc = _listOfNodes.get(i + 1).getPosition();
			}
		}
		// calculate the rest of the distance and some delta values to work with it below
		distance -= length; // maybe (length - lastLength);
		int deltaX = nodePre.x - nodeSuc.x;
		int deltaY = nodePre.y - nodeSuc.y;

		// calculate the distance between the two nodes
		double s = Math.sqrt(Math.hypot(deltaX, deltaX));
		// calculate the ratio of the node distance to the rest of the distance
		double ratio = distance / s;
		// determine the smaller value of the two x and y values
		double x = nodePre.x <= nodeSuc.x ? nodePre.x : nodeSuc.x;
		double y = nodePre.y <= nodeSuc.y ? nodePre.y : nodeSuc.y;
		// and finally calculate the position by adding ratio based value to the smallest x and y coordinate
		return new Point2D.Double(x + ratio * deltaX, y + ratio * deltaY);
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
			if(isEndNode(node, _listOfNodes)) ++amountEndNodes;
		}

		return _listOfNodes.size() >= 2 && amountEndNodes == 2;
	}

	/**
	 * Checks if the given node is one of the end nodes in the list of given nodes.
	 * 
	 * @param node The node that's probably an end node.
	 * @param listOfNodes The list of all available nodes.
	 * @return True when the given node is an end node, false if not.
	 */
	public static boolean isEndNode(RailwayNode node, ArrayList<RailwayNode> listOfNodes)
	{
		int counter = 0;

		for(RailwayNode neighborNode : node.getNeighbors())
		{
			for(RailwayNode Node : listOfNodes)
			{
				if(neighborNode.equals(Node)) ++counter;
			}
		}

		return counter == 1;
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
			return _listOfNodes.equals(line._listOfNodes);
		}
		return false;
	}
}
