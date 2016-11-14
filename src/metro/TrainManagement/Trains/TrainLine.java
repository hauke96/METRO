package metro.TrainManagement.Trains;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import metro.METRO;
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
public class TrainLine implements Cloneable
{
	private final ArrayList<RailwayNode> _listOfNodes;
	private Color _lineColor;
	private String _name;
	private final double _length;
	private final double[] _nodeDistances;

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
	@SuppressWarnings("unchecked")
	public TrainLine(ArrayList<RailwayNode> connections, String name, Color lineColor)
	{
		if(connections != null)
		{
			_listOfNodes = sortNodes((ArrayList<RailwayNode>)connections.clone(), getAnyEndNode(connections));
		}
		else
		{
			_listOfNodes = new ArrayList<RailwayNode>();
		}
		_name = name;
		_lineColor = lineColor;
		_length = calcLength();
		_nodeDistances = new double[_listOfNodes.size()];

		METRO.__debug("Calculate line length: " + _length);

		preprocessNodeDistances();
	}

	/**
	 * Calculates the distance to every node beginning at node 0.
	 */
	private void preprocessNodeDistances()
	{
		for(int i = 0; i < _listOfNodes.size(); i++)
		{
			_nodeDistances[i] = calcPartLength(0, i);
		}
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

		METRO.__debug(""
			+ "Start Node: " + startNode.getPosition() + "\n"
			+ "Line length (amount of nodes): " + list.size());

		ArrayList<RailwayNode> newList = new ArrayList<>();
		// be sure that an end node is the first element in this list
		newList.add(startNode);
		list.remove(startNode);

		// create a dummy node to enter toe first for-loop
		RailwayNode neighbor = null;
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
			if(TrainLine.__isEndNode(node, list)) return node;
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
	 * The idea of searching the position is to find two nodes that are around the distance.
	 * (E.g. the distance is 5 and one node is at 4.5 and one at 5.5). There's a detailed explanation about the calculation in the methods body.
	 * 
	 * @param distance The traveled distance of a train.
	 * @param startNode The index of the node the train started.
	 * @return The position of the train.
	 */
	public Point2D getPositionOfTrain(double distance, int startNode)
	{
		// When start node is out of bounds, return the first/last nodes position
		if(startNode < 0) return _listOfNodes.get(0).getPosition();
		if(_listOfNodes.size() <= startNode) return _listOfNodes.get(_listOfNodes.size() - 1).getPosition();

		double length = 0.0,
			lastLength = 0.0;

		Point nodePre = _listOfNodes.get(startNode).getPosition(),
			nodeSuc = _listOfNodes.get(startNode + 1).getPosition();

		/*
		 * The small bit of the distance thats between the last to nodes:
		 * ------ (node x-1) ---------- (node x) ---....... (node x+1) .......
		 * Where --- is the distance that's left over and ... some distance that irrelevant.
		 */
		double distanceLeftOver = distance;

		// iterate over the nodes and calculate the distance until it's over the distance or at the end of the list
		lastLength = calcPartLength(startNode, startNode + 1);

		// iterate over all nodes to find (x) and (x+1) which are describes below
		for(int i = startNode + 1; i + 1 < _listOfNodes.size() && nodeSuc != null && length + lastLength < distance; ++i)
		{
			length += lastLength;
			distanceLeftOver -= lastLength;

			nodePre = _listOfNodes.get(i).getPosition();
			nodeSuc = _listOfNodes.get(i + 1).getPosition();
			lastLength = calcPartLength(i, i + 1);
		}

		/* @formatter:off
		*
		*
		* 	!!	MORE OR LESS COMPLEX CALCULATION AND IDEA	!!
		* 	!!	 SO PLEASE READ BEFORE CHANGE SOMETHING		!!
		* 
		* 
		* we have node (x) and (x+1). The distance ended between (x) and (x+1) (as you can see in the comment of the distanceLeftOver variable above).
		* We have now a triangle:
		* 
		*		       c
		* 		(x)------
		* 		  \     |
		* 		   \    | c
		* 		    \   |
		* 		   l `  |
		* 		      ` |
		* 		       `|
		* 		      (x+1)
		*
		* This triangle is a right triangle, so we can use pythagoras for calculation.
		* We want to know the distance c because the end position (where the \ line stops and the ` line begins) is the vector
		* 
		* 		(x.X + c, x.Y + c)
		* 
		* We can calculate the c with
		* 
		*		c = (l / 2)
		* 
		* Thats what the following calculation does.
		*
		* In addition to this basic method explained above, we have to check of there's a diagonal orientation of the nodes at all.
		* The calculation below will check this via a deltaX/Y value, but more information in the comment of these variables...
		*
		* @formatter:on
		*/

		// calculate delta values of the coordinates.
		// This will give us an integer between -1 and 1 which gives information about the orientation of the nodes (diagonal vertical, horizontal),
		// including the direction (left, right, up, down) by 1 and -1.
		int deltaX = nodeSuc.x - nodePre.x;
		int deltaY = nodeSuc.y - nodePre.y;

		// calculate the distance between the two nodes
		double cX = lastLength == 0 ? 0 : (distanceLeftOver / lastLength) * deltaX;
		double cY = lastLength == 0 ? 0 : (distanceLeftOver / lastLength) * deltaY;

		// and finally calculate the position by adding ratio based value to the x and y coordinate
		return new Point2D.Double(nodePre.x + cX, nodePre.y + cY);
	}

	/**
	 * Calculates the two nodes that are around the given distance beginning at the given start node.
	 * 
	 * @param distance The distance from the start node that's between two nodes.
	 * @param startNode The start node from where the distance counts.
	 * @param direction The direction of the train.
	 * @return The nodes this train is between. [0] is the current node, [1] the next node, [2].x the distance and [2].y the remaining distance.
	 */
	public Point[] getNode(double distance, int startNode, int direction)
	{
		Point nodes[] = new Point[2];

		// When start node is out of bounds, return the first/last nodes position
		if(startNode < 0)
		{
			nodes[0] = _listOfNodes.get(0).getPosition();
			nodes[1] = _listOfNodes.get(1).getPosition();
			return nodes;
		}
		if(_listOfNodes.size() < startNode || distance > _length)
		{
			nodes[0] = _listOfNodes.get(_listOfNodes.size() - 2).getPosition();
			nodes[1] = _listOfNodes.get(_listOfNodes.size() - 1).getPosition();
			return nodes;
		}

		double length = 0.0,
			lastLength = calcPartLength(startNode, startNode + direction);

		// iterate over all pre-processed node distances to find the two nodes that are around the train (given by the parameters of this method).
		int i;
		for(i = startNode + direction; 0 <= i && i < _listOfNodes.size() && length + lastLength <= distance; i += direction)
		{
			length += lastLength;
			lastLength = calcPartLength(
				direction > 0 ? i : i + direction,
				direction > 0 ? i + direction : i);
		}

		// the sucessor with index i was found, the predecessor is therefore i - direction.
		nodes[0] = _listOfNodes.get(i - direction).getPosition();
		nodes[1] = _listOfNodes.get(i).getPosition();

		return nodes;
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
	 * @return The amount of nodes of this line (not the length or something).
	 */
	public int getAmountOfNodes()
	{
		return _listOfNodes.size();
	}

	/**
	 * Check if this train line is a valid line. A line is valid when both of the following conditions apply:
	 * 1.) There're more than 2 nodes in this line.
	 * 2.) There're exactly 2 end/start nodes in this line. These nodes only have one neighbor.
	 * 3.) All non-end-nodes have exactly two neighbors (no crossing allowed).
	 * 
	 * @param listOfNodes The list of nodes that may be valid.
	 * @return True when valid.
	 */
	public static boolean __isValid(ArrayList<RailwayNode> listOfNodes)
	{
		int amountEndNodes = 0;

		for(RailwayNode node : listOfNodes)
		{
			if(__isEndNode(node, listOfNodes)) ++amountEndNodes;
			else if(__getNeighborAmount(node, listOfNodes) > 2)
			{
				return false;
			}
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
	public static boolean __isEndNode(RailwayNode node, ArrayList<RailwayNode> listOfNodes)
	{
		return __getNeighborAmount(node, listOfNodes) == 1;
	}

	private static int __getNeighborAmount(RailwayNode node, ArrayList<RailwayNode> listOfNodes)
	{
		int counter = 0;

		for(RailwayNode neighborNode : node.getNeighbors())
		{
			for(RailwayNode Node : listOfNodes)
			{
				if(neighborNode.equals(Node)) ++counter;
			}
		}

		return counter;
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
	 * Sets the color of the line. This method does not check for any color duplicates in other lines.
	 * 
	 * @param newLineColor The new color of the line.
	 */
	public void setColor(Color newLineColor)
	{
		_lineColor = newLineColor;
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
	 * @return The length of this train line in field units (not the amount of nodes!).
	 */
	public double getLength()
	{
		return _length;
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
			
			isEqual &= getName().equals(line.getName());

			return isEqual;
		}
		return false;
	}

//	/**
//	 * Draws the train line. The line doesn't need to be valid because all parts are drawn by their own.
//	 * 
//	 * @param offset The map offset in pixel.
//	 * @param sp The sprite batch to draw on.
//	 * @param map A map (node -> Integer) that says how many lines are already drawn on this node (normally all integers are 0).
//	 */
//	public void draw(Point offset, SpriteBatch sp, HashMap<RailwayNode, Integer> map)
//	{
//		if(_length == 0) return;
//
//		Draw.setColor(__isValid(_listOfNodes) ? _lineColor : METRO.__metroRed);
//
//		for(int i = 0; i < _listOfNodes.size() - 1; i++)
//		{
//			// the list is sorted so we know that i+1 is the direct neighbor
//			RailwayNode node = _listOfNodes.get(i);
//			RailwayNode neighbor = _listOfNodes.get(i + 1);
//
//			Point position, positionNext;
//			position = new Point(offset.x + node.getPosition().x * _gameState.getBaseNetSpacing(),
//				offset.y + node.getPosition().y * _gameState.getBaseNetSpacing()); // Position with offset etc.
//			positionNext = new Point(offset.x + neighbor.getPosition().x * _gameState.getBaseNetSpacing(),
//				offset.y + neighbor.getPosition().y * _gameState.getBaseNetSpacing()); // Position with offset etc. for second point
//
////			if(node.isNeighbor(neighbor))
////			{
//				drawColoredLine(offset, position, positionNext, map.get(neighbor).intValue());
//
//				// update the map value
//				int layers = map.get(node).intValue();
//				map.remove(node);
//				map.put(node, new Integer(layers + 1));
////			}
//
//			Draw.Circle(position.x - _thickness, position.y - _thickness, 2 * _thickness + 1);
//		}
//
//		// Draw also a circle for the last node which won't be drawn in the for loop
//		Point position = new Point(offset.x + _listOfNodes.get(_listOfNodes.size() - 1).getPosition().x * _gameState.getBaseNetSpacing(),
//			offset.y + _listOfNodes.get(_listOfNodes.size() - 1).getPosition().y * _gameState.getBaseNetSpacing()); // Position with offset etc.
//		Draw.Circle(position.x - _thickness, position.y - _thickness, 2 * _thickness + 1);
//	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	/**
	 * Gets the predecessor of the given node in direction 1.
	 * 
	 * @param node The node which predecessor you want to know.
	 * @return The predecessor of node.
	 */
	public RailwayNode getPredecessorOf(RailwayNode node)
	{
		int index = _listOfNodes.indexOf(node) - 1;

		if(index >= 0)
		{
			return _listOfNodes.get(index);
		}
		return null;
	}

	/**
	 * Gets the successor of the given node in direction 1.
	 * 
	 * @param node The node which successor you want to know.
	 * @return The successor of node.
	 **/
	public RailwayNode getSuccessorOf(RailwayNode node)
	{
		int index = _listOfNodes.indexOf(node) + 1;

		if(index < _listOfNodes.size())
		{
			return _listOfNodes.get(index);
		}
		return null;
	}

	/**
	 * Checks if this line is valid.
	 * This method follows more the OOP-paradigm then the static method {@link #__isValid(ArrayList)} when checking the validity of an existing line.
	 * 
	 * @return True when this line is valid, false when not.
	 */
	public boolean isValid()
	{
		return __isValid(_listOfNodes);
	}
}
