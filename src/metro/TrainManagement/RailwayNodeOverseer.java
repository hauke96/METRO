package metro.TrainManagement;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RailwayNodeOverseer
{
	public static Map<Point, RailwayNode> _nodeMap = new HashMap<Point, RailwayNode>(); // yeah, i know, it sound like "road map" ...
	
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
	 * @param node The node that possibly exists.
	 * @return True when node exists.
	 */
	public static boolean doesExist(RailwayNode node)
	{
		return _nodeMap.containsValue(node);
	}
	
	/**
	 * Adds a node to the list of nodes (better: to the map).
	 * @param node The RailwayNode that should be added
	 */
	public static void add(RailwayNode node)
	{
		_nodeMap.put(node.getPosition(), node);
	}
	
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
	
	public static void drawAllNodes(Point offset, SpriteBatch sp)
	{
		for(RailwayNode node : _nodeMap.values())
		{
			node.draw(sp, offset);
		}
	}

	public static int getSize()
	{
		return _nodeMap.values().size();
	}
}
