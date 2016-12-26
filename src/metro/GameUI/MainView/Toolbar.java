package metro.GameUI.MainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
import metro.Common.Technical.Contract;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Container.Panel;
import metro.UI.Renderable.Controls.Button;
import metro.UI.Renderable.Controls.Canvas;

/**
 * This is the bar on the side with the "build station", "build tracks", "show line view" and "create train" buttons.
 * 
 * @author hauke
 *
 */
// TODO separate UI and logic code
public class Toolbar
{
	private Button _buildStation,
		_buildTracks,
		_showTrainList,
		_createNewTrain;
	private int _moneyDisplayWidth,
		_height,
		_buttonAreaXPosition,
		_buttonYPosition;
	private Panel _panel;

	/**
	 * Creates a new toolbar.
	 */
	public Toolbar()
	{
		_moneyDisplayWidth = 250;
		_height = 40;
		_buttonAreaXPosition = METRO.__SCREEN_SIZE.width - GameState.getInstance().getToolViewWidth();
		_buttonYPosition = -5;

		_panel = new Panel(new Rectangle(0, 0, METRO.__SCREEN_SIZE.width, _height), false);
		_panel.setDrawBorder(false);

		Canvas c = new Canvas(new Point(0, 0));
		c.setPainter(() -> draw());

		_buildStation = new Button(new Rectangle(_buttonAreaXPosition + 140, _buttonYPosition, 40, 50), new Rectangle(0, 28, 40, 50), METRO.__iconSet);
		_buildTracks = new Button(new Rectangle(_buttonAreaXPosition + 180, _buttonYPosition, 40, 50), new Rectangle(0, 78, 40, 50), METRO.__iconSet);
		_showTrainList = new Button(new Rectangle(_buttonAreaXPosition + 220, _buttonYPosition, 40, 50), new Rectangle(0, 128, 40, 50), METRO.__iconSet);
		_createNewTrain = new Button(new Rectangle(_buttonAreaXPosition + 260, _buttonYPosition, 40, 50), new Rectangle(0, 178, 40, 50), METRO.__iconSet);
		registerObervations();

		_panel.add(c);
		_panel.add(_buildStation);
		_panel.add(_buildTracks);
		_panel.add(_showTrainList);
		_panel.add(_createNewTrain);
	}

	private void registerObervations()
	{
		_buildStation.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetExclusiveButtonPositions(_buildStation);
			}
		});
		_buildTracks.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetExclusiveButtonPositions(_buildTracks);
			}
		});
		_showTrainList.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetExclusiveButtonPositions(_showTrainList);
			}
		});
		_createNewTrain.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				resetExclusiveButtonPositions(_createNewTrain);
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

	private void draw()
	{
		// draw the background and the red line below it
		Fill.setColor(Color.white);
		Fill.Rect(new Rectangle(0, 0, METRO.__SCREEN_SIZE.width, _height));
		Draw.setColor(METRO.__metroRed);
		Draw.Line(0, _height, METRO.__SCREEN_SIZE.width, _height);

		// separating line for the money display
		Draw.setColor(METRO.__metroRed);
		Draw.Line(_moneyDisplayWidth, 0, _moneyDisplayWidth, _height);

		// TODO implement the string with a ColorFormattedLabel

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

	public Button getBuildStationButton()
	{
		Contract.EnsureNotNull(_buildStation);

		return _buildStation;
	}

	public Button getBuildTracksButton()
	{
		Contract.EnsureNotNull(_buildTracks);

		return _buildTracks;
	}

	public Button getShowTrainListButton()
	{
		Contract.EnsureNotNull(_showTrainList);

		return _showTrainList;
	}

	public Button getCreateNewTrainButton()
	{
		Contract.EnsureNotNull(_createNewTrain);

		return _createNewTrain;
	}
	
	/**
	 * @return The container that holds all controls.
	 */
	public AbstractContainer getBackgroundPanel()
	{
		Contract.EnsureNotNull(_panel);

		return _panel;
	}
}
