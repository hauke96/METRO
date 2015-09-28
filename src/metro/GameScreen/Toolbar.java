package metro.GameScreen;

import java.awt.Point;
import java.awt.Rectangle;

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
public class Toolbar extends GameScreen
{
	private Button _buildStation,
		_buildTracks,
		_showTrainList,
		_createNewTrain;
	private Point _mapOffset;

	/**
	 * Creates a new toolbar.
	 * 
	 * @param position The position of the upper left corner.
	 * @param mapOffset The current map offset.
	 */
	public Toolbar(Point position, Point mapOffset)
	{
		_buildStation = new Button(new Rectangle(-10 + position.x, position.y, 50, 40), new Rectangle(0, 28, 50, 40), METRO.__iconSet);
		_buildTracks = new Button(new Rectangle(-10 + position.x, 39 + position.y, 50, 40), new Rectangle(0, 68, 50, 40), METRO.__iconSet);
		_showTrainList = new Button(new Rectangle(-10 + position.x, 100 + position.y, 50, 40), new Rectangle(0, 108, 50, 40), METRO.__iconSet);
		_createNewTrain = new Button(new Rectangle(-10 + position.x, 139 + position.y, 50, 40), new Rectangle(0, 148, 50, 40), METRO.__iconSet);
		_mapOffset = mapOffset;
		registerObervations();
	}

	private void registerObervations()
	{
		_buildStation.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetExclusiveButtonPositions(_buildStation);
				setChanged();
				notifyObservers(new StationPlacingTool(_mapOffset));
			}
		});
		_buildTracks.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetExclusiveButtonPositions(_buildTracks);
				setChanged();
				notifyObservers(new TrackPlacingTool(_mapOffset));
			}
		});
		_showTrainList.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetExclusiveButtonPositions(_showTrainList);
				setChanged();
				notifyObservers(new LineView(_mapOffset));
			}
		});
		_createNewTrain.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetExclusiveButtonPositions(_createNewTrain);
				setChanged();
				notifyObservers(new TrainView(_mapOffset));
			}
		});
	}

	/**
	 * Resets the position of all toolbar buttons to -10 (like "off") except of the given button.
	 *
	 * @param exceptThisButton This button should not be reset.
	 */
	public void resetExclusiveButtonPositions(Button exceptThisButton)
	{
		_buildTracks.setPosition(new Point(-10, _buildTracks.getPosition().y));
		_buildStation.setPosition(new Point(-10, _buildStation.getPosition().y));
			_showTrainList.setPosition(new Point(-10, _showTrainList.getPosition().y));
			_createNewTrain.setPosition(new Point(-10, _createNewTrain.getPosition().y));
		// Build Tracks
		if(exceptThisButton == _buildTracks)
		{
			_buildTracks.setPosition(new Point(0, _buildTracks.getPosition().y));
		}
		// Create new station
		if(exceptThisButton == _buildStation)
		{
			_buildStation.setPosition(new Point(0, _buildStation.getPosition().y));
		}
		// show list of trains
		if(exceptThisButton == _showTrainList)
		{
			_showTrainList.setPosition(new Point(0, _showTrainList.getPosition().y));
		}
		// create new train
		if(exceptThisButton == _createNewTrain)
		{
			_createNewTrain.setPosition(new Point(0, _createNewTrain.getPosition().y));
		}
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		_buildStation.draw();
		_buildTracks.draw();
		_showTrainList.draw();
		_createNewTrain.draw();
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
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
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void reset()
	{
	}
}