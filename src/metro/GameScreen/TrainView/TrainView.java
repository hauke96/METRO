package metro.GameScreen.TrainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.GameScreen.TrainInteractionTool;
import metro.GameScreen.CityView.CityView;
import metro.GameScreen.TrainLineView.TrainLineView;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.RailwayNodeOverseer;
import metro.TrainManagement.TrainStation;
import metro.WindowControls.Button;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * GameScreen with the default view. It Shows the trains, tracks, stations and basic player information.
 * 
 * @author Hauke
 *
 */

public class TrainView extends GameScreen
{
	private boolean _dragMode;
	private Point _oldMousePos; // Mouse position from last frame
	// private TrainStation _selectedTrainStation = null; // important for later stuff when the user can select a train station
	private Button _buildStation,
		_buildTracks,
		_showTrainList,
		_createNewTrain;
	private CityView _cityView;
	private TrainInteractionTool _activeTool;
	private Point2D _mapOffset; // offset for moving the map
	private TrainLineView _trainListView;

	public static List<TrainStation> _trainStationList;
//	static public ArrayList<RailwayNode> _railwayNodeList; // TODO put into RailwayNode
	public static Point _selectedCross; // out of screen;

	public TrainView()
	{
		_selectedCross = new Point(-1, -1);
//		_railwayNodeList = new ArrayList<RailwayNode>();
		_trainStationList = new ArrayList<TrainStation>();

		_buildStation = new Button(new Rectangle(-10, 100, 50, 40), new Rectangle(0, 28, 50, 40), METRO.__iconSet);
		_buildTracks = new Button(new Rectangle(-10, 139, 50, 40), new Rectangle(0, 68, 50, 40), METRO.__iconSet);
		_showTrainList = new Button(new Rectangle(-10, 200, 50, 40), new Rectangle(0, 108, 50, 40), METRO.__iconSet);
		_createNewTrain = new Button(new Rectangle(-10, 239, 50, 40), new Rectangle(0, 148, 50, 40), METRO.__iconSet);

		_mapOffset = new Point2D.Float(0, 0);

		_dragMode = false;
		_activeTool = null;

		_cityView = new CityView(); // create extra instance for general purpose actions
		_trainListView = new TrainLineView();
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

		drawToolbar(sp);
		
		printDebugStuff(sp);
		
		if(_trainListView!= null) _trainListView.updateGameScreen(sp);
	}

	/**
	 * Prints all the debug stuff, that is -more or less- important.
	 * 
	 * @param g Graphics handle to draw on
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
	 * @param g The graphic handle to draw on
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
	 * Calculates the position and draws this fancy jumping dot near the cursor.
	 * 
	 * @param g The graphic handle to draw on
	 */
	private Point drawBaseDot(SpriteBatch sp)
	{
		Point cursorPos = new Point(Math.abs((int)(METRO.__mousePosition.x - 7 - _mapOffset.getX()) % METRO.__baseNetSpacing),
			Math.abs((int)(METRO.__mousePosition.y - 7 - _mapOffset.getY()) % METRO.__baseNetSpacing));

		_selectedCross = new Point((int)Math.round(((int)(METRO.__mousePosition.x - 15 - _mapOffset.getX()) - 10) / (float)METRO.__baseNetSpacing) + 1,// ((int)(METRO.__mousePosition.x - 5 - _mapOffset.getX()) - 10) / METRO.__baseNetSpacing + 1,
			(int)Math.round(((int)(METRO.__mousePosition.y - 15 - _mapOffset.getY()) - 10) / (float)METRO.__baseNetSpacing) + 1);

		Point offsetMarker = new Point(METRO.__baseNetSpacing - cursorPos.x, METRO.__baseNetSpacing - cursorPos.y);
		if(cursorPos.x <= METRO.__baseNetSpacing / 2) offsetMarker.x = cursorPos.x;
		if(cursorPos.y <= METRO.__baseNetSpacing / 2) offsetMarker.y = cursorPos.y;

		Fill.setColor(Color.darkGray);
		Fill.Rect(METRO.__mousePosition.x + offsetMarker.x - 8,
			METRO.__mousePosition.y + offsetMarker.y - 8,
			3, 3);
		return new Point(METRO.__mousePosition.x + offsetMarker.x - 7,
			METRO.__mousePosition.y + offsetMarker.y - 7);
	}

	/**
	 * Draws all the train stations with conenctions and labels.
	 * 
	 * @param g Graphic handle to draw on.
	 */
	private void drawTrainStations(SpriteBatch sp)
	{
		Point offset = new Point((int)_mapOffset.getX(), (int)_mapOffset.getY());

		// Draw connections:
		for(TrainStation ts : _trainStationList)
		{
			ts.drawConnections(sp, offset);
		}
		// Draw stations:
		for(TrainStation ts : _trainStationList)
		{
			ts.draw(sp, offset);
		}
	}

