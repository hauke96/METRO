package metro.GameScreen.TrainView;

import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Trains.TrainTemplate;
import metro.WindowControls.ActionObserver;
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
	private Label _messageLabel,
		_informationKeys, // like name=, manufacturer=, ...
		_informationValues; // like CT-01, 1.03, ...
	private TrainManagementService _trainManagementService;

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

		_trainManagementService = TrainManagementService.getInstance();

		_buyButton = new Button(new Rectangle(_areaOffset.x + 170, 680, 210, 20), "Buy");
		registerControl(_buyButton);

		_availableTrains = new List(new Rectangle(_areaOffset.x + 20, 460, 140, 240), null, true);
		_availableTrains.register(new ActionObserver()
		{
			@Override
			public void selectionChanged(String entry)
			{
				TrainTemplate train = _trainManagementService.getTemplateTrain(_availableTrains.getText());

				if(train != null)
				{
					_informationValues.setText("= " + train.getName() + "\n= " +
						train.getManufacturer() + "\n= " +
						train.getPrice() + "$\n= " +
						train.getCosts() + "$ / month\n= " +
						(Math.round((train.getCostFactor() - 1) * 1000) / 10f) + "%\n= " +
						train.getMaxPassenger() + " people / car\n= " +
						(int)(train.getSpeed() * 80) + "km/h");
				}
			}
		});
		registerControl(_availableTrains);

		_messageLabel = new Label("", new Point(_areaOffset.x + 170, 620), 200);
		registerControl(_messageLabel);

		_informationKeys = new Label("Name\nProducer\nPrice\nCosts\nCost-factor\nPassenger\nSpeed", new Point(_areaOffset.x + 170, 460));
		_informationKeys.setColor(METRO.__metroBlue);
		registerControl(_informationKeys);

		_informationValues = new Label("=\n=\n=\n=\n=\n=\n=\n", new Point(_areaOffset.x + 235, 460));
		_informationValues.setColor(METRO.__metroBlue);
		registerControl(_informationValues);

		fillTrainList();
	}

	/**
	 * Fills the list with the available trains.
	 */
	private void fillTrainList()
	{
		for(TrainTemplate train : _trainManagementService.getTemplateTrains())
		{
			_availableTrains.addElement(train.getName());
		}
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		drawHeader();

		_availableTrains.draw();
		_messageLabel.draw();
		Draw.setColor(METRO.__metroBlue);
		_informationKeys.draw();
		_informationValues.draw();
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

	/**
	 * Changes the selected train in the list to the given one.
	 * This will also update the information list. When the model doesn't exist, nothing changes.
	 * 
	 * @param modelName The model name of the selection.
	 */
	public void setTrain(String modelName)
	{
		if(_availableTrains.contains(modelName))
		{
			_availableTrains.setText(modelName);
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

	@Override
	public boolean isHovered()
	{
		return METRO.__mousePosition.x > _areaOffset.x
			&& METRO.__mousePosition.y < _areaOffset.y;
	}
}
