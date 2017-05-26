package metro.GameUI.MainView.PlayingField;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Container.Panel;
import metro.UI.Renderable.Controls.Canvas;

/**
 * The playing field is the area where trains are driving, the stations are placed and the nodes live peaceful together with the tracks and train lines.
 * 
 * @author hauke
 *
 */
public class PlayingField implements InputProcessor
{
	private CityView				_cityView;
	private boolean					_dragMode;
	private Point					_oldMousePos,									// Mouse position from last frame
			_mapOffset,
			_selectedNode;
	private GameState				_gameState;
	private Panel					_panel;
	private TrainManagementService	_trainManagementService;
	
	/**
	 * Gets a fresh playing field.
	 * 
	 * @param gameState
	 *            The current state of the player.
	 * @param trainManagementService
	 *            The train management service.
	 */
	public PlayingField(GameState gameState, TrainManagementService trainManagementService)
	{
		_gameState = gameState;
		_trainManagementService = trainManagementService;
		
		_dragMode = false;
		_cityView = new CityView(_trainManagementService); // create extra instance for general purpose actions
		_mapOffset = new Point();
		_selectedNode = new Point(-1, -1);
		
		_panel = new Panel(new Rectangle(0, 0, METRO.__SCREEN_SIZE.width, METRO.__SCREEN_SIZE.height), false);
		
		Canvas canvas = new Canvas(new Point(0, 0));
		canvas.setPainter(() -> updateGameScreen());
		
		_panel.add(canvas);
	}
	
	/**
	 * Enables or disables the highlighting of city circles.
	 * 
	 * @param hightlight
	 *            The state of the highlighting of city circles. True to enable, false to disable.
	 */
	public void setCityCircleHighlighting(boolean hightlight)
	{
		if (hightlight) _cityView.enableCircleHighlighting();
		else _cityView.disableCircleHighlighting();
	}
	
	private void updateGameScreen()
	{
		if (_dragMode)
		{
			_mapOffset = new Point(_mapOffset.x + (METRO.__mousePosition.x - _oldMousePos.x), _mapOffset.y + (METRO.__mousePosition.y - _oldMousePos.y));
		}
		_oldMousePos = METRO.__mousePosition;
		
		_cityView.updateGameScreen(_mapOffset, _gameState.getBaseNetSpacing());
		drawBaseNet(new Color(220, 220, 220), 0);
		Point cursorDotPosition = drawBaseDot();
		_cityView.drawNumbers(cursorDotPosition);
		
		RailwayNodeOverseer.drawAllNodes(_mapOffset);
		
		_trainManagementService.drawLines(_mapOffset);
		_trainManagementService.drawStations(_mapOffset);
		_trainManagementService.drawTrains(_mapOffset);
	}
	
	/**
	 * Draws the basic gray net for kind of orientation.
	 * 
	 * @param color
	 *            The color of the net
	 * @param offset
	 *            An user made offset
	 */
	private void drawBaseNet(Color color, int offset)
	{
		Draw.setColor(color);
		for (int y = (_mapOffset.y + offset) % _gameState.getBaseNetSpacing(); y < METRO.__SCREEN_SIZE.height; y += _gameState.getBaseNetSpacing())
		{
			Draw.Line(offset, y, METRO.__SCREEN_SIZE.width, y);
		}
		for (int x = (_mapOffset.x + offset) % _gameState.getBaseNetSpacing(); x < METRO.__SCREEN_SIZE.width; x += _gameState.getBaseNetSpacing())
		{
			Draw.Line(x, offset, x, METRO.__SCREEN_SIZE.height);
		}
	}
	
	/**
	 * Calculates the position and draws the dot near the cursor.
	 */
	private Point drawBaseDot()
	{
		_selectedNode = new Point(
				(int) Math.round((int) (METRO.__mousePosition.x - _mapOffset.x)
						/ (float) _gameState.getBaseNetSpacing()), (int) Math.round((int) (METRO.__mousePosition.y - _mapOffset.y) / (float) _gameState.getBaseNetSpacing()));
		
		Point offsetMarker = new Point(
				(int) (_mapOffset.x) + _gameState.getBaseNetSpacing() * _selectedNode.x, (int) (_mapOffset.y) + _gameState.getBaseNetSpacing() * _selectedNode.y);
		
		Fill.setColor(Color.darkGray);
		Fill.Rect(offsetMarker.x - 1, offsetMarker.y - 1, 3, 3);
		return new Point(offsetMarker.x - 1, offsetMarker.y - 1);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		boolean processed = false;
		
		if (_cityView != null)
		{
			processed = true;
		}
		
		if (button == Buttons.MIDDLE) // for drag-mode
		{
			_dragMode = true;
			processed = true;
		}
		
		return processed;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		boolean processed = false;
		
		if (_cityView != null)
		{
			processed = true;
		}
		
		if (button == Buttons.MIDDLE) // for drag-mode
		{
			_dragMode = false;
			processed = true;
		}
		
		return processed;
	}
	
	@Override
	public boolean scrolled(int amount)
	{
		_gameState.zoom(amount);
		
		return true;
	}
	
	/**
	 * @return The current map offset.
	 */
	public Point getMapOffset()
	{
		return _mapOffset;
	}
	
	/**
	 * @return The currently hovered node.
	 */
	public Point getSelectedNode()
	{
		return _selectedNode;
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}
	
	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}
	
	public AbstractContainer getBackgroundPanel()
	{
		return _panel;
	}
}