	/**
	 * Draws all the railway lines
	 * 
	 * @param g Graphics handle.
	 */
	private void drawRailwayLines(SpriteBatch sp)
	{
		Point offset = new Point((int)_mapOffset.getX(), (int)_mapOffset.getY());
		
		RailwayNodeOverseer.drawAllNodes(offset, sp);
	}

	/**
	 * Draws the toolbar with its elements.
	 * 
	 * @param g Graphics handle.
	 */
	private void drawToolbar(SpriteBatch sp)
	{
		_buildStation.draw();
		_buildTracks.draw();
		_showTrainList.draw();
		_createNewTrain.draw();
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(_cityView != null) _cityView.mouseClicked(screenX, screenY, mouseButton);
		if(_trainListView!= null) _trainListView.mouseClicked(screenX, screenY, mouseButton);
		
		if(mouseButton == Buttons.MIDDLE) // for drag-mode
		{
			_dragMode = true;
		}
		else if(mouseButton == Buttons.LEFT)
		{
			if(!toolbarButtonPressed(screenX, screenY))// If no toolbar button was pressed, the user has clicked onto the map
			{
				// map stuff after Toolbar check, so there won't be a placing under a button
				if(_activeTool != null) _activeTool.leftClick(screenX, screenY, _mapOffset);
			}
		}
		else if(mouseButton == Buttons.RIGHT)
		{
			resetToolbarButtonPosition(null);
			if(_activeTool != null) _activeTool.rightClick(screenX, screenY, _mapOffset);
		}
	}

	/**
	 * Checks if a button from the toolbar is clicked. If a button was clicked, this method will execute the corresponding action.
	 * 
	 * @param screenX The x-position on screen of the click
	 * @param screenY The y-position on screen of the click
	 * @return true if any button was clicked.
	 */
	private boolean toolbarButtonPressed(int screenX, int screenY)
	{
		boolean buttonPresses = false;
		if(_buildStation.isPressed(screenX, screenY))
		{
			resetToolbarButtonPosition(_buildStation);
			_activeTool = new StationPlacingTool(this);
			buttonPresses = true;
		}
		else if(_buildTracks.isPressed(screenX, screenY))
		{
			resetToolbarButtonPosition(_buildTracks);
			_activeTool = new TrackPlacingTool(this);
			buttonPresses = true;
		}
		else if(_showTrainList.isPressed(screenX, screenY))
		{
			resetToolbarButtonPosition(_showTrainList);
			_activeTool = null;
			_trainListView = new TrainLineView();
			buttonPresses = true;
		}
		else if(_createNewTrain.isPressed(screenX, screenY))
		{
			resetToolbarButtonPosition(_createNewTrain);
			_activeTool = null;
			// TODO: show config window to create new train
			buttonPresses = true;
		}
		return buttonPresses;
	}

	/**
	 * Resets the position of all toolbar buttons to -10 (like "off").
	 * 
	 * @param exceptThisButton This button should not be reset.
	 */
	private void resetToolbarButtonPosition(Button exceptThisButton)
	{
		// Build Tracks
		if(exceptThisButton == _buildTracks)
		{
			_buildTracks.setPosition(new Point(0, _buildTracks.getPosition().y));
		}
		else
		{
			_buildTracks.setPosition(new Point(-10, _buildTracks.getPosition().y));
		}
		// Create new station
		if(exceptThisButton == _buildStation)
		{
			_buildStation.setPosition(new Point(0, _buildStation.getPosition().y));
		}
		else
		{
			_buildStation.setPosition(new Point(-10, _buildStation.getPosition().y));
		}
		// show list of trains
		if(exceptThisButton == _showTrainList)
		{
			_showTrainList.setPosition(new Point(0, _showTrainList.getPosition().y));
		}
		else
		{
			_showTrainList.setPosition(new Point(-10, _showTrainList.getPosition().y));
		}
		// create new train
		if(exceptThisButton == _createNewTrain)
		{
			_createNewTrain.setPosition(new Point(0, _createNewTrain.getPosition().y));
		}
		else
		{
			_createNewTrain.setPosition(new Point(-10, _createNewTrain.getPosition().y));
		}
	}

	/**
	 * Sets a new TrainViewTool.
	 * 
	 * @param tool The new tool.
	 */
	public void settrainViewTool(TrainInteractionTool tool)
	{
		_activeTool = tool;
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
		if(_cityView != null) _cityView.mouseReleased(mouseButton);
		if(_trainListView!= null) _trainListView.mouseReleased(mouseButton);

		if(mouseButton == Buttons.MIDDLE)
		{
			_dragMode = false;
		}
	}

	@Override
	public void keyDown(int keyCode)
	{
		// if(keyCode == Keys.ESCAPE)
		// {
		// METRO.__close();
		// }
	}

	@Override
	public void mouseScrolled(int amount)
	{
		if(_trainListView != null) _trainListView.mouseScrolled(amount);
	}
}