package metro.TrainManagement.Nodes;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Graphics.Draw;

/**
 * A railway node is a point that's used by train lines and every node has neighbors (both know each other).
 * Train lines are not drawn here even if they're only node connections (they are drawn in the {@code TrainLine} class).
 * 
 * @author hauke
 *
 */

public class RailwayNode
{
	private ArrayList<RailwayNode> _listOfNeighbors = new ArrayList<RailwayNode>(); // a list of all nodes this node is connected to
	private Point _position; // not in pixel, cross number/pos

	/**
	 * The price of one node.
	 */
	public static final int PRICE = 2000;

	/**
	 * Creates a new node at the given position with a specific neighbor.
	 * 
	 * @param position The position (NOT pixel) of the node. {@code null} indicates that the node will not be added to the overseer (because it's invalid).
	 * @param neighbor The neighbor of the node.
	 */
	public RailwayNode(Point position, RailwayNode neighbor)
	{
		_position = position;
		if(neighbor != null) _listOfNeighbors.add(neighbor);
		if(_position != null) RailwayNodeOverseer.add(this);
	}

	/**
	 * Adds a node to the List of neighbors.
	 * 
	 * @param node The node to add
	 */
	public void addNeighbor(RailwayNode node)
	{
		if(!_listOfNeighbors.contains(node)) // when node is no neighbor then add it
		{
			_listOfNeighbors.add(node); // add it to the neighbors
			node.addSimple(this); // add it WITHOUT re-adding "this" (would cause endless loop + StackOverFlow)
		}
	}

	/**
	 * Adds a railway node to the list of neighbors without (!) adding himself to the node.
	 * 
	 * @param node Node to add.
	 */
	public void addSimple(RailwayNode node)
	{
		if(!_listOfNeighbors.contains(node)) // when node is no neighbor then add it
		{
			_listOfNeighbors.add(node);
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
		return _listOfNeighbors;
	}

	/**
	 * Checks if the given node is a neighbor of this node.
	 * 
	 * @param node The presumed neighbor.
	 * @return True when the given node is a neighbor, otherwise false.
	 */
	public boolean isNeighbor(RailwayNode node)
	{
		return _listOfNeighbors.contains(node);
	}

	/**
	 * Draws a railway node and its neighbors. An algorithm takes care of NOT drawing nodes twice.
	 * 
	 * @param sp The sprite batch.
	 * @param offset An map offset in pixel.
	 */
	public void draw(SpriteBatch sp, Point offset)
	{
		Point position, positionNext;
		position = new Point(offset.x + _position.x * METRO.__baseNetSpacing,
			offset.y + _position.y * METRO.__baseNetSpacing); // Position with offset etc.

		for(RailwayNode node : _listOfNeighbors)
		{
			Point p = node.getPosition();

			if(p.y < _position.y ||
				(p.y == _position.y && p.x < _position.x))
			{
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

	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof RailwayNode) && (_position.equals(((RailwayNode)obj).getPosition()));
	}
}
