package metro.GameScreen.TrainView;

import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.TrainManagement.Trains.Train;
import metro.WindowControls.Button;
import metro.WindowControls.Label;
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
	private Label _messageLabel;

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

		_buyButton = new Button(new Rectangle(_areaOffset.x + 170, 680, 210, 20), "Buy");
		registerControl(_buyButton);
		
		_availableTrains = new List(new Rectangle(_areaOffset.x + 20, 460, 140, 240), null, true);
		registerControl(_availableTrains);
		
		_messageLabel = new Label("", new Point(_areaOffset.x + 170, 610), 200);
		registerControl(_messageLabel);
		
		fillTrainList();
	}

	/**
	 * Fills the list with the available trains.
	 */
	private void fillTrainList()
	{
		for(Train train : METRO.__gameState.getAvailableTrains())
		{
			_availableTrains.addElement(train.getName());
		}
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		drawHeader();
		drawInformation();

		_availableTrains.draw();
		_messageLabel.draw();
		_buyButton.draw();
	}

	private void drawHeader()
	{
		Draw.setColor(METRO.__metroRed);

		String text = "Available train models:";
		int length = Draw.getStringSize(text).width;
		Draw.String(text, METRO.__SCREEN_SIZE.width - _windowWidth + 25, 440);
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth + 25, 455,
			METRO.__SCREEN_SIZE.width - _windowWidth + 25 + length, 455);
	}

	private void drawInformation()
	{
		Train train = METRO.__gameState.getTrain(_availableTrains.getText());

		Draw.setColor(METRO.__metroBlue);
		Draw.String("Name", _areaOffset.x + 170, 460);
		Draw.String("Manufacturer", _areaOffset.x + 170, 485);
		Draw.String("Price", _areaOffset.x + 170, 510);
		Draw.String("Costs", _areaOffset.x + 170, 535);
		Draw.String("Cost-factor", _areaOffset.x + 170, 560);
		Draw.String("Passenger", _areaOffset.x + 170, 585);

		Draw.String("=", _areaOffset.x + 250, 460);
		Draw.String("=", _areaOffset.x + 250, 485);
		Draw.String("=", _areaOffset.x + 250, 510);
		Draw.String("=", _areaOffset.x + 250, 535);
		Draw.String("=", _areaOffset.x + 250, 560);
		Draw.String("=", _areaOffset.x + 250, 585);

		if(train != null)
		{
			Draw.String(train.getName(), _areaOffset.x + 260, 460);
			Draw.String(train.getManufacturer(), _areaOffset.x + 260, 485);
			Draw.String("" + train.getPrice(), _areaOffset.x + 260, 510);
			Draw.String("" + train.getCosts(), _areaOffset.x + 260, 535);
			Draw.String("" + train.getCostFactor(), _areaOffset.x + 260, 560);
			Draw.String("" + train.getMaxPassenger(), _areaOffset.x + 260, 585);
		}
	}
	
	/**
	 * @return The buy train button.
	 */
	public Button getBuyButton()
	{
		return _buyButton;
	}
	
	/**
	 * @return The message label that shows error messages.
	 */
	public Label getMessageLabel()
	{
		return _messageLabel;
	}

	/**
	 * @return The list of all available train models
	 */
	public List getTrainList()
	{
		return _availableTrains;
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
