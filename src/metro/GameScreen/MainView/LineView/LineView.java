package metro.GameScreen.MainView.LineView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Exceptions.NotEnoughMoneyException;
import metro.GameScreen.GameScreen;
import metro.GameScreen.MainView.NotificationView.NotificationServer;
import metro.GameScreen.MainView.NotificationView.NotificationType;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Trains.Train;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;
import metro.WindowControls.ColorBar;
import metro.WindowControls.InputField;
import metro.WindowControls.Label;
import metro.WindowControls.List;

/**
 * The LineView is a dialog to manage the train lines in METRO. It allows the user to create, modify and remove lines.
 * 
 * @author hauke
 *
 */
public class LineView extends GameScreen implements Observer
{
	private int _windowWidth;
	private List _lineList;
	private Button _createLineButton, // to create a new train line
		_editLineButton, // to change railway nodes of train line
		_removeLineButton, // to remove a train line
		_saveButton; // to save settings/changes
	private boolean _isActive, // true: TrainLineView will be displayed
		_lineSelectToolEnabled, // if enabled, the user can select nodes
		_editMode; // true when user edits a line
	private Point _areaOffset; // to get the (0,0)-coordinate very easy
	private LineSelectTool _lineSelectTool;
	private InputField _lineNameField;
	private Label _lineNameFieldLabel,
		_messageLabel;
	private TrainLine _oldLine; // when the user edits a line name, the old name of it has to be saved to correctly update the line list
	private ColorBar _colorBar;
	private TrainManagementService _trainManagementService;

	/**
	 * Creates a new TrainLineView.
	 */
	public LineView()
	{
		_isActive = true;
		_lineSelectToolEnabled = false;
		_windowWidth = 400; // TODO make this visible for all tools
		_lineSelectTool = new LineSelectTool();
		_lineSelectTool.addObserver(this);

		_trainManagementService = TrainManagementService.getInstance();

		_areaOffset = new Point(METRO.__SCREEN_SIZE.width - _windowWidth, 45);
		_lineList = new List(new Rectangle(_areaOffset.x + 20, _areaOffset.y + 130, _windowWidth - 40, 300),
			null, null, true);
		registerControl(_lineList);
		fillLineList();

		_createLineButton = new Button(new Rectangle(_areaOffset.x + 20, _areaOffset.y + 450, (_windowWidth - 40) / 3 - 10, 20), "Create line");
		registerControl(_createLineButton);
		_editLineButton = new Button(new Rectangle(_areaOffset.x + 12 + (_windowWidth / 3), _areaOffset.y + 450, (_windowWidth - 40) / 3 - 10, 20), "Edit line");
		registerControl(_editLineButton);
		_removeLineButton = new Button(new Rectangle(_areaOffset.x + 4 + (_windowWidth / 3) * 2, _areaOffset.y + 450, (_windowWidth - 40) / 3 - 10, 20), "Remove line");
		registerControl(_removeLineButton);

		_lineNameField = new InputField(new Rectangle(_areaOffset.x + 95, _areaOffset.y + 490, _windowWidth - 175, 20));
		_lineNameField.setState(false);
		registerControl(_lineNameField);
		_lineNameFieldLabel = new Label("Line Name", new Point(_areaOffset.x + 20, _areaOffset.y + 493));
		_lineNameFieldLabel.setState(false);
		registerControl(_lineNameFieldLabel);
		_colorBar = new ColorBar(new Rectangle(_areaOffset.x + 20, _areaOffset.y + 520, _windowWidth - 70, 20), null, 0.9f, 0.8f);
		_colorBar.setState(false);
		registerControl(_colorBar);
		_saveButton = new Button(new Rectangle(_areaOffset.x + (_windowWidth / 2) - 75, METRO.__SCREEN_SIZE.height - _areaOffset.y - 60, 150, 20), "Save");
		_saveButton.setState(false);
		registerControl(_saveButton);

		_messageLabel = new Label("", new Point(_areaOffset.x + 20, METRO.__SCREEN_SIZE.height - _areaOffset.y - 100));
		_messageLabel.setColor(METRO.__metroRed);
		registerControl(_messageLabel);

		addCreateLineButtonObserver();
		addEditButtonObserver();
		addRemoveLineButtonObserver();
		addSaveButtonObserver();
		addColorBarObserver();
		addMessageLabelObserver();
	}

