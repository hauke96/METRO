package metro.GameScreen.MainView.TrainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Trains.TrainTemplate;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;
import metro.WindowControls.Label;
import metro.WindowControls.List;
import metro.WindowControls.Panel;

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
	private TextureRegion _titleImage;
	private Panel _panel;

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

		_panel = new Panel(new Rectangle());
		
		_buyButton = new Button(new Rectangle(_areaOffset.x + 170, METRO.__SCREEN_SIZE.height - METRO.__getYOffset() - _areaOffset.y, 210, 20), "Buy");
		
		_availableTrains = new List(
			new Rectangle(_areaOffset.x + 20, _areaOffset.y + 440, 140, METRO.__SCREEN_SIZE.height - (_areaOffset.y + 420) - _areaOffset.y - METRO.__getYOffset()), null, true);
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
					_titleImage = train.getTitleImage();
				}
			}
		});

		_messageLabel = new Label("", new Point(_areaOffset.x + 170, _areaOffset.y + 600), 200);

		_informationKeys = new Label("Name\nProducer\nPrice\nCosts\nCost-factor\nPassenger\nSpeed", new Point(_areaOffset.x + 170, _areaOffset.y + 440));
		_informationKeys.setColor(METRO.__metroBlue);

		_informationValues = new Label("=\n=\n=\n=\n=\n=\n=\n", new Point(_areaOffset.x + 235, _areaOffset.y + 440));
		_informationValues.setColor(METRO.__metroBlue);

		fillTrainList();
		
		_panel.add(_buyButton);
		_panel.add(_availableTrains);
		_panel.add(_messageLabel);
		_panel.add(_informationKeys);
		_panel.add(_informationValues);
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
		drawTitleImage();
	}

	private void drawHeader()
	{
		Draw.setColor(METRO.__metroRed);

		String text = "Available train models:";
		int length = Draw.getStringSize(text).width;
		Draw.String(text, METRO.__SCREEN_SIZE.width - _windowWidth + 25, _areaOffset.y + 420);
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth + 25, _areaOffset.y + 435,
			METRO.__SCREEN_SIZE.width - _windowWidth + 25 + length, _areaOffset.y + 435);
	}

	/**
	 * Draws the title image of the train (a greater rendered image which simply looks nice but has no function).
	 */
	private void drawTitleImage()
	{
		// when there's an image and the space between buy-button and info label is great enough, draw the image
		if(_titleImage != null)
		{
			int diff = _buyButton.getPosition().y - (_informationValues.getPosition().y + _informationValues.getArea().height);
			int height = diff >= 220 ? 180 : diff - 40;
			Rectangle position = new Rectangle(
				METRO.__SCREEN_SIZE.width - _windowWidth + 170 + ((200 - height) / 2),
				_areaOffset.y + 460 + _informationKeys.getArea().height,
				height,
				height);

			Draw.Image(_titleImage, position);

			// larger image when mouse hovers smaller image
			if(position.contains(METRO.__mousePosition) && diff < 220)
			{
				Fill.setColor(Color.white);
				Fill.Rect(METRO.__mousePosition.x - 200, METRO.__mousePosition.y - 200, 200, 200);

				Draw.setColor(METRO.__metroBlue);
				Draw.Rect(METRO.__mousePosition.x - 200, METRO.__mousePosition.y - 200, 200, 200);
				Draw.Image(_titleImage, new Rectangle(METRO.__mousePosition.x - 200, METRO.__mousePosition.y - 200, 200, 200));
			}
		}
	}

	/**
	 * Changes the selected train in the list to the given one.
	 * This will also update the information list. When the model doesn't exist, nothing changes.
	 * 
	 * @param modelName The model name of the selection.
	 */
	void setTrain(String modelName)
	{
		if(_availableTrains.contains(modelName))
		{
			_availableTrains.setText(modelName);
		}
	}

	/**
	 * @return The buy train button.
	 */
	Button getBuyButton()
	{
		return _buyButton;
	}

	/**
	 * @return The message label that shows error messages.
	 */
	Label getMessageLabel()
	{
		return _messageLabel;
	}

	/**
	 * @return The list of all available train models
	 */
	List getTrainList()
	{
		return _availableTrains;
	}
	
	/**
	 * @return Gets the main panel of this screen.
	 */
	Panel getPanel()
	{
		return _panel;
	}

	@Override
	public boolean isActive()
	{
		return false;
	}

	@Override
	public boolean isHovered()
	{
		return METRO.__mousePosition.x > _areaOffset.x
			&& METRO.__mousePosition.y < _areaOffset.y;
	}
}
