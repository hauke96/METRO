package metro.TrainManagement.Nodes;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import metro.METRO;
import metro.Graphics.Draw;
import metro.TrainManagement.Lines.RailwayConnection;
import metro.TrainManagement.Lines.TrainLineOverseer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A railway node is a point that's used by train lines.
 * Every node has neighbors (both know each other) and by drawing a railway node, lines are drawed to these neighbors.
 * 
 * @author hauke
 *
 */

public class RailwayNode
{
	private ArrayList<RailwayNode> _neighborNodes = new ArrayList<RailwayNode>(); // a list of all nodes this node is connected to
	private Point _position; // not in pixel, cross number/pos

	public static final int PRICE = 200;

	public RailwayNode(Point position, RailwayNode neighbor)
	{
		_position = position;
		if(neighbor != null) _neighborNodes.add(neighbor);
		RailwayNodeOverseer.add(this);
	}

	/**
	 * Adds a node to the List of neighbors.
	 * 
	 * @param node The node to add
	 */
	public void addNeighbor(RailwayNode node)
	{
		if(!_neighborNodes.contains(node)) // when node is no neighbor then add it
		{
			_neighborNodes.add(node); // add it to the neighbors
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

		for(RailwayNode node : _neighborNodes)
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

				ArrayList<Color> colors = TrainLineOverseer.getColor(new RailwayConnection(this, node));
				if(colors.size() == 0)
				{
					Draw.setColor(Color.black);
				}
				else
				{
					Draw.setColor(colors.get(0)); // TODO: Draw all colors, not only the first
					Draw.Line(position.x, position.y - 1, positionNext.x, positionNext.y - 1); // TODO: make more accurate draw algo. This won't work vor vertical lines :(
				}
				Draw.Line(position.x, position.y, positionNext.x, positionNext.y);
			}
		}
		ArrayList<Color> colorList = TrainLineOverseer.getNodeColor(this);
		for(int i = 0; i < colorList.size(); i++)
		{
			Draw.setColor(colorList.get(i));
			Draw.setColor(METRO.__metroBlue);
			Draw.Circle(position.x - 4 - i, position.y - 4 - i, 9 + 2 * i);
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof RailwayNode) && (_position.equals(((RailwayNode)obj).getPosition()));
	}
}
