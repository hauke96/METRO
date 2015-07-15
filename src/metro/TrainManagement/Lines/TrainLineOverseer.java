package metro.TrainManagement.Lines;

import java.awt.Color;
import java.util.ArrayList;

public class TrainLineOverseer
{
	private static ArrayList<TrainLine> _listOfTrainLines = new ArrayList<TrainLine>();

	/**
	 * Adds a train line to the list of lines. An older version of this line will be removed.
	 * 
	 * @param line The line to add.
	 */
	public static void addLine(TrainLine line)
	{
		_listOfTrainLines.remove(line); // remove old line, because maybe line.equals(old-line) == true
		// TODO: advanced remove mechanics (what if the user changed name AND color? -> currently the lines are different)
		_listOfTrainLines.add(line); // adds the new line to the list
	}

	/**
	 * Returns a list of all colors that are just by the lines containing the given RailwayConnection.
	 * 
	 * @param connection The Connection which color you want to have.
	 * @return A list of colors.
	 */
	public static ArrayList<Color> getColor(RailwayConnection connection)
	{
		ArrayList<Color> colorList = new ArrayList<Color>();
		for(TrainLine line : _listOfTrainLines)
		{
			if(line.contains(connection)) colorList.add(line.getColor());
		}
		return colorList;
	}

}
