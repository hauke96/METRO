package metro.GameScreen.TrainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.GameScreen.Toolbar;
import metro.GameScreen.TrainInteractionTool;
import metro.GameScreen.CityView.CityView;
import metro.GameScreen.TrainLineView.TrainLineSelectTool;
import metro.GameScreen.TrainLineView.TrainLineView;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;
import metro.TrainManagement.Trains.TrainStation;
import metro.WindowControls.Button;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * GameScreen with the default view. It Shows the trains, tracks, stations and basic player information.
 * 
 * @author Hauke
 *
 */

public class TrainView extends GameScreen implements Observer
{
	private boolean _dragMode;
	private Point _oldMousePos; // Mouse position from last frame
	// private TrainStation _selectedTrainStation = null; // important for later stuff when the user can select a train station
	private CityView _cityView;
	private TrainInteractionTool _activeTool;
	private Point2D _mapOffset; // offset for moving the map
	private Toolbar _toolbar;
	private TrainLineView _trainLineView;

	public static List<TrainStation> _trainStationList;
	public static Point _selectedCross; // out of screen;

	public TrainView()
	{
		_selectedCross = new Point(-1, -1);
		_trainStationList = new ArrayList<TrainStation>();
		_mapOffset = new Point2D.Float(0, 0);// METRO.__baseNetSpacing * 3, METRO.__baseNetSpacing * 2 + 12);

		_toolbar = new Toolbar(new Point(0, 100));
		_toolbar.addObserver(this);

		_dragMode = false;
		_activeTool = null;

		_cityView = new CityView(); // create extra instance for general purpose actions

		_trainLineView = new TrainLineView(_mapOffset);
		_trainLineView.setVisibility(false);
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		if(_cityView != null) _cityView.updateGameScreen(sp);

		if(_dragMode)
		{
			_mapOffset = new Point2D.Float((float)_mapOffset.getX() + (METRO.__mousePosition.x - _oldMousePos.x),
				(float)_mapOffset.getY() + (METRO.__mousePosition.y - _oldMousePos.y));
		}
		_oldMousePos = METRO.__mousePosition;

		drawBaseNet(sp, new Color(220, 220, 220), 0);
		Point cursorDotPosition = drawBaseDot(sp);

		_cityView.drawNumbers(sp, cursorDotPosition);

		if(_activeTool != null) _activeTool.draw(sp, _mapOffset);

		drawRailwayLines(sp);
		drawTrainStations(sp);

		_toolbar.draw(sp, _mapOffset);

		printDebugStuff(sp);

		if(_trainLineView != null) _trainLineView.updateGameScreen(sp);
	}

	/**
	 * Prints all the debug stuff, that is -more or less- important.
	 * 
	 * @param sp The SpriteBatch to draw on
	 */
	private void printDebugStuff(SpriteBatch sp)
	{
		Draw.setColor(Color.blue);
		Draw.String("MapPosition:", METRO.__SCREEN_SIZE.width - 300, 65);
		Draw.String("MousePosition:", METRO.__SCREEN_SIZE.width - 300, 85);
		Draw.String("SelectedCross:", METRO.__SCREEN_SIZE.width - 300, 105);
		Draw.setColor(Color.black);
		Draw.String(_mapOffset + "", METRO.__SCREEN_SIZE.width - 200, 65);
		Draw.String(METRO.__mousePosition + "", METRO.__SCREEN_SIZE.width - 200, 85);
		Draw.String(_selectedCross + "", METRO.__SCREEN_SIZE.width - 200, 105);
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
		for(int y = ((int)_mapOffset.getY() + offset) % METRO.__baseNetSpacing; y < METRO.__SCREEN_SIZE.height; y += METRO.__baseNetSpacing)
		{
			Draw.Line(offset, y, METRO.__SCREEN_SIZE.width, y);
		}
		for(int x = ((int)_mapOffset.getX() + offset) % METRO.__baseNetSpacing; x < METRO.__SCREEN_SIZE.width; x += METRO.__baseNetSpacing)
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
	 * Draws all the train stations with conenctions and labels.
	 * 
	 * @param sp The SpriteBatch to draw on
	 */
	private void drawTrainStations(SpriteBatch sp)
	{
		Point offset = new Point((int)_mapOffset.getX(), (int)_mapOffset.getY());

		// Draw stations:
		for(TrainStation ts : _trainStationList)
		{
			ts.draw(sp, offset);
		}
	}

	/**
	 * Draws all the railway lines
	 * 
	 * @param sp The SpriteBatch to draw on
	 */
	private void drawRailwayLines(SpriteBatch sp)
	{
		Point offset = new Point((int)_mapOffset.getX(), (int)_mapOffset.getY());

		RailwayNodeOverseer.drawAllNodes(offset, sp);
	}

	/**
	 * Sets a new TrainViewTool.
	 * 
	 * @param tool The new tool.
	 */
	public void setTrainViewTool(TrainInteractionTool tool)
	{
		_activeTool = tool;
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(_cityView != null) _cityView.mouseClicked(screenX, screenY, mouseButton);
		if(_trainLineView != null) _trainLineView.mouseClicked(screenX, screenY, mouseButton);

		if(mouseButton == Buttons.MIDDLE) // for drag-mode
		{
			_dragMode = true;
		}
		else if(mouseButton == Buttons.LEFT)
		{
			_toolbar.leftClick(screenX, screenY, _mapOffset);
			// if(!toolbarButtonPressed(screenX, screenY))// If no toolbar button was pressed, the user has clicked onto the map
			// {
			// // map stuff after Toolbar check, so there won't be a placing under a button
			// if(_activeTool != null) _activeTool.leftClick(screenX, screenY, _mapOffset);
			// }
		}
		else if(mouseButton == Buttons.RIGHT && _activeTool != null)
		{
			_activeTool.rightClick(screenX, screenY, _mapOffset);
			if(_activeTool.isClosed())
			{
				// resetToolbarButtonPosition(null);
				setTrainViewTool(null);
			}
		}
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
		if(_cityView != null) _cityView.mouseReleased(mouseButton);
		if(_trainLineView != null) _trainLineView.mouseReleased(mouseButton);

		if(mouseButton == Buttons.MIDDLE)
		{
			_dragMode = false;
		}
	}

	@Override
	public void mouseScrolled(int amount)
	{
		if(_trainLineView != null) _trainLineView.mouseScrolled(amount);
	}

	@Override
	public void keyDown(int keyCode)
	{
		_trainLineView.keyDown(keyCode);
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		if(arg0.equals(_toolbar))
		{
			if(arg1 instanceof Point && _activeTool != null)
			{
				Point pos = (Point)arg1;
				_activeTool.leftClick(pos.x, pos.y, _mapOffset);
			}
			else
			{
				if(arg1 instanceof StationPlacingTool) _activeTool = (StationPlacingTool)arg1;
				else if(arg1 instanceof TrackPlacingTool) _activeTool = (TrackPlacingTool)arg1;
				else if(arg1 instanceof TrainLineView) _activeTool = _trainLineView;
				else _activeTool = null;

				if(_activeTool instanceof TrainLineView) _trainLineView.setVisibility(true);
				else _trainLineView.setVisibility(false);
			}
		}
	}
}