	/**
	 * Fills the line list with the lines.
	 */
	private void fillLineList()
	{
		// Fill line list with all lines:
		for(TrainLine line : _trainManagementService.getLines())
		{
			_lineList.addElement(line.getName());
		}
	}

	/**
	 * Creates the action listener for the "Create line"-Button.
	 * This will unlock/lock all necessary controls.
	 */
	private void addCreateLineButtonObserver()
	{
		_createLineButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				if(_lineSelectToolEnabled) return;
				createLineSelectTool();
				if(_colorBar.getClickedColor() != null) _lineSelectTool.setColor(_colorBar.getClickedColor());
				reset();
			}
		});
	}

	/**
	 * Creates the listener for the edit button.
	 * Fills controls with information about line so that the player can edit it.
	 */
	private void addEditButtonObserver()
	{
		_editLineButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				METRO.__debug("[TrainLineEditButtonClicked]\n"
					+ "Clicked line (index): " + _lineList.getSelected() + "\n"
					+ "Clicked line  (name): " + _lineList.getText());
				if(_lineList.getSelected() == -1) return;

				TrainLine line = _trainManagementService.getLine(_lineList.getText());
				Color color = line.getColor();

				// _oldLine = new TrainLine(_oldLine.getNodes(), _oldLine.getName(), _oldLine.getColor());
				try
				{
					_oldLine = (TrainLine)line.clone();
				}
				catch(CloneNotSupportedException e)
				{
					_messageLabel.setText("Can't clone the old line :/");
					METRO.__debug("[TrainLineCloningFailed]\nCan't clone the old line :/");
					return;
				}

				METRO.__debug("Color: " + color + "\n"
					+ "Old line: " + _oldLine + "\n"
					+ "New Line: " + line);

				if(color == null || line == null)
				{
					_messageLabel.setText("An erorr occured while reading data :(");
					return;
				}

				_editMode = true;

				_lineNameField.setState(true);
				_lineNameField.setText(line.getName());
				_lineNameFieldLabel.setState(true);
				_colorBar.setState(true);
				_colorBar.setValue(color);
				_createLineButton.setState(false);
				_editLineButton.setState(true);
				_removeLineButton.setState(true);
				_saveButton.setState(true);

				createLineSelectTool();
				_lineSelectTool.setLine(line);

				_lineList.setState(false);
			}
		});
	}

	/**
	 * Create the action observer for the "Remove line"-button.
	 * This will remove the whole selected line.
	 */
	private void addRemoveLineButtonObserver()
	{
		_removeLineButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				if(_lineList.getSelected() < 0 || _lineList.getSelected() >= _trainManagementService.getLines().size()) return; // out of bounds

				_trainManagementService.sellTrainsFromLine(_lineList.getText());

				_trainManagementService.removeLine(_lineList.getText());
				_lineList.removeElement(_lineList.getSelected());
				_lineSelectToolEnabled = false;
				reset();
			}
		});
	}

	/**
	 * Create the action observer for the "Save"-button
	 * Creates a new train line and saves it correctly. If something went wrong, an error message will be shown.
	 */
	private void addSaveButtonObserver()
	{
		_saveButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				try
				{
					METRO.__gameState.withdrawMoney(5000);// some administrative costs
				}
				catch(NotEnoughMoneyException e)
				{
					NotificationServer.publishNotification("You have not enough money to modify the line. It costs 5000$.", NotificationType.GAME_ERROR);
					_messageLabel.setText("Not enough money. This action costs 5000$!");
					METRO.__debug("[ChangeLineFailed]\nThere's not enough money to change the line.\n" + e.getMessage());
				}

				if(!_lineSelectToolEnabled) return;

				// if something went wrong:
				if(_lineList.contains(_lineNameField.getText()) && !_editMode) // when edit mode, then don't change the list
				{
					_messageLabel.setText("The line \"" + _lineNameField.getText() + "\" already exists.");
				}
				else if(_lineNameField.getText().equals(""))
				{
					_messageLabel.setText("Please enter a name!");
				}
				else if(TrainLine.__isValid(_lineSelectTool.getNodeList()))
				{
					TrainLine line = _lineSelectTool.getTrainLine();

					METRO.__debug("[StartFinishEditLine]");
					METRO.__debug("Amount of lines (pre)  : " + _trainManagementService.getLines().size());

					if(_oldLine != null)
					{
						_trainManagementService.removeLine(_oldLine);
						_lineList.removeElement(_lineList.getIndex(_oldLine.getName()));

						ArrayList<Train> oldTrains = getTrainsOfLine(_oldLine);

						// transfer trains to new line
						for(Train train : oldTrains)
						{
							train.setLine(line);
						}
					}

					line.setName(_lineNameField.getText());
					_trainManagementService.addLine(line);
					_lineList.addElement(_lineNameField.getText());
					_lineSelectToolEnabled = false;

					reset();
					_lineList.setState(true);
					METRO.__debug("Amount of lines (after): " + _trainManagementService.getLines().size());
				}
				else
				{
					_messageLabel.setText("Train Line is not valid!");
				}
			}

			/**
			 * Gets all trains that belongs to that given line.
			 * 
			 * @param line The train line whose trains you want.
			 * @return All trains of that line.
			 */
			private ArrayList<Train> getTrainsOfLine(TrainLine line)
			{
				ArrayList<Train> trains = new ArrayList<>();
				for(Train train : _trainManagementService.getTrains())
				{
					if(train.getLine().equals(line))
					{
						trains.add(train);
					}
				}
				return trains;
			}
		});
	}

	/**
	 * Creates the observer for the color bar.
	 * The observer will manage the click and update operations.
	 */
	private void addColorBarObserver()
	{
		_colorBar.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				_messageLabel.setText("");

				Color clickedColor = _colorBar.getClickedColor();
				String msg = _lineSelectTool.setColor(clickedColor);

				if(!msg.equals("")) // some error occurred
				{
					_messageLabel.setText(msg);
				}
				else if(_editMode)
				{
					_trainManagementService.getLine(_lineList.getText()).setColor(clickedColor);
				}
			}
		});
	}

	/**
	 * Creates an observer for the message label. When a user clicks this label, the text will be removed.
	 */
	private void addMessageLabelObserver()
	{
		_messageLabel.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				_messageLabel.setText("");
			}
		});
	}

	/**
	 * Creates a clean {@code LineSelectTool}, adds this as observer and sets the {@code _lineSelectToolEnabled} flag.
	 */
	private void createLineSelectTool()
	{
		_lineSelectTool = new LineSelectTool(); // create clean select tool
		_lineSelectTool.addObserver(this);
		_lineSelectToolEnabled = true;
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		if(!_isActive) return;
		drawBackground();
		drawTitleBox();
		drawListBox();
		drawButtons();
		drawInputFields();
		drawColorBar();

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
		Draw.Rect(METRO.__SCREEN_SIZE.width - _windowWidth + 20, _areaOffset.y + 15, _windowWidth - 40, 60);
		Color c = new Color((int)(METRO.__metroBlue.getRed() * 1.8f),
			(int)(METRO.__metroBlue.getGreen() * 1.3f),
			255); // lighter metro-blue
		Draw.setColor(c);
		Draw.Rect(METRO.__SCREEN_SIZE.width - _windowWidth + 22, _areaOffset.y + 17, _windowWidth - 44, 56);

		// Take "METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2" to center the label
		length = Draw.getStringSize("METRO lines").width;
		Draw.setColor(METRO.__metroRed);
		Draw.String("METRO lines", METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2, _areaOffset.y + 25);
		Draw.Line(METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2 - 5, _areaOffset.y + 40,
			METRO.__SCREEN_SIZE.width - (_windowWidth - length) / 2 + 5, _areaOffset.y + 40);

		length = Draw.getStringSize("Control Management").width;
		Draw.String("Control Management", METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2, _areaOffset.y + 50);
		Draw.Line(METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2 - 5, _areaOffset.y + 65,
			METRO.__SCREEN_SIZE.width - (_windowWidth - length) / 2 + 5, _areaOffset.y + 65);
	}

	/**
	 * Draws the list box with the label above it.
	 */
	private void drawListBox()
	{
		Draw.setColor(METRO.__metroRed);
		int length = Draw.getStringSize("List of your lines:").width;
		Draw.String("List of your lines:", METRO.__SCREEN_SIZE.width - _windowWidth + 20, _areaOffset.y + 110);
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth + 20, _areaOffset.y + 125,
			METRO.__SCREEN_SIZE.width - _windowWidth + 20 + length, _areaOffset.y + 125);
		_lineList.draw();
	}

	/**
	 * Draws all buttons.
	 */
	private void drawButtons()
	{
		_createLineButton.draw();
		_editLineButton.draw();
		_removeLineButton.draw();
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
	 * Draws the color bar including border and preview field next to it.
	 */
	private void drawColorBar()
	{
		_colorBar.draw();

		if(_colorBar.getClickedColor() != null)
		{
			Fill.setColor(_colorBar.getClickedColor());
			Fill.Rect(new Rectangle(_areaOffset.x + _windowWidth - 40, _areaOffset.y + 520, 20, 20));
		}
		Draw.setColor(Color.darkGray);
		Draw.Rect(new Rectangle(_areaOffset.x + _windowWidth - 40, _areaOffset.y + 520, 20, 20));
	}

	/**
	 * Sets the visibility of the TrainLineView. The visibility will also effect the usability (e.g. mouse click).
	 * 
	 * @param visible True will make this visible, false will make it invisible.
	 */
	public void setVisibility(boolean visible)
	{
		_isActive = visible;
		if(_lineSelectToolEnabled)
		{
			_trainManagementService.removeLine(_lineSelectTool.getTrainLine());
			_lineSelectToolEnabled = false;
		}
		if(!_isActive) reset();
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(!_isActive) return;
		if(_lineSelectToolEnabled)
		{

			// if select tool exists and mouse is in the area of the select tool, forward click to tool
			if(_lineSelectTool != null && _lineSelectToolEnabled && screenX <= METRO.__SCREEN_SIZE.width - _windowWidth)
			{
				// something will probably change so remove the old line (new one will be added later)
				_trainManagementService.removeLine(_lineSelectTool.getTrainLine());
				_lineSelectTool.mouseClicked(screenX, screenY, mouseButton); // add/remove node to list
			}
		}
		else if(mouseButton == Buttons.RIGHT) // if select tool is not enabled, hide/close the whole line view
		{
			_isActive = false;
			setChanged();
			notifyObservers(); // notify about close
			return;
		}

		// when the mouse is out of TrainLineView, get the new line from the select tool and add it to the overseer
		if(screenX <= METRO.__SCREEN_SIZE.width - _windowWidth && _lineSelectToolEnabled)
		{
			METRO.__debug("[AddLineToOverseer]");
			TrainLine line = _lineSelectTool.getTrainLine();
			_trainManagementService.addLine(line); // this will only change something when line is valid
		}
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
	}

	@Override
	public void keyDown(int keyCode)
	{
		_lineNameField.keyPressed(keyCode);
		_lineSelectTool.setName(_lineNameField.getText());
	}

	@Override
	public void mouseScrolled(int amount)
	{
		if(!_isActive) return;
		_lineList.mouseScrolled(amount);
		_colorBar.mouseScrolled(amount);
		String msg = _lineSelectTool.setColor(_colorBar.getClickedColor());
		if(!msg.equals("")) _messageLabel.setText(msg);
	}

	@Override
	public boolean isActive()
	{
		return _isActive;
	}

	@Override
	public void reset()
	{
		// switch controls OFF when select tool is enabled
		_editLineButton.setState(!_lineSelectToolEnabled);
		_editMode = !_lineSelectToolEnabled;
		_removeLineButton.setState(!_lineSelectToolEnabled);
		_createLineButton.setState(!_lineSelectToolEnabled);

		// switch controls ON when select tool is enabled
		_lineNameField.setState(_lineSelectToolEnabled);
		_lineNameFieldLabel.setState(_lineSelectToolEnabled);
		_colorBar.setState(_lineSelectToolEnabled);
		_saveButton.setState(_lineSelectToolEnabled);

		_colorBar.clear();

		_messageLabel.setText("");
		_lineNameField.setText("");

		_oldLine = null;

		_lineSelectTool.setState(false);
	}

	@Override
	public void close()
	{
		super.close();
	}

	@Override
	public boolean isHovered()
	{
		return METRO.__mousePosition.x > _areaOffset.x; // don't need to check y-coord. because the whole screen height is used.
	}

	@Override
	public void update(Observable o, Object arg)
	{
		if(o instanceof LineSelectTool)
		{
			_lineSelectToolEnabled = false;

			TrainLine line = ((LineSelectTool)o).getTrainLine();
			_trainManagementService.removeLine(line);

			_trainManagementService.addLine(_oldLine);
			_lineList.setText(_oldLine.getName());
			_lineList.setState(true);
			reset();
		}
	}
}
