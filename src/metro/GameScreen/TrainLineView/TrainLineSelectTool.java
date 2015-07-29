package metro.GameScreen.TrainLineView;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import metro.METRO;
import metro.GameScreen.TrainInteractionTool;
import metro.GameScreen.TrainView.TrainView;
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
	private ArrayList<RailwayNode> _listOfNodes;
	private boolean _isClosed;

	/**
	 * Creates a new tool to select the train line.
	 */
	public TrainLineSelectTool()
	{
		_listOfNodes = new ArrayList<RailwayNode>();
		_isClosed = false;
	}

	@Override
	public void draw(SpriteBatch sp, Point2D offset)
	{
	}

	@Override
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		addNodeToList(TrainView._selectedCross);
	}

	/**
	 * Add a node to the list of nodes for this train line if it doesn't exist.
	 * 
	 * @param node The node to add.
	 */
	private void addNodeToList(Point node)
	{
		RailwayNode clickedNode = RailwayNodeOverseer.getNodeByPosition(node);
		ArrayList<RailwayNode> neighbors = clickedNode.getNeighbors();
		for(RailwayNode neighborNode : neighbors)
		{
			if(_listOfNodes.contains(neighborNode))
			{
				neighborNode.addColorTo(clickedNode, Color.blue);
				clickedNode.addColorTo(neighborNode, Color.blue);
			}
		}

		if(!_listOfNodes.contains(clickedNode)) _listOfNodes.add(clickedNode);
	}

	/**
	 * Generates the TrainLine object for further using.
	 * 
	 * @return The train line as TrainLine object.
	 */
	public TrainLine getTrainLine()
	{
		return new TrainLine(_listOfNodes, "New Line", Color.blue); // TODO: Change to manual color
	}

	@Override
	public void rightClick(int screenX, int screenY, Point2D offset)
	{
		_isClosed = true;
	}

	@Override
	public boolean isClosed()
	{
		return _isClosed;
	}
}
