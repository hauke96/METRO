package metro.GameScreen;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Observable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.LineView.LineView;
import metro.GameScreen.MainView.StationPlacingTool;
import metro.GameScreen.MainView.TrackPlacingTool;
import metro.GameScreen.TrainView.TrainView;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;

/**
 * This is the bar on the side with the "build station", "build tracks", "show line view" and "create train" buttons.
 * 
 * @author hauke
 *
 */
public class Toolbar extends Observable implements TrainInteractionTool
{
	private Button _buildStation,
		_buildTracks,
		_showTrainList,
		_createNewTrain;

	/**
	 * Creates a new toolbar.
	 * 
	 * @param position The position of the upper left corner.
	 */
	public Toolbar(Point position)
	{
		_buildStation = new Button(new Rectangle(-10 + position.x, position.y, 50, 40), new Rectangle(0, 28, 50, 40), METRO.__iconSet);
		_buildTracks = new Button(new Rectangle(-10 + position.x, 39 + position.y, 50, 40), new Rectangle(0, 68, 50, 40), METRO.__iconSet);
		_showTrainList = new Button(new Rectangle(-10 + position.x, 100 + position.y, 50, 40), new Rectangle(0, 108, 50, 40), METRO.__iconSet);
		_createNewTrain = new Button(new Rectangle(-10 + position.x, 139 + position.y, 50, 40), new Rectangle(0, 148, 50, 40), METRO.__iconSet);
		registerObervations();
	}

	private void registerObervations()
	{
		_buildStation.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetToolbarButtonPosition(_buildStation);
				setChanged();
				notifyObservers(new StationPlacingTool());
			}
		});
		_buildTracks.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetToolbarButtonPosition(_buildTracks);
				setChanged();
				notifyObservers(new TrackPlacingTool());
			}
		});
		_showTrainList.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetToolbarButtonPosition(_showTrainList);
				setChanged();
				notifyObservers(new LineView(new Point2D.Float(0, 0)));
			}
		});
		_createNewTrain.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetToolbarButtonPosition(_createNewTrain);
				setChanged();
				notifyObservers(new TrainView(new Point2D.Float(0, 0)));
			}
		});
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
