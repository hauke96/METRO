package metro.GameScreen.TrainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import metro.METRO;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.TrainStation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StationPlacingTool implements TrainViewTool
{
	TrainView _trainView;

	public StationPlacingTool(TrainView view)
	{
		_trainView = view;
	}

	@Override
	public void draw(SpriteBatch sp, Point2D offset)
	{
		Point position = new Point(METRO.__mousePosition.x - 4,
			METRO.__mousePosition.y - 8); // Position with offset etc.
		Fill.setColor(Color.white);
		Fill.Rect(position.x, position.y, 8, 15);
		Draw.setColor(Color.black);
		Draw.Rect(position.x, position.y, 8, 15);
	}

	@Override
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		if(screenX >= TrainView._selectedCross.x * METRO.__baseNetSpacing - 6 + offset.getX() &&
			screenX <= TrainView._selectedCross.x * METRO.__baseNetSpacing + 6 + offset.getX() &&
			screenY >= TrainView._selectedCross.y * METRO.__baseNetSpacing - 6 + offset.getY() &&
			screenY <= TrainView._selectedCross.y * METRO.__baseNetSpacing + 6 + offset.getY())
		{
			boolean positionOccupied = false;
			Point selectPointOnScreen = new Point(TrainView._selectedCross.x * METRO.__baseNetSpacing + (int)offset.getX(),
				TrainView._selectedCross.y * METRO.__baseNetSpacing + (int)offset.getY());

			Point offsetPoint = new Point((int)offset.getX(), (int)offset.getY());
			for(TrainStation ts : TrainView._trainStationList)
			{
				positionOccupied |= ts.getPositionOnScreen(offsetPoint).equals(selectPointOnScreen); // true if this cross has already a station
			}

			if(!positionOccupied) // no doubles
			{
				TrainView._trainStationList.add(new TrainStation(TrainView._selectedCross, 0));
				METRO.__money -= TrainStation._PRICE;
			}
		}
	}

	@Override
	public void rightClick(int screenX, int screenY, Point2D offset)
	{
		_trainView.settrainViewTool(null);
	}

}
