package metro.TrainManagement.Lines;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import metro.TrainManagement.Nodes.RailwayNode;

public class RailwayConnection
{
	private ArrayList<Color> _colorList;
	private RailwayNode[] _coordinates;

	public RailwayConnection(RailwayNode node1, RailwayNode node2)
	{
		_colorList = new ArrayList<Color>();
		_coordinates = new RailwayNode[2];
		_coordinates[0] = node1;
		_coordinates[1] = node2;
	}
	
	private void sortList()
	{
		//TODO: implement sort algorithm
//		for(int i = 0; i < _colorList.size() - 1; i++)
//		{
//			boolean somethingChanged = false;
//			for(int k = 0; k < _colorList.size() - 1; k++)
//			{
//				if(getColorIndex(_colorList.get(k)) < getColorIndex(_colorList.get(k+1)))
//				{
//					//Swap element k and k+1
//				}
//			}
//		}
	}
	
	public void addColor(Color color)
	{
		_colorList.add(color);
	}
	
	public ArrayList<Color> getColors()
	{
		return _colorList;
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean result = false;
		if(obj instanceof RailwayConnection)
		{
			RailwayConnection connection = (RailwayConnection)obj;

			// Check if both coordinated are equal
			result = connection._coordinates[0].equals(_coordinates[0])
				&& connection._coordinates[1].equals(_coordinates[1])
				|| connection._coordinates[0].equals(_coordinates[1])
				&& connection._coordinates[1].equals(_coordinates[0]);
		}

		return result;
	}
	
	private int getColorIndex(Color color)
	{
		return (int)(Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0] * 255);
	}
}
