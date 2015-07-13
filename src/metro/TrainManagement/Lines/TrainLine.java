package metro.TrainManagement.Lines;

import java.awt.Color;
import java.util.ArrayList;

import metro.TrainManagement.Nodes.RailwayNode;

public class TrainLine
{
	private ArrayList<RailwayConnection> _listOfLines;
	private Color _lineColor;
	
	public TrainLine()
	{
		this(null, null);
	}
	public TrainLine(Color lineColor)
	{
		this(null, lineColor);
	}
	public TrainLine(ArrayList<RailwayConnection> nodes, Color lineColor)
	{
		_listOfLines = nodes;
		_lineColor = lineColor;
	}
	
	public ArrayList<Color> getColors(RailwayConnection connection)
	{
		return connection.getColors();
	}
	
	public boolean contains(RailwayConnection connection)
	{
		return false;
	}
}
