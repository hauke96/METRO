package metro.GameScreen;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Observable;

import metro.METRO;
import metro.GameScreen.TrainLineView.TrainLineView;
import metro.GameScreen.TrainView.StationPlacingTool;
import metro.GameScreen.TrainView.TrackPlacingTool;
import metro.WindowControls.Button;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Toolbar extends Observable implements TrainInteractionTool
{
	private Button _buildStation,
		_buildTracks,
		_showTrainList,
		_createNewTrain;

	public Toolbar(Point position)
	{
		_buildStation = new Button(new Rectangle(-10 + position.x, position.y, 50, 40), new Rectangle(0, 28, 50, 40), METRO.__iconSet);
		_buildTracks = new Button(new Rectangle(-10 + position.x, 39 + position.y, 50, 40), new Rectangle(0, 68, 50, 40), METRO.__iconSet);
		_showTrainList = new Button(new Rectangle(-10 + position.x, 100 + position.y, 50, 40), new Rectangle(0, 108, 50, 40), METRO.__iconSet);
		_createNewTrain = new Button(new Rectangle(-10 + position.x, 139 + position.y, 50, 40), new Rectangle(0, 148, 50, 40), METRO.__iconSet);
	}

	/**
	 * Draws the toolbar with its elements.
	 * 
	 * @param sp The SpriteBatch to draw on
	 */
	public void draw(SpriteBatch sp, Point2D offset)
	{
		_buildStation.draw();
		_buildTracks.draw();
		_showTrainList.draw();
		_createNewTrain.draw();
	}

	/**
	 * Checks if a button from the toolbar is clicked. If a button was clicked, this method will execute the corresponding action.
	 *
	 * @param screenX The x-position on screen of the click
	 * @param screenY The y-position on screen of the click
	 * @return true if any button was clicked.
	 */
	private Object toolbarButtonPressed(int screenX, int screenY)
	{
		 // as default value to give the click-position back to the observer
		Object tool = new Point(screenX, screenY);

		if(_buildStation.isPressed(screenX, screenY))
		{
			resetToolbarButtonPosition(_buildStation);
			tool = new StationPlacingTool();
		}
		else if(_buildTracks.isPressed(screenX, screenY))
		{
			resetToolbarButtonPosition(_buildTracks);
			tool = new TrackPlacingTool();
		}
		else if(_showTrainList.isPressed(screenX, screenY))
		{
			resetToolbarButtonPosition(_showTrainList);
			tool = new TrainLineView(new Point2D.Float(0, 0));
		}
		else if(_createNewTrain.isPressed(screenX, screenY))
		{
			resetToolbarButtonPosition(_createNewTrain);
			tool = null;
			// TODO: show config window to create new train
		}
		return tool;
	}

	/**
	 * Resets the position of all toolbar buttons to -10 (like "off").
	 *
	 * @param exceptThisButton This button should not be reset.
	 */
	private void resetToolbarButtonPosition(Button exceptThisButton)
	{
		// Build Tracks
		if(exceptThisButton == _buildTracks)
		{
			_buildTracks.setPosition(new Point(0, _buildTracks.getPosition().y));
		}
		else
		{
			_buildTracks.setPosition(new Point(-10, _buildTracks.getPosition().y));
		}
		// Create new station
		if(exceptThisButton == _buildStation)
		{
			_buildStation.setPosition(new Point(0, _buildStation.getPosition().y));
		}
		else
		{
			_buildStation.setPosition(new Point(-10, _buildStation.getPosition().y));
		}
		// show list of trains
		if(exceptThisButton == _showTrainList)
		{
			_showTrainList.setPosition(new Point(0, _showTrainList.getPosition().y));
		}
		else
		{
			_showTrainList.setPosition(new Point(-10, _showTrainList.getPosition().y));
		}
		// create new train
		if(exceptThisButton == _createNewTrain)
		{
			_createNewTrain.setPosition(new Point(0, _createNewTrain.getPosition().y));
		}
		else
		{
			_createNewTrain.setPosition(new Point(-10, _createNewTrain.getPosition().y));
		}
	}

	@Override
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		setChanged();
		notifyObservers(toolbarButtonPressed(screenX, screenY));
	}

	@Override
	public void rightClick(int screenX, int screenY, Point2D offset)
	{
	}

	@Override
	public boolean isClosed()
	{
		return false;
	}
}
