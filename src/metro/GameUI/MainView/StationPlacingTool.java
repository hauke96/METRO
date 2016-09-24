package metro.GameUI.MainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.GameUI.Screen.GameScreen;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.TrainManagementService;
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
	private PlayingField _playingField;

	/**
	 * Creates a new station placing tool.
	 */
	public StationPlacingTool()
	{
		_isActive = true;
		_playingField = PlayingField.getInstance();
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
		if(mouseButton == Buttons.RIGHT) rightClick(screenX, screenY, _playingField.getMapOffset());
		else if(mouseButton == Buttons.LEFT) leftClick(screenX, screenY, _playingField.getMapOffset());
	}

	/**
	 * Places a station on the pointed position (screenX, screenY).
	 * 
	 * @param screenX The y-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param offset The current map offset.
	 */
	private void leftClick(int screenX, int screenY, Point2D offset)
	{
		boolean positionOccupied = false;
		Point selectPointOnScreen = new Point(_playingField.getSelectedNode().x * GameState.getInstance().getBaseNetSpacing() + (int)offset.getX(),
			_playingField.getSelectedNode().y * GameState.getInstance().getBaseNetSpacing() + (int)offset.getY());

		Point offsetPoint = new Point((int)offset.getX(), (int)offset.getY());
		for(TrainStation ts : TrainManagementService.getInstance().getStations())
		{
			positionOccupied |= ts.getPositionOnScreen(offsetPoint).equals(selectPointOnScreen); // true if this cross has already a station
		}

		if(!positionOccupied) // no doubles
		{
			TrainManagementService.getInstance().addStation(new TrainStation(_playingField.getSelectedNode(), 0));
		}
	}

	/**
	 * Deactivates the tool to point out that this tool can be closed.
	 * 
	 * @param screenX The y-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param offset The current map offset.
	 */
	private void rightClick(int screenX, int screenY, Point2D offset)
	{
		_isActive = false;
		setChanged();
		notifyObservers(); // notify about close
	}

	@Override
	public boolean isActive()
	{
		return _isActive;
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
