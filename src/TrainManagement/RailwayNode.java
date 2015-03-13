package TrainManagement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import Game.GameScreen_TrainView;
import Game.METRO;

public class RailwayNode
{
	private ArrayList<RailwayNode> _neighborNodes = new ArrayList<RailwayNode>();
	private Point _position; // not in pixel, cross number/pos
	private int _ID;
	
	public static ArrayList<Integer> _IDList;

	public RailwayNode(Point position, RailwayNode neighbor)
	{
		if(_IDList == null) _IDList = new ArrayList<Integer>();
		_position = position;
		_ID = (int)(((Math.pow(position.x + position.y, 2) + position.x + position.y) / 2.0) + position.x);
		// ID Table:
		// 0   2   5   9  14  20 ...
		// 1   4   8  13  19  26 ...
		// 3   7  12  18  25  33 ...
		// 6  11  17  24  32  41 ...
		// ...
		_IDList.add(_ID);
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
	/**
	 * Returns the ID of the node.
	 * @return ID
	 */
	public int getID()
	{
		return _ID;
	}
	/**
	 * Calculates the ID of a specific position.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return Calculated ID.
	 */
	public static int calcID(int x, int y)
	{
		return (int)(((Math.pow(x + y, 2) + x + y ) / 2.0) + x);
	}
	public static RailwayNode getNodeByID(int ID)
	{
		for(RailwayNode node : GameScreen_TrainView._railwayNodeList)
		{
			if(node.getID() == ID)
			{
				return node;
			}
		}
		return null;
	}
}
