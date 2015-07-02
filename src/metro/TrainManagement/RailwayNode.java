package metro.TrainManagement;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import metro.METRO;
import metro.Graphics.Draw;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A railway node is a point that's used by tracks. There's no track-class but every railway node has neighbors and by drawing a railway node, lines are drawed to these neighbors.
 * 
 * @author hauke
 *
 */

public class RailwayNode
{
	private ArrayList<RailwayNode> _neighborNodes = new ArrayList<RailwayNode>();
	private Point _position; // not in pixel, cross number/pos

	public static Map<Point, RailwayNode> _nodeMap = new HashMap<Point, RailwayNode>(); // yeah, i know, it sound like "road map" ...
	public static final int PRICE = 200;

	public RailwayNode(Point position, RailwayNode neighbor)
	{
		_position = position;
		if(neighbor != null) _neighborNodes.add(neighbor);
	}

	/**
	 * Add a Point to the List of neighbors.
	 * 
	 * @param node The Point to add
	 */
	public void add(RailwayNode node)
	{
		if(!_neighborNodes.contains(node)) // when node is no neighbor then add it
		{
			_neighborNodes.add(node); // add it to the neighbors
			node.addSimple(this); // add it WITHOUT re-adding "this" (would cause endless loop + StackOverFlow)
		}
	}

	/**
	 * Adds a railway node to the list of neighbors without (!) adding himself to the node p.
	 * 
	 * @param node Node to add.
	 */
	public void addSimple(RailwayNode node)
	{
		if(!_neighborNodes.contains(node)) // when node is no neighbor then add it
		{
			_neighborNodes.add(node);
		}
	}

	/**
	 * Returns position of Node. NOT in pixel but the cross coordinates.
	 * 
	 * @return Position.
	 */
	public Point getPosition()
	{
		return _position;
	}

	/**
	 * Returns all the neighbors of this node as ArrayList<RailwayNode>.
	 * 
	 * @return Neighbors as ArrayList<RailwayNode>.
	 */
	public ArrayList<RailwayNode> getNeighbors()
	{
		return _neighborNodes;
	}

	/**
	 * Draws a railway node.
	 * 
	 * @param g
	 * @param offset
	 */
	public void draw(SpriteBatch sp, Point offset)
	{
		Point position, positionNext;

		for(RailwayNode node : _neighborNodes)
		{
			Point p = node.getPosition();

			if(p.y < _position.y ||
				(p.y == _position.y && p.x < _position.x))
			{
				position = new Point(offset.x + _position.x * METRO.__baseNetSpacing,
					offset.y + _position.y * METRO.__baseNetSpacing); // Position with offset etc.
				positionNext = new Point(offset.x + p.x * METRO.__baseNetSpacing,
					offset.y + p.y * METRO.__baseNetSpacing); // Position with offset etc. for second point

				// if the track is a straight one (horizontal or vertical but not diagonal), make it longer (because of drawing inaccuracy)
				if(position.y == positionNext.y) positionNext.x--;
				if(position.x == positionNext.x) positionNext.y--;

				Draw.setColor(Color.black);
				Draw.Line(position.x, position.y, positionNext.x, positionNext.y);
			}
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

	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof RailwayNode) && (_position == ((RailwayNode)obj).getPosition());
	}
}
