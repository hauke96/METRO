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
class TrainLineDrawingService
{
	private HashMap<RailwayNode, TrainLine[]> _sortedLineMap;
	private GameState _gameState;
	private int _lineThickness;

	TrainLineDrawingService()
	{
		_sortedLineMap = new HashMap<RailwayNode, TrainLine[]>();
		_gameState = GameState.getInstance();
		_lineThickness = 3;
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
	private int indexOf(TrainLine[] array, TrainLine element)
	{
		int counter = 0;
		for(int i = 0; i < array.length; ++i)
		{
			if(array[i] != null)
			{
				if(array[i].equals(element)) return counter;
				if(array[i] != null) ++counter;
			}
		}
		return -1;
	}

	/**
	 * Draws all train lines.
	 * 
	 * @param mapOffset The map offset in pixel.
	 * @param sp The sprite batch to draw on.
	 * @param lineList An ArrayList of all existing train lines.
	 */
	public void drawLines(Point mapOffset, SpriteBatch sp, ArrayList<TrainLine> lineList)
	{
		for(TrainLine line : lineList)
		{
			if(line.getLength() == 0) continue;

			ArrayList<RailwayNode> listOfNodes = line.getNodes();

			Draw.setColor(line.isValid() ? line.getColor() : METRO.__metroRed);

			RailwayNode thisNode = listOfNodes.get(0);
			int thisNodeIndex = indexOf(_sortedLineMap.get(thisNode), line);
			if(thisNodeIndex == -1) thisNodeIndex = 0;
			Point thisNodePosition = thisNode.getPosition();
			Point thisPosition = null;

			for(int i = 0; i < listOfNodes.size() - 1; ++i)
			{
				// the list is sorted so we know that i+1 is the direct neighbor node of "node", so the next node we want to draw a line to
				RailwayNode nextNode = listOfNodes.get(i + 1);

				int nextNodeIndex = indexOf(_sortedLineMap.get(nextNode), line);

				if(nextNodeIndex == -1) nextNodeIndex = 0;

				Point nextNodePosition = nextNode.getPosition();

				Point nextPosition, directionOffset;
				// TODO: make more accurate positions. This won't work for vertical and diagonal lines :(

				directionOffset = getDirectionOffset(thisNodePosition, nextNodePosition);

				if(thisPosition == null)
				{
					thisPosition = new Point(
						mapOffset.x +
							thisNodePosition.x * _gameState.getBaseNetSpacing() +
							directionOffset.x * (thisNodeIndex * _lineThickness + thisNodeIndex),
						mapOffset.y +
							thisNodePosition.y * _gameState.getBaseNetSpacing() +
							directionOffset.y * (thisNodeIndex * _lineThickness + thisNodeIndex));
				}

				nextPosition = new Point(
					mapOffset.x +
						nextNodePosition.x * _gameState.getBaseNetSpacing() +
						directionOffset.x * (nextNodeIndex * _lineThickness + nextNodeIndex),
					mapOffset.y +
						nextNodePosition.y * _gameState.getBaseNetSpacing() +
						directionOffset.y * (nextNodeIndex * _lineThickness + nextNodeIndex));

				drawColoredLine(mapOffset, thisPosition, nextPosition);
				
				thisNode = nextNode;
				thisNodeIndex = nextNodeIndex;
				thisNodePosition = nextNodePosition;
				thisPosition = (Point)nextPosition.clone();
			}
		}
	}

	/**
	 * Gets an offset that indicates the direction of the connection between the two nodes (directions are: | - / \ ).
	 * The offsets are:<br>
	 * - (0,1)<br>
	 * | (1,0)<br>
	 * / (1,1)<br>
	 * \ (-1,1)
	 * 
	 * @param p1 The first node.
	 * @param p2 The second node.
	 * @return The offset describing the direction of the connection between {@code p1} and {@code p2}.
	 */
	private Point getDirectionOffset(Point p1, Point p2)
	{
		if(p1.x == p2.x || p1.y == p2.y) // vertical or horizontal
		{
			return new Point(Math.abs(p1.y - p2.y), Math.abs(p1.x - p2.x)); // yes, x and y are switched, but they have to be switched
		}
		// else diagonal: The y offset is definitely 1, the x offset describes if the connection is / or \
		return new Point(p1.y - p2.y, 1);
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
		Draw.Line(position.x,
			position.y,
			positionNext.x,
			positionNext.y,
			_lineThickness);
		}
	}
}
