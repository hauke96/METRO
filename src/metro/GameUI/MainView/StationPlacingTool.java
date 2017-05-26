package metro.GameUI.MainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import com.badlogic.gdx.Input.Buttons;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
import metro.GameUI.Common.ToolView;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Trains.TrainStation;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Container.Panel;
import metro.UI.Renderable.Controls.Canvas;

/**
 * A station placing tool makes it possible to create train stations.
 * 
 * @author hauke
 *
 */
// TODO also resolve by ServiceLocator
// reset after use
public class StationPlacingTool extends ToolView
{
	private Canvas	_canvas;
	private Panel	_panel;
	
	private GameState				_gameState;
	private PlayingField			_playingField;
	private TrainManagementService	_trainManagementService;
	
	/**
	 * Creates a new station placing tool.
	 * 
	 * @param gameState
	 *            The current state of the player.
	 * @param playingField
	 *            The current playing field.
	 * @param trainManagementService
	 *            The train management service.
	 */
	public StationPlacingTool(GameState gameState, PlayingField playingField, TrainManagementService trainManagementService)
	{
		_gameState = gameState;
		_playingField = playingField;
		_trainManagementService = trainManagementService;
		
		_canvas = new Canvas(new Point(0, 0));
		_canvas.setPainter(() -> draw());
		
		_panel = new Panel(new Rectangle(METRO.__SCREEN_SIZE.width, METRO.__SCREEN_SIZE.height), false);
		_panel.add(_canvas);
		_panel.setBackgroundColor(new Color(0, 0, 0, 0));
		
		_panel.setAboveOf(_playingField.getBackgroundPanel());
	}
	
	private void draw()
	{
		Point position = new Point(METRO.__mousePosition.x - 4, METRO.__mousePosition.y - 8); // Position with offset etc.
		Fill.setColor(Color.white);
		Fill.Rect(position.x, position.y, 8, 15);
		Draw.setColor(Color.black);
		Draw.Rect(position.x, position.y, 8, 15);
	}
	
	@Override
	public boolean mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if (mouseButton == Buttons.RIGHT) return rightClick(screenX, screenY, _playingField.getMapOffset());
		else if (mouseButton == Buttons.LEFT) return leftClick(screenX, screenY, _playingField.getMapOffset());
		
		return false;
	}
	
	/**
	 * Places a station on the pointed position (screenX, screenY).
	 * 
	 * @param screenX
	 *            The y-coordinate of the click.
	 * @param screenY
	 *            The y-coordinate of the click.
	 * @param mapOffset
	 *            The current map offset.
	 */
	private boolean leftClick(int screenX, int screenY, Point2D mapOffset)
	{
		boolean positionAlreadyHasStation = false;
		Point offsetAsPoint = new Point((int) mapOffset.getX(), (int) mapOffset.getY());
		Point selectedNode = _playingField.getSelectedNode();
		int baseNetSpacing = _gameState.getBaseNetSpacing();
		
		// TODO maybe get the position from the playing field?
		Point selectPointOnScreen = new Point(selectedNode.x * baseNetSpacing + (int) mapOffset.getX(), selectedNode.y * baseNetSpacing + (int) mapOffset.getY());
		
		for (TrainStation ts : _trainManagementService.getStations())
		{
			positionAlreadyHasStation |= ts.getPositionOnScreen(offsetAsPoint, baseNetSpacing).equals(selectPointOnScreen); // true if this cross has already a station
		}
		
		if (!positionAlreadyHasStation) // no doubles
		{
			_trainManagementService.addStation(new TrainStation(selectedNode, 0));
			return true;
		}
		
		return false;
	}
	
	/**
	 * Deactivates the tool to point out that this tool can be closed.
	 * 
	 * @param screenX
	 *            The y-coordinate of the click.
	 * @param screenY
	 *            The y-coordinate of the click.
	 * @param offset
	 *            The current map offset.
	 */
	private boolean rightClick(int screenX, int screenY, Point2D offset)
	{
		close();
		
		return true;
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
