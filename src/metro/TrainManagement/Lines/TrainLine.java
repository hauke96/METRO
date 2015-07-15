package metro.TrainManagement.Lines;

import java.awt.Color;
import java.util.ArrayList;

public class TrainLine
{
	private ArrayList<RailwayConnection> _listOfConnections;
	private Color _lineColor;
	private String _name;

	public TrainLine(String name, Color lineColor)
	{
		this(null, name, lineColor);
	}

	public TrainLine(ArrayList<RailwayConnection> nodes, String name, Color lineColor)
	{
		if(nodes != null) _listOfConnections = nodes;
		else _listOfConnections = new ArrayList<RailwayConnection>();
		_name = name;
		_lineColor = lineColor;
	}

	public Color getColor()
	{
		return _lineColor;
	}

	public boolean contains(RailwayConnection connection)
	{
		return _listOfConnections.contains(connection);
	}

	public String getName()
	{
		return _name;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof TrainLine)
		{
			TrainLine line = (TrainLine)o;
			return line._name.equals(_name) && line._lineColor.equals(_lineColor); // equal when color and name are equal
		}
		return false;
	}
}
