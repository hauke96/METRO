package metro.TrainManagement;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.GameState;
import metro.METRO;
import metro.Graphics.Draw;
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
	private GameState _gameState;
	private int _lineThickness;

	private static TrainLineDrawingService __INSTANCE;// = new TrainLineDrawingService();

	private TrainLineDrawingService()
	{
		_sortedLineMap = new HashMap<RailwayNode, TrainLine[]>();
		_gameState = GameState.getInstance();
		_lineThickness = 3;
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
				if(_sortedLineMap.containsKey(node)) array = _sortedLineMap.get(node);
				else array = new TrainLine[amountLines];

				array[i] = line;
				_sortedLineMap.put(node, array);
			}
		}
	}

	/**
	 * Searches for an element in an array of train lines and returns the position of the first occurrence. Empty fields in the array will be ignored.
	 * 
	 * @param array The array of train lines.
	 * @param element The element which position you want to know.
	 * @return The position of the first occurrence of the element. If the element is not in the array, -1 will be returned.
	 */
	private int positionOf(TrainLine[] array, TrainLine element)
	{
		int counter = 0;
		for(int i = 0; i < array.length && array[i] != null; ++i)
		{
			if(array[i].equals(element)) return counter;
			if(array[i] != null) ++counter;
		}
		return -1;
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
		for(TrainLine line : lineList)
		{
			if(line.getLength() == 0) continue;

			ArrayList<RailwayNode> listOfNodes = line.getNodes();

			Draw.setColor(line.isValid() ? line.getColor() : METRO.__metroRed);

			for(int i = 0; i < listOfNodes.size() - 1; ++i)
			{
				// the list is sorted so we know that i+1 is the direct neighbor
				RailwayNode node = listOfNodes.get(i);
				RailwayNode neighbor = listOfNodes.get(i + 1);

				int nodeIndex = positionOf(_sortedLineMap.get(node), line);
				int neighborIndex = positionOf(_sortedLineMap.get(neighbor), line);

				if(nodeIndex == -1) nodeIndex = 0;
				if(neighborIndex == -1) neighborIndex = 0;

				Point nodePosition = node.getPosition();
				Point neighborPosition = neighbor.getPosition();

				Point position, positionNext;
				// TODO: make more accurate positions. This won't work for vertical and diagonal lines :(

				position = new Point(offset.x + nodePosition.x * _gameState.getBaseNetSpacing(),
					offset.y + nodePosition.y * _gameState.getBaseNetSpacing() + nodeIndex * _lineThickness + nodeIndex); // Position with offset etc.

				positionNext = new Point(offset.x + neighborPosition.x * _gameState.getBaseNetSpacing(),
					offset.y + neighborPosition.y * _gameState.getBaseNetSpacing() + neighborIndex * _lineThickness + neighborIndex); // Position with offset etc. for second point

				drawColoredLine(offset, position, positionNext);
			}
		}
	}

	/**
	 * Draws a colored line from one node to another.
	 * 
	 * @param offset The offset of the game screen.
	 * @param position The position of one node.
	 * @param positionNext The position of another node.
	 * @param layer The layer this line is (shifts the line up or down).
	 */
	private void drawColoredLine(Point offset, Point position, Point positionNext)
	{
		Point diff = new Point((position.y - positionNext.y) / _gameState.getBaseNetSpacing(),
			(position.x - positionNext.x) / _gameState.getBaseNetSpacing());

		if(diff.x != 0 && diff.y != 0)
		{
			if(diff.x == 1 && diff.y == 1) diff.x = -1;
			else if(diff.x == 1 && diff.y == -1) diff.y = 1;
		}
		
		Draw.Line(position.x,
			position.y,
			positionNext.x,
			positionNext.y,
			_lineThickness);
	}
}
