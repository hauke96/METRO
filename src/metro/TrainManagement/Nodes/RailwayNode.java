package metro.TrainManagement.Nodes;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import metro.METRO;
import metro.Graphics.Draw;

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
	private ArrayList<RailwayNode> _listOfNeighbors = new ArrayList<RailwayNode>(); // a list of all nodes this node is connected to
	private HashMap<RailwayNode, ArrayList<Color>> _mapOfColors = new HashMap<RailwayNode, ArrayList<Color>>();
	private Point _position; // not in pixel, cross number/pos

	/**
	 * The price of one node.
	 */
	public static final int PRICE = 2000;

	/**
	 * Creates a new node at the given position with a specific neighbor.
	 * 
	 * @param position The position (NOT pixel) of the node.
	 * @param neighbor The neighbor of the node.
	 */
	public RailwayNode(Point position, RailwayNode neighbor)
	{
		_position = position;
		if(neighbor != null) _listOfNeighbors.add(neighbor);
		RailwayNodeOverseer.add(this);
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

				ArrayList<Color> colors = _mapOfColors.get(node);
				if(colors == null || colors.size() == 0)
				{
					Draw.setColor(Color.black);
					Draw.Line(position.x, position.y, positionNext.x, positionNext.y);
				}
				else
				{
					drawColoredLines(position, positionNext, node.getPosition(), colors);
				}
			}
		}
		ArrayList<Color> colorList = _mapOfColors.get(this);
		for(int i = 0; colorList != null && i < colorList.size(); i++)
		{
			Draw.setColor(colorList.get(i));
			Draw.setColor(METRO.__metroBlue);
			Draw.Circle(position.x - 4 - i, position.y - 4 - i, 9 + 2 * i);
		}
	}

	/**
	 * Draws all train lines in their own color.
	 * 
	 * @param position The position with offset.
	 * @param positionNext The position of the next node (also with offset).
	 * @param nodePosition The node position (in fields, not pixel).
	 * @param listOfColors List of all colors of this track.
	 */
	private void drawColoredLines(Point position, Point positionNext, Point nodePosition, ArrayList<Color> listOfColors)
	{
		Point diff = new Point(_position.y - nodePosition.y, _position.x - nodePosition.x);
		boolean diagonal = false;
		if(diff.x != 0 && diff.y != 0)
		{
			if(diff.x == 1 && diff.y == 1) diff.x = -1;
			else if(diff.x == 1 && diff.y == -1) diff.y = 1;
			diagonal = true;
		}
		for(int i = 0; i < listOfColors.size(); i++)
		{
			// TODO: make more accurate draw algo. This won't work for vertical lines :(
			Draw.setColor(listOfColors.get(i));
			Draw.Line(position.x - (i * 4) * diff.x,
				position.y - (i * 4) * diff.y,
				positionNext.x - (i * 4) * diff.x,
				positionNext.y - (i * 4) * diff.y);
			Draw.Line(position.x - (1 + i * 4) * diff.x,
				position.y - (1 + 4 * i) * diff.y,
				positionNext.x - (1 + i * 4) * diff.x,
				positionNext.y - (1 + i * 4) * diff.y);
			if(!diagonal)
			{
				Draw.Line(position.x - (2 + i * 4) * diff.x,
					position.y - (2 + 4 * i) * diff.y,
					positionNext.x - (2 + i * 4) * diff.x,
					positionNext.y - (2 + i * 4) * diff.y);
			}
			else
			{
				Draw.Line(position.x - (1 + i * 4) * diff.x,
					position.y - (4 * i) * diff.y,
					positionNext.x - (1 + i * 4) * diff.x,
					positionNext.y - (i * 4) * diff.y);
				Draw.Line(position.x - (-1 + i * 4) * diff.x,
					position.y - (i * 4) * diff.y,
					positionNext.x - (-1 + i * 4) * diff.x,
					positionNext.y - (i * 4) * diff.y);
			}
		}
	}

	/**
	 * Adds a color to the connection "this node" <--[color]--> "parameter node".
	 * 
	 * @param node The other node with same color.
	 * @param color The color of both nodes.
	 */
	public void addColorTo(RailwayNode node, Color color)
	{
		if(!_mapOfColors.containsKey(node)) _mapOfColors.put(node, new ArrayList<Color>());
		_mapOfColors.get(node).add(color);
	}

	/**
	 * Removes the connection "this node" <--[color]--> "parameter node".
	 * 
	 * @param node The neighbor node with the same color.
	 * @param color The color of these nodes.
	 */
	public void removeColorTo(RailwayNode node, Color color)
	{
		if(_mapOfColors.containsKey(node))
		{
			_mapOfColors.get(node).remove(color);
		}
	}

	/**
	 * Removes a specific color from this node.
	 * 
	 * @param color The color to remove.
	 */
	public void removeColor(Color color)
	{
		for(ArrayList<Color> list : _mapOfColors.values())
		{
			list.remove(color);
		}
	}

	/**
	 * Replaces all colors to a new color.
	 * This method may throw an IllegalArgumentException when the newColor is already in use!
	 * 
	 * @param oldColor The color to replace.
	 * @param newColor The new color.
	 * @throws IllegalArgumentException Throws this exception when the user selected a color that has already been taken (exception because no duplicate colors are allowed).
	 */
	public void changeColor(Color oldColor, Color newColor) throws IllegalArgumentException
	{
		for(ArrayList<Color> list : _mapOfColors.values())
		{
			if(list.contains(newColor)) throw new IllegalArgumentException("No duplicate colors are allowed!"); // to avoid duplicates
			if(list.contains(oldColor))
			{
				list.remove(oldColor);
				list.add(newColor);
			}
		}
	}

	/**
	 * Checks if this node is the end/start node of the train line specified by the given color.
	 * 
	 * @param colorOfLine The color of the train line this node may be an end/start node.
	 * @return True when it's an end/start node.
	 */
	public boolean isEndOfLine(Color colorOfLine)
	{
		int i = 0;
		for(ArrayList<Color> list : _mapOfColors.values())
		{
			if(list.contains(colorOfLine)) i++;
		}

		return i == 1;
	}

	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof RailwayNode) && (_position.equals(((RailwayNode)obj).getPosition()));
	}
}
