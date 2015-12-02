package metro.GameScreen.LineView;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.GameScreen.MainView.MainView;
import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Lines.TrainLineOverseer;
import metro.TrainManagement.Nodes.RailwayNode;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;

/**
 * The LineSelectTool allows the user to create train lines by clicking on all the nodes this line should contain.
 * It can also generate a new train line from all selected nodes.
 * 
 * @author hauke
 *
 */
public class LineSelectTool extends GameScreen
{
	private ArrayList<RailwayNode> _listOfNodes;
	private boolean _isActive;
	private Color _color;
	private String _lineName;

	/**
	 * Creates a new tool to select the train line.
	 */
	public LineSelectTool()
	{
		_listOfNodes = new ArrayList<RailwayNode>();
		_isActive = true;
		_color = METRO.__metroBlue;
		_lineName = "";
	}

	/**
	 * Creates a new tool to edit the given line.
	 * 
	 * @param line A line to edit.
	 */
	public LineSelectTool(TrainLine line)
	{
		_listOfNodes = line.getNodes();
		_isActive = true;
		_color = line.getColor();
		_lineName = line.getName();
	}

	/**
	 * @return The list with all selected nodes.
	 */
	public ArrayList<RailwayNode> getNodeList()
	{
		return _listOfNodes;
	}

	/**
	 * Creates a train line which can never be {@code null}!
	 * This line might be invalid but all parts of it are sorted.
	 * 
	 * @return A sorted train line (can also be {@code null}!).
	 */
	@SuppressWarnings("unchecked") // _listOfNodes.clone can safely be converted into an array list (because listOfNodes is an array list)
	public TrainLine getTrainLine()
	{
		ArrayList<RailwayNode> newList = sortNodes((ArrayList<RailwayNode>)_listOfNodes.clone(), getAnyEndNode(_listOfNodes));// (ArrayList<RailwayNode>)_listOfNodes.clone(), getAnyEndNode(_listOfNodes));
		return new TrainLine(newList, _lineName, _color);
	}

	/**
	 * Sorts the given list of nodes by choosing one start node an then the neighbors.
	 * 
	 * @param list The list of nodes that should be sorted.
	 * @param startNode An end node of the line.
	 * @return A sorted list with all the nodes.
	 */
	private ArrayList<RailwayNode> sortNodes(ArrayList<RailwayNode> list, RailwayNode startNode)
	{
		if(list.size() <= 1 || startNode == null) return list;

		METRO.__debug("[TrainLineSorting]\n"
			+ "Start Node: " + startNode.getPosition() + "\n"
			+ "Line length (amount of nodes): " + list.size());

		ArrayList<RailwayNode> newList = new ArrayList<>();
		// be sure that an end node is the first element in this list
		newList.add(startNode);
		list.remove(startNode);

		// create a dummy node to enter toe first for-loop
		RailwayNode neighbor = new RailwayNode(null, null);
		int listLength = list.size();

		for(int i = 0; i < listLength; ++i)
		{
			neighbor = null;
			int k; // the index of the neighbor for the node at index i
			// search for the left neighbor of node i 
			for(k = 0; k < list.size() && neighbor == null; ++k)
			{
				if(newList.get(i).isNeighbor(list.get(k))) neighbor = list.get(k);
			}
			
			if(neighbor != null)
			{
				METRO.__debug(newList.get(i).getPosition() + "  ==>  " + neighbor.getPosition());
				newList.add(neighbor);
				list.remove(neighbor);
			}
			else
			{
				METRO.__debug("No Neighbor found, try other start node ...");
				/* 
				 * When there's no neighbor, choose another start node and try to sort this branch.
				 * This also means that this line is invalid!
				 */
				newList.addAll(sortNodes(list, getAnyEndNode(list)));
			}
		}

		return newList;
	}

	/**
	 * This method determines an end node of the given line.
	 * It's not specified which node of the two end nodes will be chosen, because the algorithm will take the first in the list of nodes.
	 * 
	 * @param list A list with all nodes where the end nodes should be determined.
	 * @return One of the two end nodes of the given line.
	 */
	private RailwayNode getAnyEndNode(ArrayList<RailwayNode> list)
	{
		for(RailwayNode node : list)
		{
			if(TrainLine.isEndNode(node, list)) return node;
		}

		return null;
	}

	/**
	 * Sets the color for this in-progress-line.
	 * 
	 * @param newColor The new color.
	 * @return A message if something went wrong.
	 */
	public String setColor(Color newColor)
	{
		if(TrainLineOverseer.isColorUsed(newColor))
		{
			return "No duplicate colors allowed!";
		}

		_color = newColor;
		return "";
	}

	/**
	 * Sets a new name for this train line.
	 * 
	 * @param newName The new name.
	 */
	public void setName(String newName)
	{
		_lineName = newName;
	}

	/**
	 * Enables or disables this tool. When enabled it works otherwise it won't do anything.
	 * 
	 * @param enabled True to enable, false to disable.
	 */
	public void setState(boolean enabled)
	{
		_isActive = enabled;
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
	}

	/**
	 * Adds or removes a node at the clicked position (screenX, screenY).
	 * 
	 * @param screenX The y-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param offset The current map offset.
	 */
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		RailwayNode clickedNode = RailwayNodeOverseer.getNodeByPosition(MainView._selectedCross);
		if(clickedNode == null) return;
		METRO.__debug("[ClickOnNode]");
		if(_listOfNodes.contains(clickedNode))
		{
			METRO.__debug("Removed node " + clickedNode.getPosition());
			_listOfNodes.remove(clickedNode);
		}
		else
		{
			METRO.__debug("Added node " + clickedNode.getPosition());
			_listOfNodes.add(clickedNode);
		}
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
	}

	@Override
	public void keyDown(int keyCode)
	{
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public boolean isActive()
	{
		return _isActive;
	}

	@Override
	public void reset()
	{
	}
}
