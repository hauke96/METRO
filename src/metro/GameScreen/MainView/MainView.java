package metro.GameScreen.MainView;

import java.awt.Color;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.GameState;
import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.GameScreen.MainView.CityView.CityView;
import metro.GameScreen.MainView.LineView.LineView;
import metro.GameScreen.MainView.TrainView.TrainView;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;
import metro.TrainManagement.Trains.TrainStation;

/**
 * The main view is the normal screen the player sees.
 * All sub tools like the station placing tool or the train management will be organized and drawn by this tool.
 * Furthermore it shows all player information and draws the whole map with its trains, stations and tracks.
 * 
 * @author Hauke
 *
 */

public class MainView extends GameScreen implements Observer
{
	private boolean _dragMode;
	private Point _oldMousePos; // Mouse position from last frame
	private GameScreen _activeTool;
	private Toolbar _toolbar;
	private CityView _cityView;
	private GameState _gameState;

	/**
	 * The position of the selected cross of the game grid.
	 */
	public static Point _selectedCross;
	/**
	 * The offset of the map in pixel.
	 */
	public static Point _mapOffset;

	/**
	 * Creates a new main view with no active sub tools.
	 */
	public MainView()
	{
		_selectedCross = new Point(-1, -1);
		_mapOffset = new Point(0, 0);// METRO.__baseNetSpacing * 3, METRO.__baseNetSpacing * 2 + 12);

		_toolbar = new Toolbar();
		_toolbar.addObserver(this);

		_dragMode = false;
		_activeTool = null;

		_cityView = new CityView(); // create extra instance for general purpose actions

		_gameState = GameState.getInstance();
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		if(_activeTool != null && _activeTool.isHovered()) _cityView.disableCircleHighlighting();
		else _cityView.enableCircleHighlighting();

		if(_cityView != null) _cityView.updateGameScreen(sp);

		if(_dragMode)
		{
			_mapOffset = new Point(_mapOffset.x + (METRO.__mousePosition.x - _oldMousePos.x),
				_mapOffset.y + (METRO.__mousePosition.y - _oldMousePos.y));
		}
		_oldMousePos = METRO.__mousePosition;

		drawBaseNet(sp, new Color(220, 220, 220), 0);
		Point cursorDotPosition = drawBaseDot(sp);

		_cityView.drawNumbers(sp, cursorDotPosition);

		RailwayNodeOverseer.drawAllNodes(_mapOffset, sp);

		TrainManagementService trainManagementService = TrainManagementService.getInstance();
		trainManagementService.drawLines(_mapOffset, sp);
		drawTrainStations(sp);
		trainManagementService.drawTrains(_mapOffset, sp);

		if(_activeTool != null) _activeTool.updateGameScreen(sp);

		_toolbar.updateGameScreen(sp);

		printDebugStuff(sp);
	}

	/**
	 * Prints all the debug stuff, that is -more or less- important.
	 * 
	 * @param sp The SpriteBatch to draw on
	 */
	private void printDebugStuff(SpriteBatch sp)
	{
		Draw.setColor(Color.blue);
		Draw.String("MapPosition:", METRO.__SCREEN_SIZE.width - 680, 10);
		Draw.String("MousePosition:", METRO.__SCREEN_SIZE.width - 680, 30);
		Draw.String("SelectedCross:", METRO.__SCREEN_SIZE.width - 680, 50);
		Draw.setColor(Color.black);
		Draw.String(_mapOffset + "", METRO.__SCREEN_SIZE.width - 580, 10);
		Draw.String(METRO.__mousePosition + "", METRO.__SCREEN_SIZE.width - 580, 30);
		Draw.String(_selectedCross + "", METRO.__SCREEN_SIZE.width - 580, 50);
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
		_selectedCross = new Point(
			(int)Math.round((int)(METRO.__mousePosition.x - _mapOffset.getX()) / (float)_gameState.getBaseNetSpacing()),
			(int)Math.round((int)(METRO.__mousePosition.y - _mapOffset.getY()) / (float)_gameState.getBaseNetSpacing()));

		Point offsetMarker = new Point(
			(int)(_mapOffset.getX()) + _gameState.getBaseNetSpacing() * _selectedCross.x,
			(int)(_mapOffset.getY()) + _gameState.getBaseNetSpacing() * _selectedCross.y);

		Fill.setColor(Color.darkGray);
		Fill.Rect(offsetMarker.x - 1,
			offsetMarker.y - 1,
			3, 3);
		return new Point(offsetMarker.x - 1,
			offsetMarker.y - 1);
	}

	/**
	 * Draws all the train stations with conenctions and labels.
	 * 
	 * @param sp The SpriteBatch to draw on
	 */
	private void drawTrainStations(SpriteBatch sp)
	{
		Point offset = new Point((int)_mapOffset.getX(), (int)_mapOffset.getY());

		for(TrainStation ts : METRO.__gameState.getStations())
		{
			ts.draw(sp, offset);
		}
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(_cityView != null) _cityView.mouseClicked(screenX, screenY, mouseButton);

		if(mouseButton == Buttons.MIDDLE) // for drag-mode
		{
			_dragMode = true;
		}
		else if(mouseButton == Buttons.LEFT)
		{
			if(_activeTool != null)
			{
				_activeTool.mouseClicked(screenX, screenY, mouseButton);
			}
		}
		else if(mouseButton == Buttons.RIGHT && _activeTool != null)
		{
			_activeTool.mouseClicked(screenX, screenY, mouseButton);
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
		if(_activeTool instanceof LineView) ((LineView)_activeTool).mouseScrolled(amount);
	}

	@Override
	public void keyDown(int keyCode)
	{
		if(_activeTool instanceof LineView) ((LineView)_activeTool).keyDown(keyCode);
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		if(arg0.equals(_toolbar))
		{
			if(_activeTool != null) _activeTool.close();

			if(arg1 instanceof StationPlacingTool
				|| arg1 instanceof TrackPlacingTool
				|| arg1 instanceof LineView
				|| arg1 instanceof TrainView)
			{
				setActiveTool((GameScreen)arg1);
			}
			else
			{
				_activeTool = null;
			}
		}
		else
		{
			if(!_activeTool.isActive())
			{
				_toolbar.resetExclusiveButtonPositions(null);
				_activeTool.close();
				_activeTool = null;
			}
		}
	}

	/**
	 * Sets the active tool and adds an observer to this.
	 * 
	 * @param newTool The new tool that should be used.
	 */
	private void setActiveTool(GameScreen newTool)
	{
		_activeTool = newTool;
		_activeTool.addObserver(this);
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void reset()
	{
	}

	@Override
	public boolean isHovered()
	{
		return true;
	}
}