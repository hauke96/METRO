package metro.GameScreen.TrainLineView;

import java.awt.Color;
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
	private Color _color;
	private String _lineName;

	/**
	 * Creates a new tool to select the train line.
	 */
	public TrainLineSelectTool()
	{
		_listOfNodes = new ArrayList<RailwayNode>();
		_isClosed = false;
		_color = METRO.__metroBlue;
		_lineName = "";
	}

	/**
	 * Generates the TrainLine object for further using.
	 * This method WON'T return null under any circumstances.
	 * 
	 * @return Won't be null! The train line as TrainLine object.
	 */
	public TrainLine getTrainLine()
	{
		return new TrainLine(_listOfNodes, _lineName, _color);
	}

	/**
	 * Sets the color for this in-progress-line.
	 * 
	 * @param newColor The new color.
	 * @return A message if something went wrong.
	 */
	public String setColor(Color newColor)
	{
		for(RailwayNode node : _listOfNodes)
		{
			try
			{
				node.changeColor(_color, newColor);
			}
			catch(IllegalArgumentException ex)
			{
				return ex.getMessage();
			}
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
	 * Sets the current line to a new one.
	 * 
	 * @param line The new line.
	 */
	public void setLine(TrainLine line)
	{
		_listOfNodes = new ArrayList<RailwayNode>(line.getNodes());
		_color = line.getColor();
		_lineName = line.getName();
	}

	/**
	 * Enables or disables this tool. When enabled it works otherwise it won't do anything.
	 * 
	 * @param enabled True to enable, false to disable.
	 */
	public void setState(boolean enabled)
	{
		_isClosed = enabled;
	}

	@Override
	public void draw(SpriteBatch sp, Point2D offset)
	{
	}

	@Override
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		RailwayNode clickedNode = RailwayNodeOverseer.getNodeByPosition(TrainView._selectedCross);
		ArrayList<RailwayNode> neighbors = clickedNode.getNeighbors();

		for(RailwayNode neighborNode : neighbors)
		{
			if(_listOfNodes.contains(neighborNode))
			{
				if(_listOfNodes.contains(clickedNode)) // if list contains node, remove it
				{
					neighborNode.removeColorTo(clickedNode, _color);
					clickedNode.removeColorTo(neighborNode, _color);
				}
				else
				{
					neighborNode.addColorTo(clickedNode, _color);
					clickedNode.addColorTo(neighborNode, _color);
				}
			}
		}
		if(_listOfNodes.contains(clickedNode)) _listOfNodes.remove(clickedNode);
		else _listOfNodes.add(clickedNode);
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
