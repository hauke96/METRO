package metro.GameScreen.TrainView;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.TrainManagement.Trains.Train;
import metro.WindowControls.Button;
import metro.WindowControls.List;

/**
 * A part of the TrainView where the user can buy new trains.
 * It will also show information about different train types but this tool can't edit the trains per line.
 * The player can only buy trains with this dialog but can't sell any trains.
 * 
 * @author hauke
 *
 */
public class TrainViewBuy extends GameScreen
{
	private List _availableTrains; // all available train models
	private int _windowWidth;
	private Point _areaOffset;
	private Button _buyButton;

	/**
	 * Creates a new dialog.
	 * 
	 * @param areaOffset The position of the right edge of the main dialog (to better determine positions).
	 * @param windowWidth The width of the main dialog.
	 */
	public TrainViewBuy(Point areaOffset, int windowWidth)
	{
		_windowWidth = windowWidth;
		_areaOffset = areaOffset;

		_buyButton = new Button(new Rectangle(_areaOffset.x + 180, 680, 200, 20), "Buy");
		registerControl(_buyButton);
		_availableTrains = new List(new Rectangle(_areaOffset.x + 20, 460, 150, 240), null, true);
		registerControl(_availableTrains);
		fillTrainList();
	}


	/**
	 * Fills the list with the available trains.
	 */
	private void fillTrainList()
	{
		ArrayList<Train> list = METRO.__gameState.getAvailableTrains();
		for(Train train : list)
		{
			_availableTrains.addElement(train.getName());
		}
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		Draw.setColor(METRO.__metroRed);

		String text = "Available train models:";
		int length = Draw.getStringSize(text).width;
		Draw.String(text, METRO.__SCREEN_SIZE.width - _windowWidth + 25, 440);
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth + 25, 455,
			METRO.__SCREEN_SIZE.width - _windowWidth + 25 + length, 455);

		Draw.String("INFOS ABOUT SELECTED TRAIN HERE!", _areaOffset.x + 220, 530, 160);

		_availableTrains.draw();
		_buyButton.draw();
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
		return false;
	}

	@Override
	public void reset()
	{
	}
}
