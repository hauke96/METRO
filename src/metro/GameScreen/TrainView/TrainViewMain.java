package metro.GameScreen.TrainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Lines.TrainLineOverseer;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;
import metro.WindowControls.Label;
import metro.WindowControls.List;

public class TrainViewMain extends GameScreen
{
	private int _windowWidth;
	private List _trainList,
		_lineList;
	private Button _buyTrainButton, // to create a new train
		_editTrainButton, // to change a train
		_sellTrainButton, // to remove a train
		_saveButton; // to save settings/changes
	private boolean	_messageLabelClicked;
	private Label _messageLabel;
	private Point _areaOffset; // to get the (0,0)-coordinate very easy
	
	/**
	 * Creates a new main view for the train screen.
	 * 
	 * @param areaOffset The offset of the screen area.
	 * @param windowWidth The window width.
	 */
	public TrainViewMain(Point areaOffset, int windowWidth)
	{
		_windowWidth = windowWidth;
		_areaOffset = areaOffset;
		_trainList = new List(new Rectangle(_areaOffset.x + 121, 130, _windowWidth - 140, 300),
			null, null, true);
		_lineList = new List(new Rectangle(_areaOffset.x + 20, 130, _windowWidth - 300, 300),
			null, null, true);
		_buyTrainButton = new Button(new Rectangle(_areaOffset.x + 20, 450, (_windowWidth - 40) / 3 - 10, 20), "Buy train");
		_editTrainButton = new Button(new Rectangle(_areaOffset.x + 12 + (_windowWidth / 3), 450, (_windowWidth - 40) / 3 - 10, 20), "Edit train");
		_sellTrainButton = new Button(new Rectangle(_areaOffset.x + 4 + (_windowWidth / 3) * 2, 450, (_windowWidth - 40) / 3 - 10, 20), "Sell train");

		_saveButton = new Button(new Rectangle(_areaOffset.x + (_windowWidth / 2) - 75, METRO.__SCREEN_SIZE.height - _areaOffset.y - 60, 150, 20), "Save");
		_saveButton.setState(false);

		_messageLabel = new Label("", new Point(_areaOffset.x + 20, METRO.__SCREEN_SIZE.height - _areaOffset.y - 30));
		_messageLabel.setColor(METRO.__metroRed);

		addButtonObserver();
		addMessageLabelObserver();
	}

	/**
	 * Creates all needed observers for the buttons.
	 */
	private void addButtonObserver()
	{
		_buyTrainButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				buyTrainButton_action();
			}
		});
		_editTrainButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				editTrainButton_action();
			}
		});
		_sellTrainButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				sellTrainButton_action();
			}
		});
		_saveButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				saveButton_action();
			}
		});
	}

	private void addMessageLabelObserver()
	{
		_messageLabel.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				_messageLabel.setText("");
				_messageLabelClicked = true;
			}
		});
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		drawListBox();
		drawButtons();

		_messageLabel.draw();
	}

	/**
	 * Draws the list box with the label above it.
	 */
	private void drawListBox()
	{
		Draw.setColor(METRO.__metroRed);
		
		String text = "Your lines:";
		int length = Draw.getStringSize(text).width;
		Draw.String(text, METRO.__SCREEN_SIZE.width - _windowWidth + 25, 110);
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth + 25, 125,
			METRO.__SCREEN_SIZE.width - _windowWidth + 25 + length, 125);
		
		text = "Your trains:";
		Draw.String(text, METRO.__SCREEN_SIZE.width - _windowWidth + 125, 110);
		length = Draw.getStringSize(text).width;
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth + 125, 125,
			METRO.__SCREEN_SIZE.width - _windowWidth + 125 + length, 125);
		
		_trainList.draw();
		_lineList.draw();
	}

	/**
	 * Draws all buttons.
	 */
	private void drawButtons()
	{
		_buyTrainButton.draw();
		_editTrainButton.draw();
		_sellTrainButton.draw();
		_saveButton.draw();
	}

	/**
	 * Will be executed after a click on the "Create Line" button. This will unlock/lock all necessary controls.
	 */
	private void buyTrainButton_action()
	{
		reset();
		setChanged();
		notifyObservers(new TrainViewBuy(_areaOffset, _windowWidth));
	}

	/**
	 * Creates a new train line and saves it correctly. If something went wrong, an error message will be shown.
	 */
	private void saveButton_action()
	{
	}

	/**
	 * Checks if the message label has been clicked since last call.
	 * 
	 * @return True when the message label has been clicked since last call.
	 */
	private boolean hasMessageLabelClicked()
	{
		return _messageLabelClicked;
	}

	/**
	 * Removes the whole selected line.
	 */
	private void sellTrainButton_action()
	{
		TrainLineOverseer.removeLine(_trainList.getText(_trainList.getSelected()));
		_trainList.remove(_trainList.getSelected());
	}

	/**
	 * Fills controls with information about line so that the player can edit it.
	 */
	private void editTrainButton_action()
	{
		if(_trainList.getSelected() == -1) return;
		Color color = TrainLineOverseer.getColor(_trainList.getText(_trainList.getSelected()));
		TrainLine line = TrainLineOverseer.getLine(_trainList.getText(_trainList.getSelected()));

		if(color == null || line == null)
		{
			_messageLabel.setText("An erorr occured while reading data :(");
			return;
		}

		_buyTrainButton.setState(false);
		_editTrainButton.setState(false);
		_sellTrainButton.setState(false);
		_saveButton.setState(true);
	}

	@Override
	public void reset()
	{
		_messageLabel.setText("");
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		// TODO implement ActionObserver
		if(_trainList.clickOnControlElement()) return;
	}

	@Override
	public void mouseScrolled(int amount)
	{
		_trainList.mouseScrolled(amount);
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
	}

	@Override
	public void keyDown(int keyCode)
	{
	}
}
