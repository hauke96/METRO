package metro.GameUI.MainView.LineView;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Buttons;

import metro.METRO;
import juard.log.Logger;
import metro.GameUI.Common.ToolView;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Nodes.RailwayNode;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;
import metro.TrainManagement.TrainLines.TrainLine;

/**
 * The LineSelectTool allows the user to create train lines by clicking on all the nodes this line should contain.
 * It can also generate a new train line from all selected nodes.
 * 
 * @author hauke
 *
 */
public class LineSelectTool extends ToolView
{
	private List<RailwayNode>		_listOfNodes;
	private Color					_color;
	private String					_lineName;
	private PlayingField			_playingField;
	private TrainManagementService	_trainManagementService;
	
	/**
	 * Creates a new tool to select the train line.
	 * 
	 * @param playingField
	 *            The field the player plays on.
	 * @param trainManagementService
	 *            The train management service.
	 */
	public LineSelectTool(PlayingField playingField, TrainManagementService trainManagementService)
	{
		_playingField = playingField;
		_trainManagementService = trainManagementService;
		
		_listOfNodes = new ArrayList<RailwayNode>();
		_color = METRO.__metroBlue;
		_lineName = "";
	}
	
	/**
	 * @return The list with all selected nodes.
	 */
	public List<RailwayNode> getNodeList()
	{
		return _listOfNodes;
	}
	
	/**
	 * Creates a train line which can never be {@code null}!
	 * This line might be invalid but all parts of it are sorted.
	 * 
	 * @return A sorted train line (can't be {@code null}!).
	 */
	public TrainLine getTrainLine()
	{
		return new TrainLine(_listOfNodes, _lineName, _color);
	}
	
	/**
	 * Sets the color for this in-progress-line.
	 * 
	 * @param newColor
	 *            The new color.
	 * @return A message if something went wrong. Returns {@code null} when nothing went wrong.
	 */
	public String setColor(Color newColor)
	{
		if (_trainManagementService.isLineColorUsed(newColor))
		{
			Logger.__debug("Old color is " + _color + "\n"
			        + "New color is " + newColor.toString());
			return "No duplicate colors allowed!";
		}
		
		_color = newColor;
		return null;
	}
	
	/**
	 * Sets a new name for this train line.
	 * 
	 * @param newName
	 *            The new name.
	 */
	public void setName(String newName)
	{
		_lineName = newName;
	}
	
	/**
	 * Sets the current line and allows to make changed on it. Updates the list of nodes, the color and the name.
	 * 
	 * @param line
	 *            The new line of this tool.
	 */
	public void setLine(TrainLine line)
	{
		_listOfNodes = line.getNodes();
		_color = line.getColor();
		_lineName = line.getName();
	}
	
	@Override
	public boolean mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if (mouseButton == Buttons.LEFT)
		{
			return leftClick(screenX, screenY, _playingField.getMapOffset());
		}
		else if (mouseButton == Buttons.RIGHT)
		{
			close();
			return true;
		}
		
		return false;
	}
	
	/**
	 * Adds or removes a node at the clicked position (screenX, screenY).
	 * 
	 * @param screenX
	 *            The y-coordinate of the click.
	 * @param screenY
	 *            The y-coordinate of the click.
	 * @param offset
	 *            The current map offset.
	 * @return True when click handled.
	 */
	private boolean leftClick(int screenX, int screenY, Point2D offset)
	{
		RailwayNode clickedNode = RailwayNodeOverseer.getNodeByPosition(_playingField.getSelectedNode());
		
		Logger.__debug("node is " + clickedNode);
		
		if (clickedNode == null) return false;
		
		if (_listOfNodes.contains(clickedNode))
		{
			Logger.__debug("Removed node " + clickedNode.getPosition());
			_listOfNodes.remove(clickedNode);
		}
		else
		{
			Logger.__debug("Added node " + clickedNode.getPosition());
			_listOfNodes.add(clickedNode);
		}
		
		return true;
	}
	
	@Override
	public boolean isHovered()
	{
		return false;
	}
}
