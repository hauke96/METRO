package metro.GameScreen.MainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.GameState;
import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.GameScreen.MainView.LineView.LineView;
import metro.GameScreen.MainView.TrainView.TrainView;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
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
	private int _moneyDisplayWidth,
		_height,
		_buttonAreaXPosition,
		_buttonYPosition;

	/**
	 * Creates a new toolbar.
	 */
	public Toolbar()
	{
		_moneyDisplayWidth = 250;
		_height = 40;
		_buttonAreaXPosition = METRO.__SCREEN_SIZE.width - GameState.getInstance().getToolViewWidth();
		_buttonYPosition = -9;
		
		//TODO create a panel for controls

		_buildStation = new Button(new Rectangle(_buttonAreaXPosition + 140, _buttonYPosition, 40, 50), new Rectangle(0, 28, 40, 50), METRO.__iconSet);
		_buildTracks = new Button(new Rectangle(_buttonAreaXPosition + 180, _buttonYPosition, 40, 50), new Rectangle(0, 78, 40, 50), METRO.__iconSet);
		_showTrainList = new Button(new Rectangle(_buttonAreaXPosition + 220, _buttonYPosition, 40, 50), new Rectangle(0, 128, 40, 50), METRO.__iconSet);
		_createNewTrain = new Button(new Rectangle(_buttonAreaXPosition + 260, _buttonYPosition, 40, 50), new Rectangle(0, 178, 40, 50), METRO.__iconSet);
		registerObervations();
		
		//TODO add controls to panel
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
				notifyObservers(new StationPlacingTool());
			}
		});
		_buildTracks.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetExclusiveButtonPositions(_buildTracks);
				setChanged();
				notifyObservers(new TrackPlacingTool());
			}
		});
		_showTrainList.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetExclusiveButtonPositions(_showTrainList);
				setChanged();
				notifyObservers(new LineView());
			}
		});
		_createNewTrain.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetExclusiveButtonPositions(_createNewTrain);
				setChanged();
				notifyObservers(new TrainView());
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
		_buildTracks.setPosition(new Point(_buildTracks.getPosition().x, _buttonYPosition));
		_buildStation.setPosition(new Point(_buildStation.getPosition().x, _buttonYPosition));
		_showTrainList.setPosition(new Point(_showTrainList.getPosition().x, _buttonYPosition));
		_createNewTrain.setPosition(new Point(_createNewTrain.getPosition().x, _buttonYPosition));
		
		// Move the selected button a bit down:
		if(exceptThisButton != null)
		{
			exceptThisButton.setPosition(new Point(exceptThisButton.getPosition().x, 0));
		}
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		// draw the background and the red line below it
		Fill.setColor(Color.white);
		Fill.Rect(new Rectangle(0, 0, METRO.__SCREEN_SIZE.width, _height));
		Draw.setColor(METRO.__metroRed);
		Draw.Line(0, _height, METRO.__SCREEN_SIZE.width, _height);

		// separating line for the money display
		Draw.setColor(METRO.__metroRed);
		Draw.Line(_moneyDisplayWidth, 0, _moneyDisplayWidth, _height);

		// draw amount of money like 935.258.550 $
		Draw.setColor(METRO.__metroRed);
		Draw.String("$", 5, 15);

		String str = " = " + String.format("%,d", METRO.__gameState.getMoney());
		str = str.replace(".", ". ");
		Draw.setColor(METRO.__metroBlue);
		Draw.String(str, 13, 15);

		Draw.setColor(METRO.__metroRed);
		Draw.Line(_buttonAreaXPosition, 0, _buttonAreaXPosition, _height);
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public boolean isHovered()
	{
		Point mPos = METRO.__mousePosition;
		return _buildStation.getArea().contains(mPos)
			|| _buildTracks.getArea().contains(mPos)
			|| _showTrainList.getArea().contains(mPos)
			|| _createNewTrain.getArea().contains(mPos);
	}
}
