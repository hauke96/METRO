package metro.GameUI.MainView;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.GameUI.MainView.LineView.LineView;
import metro.GameUI.MainView.NotificationView.NotificationArea;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.GameUI.MainView.TrainView.TrainView;
import metro.GameUI.Screen.GameScreen;
import metro.Graphics.Draw;

/**
 * The main view is the normal screen the player sees.
 * All sub tools like the station placing tool or the train management will be organized and drawn by this tool.
 * Furthermore it shows all player information and draws the whole map with its trains, stations and tracks.
 * 
 * @author Hauke
 *
 */

public class MainView extends GameScreen implements Observer
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
		_toolbar = new Toolbar();
		_toolbar.addObserver(this);

		_activeTool = null;

		_playingField = PlayingField.getInstance();
		_notificationArea = NotificationArea.getInstance();
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		_playingField.setCityCircleHighlighting(_activeTool == null || !_activeTool.isHovered());
		_playingField.updateGameScreen(sp);

		_notificationArea.updateGameScreen(sp);

		if(_activeTool != null) _activeTool.updateGameScreen(sp);

		_toolbar.updateGameScreen(sp);

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
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		_playingField.mouseClicked(screenX, screenY, mouseButton);
		_notificationArea.mouseClicked(screenX, screenY, mouseButton);

		if(mouseButton == Buttons.LEFT)
		{
			if(_activeTool != null)
			{
				_activeTool.mouseClicked(screenX, screenY, mouseButton);
			}
		}
		else if(mouseButton == Buttons.RIGHT && _activeTool != null)
		{
			_activeTool.mouseClicked(screenX, screenY, mouseButton);
		}
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
		_playingField.mouseReleased(mouseButton);
	}

	@Override
	public void mouseScrolled(int amount)
	{
		if(_activeTool instanceof LineView) ((LineView)_activeTool).mouseScrolled(amount);
		_playingField.mouseScrolled(amount);
	}

	@Override
	public void keyDown(int keyCode)
	{
		if(_activeTool instanceof LineView) ((LineView)_activeTool).keyDown(keyCode);
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		if(arg0.equals(_toolbar))
		{
			if(_activeTool != null) _activeTool.close();

			if(arg1 instanceof StationPlacingTool
				|| arg1 instanceof TrackPlacingTool
				|| arg1 instanceof LineView
				|| arg1 instanceof TrainView)
			{
				setActiveTool((GameScreen)arg1);
			}
			else
			{
				closeActiveTool();
			}
		}
		else
		{
			if(!_activeTool.isActive())
			{
				closeActiveTool();
			}
		}
	}

	/**
	 * Sets the active tool and adds an observer to this.
	 * 
	 * @param newTool The new tool that should be used.
	 */
	private void setActiveTool(GameScreen newTool)
	{
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
		_toolbar.resetExclusiveButtonPositions(null);
		_activeTool.close();
		_activeTool = null;
		_notificationArea.setWidth(METRO.__SCREEN_SIZE.width);
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

	@Override
	public boolean isHovered()
	{
		return true;
	}
}