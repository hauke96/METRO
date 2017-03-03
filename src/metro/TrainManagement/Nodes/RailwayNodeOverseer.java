package metro.TrainManagement.Nodes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manages the RailwayNodes, offers some functions in adding, removing and clearing the map of nodes.
 * 
 * @author hauke
 *
 */
public class RailwayNodeOverseer
{
	/**
	 * This map connects a point on the grid with a possibly existing node.
	 * An yeah, I know, it sound like "road map" ...
	 */
	public static Map<Point, RailwayNode> __nodeMap = new HashMap<Point, RailwayNode>();

	/**
	 * Creates a new node when there's no node at the given position yet.
	 * If there's already a node, return this existing node.
	 * 
	 * @param position The position of the node.
	 * @return A new or existing node at the given position.
	 */
	public static RailwayNode createNode(Point position)
	{
		return createNode(position, (RailwayNode)null); // just cast to RailwayNode to bypass the ambiguity. Ugly but works :/
	}

	/**
	 * Creates a new node when there's no node at the given position yet.
	 * If there's already a node, return this existing node (it might have different neighbors then the given one).
	 * 
	 * @param position The position of the node.
	 * @param neighbor The neighbor of the node.
	 * @return A new or existing node at the given position.
	 */
	public static RailwayNode createNode(Point position, RailwayNode neighbor)
	{
		List<RailwayNode> list = null;
		if(neighbor != null)
		{
			list = new ArrayList<RailwayNode>();
			list.add(neighbor);
		}
		return createNode(position, list);
	}

	/**
	 * Creates a new node when there's no node at the given position yet.
	 * If there's already a node, return this existing node (it might have different neighbors then the given list of neighbor nodes).
	 * 
	 * @param position The position of the node.
	 * @param neighbors A list of neighbors of this node.
	 * @return A new or existing node at the given position.
	 */
	public static RailwayNode createNode(Point position, List<RailwayNode> neighbors)
	{
		if(isNodeAt(position))
		{
			return __nodeMap.get(position);
		}
		else
		{
			RailwayNode node = new RailwayNode(position, neighbors);
			add(node);
			return node;
		}
	}

	/**
	 * Gets the RailwayNode at a specific position.
	 * 
	 * @param position The position.
	 * @return The node that should be found. "null" when no node exists.
	 */
	public static RailwayNode getNodeByPosition(Point position)
	{
		return __nodeMap.get(position);
	}

	/**
	 * Checks if a node at a specific position exists.
	 * 
	 * @param position The position to check on.
	 * @return True when a node exists, false if not.
	 */
	public static boolean isNodeAt(Point position)
	{
		return __nodeMap.containsKey(position);
	}

	/**
	 * Checks if an node already exists
	 * 
	 * @param node The node that possibly exists.
	 * @return True when node exists.
	 */
	public static boolean doesExist(RailwayNode node)
	{
		return __nodeMap.containsValue(node);
	}

	/**
	 * Adds a node to the map of nodes.
	 * 
	 * @param node The RailwayNode that should be added
	 */
	public static void add(RailwayNode node)
	{
		__nodeMap.put(node.getPosition(), node);
	}

	/**
	 * Removes all nodes with no neighbors. These nodes are normally not visible and have no function.
	 */
	public static void removeNodesWithoutNeighbors()
	{
		List<RailwayNode> list = new ArrayList<RailwayNode>();

		for(RailwayNode node : __nodeMap.values())
		{
			if(node.getNeighbors().size() == 0) list.add(node);
		}

		for(RailwayNode node : list)
		{
			__nodeMap.remove(node.getPosition());
		}
	}

	/**
	 * Draws all node connections (lines between nodes) onto the screen.
	 * 
	 * @param offset The map offset in pixel.
	 */
	public static void drawAllNodes(Point offset)
	{
		for(RailwayNode node : __nodeMap.values())
		{
			node.draw(offset);
		}
	}

	/**
	 * Sets every signal to "green".
	 **/
	public static void unlockAllSignals()
	{
		for(RailwayNode node : __nodeMap.values())
		{
			node.unlockSignals();
		}
	}
}
