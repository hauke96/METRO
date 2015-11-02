package metro.TrainManagement.Nodes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
	public static Map<Point, RailwayNode> _nodeMap = new HashMap<Point, RailwayNode>();

	/**
	 * Gets the RailwayNode at a specific position.
	 * 
	 * @param position The position.
	 * @return The node that should be found. "null" when no node exists.
	 */
	public static RailwayNode getNodeByPosition(Point position)
	{
		return _nodeMap.get(position);
	}

	/**
	 * Checks if a node at a specific position exists.
	 * 
	 * @param position The position to check on.
	 * @return True when a node exists, false if not.
	 */
	public static boolean isNodeAt(Point position)
	{
		return _nodeMap.containsKey(position);
	}

	/**
	 * Checks if an node already exists
	 * 
	 * @param node The node that possibly exists.
	 * @return True when node exists.
	 */
	public static boolean doesExist(RailwayNode node)
	{
		return _nodeMap.containsValue(node);
	}

	/**
	 * Adds a node to the list of nodes (better: to the map).
	 * 
	 * @param node The RailwayNode that should be added
	 */
	public static void add(RailwayNode node)
	{
		_nodeMap.put(node.getPosition(), node);
	}

	/**
	 * Removes all nodes with no neighbors. These nodes are normally not visible and have no function.
	 */
	public static void removeNodesWithoutNeighbors()
	{
		ArrayList<RailwayNode> list = new ArrayList<RailwayNode>();

		for(RailwayNode node : _nodeMap.values())
		{
			if(node.getNeighbors().size() == 0) list.add(node);
		}

		for(RailwayNode node : list)
		{
			_nodeMap.remove(node.getPosition());
		}
	}

	/**
	 * Draws all nodes onto the sprite batch.
	 * 
	 * @param offset The map offset in pixel.
	 * @param sp The sprite batch.
	 */
	public static void drawAllNodes(Point offset, SpriteBatch sp)
	{
		for(RailwayNode node : _nodeMap.values())
		{
			node.draw(sp, offset);
		}
	}
}
