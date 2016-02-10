package metro.TrainManagement.Lines;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.TrainManagement.Nodes.RailwayNode;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;
import metro.TrainManagement.Trains.TrainOverseer;

/**
 * The TrainLineOverseer knows all train lines and with this overseer you can add, remove and change a train line.
 * 
 * @author hauke
 *
 */
public class TrainLineOverseer
{
	private static ArrayList<TrainLine> _listOfTrainLines = new ArrayList<TrainLine>();
	private final static TrainLineOverseer __INSTANCE = new TrainLineOverseer();
	
	private TrainLineOverseer()
	{
	}

	/**
	 * @return The instance of the train line observer. There can only be one instance per game.
	 */
	public static TrainLineOverseer getInstance()
	{
		return __INSTANCE;
	}

	/**
	 * Adds a train line to the list of lines. An older version of this line will be removed.
	 * 
	 * @param line The line to add.
	 */
	public void addLine(TrainLine line)
	{
		if(line == null) return;
		_listOfTrainLines.remove(line); // remove old line, because maybe line.equals(old-line) == true
		_listOfTrainLines.add(line); // adds the new line to the list
	}

	/**
	 * Removes a line from the list.
	 * 
	 * @param line The line to remove.
	 */
	public void removeLine(TrainLine line)
	{
		if(line == null) return;
		_listOfTrainLines.remove(line);
		TrainOverseer.getInstance().removeTrain(line.getName()); // FIXME BAAAAAD (circle dependency)
	}

	/**
	 * Removes a line including all containing colors and settings.
	 * 
	 * @param lineName The name of the line.
	 */
	public void removeLine(String lineName)
	{
		removeLine(getLine(lineName));
	}

	/**
	 * Returns the color of a given train line.
	 * 
	 * @param lineName The name of the line.
	 * @return The color of the given line. null if line does not exist.
	 */
	public Color getColor(String lineName)
	{
		for(TrainLine line : _listOfTrainLines)
		{
			if(line.getName().equals(lineName))
			{
				return line.getColor();
			}
		}
		return null;
	}

	/**
	 * Creates a copy of the list train lines and returns this copy.
	 * 
	 * @return A copy of the list of all lines.
	 */
	@SuppressWarnings("unchecked") // cast will always succeed, because the list only hold TrainLine objects
	public ArrayList<TrainLine> getLines()
	{
		return (ArrayList<TrainLine>)_listOfTrainLines.clone();
	}

	/**
	 * Searches for a special line.
	 * 
	 * @param lineName The name of the line.
	 * @return The line. null if line does not exist.
	 */
	public TrainLine getLine(String lineName)
	{
		for(TrainLine line : _listOfTrainLines)
		{
			if(line.getName().equals(lineName))
			{
				return line;
			}
		}
		return null;
	}

	/**
	 * Draws all train lines.
	 * 
	 * @param offset The map offset in pixel.
	 * @param sp The sprite batch to draw on.
	 */
	public void drawLines(Point offset, SpriteBatch sp)
	{
		HashMap<RailwayNode, Integer> map = new HashMap<RailwayNode, Integer>();
		for(RailwayNode node : RailwayNodeOverseer._nodeMap.values())
		{
			map.put(node, new Integer(0));
		}

		for(TrainLine line : _listOfTrainLines)
		{
			line.draw(offset, sp, map);
		}
	}

	/**
	 * Checks if a color is already in use.
	 * 
	 * @param color The color that might be used.
	 * @return True when color is used, false when color is free.
	 */
	public boolean isColorUsed(Color color)
	{
		for(TrainLine line : _listOfTrainLines)
		{
			if(line.getColor().equals(color)) return true;
		}
		return false;
	}
}
