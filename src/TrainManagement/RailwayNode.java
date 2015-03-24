package TrainManagement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Game.GameScreen_TrainView;
import Game.METRO;

public class RailwayNode
{
	private ArrayList<RailwayNode> _neighborNodes = new ArrayList<RailwayNode>();
	private Point _position; // not in pixel, cross number/pos
	private int _ID;
	
	public static ArrayList<Integer> _IDList = new ArrayList<Integer>();
	public static final int _PRICE = 200;

	public RailwayNode(Point position, RailwayNode neighbor)
	{
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
		boolean alreadyNeighbor = false;
		for(RailwayNode node : _neighborNodes)
		{
			alreadyNeighbor |= node.getID() == p.getID(); // true when node is already a neighbor 
		}
		if(!alreadyNeighbor)
		{
			_neighborNodes.add(p); // when p is no neighbor then add it
			p.addSimple(this); // when p is no neighbor then add it WITHOUT re-adding "this" (would cause endless loop + StackOverFlow)
		}
	}
	/**
	 * Adds a railway node to the list of neighbors without (!) adding himself to the node p.
	 * @param p Node to add.
	 */
	public void addSimple(RailwayNode p)
	{
		boolean alreadyNeighbor = false;
		for(RailwayNode node : _neighborNodes)
		{
			alreadyNeighbor |= node.getID() == p.getID(); // true when node is already a neighbor 
		}
		if(!alreadyNeighbor) // when p is no neighbor then add it
		{
			_neighborNodes.add(p);
		}
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
				//TODO: Recreate drawing stuff for railwaynode
//				g.setColor(Color.black);
//				g.drawLine(position.x, position.y, positionNext.x, positionNext.y);
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
	/**
	 * Returns a specific node with an specific ID.
	 * @param ID The ID of the node to return.
	 * @return The node.
	 */
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
