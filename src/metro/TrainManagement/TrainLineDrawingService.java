package metro.TrainManagement;

import java.util.ArrayList;
import java.util.HashMap;

import metro.TrainManagement.Nodes.RailwayNode;
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

	private static final TrainLineDrawingService __INSTANCE = new TrainLineDrawingService();

	private TrainLineDrawingService()
	{
		_sortedLineMap = new HashMap<>();
	}

	/**
	 * @return The instance of the drawing service.
	 */
	public static TrainLineDrawingService getInstance()
	{
		return __INSTANCE;
	}

	/**
	 * Calculates the position of each Line per Node, so that the drawing routine can draw the lines in an order that looks like a normal tube-plan.
	 */
	public void calcLinePositions()
	{
		ArrayList<TrainLine> lineList = TrainManagementService.getInstance().getLines();
		int amountLines = lineList.size();
		
		for(TrainLine line : lineList)
		{
			ArrayList<RailwayNode> nodeList = line.getNodes();
			
			for(RailwayNode node : nodeList)
			{
				// map contains this node --> add current line to it (at the right position!)
				if(_sortedLineMap.containsKey(node))
				{
					// TODO implement stuff here
				}
			}
		}
	}
}
