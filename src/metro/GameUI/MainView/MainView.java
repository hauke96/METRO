package metro.GameUI.MainView;

import java.awt.Color;
import java.awt.Rectangle;

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
import metro.GameUI.MainView.Toolbar.ToolbarTool;
import metro.GameUI.MainView.TrainView.TrainView;
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
	private ToolView				_activeTool;
	private ToolbarTool				_toolbar;
	private NotificationArea		_notificationArea;
	private PlayingField			_playingField;
	private GameState				_gameState;
	
	/**
	 * Creates a new main view with no active sub tools.
	 * 
	 * @param gameState
	 *            The current state of the player.
	 * @param playingField
	 *            The field, the player should play on.
	 * @param notificationArea
	 *            The notification area, where in-game notifications will be displayed.
	 */
	public MainView(GameState gameState, PlayingField playingField, NotificationArea notificationArea)
	{
		// TODO contracts
		_gameState = gameState;
		_playingField = playingField;
		_notificationArea = notificationArea;
		
		_activeTool = null;
		
		setInputProcessor(this);
	}
	
	@Override
	protected void initializeUi()
	{
		int toolbarHeight = 40;
		int height = METRO.__SCREEN_SIZE.height - toolbarHeight - _notificationArea.getHeight();
		Rectangle playingFieldArea = new Rectangle(0, toolbarHeight, METRO.__SCREEN_SIZE.width, height);
		
		_playingField.setArea(playingFieldArea);
		
		_toolbar = new ToolbarTool(_gameState.getToolViewWidth(), toolbarHeight);
		
		_toolbar.StationPlacingToolSelected.add(() ->
		{
			StationPlacingTool stationPlacingTool = Locator.get(StationPlacingTool.class);
			
			_toolbar.setAboveOf(stationPlacingTool.getBackgroundPanel());
			_notificationArea.getBackgroundPanel().setAboveOf(stationPlacingTool.getBackgroundPanel());
			
			setActiveTool(stationPlacingTool);
		});
		
		_toolbar.TrackPlacingToolSelected.add(() ->
		{
			TrackPlacingTool trackPlacingTool = Locator.get(TrackPlacingTool.class);
			
			_toolbar.setAboveOf(trackPlacingTool.getBackgroundPanel());
			_notificationArea.getBackgroundPanel().setAboveOf(trackPlacingTool.getBackgroundPanel());
			
			setActiveTool(trackPlacingTool);
		});
		
		_toolbar.LineViewToolSelected.add(() ->
		{
			LineView lineView = Locator.get(LineView.class);
			
			_toolbar.setAboveOf(lineView.getBackgroundPanel());
			
			setActiveTool(lineView);
		});
		
		_toolbar.TrainViewToolSelected.add(() ->
		{
			TrainView trainView = Locator.get(TrainView.class);
			
			AbstractContainer trainViewPanel = trainView.getBackgroundPanel();
			_toolbar.setAboveOf(trainViewPanel);
			
			setActiveTool(trainView);
		});
		
		AbstractContainer playingFieldBackground = _playingField.getBackgroundPanel();
		playingFieldBackground.setState(false);
		
		// TODO do it right
		// _toolbar.getBackgroundPanel().setAboveOf(playingFieldBackground);
	}
	
	/**
	 * Prints all the debug stuff, that is -more or less- important.
	 * 
	 * @param sp
	 *            The SpriteBatch to draw on
	 */
	// TODO create own tool to display these information.
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
		
		if (_activeTool != null)
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
	 * @param newTool
	 *            The new tool that should be used.
	 */
	private void setActiveTool(ToolView newTool)
	{
		Contract.RequireNotNull(newTool);
		
		if (_activeTool != null)
		{
			_activeTool.close();
		}
		
		// set new tool and add new observer
		_activeTool = newTool;
		_activeTool.CloseEvent.add(() ->
		{
			_activeTool = null;
			_notificationArea.setWidth(METRO.__SCREEN_SIZE.width);
			_toolbar.selectButton(null);
		});
		
		if (_activeTool instanceof LineView || _activeTool instanceof TrainView) // tools that have own window and influence the notification area
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
