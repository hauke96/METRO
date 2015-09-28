package metro.GameScreen.TrainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.GameScreen.TrainInteractionTool;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Lines.TrainLineOverseer;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;
import metro.WindowControls.InputField;
import metro.WindowControls.Label;
import metro.WindowControls.List;

public class TrainView extends GameScreen implements TrainInteractionTool
{
	private int _windowWidth;
	private List _trainList;
	private Button _createTrainButton, // to create a new train
		_editTrainButton, // to change a train
		_removeTrainButton, // to remove a train
		_saveButton; // to save settings/changes
	private boolean	_isClosed,
		_visible,
		_buttonClicked,
		_messageLabelClicked;
	private Point _areaOffset; // to get the (0,0)-coordinate very easy
	private InputField _lineNameField;
	private Label _lineNameFieldLabel,
		_messageLabel;

	/**
	 * Creates a new TrainLineView.
	 * 
	 * @param mapOffset The offset of the map (for correct mouse clicks)
	 */
	public TrainView(Point2D mapOffset)
	{
		_isClosed = false;
		_buttonClicked = false;
		_windowWidth = 400;

		_areaOffset = new Point(METRO.__SCREEN_SIZE.width - _windowWidth, 0);
		_trainList = new List(new Rectangle(_areaOffset.x + 20, 130, _windowWidth - 40, 300),
			null, null, true);
		_createTrainButton = new Button(new Rectangle(_areaOffset.x + 20, 450, (_windowWidth - 40) / 3 - 10, 20), "Create line");
		_editTrainButton = new Button(new Rectangle(_areaOffset.x + 12 + (_windowWidth / 3), 450, (_windowWidth - 40) / 3 - 10, 20), "Edit line");
		_removeTrainButton = new Button(new Rectangle(_areaOffset.x + 4 + (_windowWidth / 3) * 2, 450, (_windowWidth - 40) / 3 - 10, 20), "Remove line");

		_lineNameField = new InputField(new Rectangle(_areaOffset.x + 95, 490, _windowWidth - 175, 20));
		_lineNameField.setState(false);
		_lineNameFieldLabel = new Label("Line Name", new Point(_areaOffset.x + 20, 493));
		_lineNameFieldLabel.setState(false);
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
		_createTrainButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				createTrainButton_action();
			}
		});
		_editTrainButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				editLineButton_action();
			}
		});
		_removeTrainButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				removeLineButton_action();
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
		drawBackground();
		drawTitleBox();
		drawListBox();
		drawButtons();
		drawInputFields();

		_messageLabel.draw();
	}

	/**
	 * Draws the white background and the blue line at the left.
	 */
	private void drawBackground()
	{
		Fill.setColor(Color.white);
		Fill.Rect(METRO.__SCREEN_SIZE.width - _windowWidth, 0, _windowWidth, METRO.__SCREEN_SIZE.height);
		Draw.setColor(METRO.__metroBlue);
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth, 0, METRO.__SCREEN_SIZE.width - _windowWidth, METRO.__SCREEN_SIZE.height);
	}

	/**
	 * Draws the title bar with the Label and box.
	 */
	private void drawTitleBox()
	{
		int length = Draw.getStringSize("Control Management").width;
		Draw.Rect(METRO.__SCREEN_SIZE.width - _windowWidth + 20, 15, _windowWidth - 40, 60);
		Color c = new Color((int)(METRO.__metroBlue.getRed() * 1.8f),
			(int)(METRO.__metroBlue.getGreen() * 1.3f),
			255); // lighter metro-blue
		Draw.setColor(c);
		Draw.Rect(METRO.__SCREEN_SIZE.width - _windowWidth + 22, 17, _windowWidth - 44, 56);

		// Take "METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2" to center the label
		length = Draw.getStringSize("METRO lines").width;
		Draw.setColor(METRO.__metroRed);
		Draw.String("METRO lines", METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2, 25);
		Draw.Line(METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2 - 5, 40,
			METRO.__SCREEN_SIZE.width - (_windowWidth - length) / 2 + 5, 40);

		length = Draw.getStringSize("Control Management").width;
		Draw.String("Control Management", METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2, 50);
		Draw.Line(METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2 - 5, 65,
			METRO.__SCREEN_SIZE.width - (_windowWidth - length) / 2 + 5, 65);
	}

	/**
	 * Draws the list box with the label above it.
	 */
	private void drawListBox()
	{
		Draw.setColor(METRO.__metroRed);
		int length = Draw.getStringSize("List of your lines:").width;
		Draw.String("List of your lines:", METRO.__SCREEN_SIZE.width - _windowWidth + 20, 110);
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth + 20, 125,
			METRO.__SCREEN_SIZE.width - _windowWidth + 20 + length, 125);
		_trainList.draw();
	}

	/**
	 * Draws all buttons.
	 */
	private void drawButtons()
	{
		_createTrainButton.draw();
		_editTrainButton.draw();
		_removeTrainButton.draw();
	}

	/**
	 * Draws all input fields
	 */
	private void drawInputFields()
	{
		_lineNameFieldLabel.draw();
		_lineNameField.draw();
		_saveButton.draw();
	}

	/**
	 * Sets the visibility of the TrainLineView. The visibility will also effect the usability (e.g. mouse click).
	 * 
	 * @param visible True will make this visible, false will make it invisible.
	 */
	public void setVisibility(boolean visible)
	{
		_visible = visible;
		if(!_visible) resetControls();
	}

	/**
	 * Checks if a button has been clicked since last call.
	 * 
	 * @return True when one of the buttons has been clicked since last call.
	 */
	private boolean hasButtonClicked()
	{
		return _buttonClicked;
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
	 * Will be executed after a click on the "Create Line" button. This will unlock/lock all necessary controls.
	 */
	private void createTrainButton_action()
	{
		_buttonClicked = true;
		resetControls();
	}

	/**
	 * Creates a new train line and saves it correctly. If something went wrong, an error message will be shown.
	 */
	private void saveButton_action()
	{
		_buttonClicked = true;
	}

	/**
	 * Removes the whole selected line.
	 */
	private void removeLineButton_action()
	{
		TrainLineOverseer.removeLine(_trainList.getText(_trainList.getSelected()));
		_trainList.remove(_trainList.getSelected());
		_buttonClicked = true;
	}

	/**
	 * Fills controls with information about line so that the player can edit it.
	 */
	private void editLineButton_action()
	{
		if(_trainList.getSelected() == -1) return;
		Color color = TrainLineOverseer.getColor(_trainList.getText(_trainList.getSelected()));
		TrainLine line = TrainLineOverseer.getLine(_trainList.getText(_trainList.getSelected()));

		if(color == null || line == null)
		{
			_messageLabel.setText("An erorr occured while reading data :(");
			return;
		}

		_lineNameField.setState(true);
		_lineNameField.setText(_trainList.getText(_trainList.getSelected()));
		_createTrainButton.setState(false);
		_editTrainButton.setState(false);
		_removeTrainButton.setState(false);
		_saveButton.setState(true);

		_buttonClicked = true;
	}

	/**
	 * Resets all controls to their default values.
	 */
	private void resetControls()
	{
		_messageLabel.setText("");
		_lineNameField.setText("");
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		// TODO implement ActionObserver
		if(!_visible || _trainList.clickOnControlElement()) return;

		// set lineNameField as inactive when mouse NOT clicked on it
		// TODO implement ActionObserver
		if(_lineNameField.clickOnControlElement()) _lineNameField.select();
		else _lineNameField.disselect();

		boolean controlClicked = false;

		controlClicked |= hasButtonClicked();
		controlClicked |= hasMessageLabelClicked();//checkMessageLabelClick(screenX, screenY, mouseButton);
		// "create new train"/"finish" line has been pressed
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
	}

	@Override
	public void keyDown(int keyCode)
	{
		_lineNameField.keyPressed(keyCode);
	}

	@Override
	public void mouseScrolled(int amount)
	{
		if(!_visible) return;
		_trainList.mouseScrolled(amount);
	}

	@Override
	public void draw(SpriteBatch sp, Point2D offset)
	{
		updateGameScreen(sp);
	}

	@Override
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		if(!_visible) return;
	}

	@Override
	public void rightClick(int screenX, int screenY, Point2D offset)
	{
		if(!_visible) return;
	}

	@Override
	public boolean isClosed()
	{
		return _isClosed;
	}
}
