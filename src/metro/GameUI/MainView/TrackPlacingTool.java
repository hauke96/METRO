package metro.GameUI.MainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.Input.Buttons;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.Common.Graphics.Draw;
import metro.Common.Technical.Logger;
import metro.Exceptions.NotEnoughMoneyException;
import metro.GameUI.Common.ToolView;
import metro.GameUI.MainView.NotificationView.NotificationServer;
import metro.GameUI.MainView.NotificationView.NotificationType;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.TrainManagement.Nodes.RailwayNode;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Container.Panel;
import metro.UI.Renderable.Controls.Canvas;

/**
 * A track placing tool is used by the player to build new tracks for the trains.
 * 
 * @author hauke
 *
 */
public class TrackPlacingTool extends ToolView
{
	private RailwayNode _currentRailwayNode; // click -> set railwaynode -> click -> connect/create
	private PlayingField _playingField;

	private Canvas _canvas;
	private Panel _panel;
	private GameState _gameState;

	/**
	 * Creates a new Track placing tool.
	 * 
	 * @param gameState The current state of the player.
	 * @param playingField The current playing field.
	 */
	public TrackPlacingTool(GameState gameState, PlayingField playingField)
	{
		_gameState = gameState;
		_playingField = playingField;

		_canvas = new Canvas(new Point(0, 0));
		_canvas.setPainter(() -> draw());

		_panel = new Panel(new Rectangle(METRO.__SCREEN_SIZE.width, METRO.__SCREEN_SIZE.height), false);
		_panel.add(_canvas);
		_panel.setBackgroundColor(new Color(0, 0, 0, 0));

		_panel.setAboveOf(_playingField.getBackgroundPanel());
	}

	private void draw()
	{
		Point mapOffset = _playingField.getMapOffset();
		int baseNetSpacing = _gameState.getBaseNetSpacing();

		if(_currentRailwayNode != null) // if not null, calc and draw preview of new tracks
		{
			Point nodePosition = _currentRailwayNode.getPosition();
			Draw.setColor(Color.black);

			int diagonalOffset = 0,
				B = _playingField.getSelectedNode().x - nodePosition.x, // horizontal distance
				H = _playingField.getSelectedNode().y - nodePosition.y, // vertical distance
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
					mapOffset.x + _playingField.getSelectedNode().x * baseNetSpacing,
					mapOffset.y + _playingField.getSelectedNode().y * baseNetSpacing);
			}
		}
	}

	@Override
	public boolean mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(mouseButton == Buttons.RIGHT) return rightClick(screenX, screenY, _playingField.getMapOffset());
		else if(mouseButton == Buttons.LEFT) return leftClick(screenX, screenY, _playingField.getMapOffset());

		return false;
	}

	/**
	 * Removes all unnecessary nodes that have been created. After second click, the view will be deactivated.
	 * 
	 * @param screenX The y-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param offset The current map offset.
	 */
	protected boolean rightClick(int screenX, int screenY, Point offset)
	{
		if(_currentRailwayNode != null)
		{
			RailwayNodeOverseer.removeNodesWithoutNeighbors();
			_currentRailwayNode = null;
		}
		else
		{
			close();
		}

		return true;
	}

	/**
	 * Places tracks.
	 * 
	 * @param screenX The y-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param offset The current map offset.
	 * @return
	 */
	protected boolean leftClick(int screenX, int screenY, Point offset)
	{
		placeTracks(screenX, screenY, Buttons.LEFT, offset);

		return true;
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
			_currentRailwayNode = RailwayNodeOverseer.getNodeByPosition(_playingField.getSelectedNode()); // implicit check if node exists: null = node doesn't exist

			if(_currentRailwayNode == null) // if there's no node, create new one and set it as current node
			{
				RailwayNode node = RailwayNodeOverseer.createNode(_playingField.getSelectedNode());
				_currentRailwayNode = node;
			}
		}
		else
		// second click
		{
			int diagonalOffset = 0,
				B = _playingField.getSelectedNode().x - _currentRailwayNode.getPosition().x, // horizontal distance
				H = _playingField.getSelectedNode().y - _currentRailwayNode.getPosition().y, // vertical distance
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
				Logger.__info("The creation of tracks failes due to too less money on the players account.\n" + e.getMessage());
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

	/**
	 * @return The container that holds all controls.
	 */
	public AbstractContainer getBackgroundPanel()
	{
		return _panel;
	}

	@Override
	public boolean isHovered()
	{
		return false;
	}

	@Override
	public void close()
	{
		_panel.close();
		super.close();
	}
}
