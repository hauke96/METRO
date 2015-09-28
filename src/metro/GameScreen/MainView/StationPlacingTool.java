package metro.GameScreen.MainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.Trains.TrainStation;

/**
 * A station placing tool makes it possible to create train stations.
 * 
 * @author hauke
 *
 */
public class StationPlacingTool extends GameScreen
{
	private boolean _isActive;
	private Point _mapOffset;

	/**
	 * Creates a new station placing tool.
	 * 
	 * @param mapOffset The current map offset.
	 */
	public StationPlacingTool(Point mapOffset)
	{
		_isActive = true;
		_mapOffset = mapOffset;
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		Point position = new Point(METRO.__mousePosition.x - 4,
			METRO.__mousePosition.y - 8); // Position with offset etc.
		Fill.setColor(Color.white);
		Fill.Rect(position.x, position.y, 8, 15);
		Draw.setColor(Color.black);
		Draw.Rect(position.x, position.y, 8, 15);
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(mouseButton == Buttons.RIGHT) rightClick(screenX, screenY, _mapOffset);
		else if(mouseButton == Buttons.LEFT) leftClick(screenX, screenY, _mapOffset);
	}
	
	/**
	 * Places a station on the pointed position (screenX, screenY).
	 * 
	 * @param screenX The y-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param offset The current map offset.
	 */
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		boolean positionOccupied = false;
		Point selectPointOnScreen = new Point(MainView._selectedCross.x * METRO.__baseNetSpacing + (int)offset.getX(),
			MainView._selectedCross.y * METRO.__baseNetSpacing + (int)offset.getY());

		Point offsetPoint = new Point((int)offset.getX(), (int)offset.getY());
		for(TrainStation ts : MainView._trainStationList)
		{
			positionOccupied |= ts.getPositionOnScreen(offsetPoint).equals(selectPointOnScreen); // true if this cross has already a station
		}

		if(!positionOccupied) // no doubles
		{
			MainView._trainStationList.add(new TrainStation(MainView._selectedCross, 0));
			METRO.__money -= TrainStation._PRICE;
		}
	}

	/**
	 * Deactivates the tool to point out that this tool can be closed.
	 * 
	 * @param screenX The y-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param offset The current map offset.
	 */
	public void rightClick(int screenX, int screenY, Point2D offset)
	{
		_isActive = false;
	}

	@Override
	public boolean isActive()
	{
		return _isActive;
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
	}

	@Override
	public void keyDown(int keyCode)
	{
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public void reset()
	{
	}
}