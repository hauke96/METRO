package metro.TrainManagement;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.TrainManagement.Nodes.RailwayNode;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;
import metro.TrainManagement.Trains.TrainLine;

/**
 * This class draws the train lines by a specific sorting algorithm.
 * 
 * @author hauke
 *
 */
public class TrainLineDrawingService
{
	private HashMap<RailwayNode, TrainLine[]> _sortedLineMap;

	private static TrainLineDrawingService __INSTANCE;// = new TrainLineDrawingService();

	private TrainLineDrawingService()
	{
		_sortedLineMap = new HashMap<RailwayNode, TrainLine[]>();
	}

	/**
	 * @return The instance of the drawing service. If there's no drawing service, it'll be created.
	 */
	public static TrainLineDrawingService getInstance()
	{
		if(__INSTANCE == null)
		{
			__INSTANCE = new TrainLineDrawingService();
		}
		return __INSTANCE;
	}

	/**
	 * Calculates the position of each Line per Node, so that the drawing routine can draw the lines in an order that looks like a normal tube-plan.
	 * 
	 * @param lineList An ArrayList of all existing train lines.
	 */
	public void calcLinePositions(ArrayList<TrainLine> lineList)
	{
		_sortedLineMap = new HashMap<RailwayNode, TrainLine[]>();
		int amountLines = lineList.size();

		for(int i = 0; i < amountLines; ++i)
		{
			TrainLine line = lineList.get(i);

			for(RailwayNode node : line.getNodes())
			{
				TrainLine[] array;
				if(_sortedLineMap.containsKey(node))
				{
					array = _sortedLineMap.get(node);
				}
				else
				{
					array = new TrainLine[amountLines];
				}
				
				array[i] = line;
			}
		}
	}

	/**
	 * Inserts a train line into the array and pushes the existing lines back.
	 * 
	 * @param array The array where the train line should be inserted.
	 * @param line The line that should be inserted.
	 * @param index The index where the line should be inserted.
	 */
//	private void insertAt(TrainLine[] array, TrainLine line, int index)
//	{
//		for(int i = array.length - 1; i > index; --i)
//		{
//			array[i] = array[i - 1];
//		}
//		array[index] = line;
//	}

	/**
	 * Draws all train lines.
	 * 
	 * @param offset The map offset in pixel.
	 * @param sp The sprite batch to draw on.
	 * @param lineList An ArrayList of all existing train lines.
	 */
	public void drawLines(Point offset, SpriteBatch sp, ArrayList<TrainLine> lineList)
	{
		for(TrainLine line : lineList)
		{
//			line.draw(offset, sp);
			// TODO: Implement own drawing algorithm that considers the sorted line list.
		}
	}
}
