package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import TrainManagement.TrainStation;
import WindowControls.Button;


/**
 * 
 */

/**
 * @author Hauke
 *
 */
public class GameScreen_TrainView implements GameScreen
{
	private boolean _dragMode = false,
		_trainStationConnectMode = false; // when user clicked on a train station, he can connect this to another
	private Point _oldMousePos; // Mouse position from last frame
	private Point2D _offset = new Point2D.Float(0, 0); // offset for moving the map
	private List<TrainStation> _trainStationList = new ArrayList<TrainStation>();
	private TrainStation _selectedTrainStation = null;
	private Button _buildStation,
		_buildTracks;
	
	static public GameScreen _cityGameScreen; // to go into the city game-screen without loosing reference
	static public Point _selectedCross = new Point(-1, -1); // out of screen 
	
	public GameScreen_TrainView()
	{
		_buildStation = new Button(new Rectangle(0, 100, 50, 40), new Rectangle(0, 28, 50, 40), METRO.__iconSet);
		_buildTracks = new Button(new Rectangle(-10, 139, 50, 40), new Rectangle(0, 68, 50, 40), METRO.__iconSet);
	}
	/* (non-Javadoc)
	 * @see GameScreen#update(java.awt.Graphics2D)
	 */
	@Override
	public void update(Graphics2D g)
	{
		if(_dragMode)
		{
			_offset = new Point2D.Float((float)_offset.getX() + (METRO.__mousePosition.x - _oldMousePos.x) * 2f,
				(float)_offset.getY() + (METRO.__mousePosition.y - _oldMousePos.y) * 2f);
		}
		_oldMousePos = METRO.__mousePosition;

		drawBaseNet(g, new Color(220, 220, 220));//Color.lightGray);
		drawBaseDot(g);
		
		drawTrainStations(g);
		
		drawToolbar(g);
		
		printDebugStuff(g);
	}
	/**
	 * Prints all the debug stuff, that is -more or less- important.
	 * @param g Graphics handle to draw on
	 */
	private void printDebugStuff(Graphics2D g)
	{
		g.setColor(Color.blue);
		g.drawString("MapPosition:", METRO.__SCREEN_SIZE.width - 300, 65);
		g.drawString("MousePosition:", METRO.__SCREEN_SIZE.width - 300, 85);
		g.setColor(Color.black);
		g.drawString(_offset + "", METRO.__SCREEN_SIZE.width - 200, 65);
		g.drawString(METRO.__mousePosition + "", METRO.__SCREEN_SIZE.width - 200, 85);
	}
	/**
	 * Draws the basic gray net for kind of orientation.
	 * @param g The graphic handle to draw on
	 * @param color The color of the net
	 * @param offset An user made offset
	 */
	private void drawBaseNet(Graphics2D g, Color color, int offset)
	{
		g.setColor(color);
		for(int y = ((int)_offset.getY() + offset) % METRO.__baseNetSpacing; y < METRO.__SCREEN_SIZE.height; y += METRO.__baseNetSpacing)
		{
			g.drawLine(offset, y, METRO.__SCREEN_SIZE.width, y);
		}
		for(int x = ((int)_offset.getX() + offset) % METRO.__baseNetSpacing; x < METRO.__SCREEN_SIZE.width; x += METRO.__baseNetSpacing)
		{
			g.drawLine(x, offset, x, METRO.__SCREEN_SIZE.height);
		}
		g.setColor(Color.white);
	}
	/**
	 * Draws the basic gray net for kind of orientation.
	 * @param g The graphic handle to draw on
	 * @param color The color of the net
	 */
	private void drawBaseNet(Graphics2D g, Color color)
	{
		drawBaseNet(g, color, 0);
	}
	/**
	 * Calculates the position and draws this fancy jumping dot near the cursor.
	 * @param g The graphic handle to draw on
	 */
	private void drawBaseDot(Graphics2D g)
	{
		Point cursorPos = new Point(Math.abs((int)(METRO.__mousePosition.x - 7 - _offset.getX()) % METRO.__baseNetSpacing), 
				Math.abs((int)(METRO.__mousePosition.y - 7 - _offset.getY()) % METRO.__baseNetSpacing));

		_selectedCross = new Point(((int)(METRO.__mousePosition.x - 5 - _offset.getX()) - 10)/METRO.__baseNetSpacing + 1, 
			((int)(METRO.__mousePosition.y - 5 - _offset.getY()) - 10)/METRO.__baseNetSpacing + 1);
		
		Point offsetMarker = new Point(METRO.__baseNetSpacing - cursorPos.x, METRO.__baseNetSpacing - cursorPos.y);
		if(cursorPos.x <= METRO.__baseNetSpacing / 2) offsetMarker.x = cursorPos.x;
		if(cursorPos.y <= METRO.__baseNetSpacing / 2) offsetMarker.y = cursorPos.y;

		g.setColor(Color.darkGray);
		g.fillRect(METRO.__mousePosition.x + offsetMarker.x - 8, 
				METRO.__mousePosition.y + offsetMarker.y - 8, 
				3, 3);
		g.setColor(Color.white);
	}
	/**
	 * Draws all the train stations with conenctions and labels.
	 * @param g Graphic handle to draw on.
	 */
	private void drawTrainStations(Graphics2D g)
	{
		Point offset = new Point((int)_offset.getX(), (int)_offset.getY());
		//Draw line if _trainStationConnectMode is true
		if(_trainStationConnectMode)
		{
			Point mPos = METRO.__mousePosition;
			g.setColor(Color.black);
			g.drawLine(_selectedTrainStation.getPositionOnScreen(offset).x, _selectedTrainStation.getPositionOnScreen(offset).y, mPos.x, mPos.y);
		}
		//Draw connections:
		for(TrainStation ts : _trainStationList)
		{
			ts.drawConnections(g, offset);
		}
		//Draw stations:
		for(TrainStation ts : _trainStationList)
		{
			ts.draw(g, offset);
		}
	}
	private void drawToolbar(Graphics2D g)
	{
		_buildStation.draw(g);
		_buildTracks.draw(g);
	}
	public void mouseClicked(MouseEvent e)
	{
		Point mPos = e.getPoint(),
			offset = new Point((int)_offset.getX(), (int)_offset.getY());
			
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			_dragMode = true;
		}
		else if(SwingUtilities.isRightMouseButton(e))
		{
			for(TrainStation ts : _trainStationList)
			{
				Point pos = ts.getPositionOnScreen(offset);
				if(mPos.x >= pos.x - 4
					&& mPos.x <= pos.x + 3
					&& mPos.y >= pos.y - 7
					&& mPos.y <= pos.y + 7)
				{
					_trainStationConnectMode = true;
					_selectedTrainStation = ts;
					break;
				}	
			}
		}
		else if(SwingUtilities.isLeftMouseButton(e))
		{
			if(_trainStationConnectMode)
			{
				for(TrainStation ts : _trainStationList)
				{
					Point pos = ts.getPositionOnScreen(offset);
					if(mPos.x >= pos.x - 4
						&& mPos.x <= pos.x + 3
						&& mPos.y >= pos.y - 7
						&& mPos.y <= pos.y + 7)
					{ 
						_selectedTrainStation.addConnection(ts);
						_trainStationConnectMode = false;
						_selectedTrainStation = null;
						break;
					}	
				}
			}
			else if(METRO.__viewPortButton_City.isPressed(e.getPoint().x, e.getPoint().y))
			{
				METRO.__currentGameScreen = _cityGameScreen;
				METRO.__viewPortButton_City.setPosition(new Point(METRO.__SCREEN_SIZE.width / 2 - 200, -5));
				METRO.__viewPortButton_Train.setPosition(new Point(METRO.__SCREEN_SIZE.width / 2, -15));
			}
			// Toolbar-Buttons:
			else if(_buildStation.isPressed(e.getPoint().x, e.getPoint().y))
			{
				_buildTracks.setPosition(new Point(-10, _buildTracks.getPosition().y));
				_buildStation.setPosition(new Point(0, _buildStation.getPosition().y));
				_trainStationConnectMode = false;
			}
			else if(_buildTracks.isPressed(e.getPoint().x, e.getPoint().y))
			{
				_buildTracks.setPosition(new Point(0, _buildTracks.getPosition().y));
				_buildStation.setPosition(new Point(-10, _buildStation.getPosition().y));
				_trainStationConnectMode = true;
			}
			// cross-stuff after buttons, so player wont place station or track then pressing a button ;)
			else if(e.getPoint().x >= _selectedCross.x * METRO.__baseNetSpacing - 6 + _offset.getX() &&
					e.getPoint().x <= _selectedCross.x * METRO.__baseNetSpacing + 6 + _offset.getX() &&
					e.getPoint().y >= _selectedCross.y * METRO.__baseNetSpacing - 6 + _offset.getY() &&
					e.getPoint().y <= _selectedCross.y * METRO.__baseNetSpacing + 6 + _offset.getY())
			{
				boolean positionOccupied = false;
				Point _selectPointOnScreen = new Point(_selectedCross.x * METRO.__baseNetSpacing + (int)_offset.getX(),
						_selectedCross.y * METRO.__baseNetSpacing + (int)_offset.getY());
				
				for(TrainStation ts : _trainStationList)
				{
					positionOccupied |= ts.getPositionOnScreen(offset).equals(_selectPointOnScreen); // true if this cross has already a station
				}
				
				if(!positionOccupied) _trainStationList.add(new TrainStation(_selectedCross, 0)); // no doubles
			}
		}
	}
	public void mouseReleased(MouseEvent e)
	{
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			_dragMode = false;
		}
	}
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			METRO.__close();
		}
	}
}
