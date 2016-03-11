package metro.TrainManagement.Nodes;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.GameState;
import metro.Graphics.Draw;
import metro.TrainManagement.Trains.Train;

/**
 * A railway node is a point that's used by train lines and every node has neighbors (both know each other).
 * Train lines are not drawn here even if they're only node connections (they are drawn in the {@link metro.TrainManagement.Trains.TrainLine} class).
 * 
 * @author hauke
 *
 */

public class RailwayNode
{
	private ArrayList<RailwayNode> _listOfNeighbors; // a list of all nodes this node is connected to
	private Point _position; // not in pixel, cross number/pos
	private Map<RailwayNode, Train> _signals;

	/**
	 * The price of one node.
	 */
	public static int __price = 2000;

	/**
	 * Creates a new node at the given position with a specific neighbor.
	 * 
	 * @param position The position (NOT pixel) of the node. {@code null} indicates that the node will not be added to the overseer (because it's invalid).
	 * @param neighbor The neighbor of the node.
	 */
	RailwayNode(Point position, RailwayNode neighbor)
	{
		_position = position;
		_listOfNeighbors = new ArrayList<RailwayNode>();
		if(neighbor != null) _listOfNeighbors.add(neighbor);
		_signals = new HashMap<>();
	}

	/**
	 * Creates a new node at the given position with some specified neighbors.
	 * 
	 * @param position The position (NOT pixel) of the node. {@code null} indicates that the node will not be added to the overseer (because it's invalid).
	 * @param neighbors The neighbors of the node.
	 */
	RailwayNode(Point position, ArrayList<RailwayNode> neighbors)
	{
		this(position, (RailwayNode)null); // just cast to RailwayNode to bypass the ambiguity. Ugly but works :/
		if(neighbors != null) _listOfNeighbors.addAll(neighbors);
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
		GameState gameState = GameState.getInstance();
		position = new Point(offset.x + _position.x * gameState.getBaseNetSpacing(),
			offset.y + _position.y * gameState.getBaseNetSpacing()); // Position with offset etc.

		for(RailwayNode node : _listOfNeighbors)
		{
			Point p = node.getPosition();

			if(p.y < _position.y ||
				(p.y == _position.y && p.x < _position.x))
			{
				positionNext = new Point(offset.x + p.x * gameState.getBaseNetSpacing(),
					offset.y + p.y * gameState.getBaseNetSpacing()); // Position with offset etc. for second point

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

	/**
	 * Sets the value of a signal for the part between this node and the successor (looking in the successors direction).
	 * The successor gives the direction of the signal and the value is like a "red" or "green" for the signal.
	 * 
	 * @param successor The successor of this node.
	 * @param train The train that's allowed to drive in the part between this node and the successor.
	 */
	public void setSignalValue(RailwayNode successor, Train train)
	{
		if(_listOfNeighbors.contains(successor))
		{
			_signals.put(successor, train);
		}
	}

	/**
	 * Gets the value of the signal for the part between this node and the successor (looking in the successors direction).
	 * 
	 * @param successor The successor of the signal to determine the direction of it.
	 * @param train The train that wants to pass the signal.
	 * @return The value of the signal for the given train, where true means "green" and false means "red", for the part between this node and the successor.
	 *         When there's no signal specified by the {@link #setSignalValue(RailwayNode, Train)} method, the value is true.
	 */
	public boolean getSignalValue(RailwayNode successor, Train train)
	{
		// when train does not appear in the list, every train can move.
		if(!_signals.containsKey(successor))
		{
			return true;
		}

		if(_signals.get(successor).getCurrentNode().equals(getPosition())) // train's in this area, give value. Otherwise remove train.
		{
			return _signals.get(successor).getName().equals(train.getName());
		}
		else
		{
			_signals.remove(successor);
		}

		// when the train has passed the part from this node to the successor, other trains can move.
		return true;
	}

	/**
	 * Sets every signal to "green".
	 */
	public void unlockSignals()
	{
		_signals.clear();
	}
}
