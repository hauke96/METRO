package TrainManagement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import Game.METRO;

public class RailwayNode
{
	private ArrayList<RailwayNode> _neighborNodes = new ArrayList<RailwayNode>();
	private Point _position; // not in pixel, cross number/pos

	public RailwayNode(Point position, RailwayNode neighbor)
	{
		_position = position;
		if(neighbor != null) _neighborNodes.add(neighbor);
	}
	/**
	 * Add a Point to the List of neighbors
	 * @param p The Point to add
	 */
	public void add(RailwayNode p)
	{
		_neighborNodes.add(p);
	}
	/**
	 * Returns position of Node. NOT in pixel but the cross coordinates.
	 * @return Position.
	 */
	public Point getPosition()
	{
		return _position;
	}
	/**
	 * Returns all the neighbors of this node as ArrayList<RailwayNode>.
	 * @return Neighbors as ArrayList<RailwayNode>.
	 */
	public ArrayList<RailwayNode> getNeighbors()
	{
		return _neighborNodes;
	}
	/**
	 * 
	 * @param g
	 * @param offset
	 */
	public void draw(Graphics2D g, Point offset)
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
				g.setColor(Color.black);
				g.drawLine(position.x, position.y, positionNext.x, positionNext.y);
			}
		}
	}
}
