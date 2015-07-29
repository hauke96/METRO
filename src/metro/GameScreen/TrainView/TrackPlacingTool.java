package metro.GameScreen.TrainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import metro.METRO;
import metro.GameScreen.TrainInteractionTool;
import metro.Graphics.Draw;
import metro.TrainManagement.Nodes.RailwayNode;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TrackPlacingTool implements TrainInteractionTool
{
	private RailwayNode _currentRailwayNode; // click -> set railwaynode -> click -> connect/create
	private boolean _isClosed;

	public TrackPlacingTool()
	{
		_isClosed = false;
	}

	@Override
	public void draw(SpriteBatch sp, Point2D offset)
	{
		if(_currentRailwayNode != null) // if not null, calc and draw preview of new tracks
		{
			Draw.setColor(Color.black);

			int diagonalOffset = 0, B = TrainView._selectedCross.x - _currentRailwayNode.getPosition().x, H = TrainView._selectedCross.y - _currentRailwayNode.getPosition().y, preFactor = 1; // counts the amount of fields covered

			if(Math.abs(H) > Math.abs(B)) // vertical tracks
			{
				diagonalOffset = (int)((Math.abs(H) - Math.abs(B)) / 2f);
				if(H < 0) preFactor = -1;

				Draw.Line(offset.getX() + _currentRailwayNode.getPosition().x * METRO.__baseNetSpacing,
					offset.getY() + _currentRailwayNode.getPosition().y * METRO.__baseNetSpacing,
					offset.getX() + _currentRailwayNode.getPosition().x * METRO.__baseNetSpacing,
					offset.getY() + (_currentRailwayNode.getPosition().y + preFactor * diagonalOffset) * METRO.__baseNetSpacing);

				Draw.Line(offset.getX() + _currentRailwayNode.getPosition().x * METRO.__baseNetSpacing,
					offset.getY() + (_currentRailwayNode.getPosition().y + preFactor * diagonalOffset) * METRO.__baseNetSpacing,
					offset.getX() + (_currentRailwayNode.getPosition().x + B) * METRO.__baseNetSpacing,
					offset.getY() + (_currentRailwayNode.getPosition().y + preFactor * (diagonalOffset + Math.abs(B))) * METRO.__baseNetSpacing);

				Draw.Line(offset.getX() + (_currentRailwayNode.getPosition().x + B) * METRO.__baseNetSpacing,
					offset.getY() + (_currentRailwayNode.getPosition().y + preFactor * (diagonalOffset + Math.abs(B))) * METRO.__baseNetSpacing,
					offset.getX() + (_currentRailwayNode.getPosition().x + B) * METRO.__baseNetSpacing,
					offset.getY() + (_currentRailwayNode.getPosition().y + preFactor * (2 * diagonalOffset + preFactor * ((H - B) % 2) + Math.abs(B))) * METRO.__baseNetSpacing);
			}
			else if(Math.abs(B) > Math.abs(H)) // horizontal tracks
			{
				diagonalOffset = (int)((Math.abs(B) - Math.abs(H)) / 2f);
				if(B < 0) preFactor = -1;

				Draw.Line(offset.getX() + (_currentRailwayNode.getPosition().x + preFactor * diagonalOffset) * METRO.__baseNetSpacing,
					offset.getY() + _currentRailwayNode.getPosition().y * METRO.__baseNetSpacing,
					offset.getX() + _currentRailwayNode.getPosition().x * METRO.__baseNetSpacing,
					offset.getY() + _currentRailwayNode.getPosition().y * METRO.__baseNetSpacing);

				Draw.Line(offset.getX() + (_currentRailwayNode.getPosition().x + preFactor * diagonalOffset) * METRO.__baseNetSpacing,
					offset.getY() + _currentRailwayNode.getPosition().y * METRO.__baseNetSpacing,
					offset.getX() + (_currentRailwayNode.getPosition().x + preFactor * (diagonalOffset + Math.abs(H))) * METRO.__baseNetSpacing,
					offset.getY() + (_currentRailwayNode.getPosition().y + H) * METRO.__baseNetSpacing);

				Draw.Line(offset.getX() + (_currentRailwayNode.getPosition().x + preFactor * (diagonalOffset + Math.abs(H))) * METRO.__baseNetSpacing,
					offset.getY() + (_currentRailwayNode.getPosition().y + H) * METRO.__baseNetSpacing,
					offset.getX() + (_currentRailwayNode.getPosition().x + preFactor * (2 * diagonalOffset + preFactor * ((B - H) % 2) + Math.abs(H))) * METRO.__baseNetSpacing,
					offset.getY() + (_currentRailwayNode.getPosition().y + H) * METRO.__baseNetSpacing);
			}
			else if(Math.abs(B) == Math.abs(H)) // diagonal tracks
			{
				Draw.Line(offset.getX() + _currentRailwayNode.getPosition().x * METRO.__baseNetSpacing,
					offset.getY() + _currentRailwayNode.getPosition().y * METRO.__baseNetSpacing,
					offset.getX() + TrainView._selectedCross.x * METRO.__baseNetSpacing,
					offset.getY() + TrainView._selectedCross.y * METRO.__baseNetSpacing);
			}
		}
	}

	@Override
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		placeTracks(screenX, screenY, Buttons.LEFT, offset);
	}

	@Override
	public void rightClick(int screenX, int screenY, Point2D offset)
	{
		if(_currentRailwayNode != null)
		{
			RailwayNodeOverseer.removeNodesWithoutNeighbors();
			_currentRailwayNode = null;
		}
		else
		{
			_isClosed = true;
		}
	}

	/**
	 * Created nodes after second mouse click, removes doubles and manages calculation of automatic routiung.
	 * 
	 * @param screenX The x-position on screen of the click
	 * @param screenY The y-position on screen of the click
	 * @param mouseButton The mouse button that has been pressed.
	 * @param offset The map offset
	 */
	private void placeTracks(int screenX, int screenY, int mouseButton, Point2D offset)
	{
		if(_currentRailwayNode == null) // first click
		{
			_currentRailwayNode = RailwayNodeOverseer.getNodeByPosition(TrainView._selectedCross); // implicit check if node exists: null = node doesn't exist

			if(_currentRailwayNode == null) // if there's no node, create new one and set it as current node
			{
				RailwayNode node = new RailwayNode(TrainView._selectedCross, null);
				_currentRailwayNode = node;
			}
		}
		else
		// second click
		{
			int diagonalOffset = 0, B = TrainView._selectedCross.x - _currentRailwayNode.getPosition().x, // horizontal distance
			H = TrainView._selectedCross.y - _currentRailwayNode.getPosition().y, // vertical distance
			preFactorH = 1, preFactorB = 1;
			RailwayNode prevNode = _currentRailwayNode;

			if(Math.abs(H) > Math.abs(B)) // vertical tracks
			{
				diagonalOffset = (int)((Math.abs(H) - Math.abs(B)) / 2f); // calculate length of one vertical part
				if(H < 0) preFactorH = -1;
				if(B < 0) preFactorB = -1;

				prevNode = createTrack(0, preFactorH, 0, diagonalOffset, prevNode); // vertical line
				prevNode = createTrack(preFactorB, preFactorH, 0, Math.abs(B), prevNode); // diagonal lines
				createTrack(0, preFactorH, diagonalOffset + Math.abs(B), Math.abs(H), prevNode); // vertical lines
			}
			else if(Math.abs(B) > Math.abs(H))
			{
				diagonalOffset = (int)((Math.abs(B) - Math.abs(H)) / 2f);
				if(H < 0) preFactorH = -1;
				if(B < 0) preFactorB = -1;

				prevNode = createTrack(preFactorB, 0, 0, diagonalOffset, prevNode); // vertical lines
				prevNode = createTrack(preFactorB, preFactorH, 0, Math.abs(H), prevNode); // diagonal lines
				createTrack(preFactorB, 0, diagonalOffset + Math.abs(H), Math.abs(B), prevNode); // vertical lines
			}
			else if(Math.abs(B) == Math.abs(H))
			{
				if(H < 0) preFactorH = -1;
				if(B < 0) preFactorB = -1;

				createTrack(preFactorB, preFactorH, 0, Math.abs(H), prevNode); // diagonal lines
			}
			_currentRailwayNode = null;
		}
	}

	/**
	 * Creates one Track (~ one line) for the parameter.
	 * 
	 * @param offsetB Offset for horizontal position.
	 * @param offsetH Offset for vertical position.
	 * @param start Start of counter.
	 * @param end End of counter.
	 * @param prevNode The previous node that should be connected to new ones.
	 */
	private RailwayNode createTrack(int offsetB, int offsetH, int start, int end, RailwayNode prevNode)
	{
		for(int i = start; i < end; i++)
		{
			Point nodePosition = new Point(prevNode.getPosition().x + offsetB, prevNode.getPosition().y + offsetH);
			RailwayNode node = null;

			if(!RailwayNodeOverseer.isNodeAt(nodePosition)) // if there's NO node at this position
			{
				node = new RailwayNode(new Point(
					prevNode.getPosition().x + offsetB,
					prevNode.getPosition().y + offsetH),
					prevNode);
				METRO.__money -= RailwayNode.PRICE;
			}
			else
			// if there's a node at this position, set it as node instead of new one
			{
				node = RailwayNodeOverseer.getNodeByPosition(nodePosition);
			}

			prevNode.addNeighbor(node); // connect to previous node
			prevNode = node; // set previous node to current one to go on
		}
		return prevNode;
	}

	@Override
	public boolean isClosed()
	{
		return _isClosed;
	}
}
