package metro.TrainManagement.Lines;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Graphics.Draw;
import metro.TrainManagement.Nodes.RailwayNode;

/**
 * A Train line is a collection of several railway connections that touches each other (building one big line) or mostly touching each other (building an invalid line).
 * Trains can only move on one of this train lines.
 * To create a line, the {@code TrainLineSelectTool} is used to select different nodes, put them into connections and building this line.
 * A train line in immutable which means that the line stays sorted as it is. To change something you have to create a new line.
 * "Sorted" means that there're no jumps in the connections (e.g. (0,1) -> (0,5) -> (0,3) but (0,1) -> (0,3) -> (0,5) ).
 * An invalid line (= a line with gaps) is partly sorted, so every part is sorted in itself. The order of these parts must not be sorted.
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
	private final int _thickness;

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
		if(connections != null)
		{
			_listOfNodes = sortNodes(connections, getAnyEndNode(connections));
		}
		else
		{
			_listOfNodes = new ArrayList<RailwayNode>();
		}
		_name = name;
		_lineColor = lineColor;
		_thickness = 3;
		METRO.__debug("[CalcTrainLineLength]");
		_length = calcLength();
		METRO.__debug("Length: " + _length);
	}

	/**
	 * Sorts the given list of nodes by choosing one start node an then the neighbors.
	 * 
	 * @param list The list of nodes that should be sorted.
	 * @param startNode An end node of the line.
	 * @return A sorted list with all the nodes.
	 */
	private ArrayList<RailwayNode> sortNodes(ArrayList<RailwayNode> list, RailwayNode startNode)
	{
		if(list.size() <= 1 || startNode == null) return list;

		METRO.__debug("[TrainLineSorting]\n"
			+ "Start Node: " + startNode.getPosition() + "\n"
			+ "Line length (amount of nodes): " + list.size());

		ArrayList<RailwayNode> newList = new ArrayList<>();
		// be sure that an end node is the first element in this list
		newList.add(startNode);
		list.remove(startNode);

		// create a dummy node to enter toe first for-loop
		RailwayNode neighbor = new RailwayNode(null, null);
		int listLength = list.size();

		for(int i = 0; i < listLength; ++i)
		{
			neighbor = null;
			int k; // the index of the neighbor for the node at index i
			// search for the left neighbor of node i
			for(k = 0; k < list.size() && neighbor == null; ++k)
			{
				if(newList.get(i).isNeighbor(list.get(k))) neighbor = list.get(k);
			}

			if(neighbor != null)
			{
				METRO.__debug(newList.get(i).getPosition() + "  ==>  " + neighbor.getPosition());
				newList.add(neighbor);
				list.remove(neighbor);
			}
			else
			{
				METRO.__debug("No Neighbor found, try other start node ...");
				/*
				 * When there's no neighbor, choose another start node and try to sort this branch.
				 * This also means that this line is invalid!
				 */
				newList.addAll(sortNodes(list, getAnyEndNode(list)));
			}
		}

		return newList;
	}

	/**
	 * This method determines an end node of the given line.
	 * It's not specified which node of the two end nodes will be chosen, because the algorithm will take the first in the list of nodes.
	 * 
	 * @param list A list with all nodes where the end nodes should be determined.
	 * @return One of the two end nodes of the given line.
	 */
	private RailwayNode getAnyEndNode(ArrayList<RailwayNode> list)
	{
		for(RailwayNode node : list)
		{
			if(TrainLine.isEndNode(node, list)) return node;
		}

		return null;
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
		return new Point2D.Double(x + ratio * deltaX, y + ratio * deltaY); // nodePre.x, nodePre.y); // <-- works!
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
	 * Check if this train line is a valid line. A line is valid when both of the following conditions apply:
	 * 1.) There're more than 2 nodes in this line.
	 * 2.) There're exactly 2 end/start nodes in this line. These nodes only have one neighbor.
	 * 
	 * @param listOfNodes The list of nodes that may be valid.
	 * @return True when valid.
	 */
	public static boolean isValid(ArrayList<RailwayNode> listOfNodes)
	{
		int amountEndNodes = 0;

		for(RailwayNode node : listOfNodes)
		{
			if(isEndNode(node, listOfNodes)) ++amountEndNodes;
		}

		return listOfNodes.size() >= 2 && amountEndNodes == 2;
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

	/**
	 * Checks if two lines are equal. They are equal when line A has the same nodes as B.
	 * The order of the nodes doesn't matter.
	 */
	public boolean equals(Object o)
	{
		if(o instanceof TrainLine)
		{
			TrainLine line = (TrainLine)o;

			if(line.getNodes().size() != _listOfNodes.size()) return false;

			boolean isEqual = true;

			for(RailwayNode node : line.getNodes())
			{
				isEqual &= _listOfNodes.contains(node);
			}

			return isEqual;
		}
		return false;
	}

	/**
	 * Draws the train line. The line doesn't need to be valid because all parts are drawn by their own.
	 * 
	 * @param offset The map offset in pixel.
	 * @param sp The sprite batch to draw on.
	 * @param map A map (node -> Integer) that says how many lines are already drawn on this node (normally all integers are 0).
	 */
	public void draw(Point offset, SpriteBatch sp, HashMap<RailwayNode, Integer> map)
	{
		Draw.setColor(isValid(_listOfNodes) ? _lineColor : METRO.__metroRed);
		for(int i = 0; i < _listOfNodes.size() - 1; i++)
		{
			// the list is sorted so we know that i+1 is the direct neighbor
			RailwayNode node = _listOfNodes.get(i);
			RailwayNode neighbor = _listOfNodes.get(i + 1);

			Point position, positionNext;
			position = new Point(offset.x + node.getPosition().x * METRO.__baseNetSpacing,
				offset.y + node.getPosition().y * METRO.__baseNetSpacing); // Position with offset etc.
			positionNext = new Point(offset.x + neighbor.getPosition().x * METRO.__baseNetSpacing,
				offset.y + neighbor.getPosition().y * METRO.__baseNetSpacing); // Position with offset etc. for second point

			if(node.isNeighbor(neighbor))
			{
				drawColoredLine(offset, position, positionNext, map.get(neighbor).intValue());

				// update the map value
				int layers = map.get(node).intValue();
				map.remove(node);
				map.put(node, layers + 1);
			}

			Draw.Circle(position.x - _thickness, position.y - _thickness, 2 * _thickness + 1);
		}

		// Draw also a circle for the last node which won't be drawn in the for loop
		Point position = new Point(offset.x + _listOfNodes.get(_listOfNodes.size() - 1).getPosition().x * METRO.__baseNetSpacing,
			offset.y + _listOfNodes.get(_listOfNodes.size() - 1).getPosition().y * METRO.__baseNetSpacing); // Position with offset etc.
		Draw.Circle(position.x - _thickness, position.y - _thickness, 2 * _thickness + 1);
	}

	private void drawColoredLine(Point offset, Point position, Point positionNext, int layer)
	{
		Point diff = new Point((position.y - positionNext.y) / METRO.__baseNetSpacing,
			(position.x - positionNext.x) / METRO.__baseNetSpacing);

		if(diff.x != 0 && diff.y != 0)
		{
			if(diff.x == 1 && diff.y == 1) diff.x = -1;
			else if(diff.x == 1 && diff.y == -1) diff.y = 1;
		}
		// TODO: make more accurate draw algo. This won't work for vertical lines :(
		// FIXME Layers not working when trainline has been edited :(
		Draw.Line(position.x - (layer * 4) * diff.x,
			position.y - (layer * 4) * diff.y,
			positionNext.x - (layer * 4) * diff.x,
			positionNext.y - (layer * 4) * diff.y,
			_thickness);
	}
}
