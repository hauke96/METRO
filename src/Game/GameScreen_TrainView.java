package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import TrainManagement.TrainStation;


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
	
	static public GameScreen _cityGameScreen; // to go into the city game-screen without loosing reference
	static public Point _selectedCross = new Point(-1, -1); // out of screen 
	
	public GameScreen_TrainView()
	{
	}
	/* (non-Javadoc)
	 * @see GameScreen#update(java.awt.Graphics2D)
	 */
	@Override
	public void update(Graphics2D g)
	{
		if(_dragMode)
		{
			_offset = new Point2D.Float((float)_offset.getX() + (MouseInfo.getPointerInfo().getLocation().x - _oldMousePos.x) * 2f,
				(float)_offset.getY() + (MouseInfo.getPointerInfo().getLocation().y - _oldMousePos.y) * 2f);
		}
		_oldMousePos = MouseInfo.getPointerInfo().getLocation();

		drawBaseNet(g, new Color(220, 220, 220));//Color.lightGray);
		drawBaseDot(g);
		
		drawTrainStations(g);
		
		printDebugStuff(g);
	}
	/**
	 * Prints all the debug stuff, that is -more or less- important.
	 * @param g Graphics handle to draw on
	 */
	private void printDebugStuff(Graphics2D g)
	{
		g.setColor(Color.blue);
		g.drawString("MapPosition:", 5, 65);
		g.drawString("MousePosition:", 5, 85);
		g.setColor(Color.black);
		g.drawString(_offset + "", 100, 65);
		g.drawString(MouseInfo.getPointerInfo().getLocation() + "", 100, 85);
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
		Point cursorPos = new Point(Math.abs((int)(MouseInfo.getPointerInfo().getLocation().x - 7 - _offset.getX()) % METRO.__baseNetSpacing), 
				Math.abs((int)(MouseInfo.getPointerInfo().getLocation().y - 7 - _offset.getY()) % METRO.__baseNetSpacing));

		_selectedCross = new Point(((int)(MouseInfo.getPointerInfo().getLocation().x - 5 - _offset.getX()) - 10)/METRO.__baseNetSpacing + 1, 
			((int)(MouseInfo.getPointerInfo().getLocation().y - 5 - _offset.getY()) - 10)/METRO.__baseNetSpacing + 1);
		
		Point offsetMarker = new Point(METRO.__baseNetSpacing - cursorPos.x, METRO.__baseNetSpacing - cursorPos.y);
		if(cursorPos.x <= METRO.__baseNetSpacing / 2) offsetMarker.x = cursorPos.x;
		if(cursorPos.y <= METRO.__baseNetSpacing / 2) offsetMarker.y = cursorPos.y;

		g.setColor(Color.darkGray);
		g.fillRect(MouseInfo.getPointerInfo().getLocation().x + offsetMarker.x - 8, 
				MouseInfo.getPointerInfo().getLocation().y + offsetMarker.y - 8, 
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
			Point mPos = MouseInfo.getPointerInfo().getLocation();
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
	public void mouseClicked(MouseEvent e)
	{
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			_dragMode = true;
		}
		else if(SwingUtilities.isRightMouseButton(e))
		{
			Point mPos = e.getPoint(),
				offset = new Point((int)_offset.getX(), (int)_offset.getY());
			
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
				Point mPos = e.getPoint(),
					offset = new Point((int)_offset.getX(), (int)_offset.getY());
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
			else if(e.getPoint().x >= _selectedCross.x * METRO.__baseNetSpacing - 6 + _offset.getX() &&
					e.getPoint().x <= _selectedCross.x * METRO.__baseNetSpacing + 6 + _offset.getX() &&
					e.getPoint().y >= _selectedCross.y * METRO.__baseNetSpacing - 6 + _offset.getY() &&
					e.getPoint().y <= _selectedCross.y * METRO.__baseNetSpacing + 6 + _offset.getY())
			{
				_trainStationList.add(new TrainStation(_selectedCross, 0));
				// only for testing: add last station to connection
//				_trainStationList.get(_trainStationList.size() - 1).addConnection(_trainStationList.get(_trainStationList.size() - 2));
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
