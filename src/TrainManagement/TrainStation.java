package TrainManagement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Game.GameScreen_TrainView;
import Game.METRO;

public class TrainStation
{
	private int _waitingPassengers;
	private List<TrainStation> _connectedStations = new ArrayList<TrainStation>();
	private Point _position; // Position
	
	public TrainStation(Point position)
	{
		this(position, 0);
	}
	public TrainStation(Point position, int waitingPassengers)
	{
		_waitingPassengers = waitingPassengers;
		_position = position;
	}
	
	public void draw(Graphics2D g, Point offset)
	{
		g.setColor(Color.black);
		g.fillRect(offset.x + _position.x * METRO.__baseNetSpacing - 3, offset.y + _position.y * METRO.__baseNetSpacing - 3, 7, 7);
		//TODO display massenger number
		//TODO draw line to connected stations
	}
}
