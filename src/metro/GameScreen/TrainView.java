package metro.GameScreen;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import metro.METRO;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.RailwayNode;
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
	private boolean _dragMode = false;
	private Point _oldMousePos; // Mouse position from last frame
	private Point2D _mapOffset = new Point2D.Float(0, 0); // offset for moving the map
	private List<TrainStation> _trainStationList = new ArrayList<TrainStation>();
	// private TrainStation _selectedTrainStation = null; // important for later stuff when the user can select a train station
	private Button _buildStation,
		_buildTracks,
		_showTrainList,
		_createNewTrain;
	private int _editMode = 0; // 0 = nothing; 1 = place stations; 2 = place lines TODO: outsource to extra classes?
	private RailwayNode _currentRailwayNode; // click -> set railwaynode -> click -> connect/create

	private CityView _cityView = new CityView();

	static public ArrayList<RailwayNode> _railwayNodeList = new ArrayList<RailwayNode>();
	static public GameScreen _cityGameScreen; // to go into the city game-screen without loosing reference
	static public Point _selectedCross = new Point(-1, -1); // out of screen

	public TrainView()
	{
		_buildStation = new Button(new Rectangle(-10, 100, 50, 40), new Rectangle(0, 28, 50, 40), METRO.__iconSet);
		_buildTracks = new Button(new Rectangle(-10, 139, 50, 40), new Rectangle(0, 68, 50, 40), METRO.__iconSet);
		_showTrainList = new Button(new Rectangle(-10, 200, 50, 40), new Rectangle(0, 108, 50, 40), METRO.__iconSet);
		_createNewTrain = new Button(new Rectangle(-10, 239, 50, 40), new Rectangle(0, 148, 50, 40), METRO.__iconSet);
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		_cityView.update(sp);
		if(_dragMode)
		{
			_mapOffset = new Point2D.Float((float)_mapOffset.getX() + (METRO.__mousePosition.x - _oldMousePos.x),
				(float)_mapOffset.getY() + (METRO.__mousePosition.y - _oldMousePos.y));
		}
		_oldMousePos = METRO.__mousePosition;

		drawBaseNet(sp, new Color(220, 220, 220));// Color.lightGray);
		drawBaseDot(sp);

		drawEditMode(sp);
		drawRailwayLines(sp);
		drawTrainStations(sp);

		drawToolbar(sp);

		printDebugStuff(sp);
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
		Draw.setColor(Color.black);
		Draw.String(_mapOffset + "", METRO.__SCREEN_SIZE.width - 200, 65);
		Draw.String(METRO.__mousePosition + "", METRO.__SCREEN_SIZE.width - 200, 85);

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
	 * Draws the basic gray net for kind of orientation.
	 * 
	 * @param g The graphic handle to draw on
	 * @param color The color of the net
	 */
	private void drawBaseNet(SpriteBatch sp, Color color)
	{
		drawBaseNet(sp, color, 0);
	}

	/**
	 * Calculates the position and draws this fancy jumping dot near the cursor.
	 * 
	 * @param g The graphic handle to draw on
	 */
	private void drawBaseDot(SpriteBatch sp)
	{
		Point cursorPos = new Point(Math.abs((int)(METRO.__mousePosition.x - 7 - _mapOffset.getX()) % METRO.__baseNetSpacing),
			Math.abs((int)(METRO.__mousePosition.y - 7 - _mapOffset.getY()) % METRO.__baseNetSpacing));

		_selectedCross = new Point(((int)(METRO.__mousePosition.x - 5 - _mapOffset.getX()) - 10) / METRO.__baseNetSpacing + 1,
			((int)(METRO.__mousePosition.y - 5 - _mapOffset.getY()) - 10) / METRO.__baseNetSpacing + 1);

		Point offsetMarker = new Point(METRO.__baseNetSpacing - cursorPos.x, METRO.__baseNetSpacing - cursorPos.y);
		if(cursorPos.x <= METRO.__baseNetSpacing / 2) offsetMarker.x = cursorPos.x;
		if(cursorPos.y <= METRO.__baseNetSpacing / 2) offsetMarker.y = cursorPos.y;

		Fill.setColor(Color.darkGray);
		Fill.Rect(METRO.__mousePosition.x + offsetMarker.x - 8,
			METRO.__mousePosition.y + offsetMarker.y - 8,
			3, 3);
	}

	/**
	 * Draws all the stuff from edit mode things (previews, etc.)
	 * 
	 * @param g Graphics handle to draw on.
	 */
	private void drawEditMode(SpriteBatch sp)
	{
		Point offset = new Point((int)_mapOffset.getX(), (int)_mapOffset.getY());

		switch(_editMode)
		{
			case 1:
				Point position = new Point(METRO.__mousePosition.x - 4,
					METRO.__mousePosition.y - 8); // Position with offset etc.
				Fill.setColor(Color.white);
				Fill.Rect(position.x, position.y, 8, 15);
				Draw.setColor(Color.black);
				Draw.Rect(position.x, position.y, 8, 15);
				break;
			case 2:
				if(_currentRailwayNode != null) // if not null, calc and draw preview of new tracks
				{
					Draw.setColor(Color.black);

					int diagonalOffset = 0, B = _selectedCross.x - _currentRailwayNode.getPosition().x, H = _selectedCross.y - _currentRailwayNode.getPosition().y, preFactor = 1; // counts the amount of fields covered

					if(Math.abs(H) > Math.abs(B)) // vertical tracks
					{
						diagonalOffset = (int)((Math.abs(H) - Math.abs(B)) / 2f);
						if(H < 0) preFactor = -1;

						Draw.Line(offset.x + _currentRailwayNode.getPosition().x * METRO.__baseNetSpacing,
							offset.y + _currentRailwayNode.getPosition().y * METRO.__baseNetSpacing,
							offset.x + _currentRailwayNode.getPosition().x * METRO.__baseNetSpacing,
							offset.y + (_currentRailwayNode.getPosition().y + preFactor * diagonalOffset) * METRO.__baseNetSpacing);

						Draw.Line(offset.x + _currentRailwayNode.getPosition().x * METRO.__baseNetSpacing,
							offset.y + (_currentRailwayNode.getPosition().y + preFactor * diagonalOffset) * METRO.__baseNetSpacing,
							offset.x + (_currentRailwayNode.getPosition().x + B) * METRO.__baseNetSpacing,
							offset.y + (_currentRailwayNode.getPosition().y + preFactor * (diagonalOffset + Math.abs(B))) * METRO.__baseNetSpacing);

						Draw.Line(offset.x + (_currentRailwayNode.getPosition().x + B) * METRO.__baseNetSpacing,
							offset.y + (_currentRailwayNode.getPosition().y + preFactor * (diagonalOffset + Math.abs(B))) * METRO.__baseNetSpacing,
							offset.x + (_currentRailwayNode.getPosition().x + B) * METRO.__baseNetSpacing,
							offset.y + (_currentRailwayNode.getPosition().y + preFactor * (2 * diagonalOffset + preFactor * ((H - B) % 2) + Math.abs(B))) * METRO.__baseNetSpacing);
					}
					else if(Math.abs(B) > Math.abs(H)) // horizontal tracks
					{
						diagonalOffset = (int)((Math.abs(B) - Math.abs(H)) / 2f);
						if(B < 0) preFactor = -1;

						Draw.Line(offset.x + (_currentRailwayNode.getPosition().x + preFactor * diagonalOffset) * METRO.__baseNetSpacing,
							offset.y + _currentRailwayNode.getPosition().y * METRO.__baseNetSpacing,
							offset.x + _currentRailwayNode.getPosition().x * METRO.__baseNetSpacing,
							offset.y + _currentRailwayNode.getPosition().y * METRO.__baseNetSpacing);

						Draw.Line(offset.x + (_currentRailwayNode.getPosition().x + preFactor * diagonalOffset) * METRO.__baseNetSpacing,
							offset.y + _currentRailwayNode.getPosition().y * METRO.__baseNetSpacing,
							offset.x + (_currentRailwayNode.getPosition().x + preFactor * (diagonalOffset + Math.abs(H))) * METRO.__baseNetSpacing,
							offset.y + (_currentRailwayNode.getPosition().y + H) * METRO.__baseNetSpacing);

						Draw.Line(offset.x + (_currentRailwayNode.getPosition().x + preFactor * (diagonalOffset + Math.abs(H))) * METRO.__baseNetSpacing,
							offset.y + (_currentRailwayNode.getPosition().y + H) * METRO.__baseNetSpacing,
							offset.x + (_currentRailwayNode.getPosition().x + preFactor * (2 * diagonalOffset + preFactor * ((B - H) % 2) + Math.abs(H))) * METRO.__baseNetSpacing,
							offset.y + (_currentRailwayNode.getPosition().y + H) * METRO.__baseNetSpacing);
					}
					else if(Math.abs(B) == Math.abs(H)) // diagonal tracks
					{
						Draw.Line(offset.x + _currentRailwayNode.getPosition().x * METRO.__baseNetSpacing,
							offset.y + _currentRailwayNode.getPosition().y * METRO.__baseNetSpacing,
							offset.x + _selectedCross.x * METRO.__baseNetSpacing,
							offset.y + _selectedCross.y * METRO.__baseNetSpacing);
					}
				}
				break;
		}
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

		for(RailwayNode node : _railwayNodeList)
		{
			node.draw(sp, offset);
		}
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

	/**
	 * Created nodes after second mouse click, removes doubles and manages calculation of automatic routiung.
	 * 
	 * @param screenX The x-position on screen of the click
	 * @param screenY The y-position on screen of the click
	 * @param mouseButton The mouse button that has been pressed.
	 */
	private void placeTracks(int screenX, int screenY, int mouseButton)
	{
		if(screenX >= _selectedCross.x * METRO.__baseNetSpacing - 6 + _mapOffset.getX() &&
			screenX <= _selectedCross.x * METRO.__baseNetSpacing + 6 + _mapOffset.getX() &&
			screenY >= _selectedCross.y * METRO.__baseNetSpacing - 6 + _mapOffset.getY() &&
			screenY <= _selectedCross.y * METRO.__baseNetSpacing + 6 + _mapOffset.getY())
		{
			if(_currentRailwayNode == null) // first click
			{
				_currentRailwayNode = RailwayNode.getNodeByID(RailwayNode.calcID(_selectedCross.x, _selectedCross.y)); // get Node at this position

				if(_currentRailwayNode == null) // if there's no node, create new one and set it as current node
				{
					RailwayNode node = new RailwayNode(_selectedCross, null);
					_currentRailwayNode = node;
					_railwayNodeList.add(node);
				}
			}
			else
			// second click
			{
				int diagonalOffset = 0, B = _selectedCross.x - _currentRailwayNode.getPosition().x, // horizontal distance
				H = _selectedCross.y - _currentRailwayNode.getPosition().y, // vertical distance
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
	}

	/**
	 * Creates one Track for the parameter.
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
			int ID = RailwayNode.calcID(prevNode.getPosition().x + offsetB, prevNode.getPosition().y + offsetH);
			RailwayNode node = null;
			if(!RailwayNode._IDList.contains(ID)) // if there's a NO node at this position
			{
				node = new RailwayNode(new Point(
					prevNode.getPosition().x + offsetB,
					prevNode.getPosition().y + offsetH),
					prevNode);
				METRO.__money -= RailwayNode._PRICE;
			}
			else
			// if there's a node, set it as node instead of new one
			{
				node = RailwayNode.getNodeByID(ID);
			}

			prevNode.add(node); // connect to previous node
			if(!_railwayNodeList.contains(node)) _railwayNodeList.add(node); // ad node to list
			prevNode = node; // set previous node to current one to go on
		}
		return prevNode;
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		_cityView.mouseClicked(screenX, screenY, mouseButton);

		if(mouseButton == Buttons.MIDDLE) // for drag-mode
		{
			_dragMode = true;
		}
		else if(mouseButton == Buttons.LEFT)
		{

			if(!toolbarButtonPressed(screenX, screenY))// If no toolbar button was pressed, the user has clicked onto the map
			{
				// map stuff after Toolbar check, so there won't be a placing under a button
				Point offset = new Point((int)_mapOffset.getX(), (int)_mapOffset.getY());
				manageEditModeLeftClick(screenX, screenY, offset);
			}
		}
		else if(mouseButton == Buttons.MIDDLE)
		{
			resetToolbarButtonPosition(null);
			manageEditModeRightClick();
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
			_editMode = 1; // place stations
			buttonPresses = true;
		}
		else if(_buildTracks.isPressed(screenX, screenY))
		{
			resetToolbarButtonPosition(_buildTracks);
			_editMode = 2; // place lines
			buttonPresses = true;
		}
		else if(_showTrainList.isPressed(screenX, screenY))
		{
			resetToolbarButtonPosition(_showTrainList);
			_editMode = 0;
			// TODO: create list view with all trains
			buttonPresses = true;
		}
		else if(_createNewTrain.isPressed(screenX, screenY))
		{
			resetToolbarButtonPosition(_createNewTrain);
			_editMode = 0;
			// TODO: show config window to create new train
			buttonPresses = true;
		}
		return buttonPresses;
	}

	/**
	 * Manages the actions after mouse is pressed (LEFT) in certain edit mode.
	 * 
	 * @param screenX The x-position on screen of the click
	 * @param screenY The y-position on screen of the click
	 * @param offset The map offset as Point (not Point2D)
	 */
	private void manageEditModeLeftClick(int screenX, int screenY, Point offset)
	{
		switch(_editMode)
		{
			case 1: // station place mode
				if(screenX >= _selectedCross.x * METRO.__baseNetSpacing - 6 + _mapOffset.getX() &&
					screenX <= _selectedCross.x * METRO.__baseNetSpacing + 6 + _mapOffset.getX() &&
					screenY >= _selectedCross.y * METRO.__baseNetSpacing - 6 + _mapOffset.getY() &&
					screenY <= _selectedCross.y * METRO.__baseNetSpacing + 6 + _mapOffset.getY())
				{
					boolean positionOccupied = false;
					Point selectPointOnScreen = new Point(_selectedCross.x * METRO.__baseNetSpacing + (int)_mapOffset.getX(),
						_selectedCross.y * METRO.__baseNetSpacing + (int)_mapOffset.getY());

					for(TrainStation ts : _trainStationList)
					{
						positionOccupied |= ts.getPositionOnScreen(offset).equals(selectPointOnScreen); // true if this cross has already a station
					}

					if(!positionOccupied) // no doubles
					{
						_trainStationList.add(new TrainStation(_selectedCross, 0));
						METRO.__money -= TrainStation._PRICE;
					}
				}
				break;
			case 2: // track place mode
				placeTracks(screenX, screenY, Buttons.LEFT);
				break;
		}
	}

	/**
	 * Resets every necessary part after a RIGHT click in certain edit mode.
	 */
	private void manageEditModeRightClick()
	{
		switch(_editMode)
		{
			case 1: // station place mode
				_editMode = 0;
				break;
			case 2: // track place mode
				if(_currentRailwayNode != null)
				{
					_railwayNodeList.remove(_railwayNodeList.size() - 1);
					_currentRailwayNode = null;
				}
				else
				{
					_editMode = 0;
				}
				break;
		}
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

	@Override
	public void mouseReleased(int mouseButton)
	{
		_cityView.mouseReleased(mouseButton);

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
	}
}