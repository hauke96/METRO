package metro.GameScreen.MainView;

import java.awt.Color;
import java.awt.Point;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.GameState;
import metro.METRO;
import metro.Exceptions.NotEnoughMoneyException;
import metro.GameScreen.GameScreen;
import metro.GameScreen.MainView.NotificationView.NotificationServer;
import metro.GameScreen.MainView.NotificationView.NotificationType;
import metro.Graphics.Draw;
import metro.TrainManagement.Nodes.RailwayNode;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;

/**
 * A track placing tool is used by the player to build new tracks for the trains.
 * 
 * @author hauke
 *
 */
public class TrackPlacingTool extends GameScreen
{
	private RailwayNode _currentRailwayNode; // click -> set railwaynode -> click -> connect/create
	private boolean _isActive;

	/**
	 * Creates a new Track placing tool.
	 */
	public TrackPlacingTool()
	{
		_isActive = true;
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		if(!_isActive) return;

		Point mapOffset = MainView.__mapOffset;
		int baseNetSpacing = GameState.getInstance().getBaseNetSpacing();

		if(_currentRailwayNode != null) // if not null, calc and draw preview of new tracks
		{
			Point nodePosition = _currentRailwayNode.getPosition();
			Draw.setColor(Color.black);

			int diagonalOffset = 0,
				B = MainView.__selectedCross.x - nodePosition.x, // horizontal distance
				H = MainView.__selectedCross.y - nodePosition.y, // vertical distance
				preFactor = 1;

			if(Math.abs(H) > Math.abs(B)) // vertical tracks
			{
				diagonalOffset = (int)((Math.abs(H) - Math.abs(B)) / 2f);
				if(H < 0) preFactor = -1;

				Draw.Line(mapOffset.x + nodePosition.x * baseNetSpacing,
					mapOffset.y + nodePosition.y * baseNetSpacing,
					mapOffset.x + nodePosition.x * baseNetSpacing,
					mapOffset.y + (nodePosition.y + preFactor * diagonalOffset) * baseNetSpacing);

				Draw.Line(mapOffset.x + nodePosition.x * baseNetSpacing,
					mapOffset.y + (nodePosition.y + preFactor * diagonalOffset) * baseNetSpacing,
					mapOffset.x + (nodePosition.x + B) * baseNetSpacing,
					mapOffset.y + (nodePosition.y + preFactor * (diagonalOffset + Math.abs(B))) * baseNetSpacing);

				Draw.Line(mapOffset.x + (nodePosition.x + B) * baseNetSpacing,
					mapOffset.y + (nodePosition.y + preFactor * (diagonalOffset + Math.abs(B))) * baseNetSpacing,
					mapOffset.x + (nodePosition.x + B) * baseNetSpacing,
					mapOffset.y
						+ (nodePosition.y + preFactor * (2 * diagonalOffset + preFactor * ((H - B) % 2) + Math.abs(B))) * baseNetSpacing);
			}
			else if(Math.abs(B) > Math.abs(H)) // horizontal tracks
			{
				diagonalOffset = (int)((Math.abs(B) - Math.abs(H)) / 2f);
				if(B < 0) preFactor = -1;

				Draw.Line(mapOffset.x + (nodePosition.x + preFactor * diagonalOffset) * baseNetSpacing,
					mapOffset.y + nodePosition.y * baseNetSpacing,
					mapOffset.x + nodePosition.x * baseNetSpacing,
					mapOffset.y + nodePosition.y * baseNetSpacing);

				Draw.Line(mapOffset.x + (nodePosition.x + preFactor * diagonalOffset) * baseNetSpacing,
					mapOffset.y + nodePosition.y * baseNetSpacing,
					mapOffset.x + (nodePosition.x + preFactor * (diagonalOffset + Math.abs(H))) * baseNetSpacing,
					mapOffset.y + (nodePosition.y + H) * baseNetSpacing);

				Draw.Line(mapOffset.x + (nodePosition.x + preFactor * (diagonalOffset + Math.abs(H))) * baseNetSpacing,
					mapOffset.y + (nodePosition.y + H) * baseNetSpacing,
					mapOffset.x + (nodePosition.x + preFactor * (2 * diagonalOffset + preFactor * ((B - H) % 2) + Math.abs(H))) * baseNetSpacing,
					mapOffset.y + (nodePosition.y + H) * baseNetSpacing);
			}
			else if(Math.abs(B) == Math.abs(H)) // diagonal tracks
			{
				Draw.Line(mapOffset.x + nodePosition.x * baseNetSpacing,
					mapOffset.y + nodePosition.y * baseNetSpacing,
					mapOffset.x + MainView.__selectedCross.x * baseNetSpacing,
					mapOffset.y + MainView.__selectedCross.y * baseNetSpacing);
			}
		}
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(!_isActive) return;
		if(mouseButton == Buttons.RIGHT) rightClick(screenX, screenY, MainView.__mapOffset);
		else if(mouseButton == Buttons.LEFT) leftClick(screenX, screenY, MainView.__mapOffset);
	}

