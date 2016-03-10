package metro.GameScreen.MainView.LineView;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.GameScreen.MainView.MainView;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Nodes.RailwayNode;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;
import metro.TrainManagement.Trains.TrainLine;

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
	 * @return A sorted train line (can't be {@code null}!).
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
		if(TrainManagementService.getInstance().isLineColorUsed(newColor))
		{
			METRO.__debug("[DuplicateColorFound]");
			METRO.__debug("Old color is " + _color);
			METRO.__debug("New color is " + newColor.toString());
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

	/**
	 * Sets the current line and allows to make changed on it. Updates the list of nodes, the color and the name.
	 * 
	 * @param line The new line of this tool.
	 */
	public void setLine(TrainLine line)
	{
		_listOfNodes = line.getNodes();
		_color = line.getColor();
		_lineName = line.getName();
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(mouseButton == Buttons.LEFT)
		{
			leftClick(screenX, screenY, MainView.__mapOffset);
		}
		else if(mouseButton == Buttons.RIGHT)
		{
			_isActive = false;
			setChanged();
			notifyObservers(); // notify about close
		}
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
		METRO.__debug("[ClickOnNode]");
		RailwayNode clickedNode = RailwayNodeOverseer.getNodeByPosition(MainView.__selectedCross);
		METRO.__debug("node is " + clickedNode);
		if(clickedNode == null) return;
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

	@Override
	public boolean isHovered()
	{
		return false;
	}
}
