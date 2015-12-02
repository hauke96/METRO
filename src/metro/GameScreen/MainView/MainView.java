package metro.GameScreen.MainView;

import java.awt.Color;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.GameScreen.Toolbar;
import metro.GameScreen.CityView.CityView;
import metro.GameScreen.LineView.LineView;
import metro.GameScreen.TrainView.TrainView;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.Lines.TrainLineOverseer;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;
import metro.TrainManagement.Trains.Train;
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
	private GameScreen _controlDrawer;

	/**
	 * The position of the selected cross of the game grid.
	 */
	public static Point _selectedCross;
	/**
	 * The offset of the map in pixel.
	 */
	public static Point _mapOffset;

	/**
	 * Creates a new main view with no active subtools.
	 */
	public MainView()
	{
		_selectedCross = new Point(-1, -1);
		_mapOffset = new Point(0, 0);// METRO.__baseNetSpacing * 3, METRO.__baseNetSpacing * 2 + 12);

		_toolbar = new Toolbar(new Point(0, 100));
		_toolbar.addObserver(this);

		_dragMode = false;
		_activeTool = null;

		_cityView = new CityView(); // create extra instance for general purpose actions
		_controlDrawer = new ScreenInfoDrawer();
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
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
		drawRailwayLines(sp);
		drawTrainStations(sp);
		drawTrains(sp);

		if(_activeTool != null) _activeTool.updateGameScreen(sp);

		_toolbar.updateGameScreen(sp);

		printDebugStuff(sp);
		_controlDrawer.update(sp);
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
		for(int y = (_mapOffset.y + offset) % METRO.__baseNetSpacing; y < METRO.__SCREEN_SIZE.height; y += METRO.__baseNetSpacing)
		{
			Draw.Line(offset, y, METRO.__SCREEN_SIZE.width, y);
		}
		for(int x = (_mapOffset.x + offset) % METRO.__baseNetSpacing; x < METRO.__SCREEN_SIZE.width; x += METRO.__baseNetSpacing)
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
			(int)Math.round((int)(METRO.__mousePosition.x - _mapOffset.getX()) / (float)METRO.__baseNetSpacing),
			(int)Math.round((int)(METRO.__mousePosition.y - _mapOffset.getY()) / (float)METRO.__baseNetSpacing));

		Point offsetMarker = new Point((int)(_mapOffset.getX()) + METRO.__baseNetSpacing * _selectedCross.x,
			(int)(_mapOffset.getY()) + METRO.__baseNetSpacing * _selectedCross.y);

		Fill.setColor(Color.darkGray);
		Fill.Rect(offsetMarker.x - 1,
			offsetMarker.y - 1,
			3, 3);
		return new Point(offsetMarker.x - 1,
			offsetMarker.y - 1);
	}

	/**
	 * Draws all the railway lines
	 * 
	 * @param sp The SpriteBatch to draw on
	 */
	private void drawRailwayLines(SpriteBatch sp)
	{
		Point offset = new Point((int)_mapOffset.getX(), (int)_mapOffset.getY());

		TrainLineOverseer.drawLines(offset, sp);
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

	private void drawTrains(SpriteBatch sp)
	{
		Point offset = new Point((int)_mapOffset.getX(), (int)_mapOffset.getY());
		
		for(Train train : METRO.__gameState.getTrains())
		{
			train.draw(sp, offset);
		}
	}

	/**
	 * Sets a new TrainViewTool.
	 * 
	 * @param tool The new tool.
	 */
	public void setTrainViewTool(GameScreen tool)
	{
		_activeTool = tool;
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
			update(_toolbar, METRO.__mousePosition); // emulate the observer to update the tools correctly
		}
		else if(mouseButton == Buttons.RIGHT && _activeTool != null)
		{
			_activeTool.mouseClicked(screenX, screenY, mouseButton);
			if(!_activeTool.isActive())
			{
				_toolbar.resetExclusiveButtonPositions(null);
				setTrainViewTool(null);
			}
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
			if(arg1 instanceof Point && _activeTool != null) // there is an active tool, forward click to it
			{
				Point pos = (Point)arg1;
				_activeTool.mouseClicked(pos.x, pos.y, Buttons.LEFT); // update(...) will only be executed when the left mouse button has been pressed
			}
			else // there is no active tool -> create new one from argument
			{
				if(_activeTool != null) _activeTool.close();

				if(_activeTool instanceof LineView && !(arg1 instanceof LineView)) ((LineView)_activeTool).setVisibility(false);
				if(_activeTool instanceof TrainView && !(arg1 instanceof TrainView)) ((TrainView)_activeTool).setVisibility(false);

				if(arg1 instanceof StationPlacingTool) _activeTool = (StationPlacingTool)arg1;
				else if(arg1 instanceof TrackPlacingTool) _activeTool = (TrackPlacingTool)arg1;
				else if(arg1 instanceof LineView) _activeTool = (LineView)arg1;// _lineView;
				else if(arg1 instanceof TrainView) _activeTool = (TrainView)arg1;
				else _activeTool = null;

				// set visibility of line tool
				if(_activeTool instanceof LineView) ((LineView)_activeTool).setVisibility(true);

				// set visibility of train tool
				if(_activeTool instanceof TrainView) ((TrainView)_activeTool).setVisibility(true);
			}
		}
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
}