	/**
	 * Places tracks.
	 * 
	 * @param screenX The y-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param offset The current map offset.
	 */
	public void leftClick(int screenX, int screenY, Point offset)
	{
		placeTracks(screenX, screenY, Buttons.LEFT, offset);
	}

	/**
	 * Removes all unnecessary nodes that have been created. After second click, the view will be deactivated.
	 * 
	 * @param screenX The y-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param offset The current map offset.
	 */
	public void rightClick(int screenX, int screenY, Point offset)
	{
		if(_currentRailwayNode != null)
		{
			RailwayNodeOverseer.removeNodesWithoutNeighbors();
			_currentRailwayNode = null;
		}
		else
		{
			_isActive = false;
			setChanged();
			notifyObservers(); // notify about close
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
	private void placeTracks(int screenX, int screenY, int mouseButton, Point offset)
	{
		if(_currentRailwayNode == null) // first click
		{
			_currentRailwayNode = RailwayNodeOverseer.getNodeByPosition(MainView.__selectedCross); // implicit check if node exists: null = node doesn't exist

			if(_currentRailwayNode == null) // if there's no node, create new one and set it as current node
			{
				RailwayNode node = RailwayNodeOverseer.createNode(MainView.__selectedCross);
				_currentRailwayNode = node;
			}
		}
		else
		// second click
		{
			int diagonalOffset = 0,
				B = MainView.__selectedCross.x - _currentRailwayNode.getPosition().x, // horizontal distance
				H = MainView.__selectedCross.y - _currentRailwayNode.getPosition().y, // vertical distance
				preFactorH = 1,
				preFactorB = 1;
			RailwayNode prevNode = _currentRailwayNode;

			if(H < 0) preFactorH = -1;
			if(B < 0) preFactorB = -1;

			try
			{
				if(Math.abs(H) > Math.abs(B)) // vertical tracks
				{
					diagonalOffset = (int)((Math.abs(H) - Math.abs(B)) / 2f); // calculate length of one vertical part

					METRO.__gameState.withdrawMoney(RailwayNode.__price *
						(diagonalOffset + Math.abs(B) + (Math.abs(H) - (diagonalOffset + Math.abs(B)))));

					prevNode = createTrack(0, preFactorH, 0, diagonalOffset, prevNode); // vertical line
					prevNode = createTrack(preFactorB, preFactorH, 0, Math.abs(B), prevNode); // diagonal lines
					createTrack(0, preFactorH, diagonalOffset + Math.abs(B), Math.abs(H), prevNode); // vertical lines
				}
				else if(Math.abs(B) > Math.abs(H))
				{
					diagonalOffset = (int)((Math.abs(B) - Math.abs(H)) / 2f);
					
					METRO.__gameState.withdrawMoney(RailwayNode.__price *
						(diagonalOffset + Math.abs(H) + (Math.abs(B) - (diagonalOffset + Math.abs(H)))));

					prevNode = createTrack(preFactorB, 0, 0, diagonalOffset, prevNode); // vertical lines
					prevNode = createTrack(preFactorB, preFactorH, 0, Math.abs(H), prevNode); // diagonal lines
					createTrack(preFactorB, 0, diagonalOffset + Math.abs(H), Math.abs(B), prevNode); // vertical lines

				}
				else if(Math.abs(B) == Math.abs(H))
				{
					METRO.__gameState.withdrawMoney(RailwayNode.__price * Math.abs(H));
					
					createTrack(preFactorB, preFactorH, 0, Math.abs(H), prevNode); // diagonal lines
				}
			}
			catch(NotEnoughMoneyException e)
			{
				NotificationServer.publishNotification("You have not enough money to build these tracks.", NotificationType.GAME_ERROR);
				METRO.__debug("[CreatingTrackFailed]\nThe creation of tracks failes due to too less money on the players account.\n" + e.getMessage());
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
		for(int i = start; i < end; ++i)
		{
			Point nodePosition = new Point(prevNode.getPosition().x + offsetB, prevNode.getPosition().y + offsetH);
			RailwayNode node = null;

			if(!RailwayNodeOverseer.isNodeAt(nodePosition)) // if there's NO node at this position
			{
				node = RailwayNodeOverseer.createNode(new Point(
					prevNode.getPosition().x + offsetB,
					prevNode.getPosition().y + offsetH),
					prevNode);

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
