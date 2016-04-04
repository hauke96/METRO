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

		for(TrainLine line : lineList)
		{
			ArrayList<RailwayNode> nodeList = line.getNodes();

			for(RailwayNode node : nodeList)
			{
				// map does not contain this node --> Add line at position 0
				if(!_sortedLineMap.containsKey(node))
				{
					TrainLine[] array = new TrainLine[amountLines];
					array[0] = line;
					_sortedLineMap.put(node, array);
				}
				else // map contains this node --> add current line to it (at the right position!)
				{
					/* @formatter:off
					 * the loop begins with the top most line
					 * 
					 * 0: check if line 1 and line 2 have parallel part
					 * 		Think of the following situation:
					 * 
					 * 		   O     O
					 * 		(1) \\  / (2)
					 * 			 \\/
					 * 			  O==O==O==O=...(1 and 3)
					 * 			 /
					 * 			/ (3)
					 * 		   O
					 * 
					 * 		Line 1 and 3 are parallel since they met.
					 * 		Line 2 goes through the middle node but has no other node with line 3 in common
					 * 
					 * 		Only look at "existingLine" when it has a parallel part with line.
					 * 
					 * 		Check this via:
					 * 			if(1.pred == 2.pred ||
					 * 			   1.pred == 2.succ ||
					 * 			   1.succ == 2.pred ||
					 * 			   1.succ == 2.succ)
					 * 			{
					 * 				findNodes();
					 * 			}
					 * 		Where .succ and .pred are the neighbor nodes concerning our loop variable "node" on the line (1 or 2).
					 * 		We must check all these cases, because we have no information about the direction of the line (which node comes
					 * 
					 *  	The "findNodes()" methods finds the nodes "n1" and "n2", which are important later...
					 * 		
					 * 1: iterate over the array until "existingLine" is below "line"
					 * 
					 * 		So we have a node "n" where both lines come together
					 * 		and a node "n1" from the first line and "n2" from the second.
					 * 		Also is "n1" != "n2".
					 * 		The first line is already in the map, we have to find out where
					 * 		the second line belongs (above or below the first). 
					 * 
					 * 		1.1: if(n2.y < n1.y)
					 * 		  or if(n2.y == n1.y && n2.x > n1.x)
					 * 				-->  Second line is BELOW first.
					 * 
					 * 		1.2: if(n2.y == n1.y && n2.x < n1.x)
					 * 		  or if(n2.y > n1.y)
					 * 				-->  Second line is ABOVE first. Nothing to do here, we want to find a line that's below.
					 * 
					 * 2: move all other lines one step down
					 * 3: add "line" into the array at the correct position
					 * 
					 * @formatter:on
					 */
					RailwayNode linePred = line.getPredecessorOf(node);
					RailwayNode lineSucc = line.getSuccessorOf(node);

					TrainLine[] array = _sortedLineMap.get(node);

					for(int i = 0; i < array.length; ++i)
					{
						TrainLine existingLine = array[i];

						RailwayNode existingLinePred = existingLine.getPredecessorOf(node);
						RailwayNode existingLineSucc = existingLine.getSuccessorOf(node);

						// the following variable names (n1 and n2) are taken over from the documentation above
						Point n1, n2;

						// find the parallel part and take the other nodes as n1 and n2
						if(linePred.equals(existingLinePred))
						{
							n1 = existingLineSucc.getPosition();
							n2 = lineSucc.getPosition();
						}
						else if(linePred.equals(existingLineSucc))
						{
							n1 = existingLinePred.getPosition();
							n2 = lineSucc.getPosition();
						}
						else if(lineSucc.equals(existingLinePred))
						{
							n1 = existingLineSucc.getPosition();
							n2 = linePred.getPosition();
						}
						else // if(lineSucc.equals(otherLineSucc))
						{
							n1 = existingLinePred.getPosition();
							n2 = linePred.getPosition();
						}

						if(n1 != null && n2 != null && // there is a parallel part
							(n2.y < n1.y || (n2.y == n1.y && n2.x > n1.x))) // "line" is BELOW the "existingLine"
						{
							TrainLine[] newArray = _sortedLineMap.get(node);
							insertAt(newArray, line, i);
							_sortedLineMap.put(node, newArray);
							break;
						}
					}
				}
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
	private void insertAt(TrainLine[] array, TrainLine line, int index)
	{
		for(int i = array.length - 1; i > index; --i)
		{
			array[i] = array[i - 1];
		}
		array[index] = line;
	}

	/**
	 * Draws all train lines.
	 * 
	 * @param offset The map offset in pixel.
	 * @param sp The sprite batch to draw on.
	 * @param lineList An ArrayList of all existing train lines.
	 */
	public void drawLines(Point offset, SpriteBatch sp, ArrayList<TrainLine> lineList)
	{
		HashMap<RailwayNode, Integer> map = new HashMap<RailwayNode, Integer>();

		for(RailwayNode node : RailwayNodeOverseer.__nodeMap.values())
		{
			map.put(node, new Integer(0));
		}

		for(TrainLine line : lineList)
		{
			line.draw(offset, sp, map);
			// TODO: Implement own drawing algorithm that considers the sorted line list.
		}
	}
}
