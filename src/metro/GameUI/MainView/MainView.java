package metro.GameUI.MainView;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.AppContext.ServiceLocator;
import metro.Common.Game.GameState;
import metro.Common.Graphics.Draw;
import metro.Common.Technical.Contract;
import metro.GameUI.Common.ToolView;
import metro.GameUI.MainView.LineView.LineView;
import metro.GameUI.MainView.NotificationView.NotificationArea;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.GameUI.MainView.TrainView.TrainView;
import metro.TrainManagement.TrainManagementService;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Container.GameScreen.GameScreenContainer;
import metro.UI.Renderable.Controls.Button;

/**
 * The main view is the normal screen the player sees.
 * All sub tools like the station placing tool or the train management will be organized and drawn by this tool.
 * Furthermore it shows all player information and draws the whole map with its trains, stations and tracks.
 * 
 * @author Hauke
 */
// TODO break this class into MVC-structure
public class MainView extends GameScreenContainer implements Observer, InputProcessor
{
	private ToolView _activeTool;
	private Toolbar _toolbar;
	private NotificationArea _notificationArea;
	private PlayingField _playingField;
	private GameState _gameState;
	private TrainManagementService _trainManagementService;

	/**
	 * Creates a new main view with no active sub tools.
	 * 
	 * @param gameState The current state of the player.
	 * @param trainManagementService The train management service 
	 * @param playingField The field, the player should play on.
	 */
	public MainView(GameState gameState, TrainManagementService trainManagementService, PlayingField playingField)
	{
		_gameState = gameState;
		_trainManagementService = trainManagementService;
		_playingField = playingField;
		
		_activeTool = null;
		
		setInputProcessor(this);
	}

	@Override
	protected void initializeUi()
	{
		_playingField.getBackgroundPanel().registerContainer();
		
		_toolbar = new Toolbar(_gameState.getToolViewWidth());

		_toolbar.getBuildStationButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				Contract.Require(arg instanceof Button);

				StationPlacingTool stationPlacingTool = new StationPlacingTool(_gameState, _playingField, _trainManagementService);

				_toolbar.getBackgroundPanel().setAboveOf(stationPlacingTool.getBackgroundPanel());
				_notificationArea.getBackgroundPanel().setAboveOf(stationPlacingTool.getBackgroundPanel());

				setActiveTool(stationPlacingTool);
				_toolbar.resetExclusiveButtonPositions((Button)arg);
			}
		});
		_toolbar.getBuildTracksButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				Contract.Require(arg instanceof Button);

				TrackPlacingTool trackPlacingTool = new TrackPlacingTool(_gameState, _playingField);

				_toolbar.getBackgroundPanel().setAboveOf(trackPlacingTool.getBackgroundPanel());
				_notificationArea.getBackgroundPanel().setAboveOf(trackPlacingTool.getBackgroundPanel());

				setActiveTool(trackPlacingTool);
				_toolbar.resetExclusiveButtonPositions((Button)arg);
			}
		});
		_toolbar.getShowTrainListButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				Contract.Require(arg instanceof Button);

				LineView lineView = new LineView(_gameState.getToolViewWidth(), _playingField, _trainManagementService);

				AbstractContainer lineViewPanel = lineView.getBackgroundPanel();
				_toolbar.getBackgroundPanel().setAboveOf(lineViewPanel);

				setActiveTool(lineView);
				_toolbar.resetExclusiveButtonPositions((Button)arg);
			}
		});
		_toolbar.getCreateNewTrainButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				Contract.Require(arg instanceof Button);

				TrainView trainView = new TrainView(_gameState.getToolViewWidth(), _trainManagementService);

				AbstractContainer trainViewPanel = trainView.getBackgroundPanel();
				_toolbar.getBackgroundPanel().setAboveOf(trainViewPanel);

				setActiveTool(trainView);
				_toolbar.resetExclusiveButtonPositions((Button)arg);
			}
		});

		_notificationArea = ServiceLocator.get(NotificationArea.class);

		AbstractContainer playingFieldBackground = _playingField.getBackgroundPanel();
		playingFieldBackground.setState(false);
		_toolbar.getBackgroundPanel().setAboveOf(playingFieldBackground);
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		_playingField.setCityCircleHighlighting(_activeTool == null || !_activeTool.isHovered());

		printDebugStuff(sp);
	}

	/**
	 * Prints all the debug stuff, that is -more or less- important.
	 * 
	 * @param sp The SpriteBatch to draw on
	 */
	private void printDebugStuff(SpriteBatch sp)
	{
		Draw.setColor(Color.blue);
		Draw.String("MapPosition:", METRO.__SCREEN_SIZE.width - 680, 10);
		Draw.String("MousePosition:", METRO.__SCREEN_SIZE.width - 680, 30);
		Draw.String("SelectedCross:", METRO.__SCREEN_SIZE.width - 680, 50);
		Draw.setColor(Color.black);
		Draw.String(_playingField.getMapOffset() + "", METRO.__SCREEN_SIZE.width - 580, 10);
		Draw.String(METRO.__mousePosition + "", METRO.__SCREEN_SIZE.width - 580, 30);
		Draw.String(_playingField.getSelectedNode() + "", METRO.__SCREEN_SIZE.width - 580, 50);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		_playingField.touchDown(screenX, screenY, pointer, button);

		if(_activeTool != null)
		{
			return _activeTool.mouseClicked(screenX, screenY, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		_playingField.touchUp(screenX, screenY, pointer, button);

		return true;
	}

	@Override
	public boolean scrolled(int amount)
	{
		if(_activeTool instanceof LineView) ((LineView)_activeTool).mouseScrolled(amount);
		_playingField.scrolled(amount);

		return true;
	}

	@Override
	public boolean keyDown(int keyCode)
	{
		if(_activeTool instanceof LineView)
		{
			// TODO change this into an "onInputChangedListener" or something like that
			((LineView)_activeTool).keyDown(keyCode);
			return true;
		}
		return false;
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		_activeTool = null;
		_notificationArea.setWidth(METRO.__SCREEN_SIZE.width);
		_toolbar.resetExclusiveButtonPositions(null);
	}

	/**
	 * Sets the active tool and adds an observer to this.
	 * 
	 * @param newTool The new tool that should be used.
	 */
	private void setActiveTool(ToolView newTool)
	{
		Contract.RequireNotNull(newTool);

		if(_activeTool != null)
		{
			_activeTool.close();
		}

		// set new tool and add new observer
		_activeTool = newTool;
		_activeTool.addObserver(this);

		if(_activeTool instanceof LineView || _activeTool instanceof TrainView) // tools that have own window and influence the notification area
		{
			_notificationArea.setWidth(METRO.__SCREEN_SIZE.width - _gameState.getToolViewWidth());
		}
		else
		{
			_notificationArea.setWidth(METRO.__SCREEN_SIZE.width);
		}
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}
}