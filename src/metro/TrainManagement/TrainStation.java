package metro.TrainManagement;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import metro.Game.METRO;
import metro.graphics.Draw;
import metro.graphics.Fill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class TrainStation
{
	private int _waitingPassengers;
	private List<TrainStation> _connectedStations = new ArrayList<TrainStation>();
	private Point _position; // Position
	
	public static final int _PRICE = 1000;
	
	public TrainStation(Point position)
	{
		this(position, 0);
	}
	public TrainStation(Point position, int waitingPassengers)
	{
		_waitingPassengers = waitingPassengers;
		_position = position;
	}
	/**
	 * Draws all the connections to connected stations.
	 * @param g The Graphic handle to draw on.
	 * @param offset The current offset of the game screen.
	 */
	public void drawConnections(SpriteBatch sp, Point offset)
	{
		Point position = new Point(offset.x + _position.x * METRO.__baseNetSpacing - 4, 
				offset.y + _position.y * METRO.__baseNetSpacing - 7); // Position with offset etc.

		Draw.setColor(Color.black);
		for(TrainStation ts : _connectedStations)
		{
			Draw.Line(position.x + 4, position.y + 7, ts.getPositionOnScreen(offset).x, ts.getPositionOnScreen(offset).y);
		}
	}
	/**
	 * Draws the station with passenger amount.
	 * @param g Graphic handle to draw on.
	 * @param offset The offset of the current game screen.
	 */
	public void draw(SpriteBatch sp, Point offset)
	{
		Point position = new Point(offset.x + _position.x * METRO.__baseNetSpacing - 4, 
				offset.y + _position.y * METRO.__baseNetSpacing - 7); // Position with offset etc.
		Fill.setColor(Color.white);
		Fill.Rect(position.x, position.y, 8, 15);
		Draw.setColor(Color.black);
		Draw.Rect(position.x, position.y, 8, 15);
		
		Draw.String("" + _waitingPassengers, 
				offset.x + _position.x * METRO.__baseNetSpacing - Draw.getStringSize("" + _waitingPassengers).width / 2 - 1, 
				offset.y + _position.y * METRO.__baseNetSpacing - 25);
	}
	/**
	 * Adds a connection to this train station.
	 * @param station The train station to add to the connection list.
	 */
	public void addConnection(TrainStation station)
	{
		if(!_connectedStations.contains(station))
		{
			_connectedStations.add(station); // A -> B
			station.addConnection(this);     // B -> A
		}
	}
	/**
	 * Returns the position with offset.
	 * @param offset The offset of the current game screen.
	 * @return The position with offset.
	 */
	public Point getPositionOnScreen(Point offset)
	{
		return new Point(offset.x + _position.x * METRO.__baseNetSpacing, 
				offset.y + _position.y * METRO.__baseNetSpacing); // Position with offset etc.
	}
}
