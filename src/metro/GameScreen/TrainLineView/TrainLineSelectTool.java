package metro.GameScreen.TrainLineView;

import java.awt.geom.Point2D;

import metro.GameScreen.TrainInteractionTool;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TrainLineSelectTool implements TrainInteractionTool
{
	private Point2D _mapOffset;

	/**
	 * Creates a new tool to select the train line.
	 * 
	 * @param mapOffset The map offset to measure mouse clicks in the right way.
	 */
	public TrainLineSelectTool(Point2D mapOffset)
	{
		_mapOffset = mapOffset;
	}

	@Override
	public void draw(SpriteBatch sp, Point2D offset)
	{
	}

	@Override
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
	}

	@Override
	public void rightClick(int screenX, int screenY, Point2D offset)
	{
	}
}
