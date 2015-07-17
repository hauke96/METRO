package metro.GameScreen.TrainLineView;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import metro.METRO;
import metro.GameScreen.TrainInteractionTool;
import metro.GameScreen.TrainView.TrainView;
import metro.TrainManagement.Lines.RailwayConnection;
import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Nodes.RailwayNode;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The TrainLineSelectTool allows the user to create train lines by clicking on all the nodes this line should contain.
 * It can also generate a new train line from all selected nodes.
 * 
 * @author hauke
 *
 */
public class TrainLineSelectTool implements TrainInteractionTool
{
	private Point2D _mapOffset;
	private ArrayList<RailwayNode> _listOfNodes;
	private TrainLine _line;

	/**
	 * Creates a new tool to select the train line.
	 * 
	 * @param mapOffset The map offset to measure mouse clicks in the right way.
	 */
	public TrainLineSelectTool(Point2D mapOffset)
	{
		_mapOffset = mapOffset;
		_listOfNodes = new ArrayList<RailwayNode>();
	}

	@Override
	public void draw(SpriteBatch sp, Point2D offset)
	{
	}

	@Override
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		if(screenX >= TrainView._selectedCross.x * METRO.__baseNetSpacing - 6 + offset.getX() &&
			screenX <= TrainView._selectedCross.x * METRO.__baseNetSpacing + 6 + offset.getX() &&
			screenY >= TrainView._selectedCross.y * METRO.__baseNetSpacing - 6 + offset.getY() &&
			screenY <= TrainView._selectedCross.y * METRO.__baseNetSpacing + 6 + offset.getY())
		{
			addNodeToList(TrainView._selectedCross);
		}
	}

	/**
	 * Add a node to the list of nodes for this train line if it doesn't exist.
	 * 
	 * @param node The node to add.
	 */
	private void addNodeToList(Point node)
	{
		if(!_listOfNodes.contains(RailwayNodeOverseer.getNodeByPosition(node))) _listOfNodes.add(RailwayNodeOverseer.getNodeByPosition(node));
	}

	/**
	 * Generates the TrainLine object for further using.
	 * 
	 * @return The train line as TrainLine object.
	 */
	public TrainLine getTrainLine()
	{
		ArrayList<RailwayConnection> connections = createRailwayConnections();
		if(connections == null) return null;
		return new TrainLine(connections, "New Line", Color.blue); // TODO: Change to manual color
	}

	/**
	 * Creates all the Railway connections from the nodes of this line.
	 * 
	 * @return All connections as list. NULL if the line is invalid.
	 */
	private ArrayList<RailwayConnection> createRailwayConnections()
	{
		System.out.println("count: " + countEndNodes());
		if(countEndNodes() != 2) return null;
		RailwayNode start = findStartNode();

		ArrayList<RailwayNode> listOfNodes = new ArrayList<RailwayNode>(_listOfNodes); // copies the _listOfNodes
		ArrayList<RailwayConnection> listOfConnections = new ArrayList<RailwayConnection>();

		RailwayNode node = start; // begin at the start node
		System.out.println("Start pos: " + start.getPosition());
		for(int i = 0; i < _listOfNodes.size(); i++)
		{
			RailwayNode neighbor = getNeighbor(node, listOfNodes); // gets the only neighbor of this node
			if(neighbor == null)// || listOfNodes.size() <= 1)
			{
				System.out.println("i: " + i + " of " + (_listOfNodes.size() - 1));
				break; // the end node is alone -> no neighbors -> algorithm finished
			}
			RailwayConnection connection = new RailwayConnection(node, neighbor); // creates the connection
			listOfConnections.add(connection); // adds the connection to the list
			listOfNodes.remove(node); // removes the used node from the list of nodes
			node = neighbor; // get next node by using the neighbor AS the next node
		}
		System.out.println("amountOfConnections: " + listOfConnections.size() + "\n");

		return listOfConnections;
	}

	/**
	 * Searches for the neighbor of the given node that's only in the given list of nodes.
	 * 
	 * @param node The node which neighbor you want to know.
	 * @param listOfNodes The list of other nodes (ONE possible neighbor is in here).
	 * @return The neighbor of the given node.
	 */
	private RailwayNode getNeighbor(RailwayNode node, ArrayList<RailwayNode> listOfNodes)
	{
		RailwayNode neighborNode = null;

		for(RailwayNode neighbor : node.getNeighbors())
		{
			if(listOfNodes.contains(neighbor))
			{
				neighborNode = neighbor;
				break;
			}
		}

		return neighborNode;
	}

	/**
	 * Counts the amount of edge/end nodes of this train line.
	 * 
	 * @return The amount of end nodes.
	 */
	private int countEndNodes()
	{
		int amountEndNodes = 0;
		for(int i = 0; i < _listOfNodes.size(); i++)
		{
			RailwayNode node = _listOfNodes.get(i);
			int amountNeighbors = 0;
			for(int k = 0; node != null && k < _listOfNodes.size(); k++)
			{
				if(node.getNeighbors().contains(_listOfNodes.get(k))) amountNeighbors++;
			}
			if(amountNeighbors <= 1) amountEndNodes++;
		}
		System.out.println("countEndNodes: " + amountEndNodes);
		return amountEndNodes;
	}

	/**
	 * Find an end node. With null as parameter it's the first node (start-node), with another node as parameter, it's the second node (end-node)
	 * 
	 * @return An end node.
	 */
	private RailwayNode findStartNode()
	{
		RailwayNode endNode = null;
		int amountNeighbors = 0;
		for(int i = 0; i < _listOfNodes.size()
			&& amountNeighbors != 1; i++) // when node has 1 neighbor

		{
			amountNeighbors = 0;
			endNode = _listOfNodes.get(i);
			for(int k = 0; k < _listOfNodes.size(); k++)
			{
				if(!_listOfNodes.get(k).equals(endNode) // not the end node self
					// && !_listOfNodes.get(k).equals(start)
					&& _listOfNodes.get(k).getNeighbors().contains(endNode)) amountNeighbors++; // count nodes that are neighbors only within THIS train line
			}
			System.out.println("   " + endNode.getPosition() + " - " + amountNeighbors);
		}

		return endNode;
	}

	@Override
	public void rightClick(int screenX, int screenY, Point2D offset)
	{
		// TODO: Remove clicked node from train line
	}
}
