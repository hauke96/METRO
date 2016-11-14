package metro.GameUI.MainView.PlayingField;

import java.awt.Color;
import java.awt.Point;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
import metro.GameUI.Screen.GameScreen;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;

/**
 * The playing field is the area where trains are driving, the stations are placed and the nodes live peaceful together with the tracks and train lines.
 * 
 * @author hauke
 *
 */
public class PlayingField extends GameScreen
{
	private CityView _cityView;
	private boolean _dragMode;
	private Point _oldMousePos, // Mouse position from last frame
		_mapOffset,
		_selectedNode;
	private GameState _gameState;

	private static final PlayingField __INSTANCE = new PlayingField();

	private PlayingField()
	{
		_dragMode = false;
		_cityView = new CityView(); // create extra instance for general purpose actions
		_mapOffset = new Point();
		_selectedNode = new Point(-1, -1);
		_gameState = GameState.getInstance();
	}

	/**
	 * @return The unique instance of the playing field.
	 */
	public static PlayingField getInstance()
	{
		return __INSTANCE;
	}

	/**
	 * Enables or disables the highlighting of city circles.
	 * 
	 * @param hightlight The state of the highlighting of city circles. True to enable, false to disable.
	 */
	public void setCityCircleHighlighting(boolean hightlight)
	{
		if(hightlight) _cityView.enableCircleHighlighting();
		else _cityView.disableCircleHighlighting();
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{

		if(_dragMode)
		{
			_mapOffset = new Point(_mapOffset.x + (METRO.__mousePosition.x - _oldMousePos.x),
				_mapOffset.y + (METRO.__mousePosition.y - _oldMousePos.y));
		}
		_oldMousePos = METRO.__mousePosition;

		_cityView.updateGameScreen(sp, _mapOffset);
		drawBaseNet(sp, new Color(220, 220, 220), 0);
		Point cursorDotPosition = drawBaseDot(sp);
		_cityView.drawNumbers(sp, cursorDotPosition);

		RailwayNodeOverseer.drawAllNodes(_mapOffset, sp);

		TrainManagementService trainManagementService = TrainManagementService.getInstance();
		trainManagementService.drawLines(_mapOffset, sp);
		trainManagementService.drawStations(_mapOffset, sp);
		trainManagementService.drawTrains(_mapOffset, sp);
	}

	/**
	 * Draws the basic gray net for kind of orientation.
	 * 
	 * @param sp The SpriteBatch to draw on
	 * @param color The color of the net
	 * @param offset An user made offset
	 */
	private void drawBaseNet(SpriteBatch sp, Color color, int offset)
	{
		Draw.setColor(color);
		for(int y = (_mapOffset.y + offset) % _gameState.getBaseNetSpacing(); y < METRO.__SCREEN_SIZE.height; y += _gameState.getBaseNetSpacing())
		{
			Draw.Line(offset, y, METRO.__SCREEN_SIZE.width, y);
		}
		for(int x = (_mapOffset.x + offset) % _gameState.getBaseNetSpacing(); x < METRO.__SCREEN_SIZE.width; x += _gameState.getBaseNetSpacing())
		{
			Draw.Line(x, offset, x, METRO.__SCREEN_SIZE.height);
		}
	}

	/**
	 * Calculates the position and draws the dot near the cursor.
	 * 
	 * @param sp The SpriteBatch to draw on
	 */
	private Point drawBaseDot(SpriteBatch sp)
	{
		_selectedNode = new Point(
			(int)Math.round((int)(METRO.__mousePosition.x - _mapOffset.x) / (float)_gameState.getBaseNetSpacing()),
			(int)Math.round((int)(METRO.__mousePosition.y - _mapOffset.y) / (float)_gameState.getBaseNetSpacing()));

		Point offsetMarker = new Point(
			(int)(_mapOffset.x) + _gameState.getBaseNetSpacing() * _selectedNode.x,
			(int)(_mapOffset.y) + _gameState.getBaseNetSpacing() * _selectedNode.y);

		Fill.setColor(Color.darkGray);
		Fill.Rect(offsetMarker.x - 1,
			offsetMarker.y - 1,
			3, 3);
		return new Point(offsetMarker.x - 1,
			offsetMarker.y - 1);
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(_cityView != null) _cityView.mouseClicked(screenX, screenY, mouseButton);

		if(mouseButton == Buttons.MIDDLE) // for drag-mode
		{
			_dragMode = true;
		}
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
		if(_cityView != null) _cityView.mouseReleased(mouseButton);

		if(mouseButton == Buttons.MIDDLE)
		{
			_dragMode = false;
		}
	}

	@Override
	public void mouseScrolled(int amount)
	{
		_gameState.zoom(amount);
	}

	@Override
	public boolean isActive()
	{
		return false;
	}

	@Override
	public boolean isHovered()
	{
		return false;
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
}
