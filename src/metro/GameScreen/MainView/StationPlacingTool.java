package metro.GameScreen.MainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.GameState;
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

	/**
	 * Creates a new station placing tool.
	 */
	public StationPlacingTool()
	{
		_isActive = true;
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
		if(mouseButton == Buttons.RIGHT) rightClick(screenX, screenY, MainView._mapOffset);
		else if(mouseButton == Buttons.LEFT) leftClick(screenX, screenY, MainView._mapOffset);
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
		Point selectPointOnScreen = new Point(MainView._selectedCross.x * GameState.getInstance().getBaseNetSpacing() + (int)offset.getX(),
			MainView._selectedCross.y * GameState.getInstance().getBaseNetSpacing() + (int)offset.getY());

		Point offsetPoint = new Point((int)offset.getX(), (int)offset.getY());
		for(TrainStation ts : METRO.__gameState.getStations())
		{
			positionOccupied |= ts.getPositionOnScreen(offsetPoint).equals(selectPointOnScreen); // true if this cross has already a station
		}

		if(!positionOccupied) // no doubles
		{
			if(METRO.__gameState.addMoney(-TrainStation.PRICE))
			{
				METRO.__gameState.getStations().add(new TrainStation(MainView._selectedCross, 0)); // TODO change to gameState.addStation(...) and add the money there
			}
			else
			{
				// TODO notification in notification area (s. #40)
			}
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
		setChanged();
		notifyObservers(); //notify about close
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

	@Override
	public boolean isHovered()
	{
		return false;
	}

	@Override
	public void close()
	{
		super.close();
	}
}
