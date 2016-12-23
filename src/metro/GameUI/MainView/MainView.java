package metro.GameUI.MainView;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.Common.Graphics.Draw;
import metro.Common.Technical.Contract;
import metro.GameUI.MainView.LineView.LineView;
import metro.GameUI.MainView.NotificationView.NotificationArea;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.GameUI.MainView.TrainView.TrainView;
import metro.GameUI.Screen.GameScreen;
import metro.UI.Renderable.ActionObserver;
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
public class MainView extends GameScreenContainer implements Observer, InputProcessor
{
	private GameScreen _activeTool;
	private Toolbar _toolbar;
	private NotificationArea _notificationArea;
	private PlayingField _playingField;

	/**
	 * Creates a new main view with no active sub tools.
	 */
	public MainView()
	{
		_activeTool = null;

		// TODO add here new Controller-class
		setInputProcessor(this);
	}

	@Override
	protected void initializeUi()
	{
		_toolbar = new Toolbar();

		_toolbar.getBuildStationButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				setActiveTool(new StationPlacingTool());
			}
		});
		_toolbar.getBuildTracksButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				setActiveTool(new TrackPlacingTool());
			}
		});
		_toolbar.getShowTrainListButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				LineView lineView = new LineView();
				AbstractContainer lineViewPanel = lineView.getBackgroundPanel();
				_toolbar.getBackgroundPanel().setAboveOf(lineViewPanel);

				setActiveTool(lineView);
			}
		});
		_toolbar.getCreateNewTrainButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				TrainView trainView = new TrainView();
				AbstractContainer trainViewPanel = trainView.getBackgroundPanel();
				_toolbar.getBackgroundPanel().setAboveOf(trainViewPanel);

				setActiveTool(trainView);
			}
		});
		;

		_playingField = PlayingField.getInstance();
		_notificationArea = NotificationArea.getInstance();

		AbstractContainer playingFieldBackground = _playingField.getBackgroundPanel();
		_toolbar.getBackgroundPanel().setAboveOf(playingFieldBackground);
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		_playingField.setCityCircleHighlighting(_activeTool == null || !_activeTool.isHovered());
		// _playingField.updateGameScreen();
		
		if(_activeTool != null) _activeTool.updateGameScreen(sp);

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

		if(_notificationArea.isInArea(screenX, screenY))
		{
			_notificationArea.mouseClicked(screenX, screenY, button);
		}

		if(_activeTool != null)
		{
			if(button == Buttons.LEFT)
			{
				_activeTool.mouseClicked(screenX, screenY, button);
			}
			else if(button == Buttons.RIGHT)
			{
				_activeTool.mouseClicked(screenX, screenY, button);
			}
			return true;
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
			((LineView)_activeTool).keyDown(keyCode);
			return true;
		}
		return false;
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		if(!_activeTool.isActive())
		{
			closeActiveTool();
		}
	}

	/**
	 * Sets the active tool and adds an observer to this.
	 * 
	 * @param newTool The new tool that should be used.
	 */
	private void setActiveTool(GameScreen newTool)
	{
		Contract.RequireNotNull(newTool);

		if(_activeTool != null)
		{
			closeActiveTool();
		}

		// set new tool and add new observer
		_activeTool = newTool;
		_activeTool.addObserver(this);

		if(_activeTool instanceof LineView || _activeTool instanceof TrainView) // tools that have own window and influence the notification area
		{
			_notificationArea.setWidth(METRO.__SCREEN_SIZE.width - GameState.getInstance().getToolViewWidth());
		}
		else
		{
			_notificationArea.setWidth(METRO.__SCREEN_SIZE.width);
		}
	}

	/**
	 * Closes the active tool and resets everything that depends on this.
	 */
	private void closeActiveTool()
	{
		Contract.RequireNotNull(_activeTool);

		_activeTool.close();
		_activeTool = null;
		_notificationArea.setWidth(METRO.__SCREEN_SIZE.width);
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