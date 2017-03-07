package metro.GameUI.MainView;

import java.awt.Color;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import juard.Contract;
import metro.METRO;
import metro.AppContext.Locator;
import metro.Common.Game.GameState;
import metro.Common.Graphics.Draw;
import metro.GameUI.Common.ToolView;
import metro.GameUI.MainView.LineView.LineView;
import metro.GameUI.MainView.NotificationView.NotificationArea;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.GameUI.MainView.Toolbar.ToolbarController;
import metro.GameUI.MainView.TrainView.TrainView;
import metro.TrainManagement.TrainManagementService;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Container.GameScreen.GameScreenContainer;

/**
 * The main view is the normal screen the player sees.
 * All sub tools like the station placing tool or the train management will be organized and drawn by this tool.
 * Furthermore it shows all player information and draws the whole map with its trains, stations and tracks.
 * 
 * @author Hauke
 */
// TODO break this class into MVC-structure
public class MainView extends GameScreenContainer implements InputProcessor
{
	private ToolView _activeTool;
	private ToolbarController _toolbar;
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
		// Be sure the playground is visible after switching the game screen (and bypass the including renderer change):
		_playingField.getBackgroundPanel().registerContainer();

		_toolbar = new ToolbarController(_gameState.getToolViewWidth());

		_toolbar.StationPlacingToolSelected.add(() -> {
			StationPlacingTool stationPlacingTool = new StationPlacingTool(_gameState, _playingField, _trainManagementService);

			_toolbar.setAboveOf(stationPlacingTool.getBackgroundPanel());
			_notificationArea.getBackgroundPanel().setAboveOf(stationPlacingTool.getBackgroundPanel());

			setActiveTool(stationPlacingTool);
		});

		_toolbar.TrackPlacingToolSelected.add(() -> {
			TrackPlacingTool trackPlacingTool = new TrackPlacingTool(_gameState, _playingField);

			_toolbar.setAboveOf(trackPlacingTool.getBackgroundPanel());
			_notificationArea.getBackgroundPanel().setAboveOf(trackPlacingTool.getBackgroundPanel());

			setActiveTool(trackPlacingTool);
		});

		_toolbar.LineViewToolSelected.add(() -> {
			LineView lineView = new LineView(_gameState.getToolViewWidth(), _playingField, _trainManagementService);

			_toolbar.setAboveOf(lineView.getBackgroundPanel());

			setActiveTool(lineView);
		});

		_toolbar.TrainViewToolSelected.add(() -> {
			TrainView trainView = new TrainView(_gameState.getToolViewWidth(), _trainManagementService);

			AbstractContainer trainViewPanel = trainView.getBackgroundPanel();
			_toolbar.setAboveOf(trainViewPanel);

			setActiveTool(trainView);
		});

		_notificationArea = Locator.get(NotificationArea.class);

		AbstractContainer playingFieldBackground = _playingField.getBackgroundPanel();
		playingFieldBackground.setState(false);

		// TODO do it right
		// _toolbar.getBackgroundPanel().setAboveOf(playingFieldBackground);
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
		_playingField.scrolled(amount);

		return true;
	}

	@Override
	public boolean keyDown(int keyCode)
	{
		return false;
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
		_activeTool.CloseEvent.add(() -> {
			_activeTool = null;
			_notificationArea.setWidth(METRO.__SCREEN_SIZE.width);
			_toolbar.selectButton(null);
		});

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