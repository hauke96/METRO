package metro.GameUI.MainView.LineView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
import metro.Common.Technical.Contract;
import metro.Common.Technical.Logger;
import metro.Exceptions.NotEnoughMoneyException;
import metro.GameUI.MainView.NotificationView.NotificationServer;
import metro.GameUI.MainView.NotificationView.NotificationType;
import metro.GameUI.Screen.ToolView;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Trains.Train;
import metro.TrainManagement.Trains.TrainLine;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Container.Panel;
import metro.UI.Renderable.Controls.Button;
import metro.UI.Renderable.Controls.Canvas;
import metro.UI.Renderable.Controls.ColorBar;
import metro.UI.Renderable.Controls.InputField;
import metro.UI.Renderable.Controls.Label;
import metro.UI.Renderable.Controls.List;

/**
 * The LineView is a dialog to manage the train lines in METRO. It allows the user to create, modify and remove lines.
 * 
 * @author hauke
 *
 */
public class LineView extends ToolView implements Observer
{
	private Panel _panel;
	private List _lineList;
	private Button _createLineButton, // to create a new train line
		_editLineButton, // to change railway nodes of train line
		_removeLineButton, // to remove a train line
		_saveButton; // to save settings/changes
	private ColorBar _colorBar;
	private Label _lineNameFieldLabel,
		_messageLabel;
	private InputField _lineNameField;

	private int _windowWidth;
	private boolean _lineSelectToolEnabled, // if enabled, the user can select nodes
		_editMode; // true when user edits a line
	private Point _areaOffset; // to get the (0,0)-coordinate very easy
	private TrainLine _oldLine; // when the user edits a line name, the old name of it has to be saved to correctly update the line list

	private TrainManagementService _trainManagementService;

	private LineSelectTool _lineSelectTool;

	/**
	 * Creates a new TrainLineView.
	 */
	public LineView()
	{
		_lineSelectToolEnabled = false;
		_windowWidth = GameState.getInstance().getToolViewWidth();
		_lineSelectTool = new LineSelectTool();
		_lineSelectTool.addObserver(this);

		_trainManagementService = TrainManagementService.getInstance();

		_areaOffset = new Point(METRO.__SCREEN_SIZE.width - _windowWidth, 40);

		createControls();
		addObserver();
	}

	/**
	 * Creates all controls for this view and registers them properly in the game screen.
	 */
	private void createControls()
	{
		_panel = new Panel(new Rectangle(_areaOffset.x, _areaOffset.y, _windowWidth, METRO.__SCREEN_SIZE.height));
		_panel.setDrawBorder(true, METRO.__metroBlue);
		
		Canvas canvas = new Canvas(new Point(_panel.getPosition().x, _panel.getPosition().y + 20));
		canvas.setPainter(() -> draw());

		_lineList = new List(new Rectangle(_areaOffset.x + 20, _areaOffset.y + 130, _windowWidth - 40, 300), true);
		fillLineList();

		_createLineButton = new Button(new Rectangle(_areaOffset.x + 20, _areaOffset.y + 450, (_windowWidth - 40) / 3 - 10, 20), "Create line");
		_editLineButton = new Button(new Rectangle(_areaOffset.x + 12 + (_windowWidth / 3), _areaOffset.y + 450, (_windowWidth - 40) / 3 - 10, 20), "Edit line");
		_removeLineButton = new Button(new Rectangle(_areaOffset.x + 4 + (_windowWidth / 3) * 2, _areaOffset.y + 450, (_windowWidth - 40) / 3 - 10, 20), "Remove line");

		_lineNameField = new InputField(new Rectangle(_areaOffset.x + 95, _areaOffset.y + 490, _windowWidth - 175, 20));
		_lineNameField.setState(false);

		_lineNameFieldLabel = new Label("Line Name", new Point(_areaOffset.x + 20, _areaOffset.y + 493));
		_lineNameFieldLabel.setState(false);

		_colorBar = new ColorBar(new Rectangle(_areaOffset.x + 20, _areaOffset.y + 520, _windowWidth - 70, 20), 0.9f, 0.8f);
		_colorBar.setState(false);

		_saveButton = new Button(new Rectangle(_areaOffset.x + (_windowWidth / 2) - 75, METRO.__SCREEN_SIZE.height - _areaOffset.y - 60, 150, 20), "Save");
		_saveButton.setState(false);

		_messageLabel = new Label("", new Point(_areaOffset.x + 20, METRO.__SCREEN_SIZE.height - _areaOffset.y - 100));
		_messageLabel.setColor(METRO.__metroRed);

		_panel.add(_lineList);
		_panel.add(_createLineButton);
		_panel.add(_editLineButton);
		_panel.add(_removeLineButton);
		_panel.add(_lineNameField);
		_panel.add(_lineNameFieldLabel);
		_panel.add(_colorBar);
		_panel.add(_saveButton);
		_panel.add(_messageLabel);
		_panel.add(canvas);
	}

	/**
	 * Add the observer for all controls.
	 */
	private void addObserver()
	{
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
				Logger.__debug(""
					+ "Clicked line (index): " + _lineList.getSelected() + "\n"
					+ "Clicked line  (name): " + _lineList.getText());
				if(_lineList.getSelected() == -1) return;

				TrainLine line = _trainManagementService.getLine(_lineList.getText());
				Color color = line.getColor();

				try
				{
					_oldLine = (TrainLine)line.clone();
				}
				catch(CloneNotSupportedException e)
				{
					_messageLabel.setText("Can't clone the old line :/");
					Logger.__fatal("Can't clone the old line :/", e);
					return;
				}

				Logger.__debug("Color: " + color + "\n"
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
					Logger.__info("There's not enough money to change the line.\n" + e.getMessage());
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

					Logger.__debug("Amount of lines (pre)  : " + _trainManagementService.getLines().size());

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

					_trainManagementService.addLine(line);
					line.setName(_lineNameField.getText());
					_lineList.addElement(_lineNameField.getText());
					_lineSelectToolEnabled = false;

					reset();
					_lineList.setState(true);
					Logger.__debug("Amount of lines (after): " + _trainManagementService.getLines().size());
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
	 * Creates a clean {@link metro.GameUI.MainView#LineSelectTool}, adds this as observer and sets the {@code _lineSelectToolEnabled} flag.
	 */
	void createLineSelectTool()
	{
		_lineSelectTool = new LineSelectTool(); // create clean select tool
		_lineSelectTool.addObserver(this);
		_lineSelectToolEnabled = true;
	}

	private void draw()
	{
		drawTitleBox();
		drawListBox();
		drawColorBar();
	}

	/**
	 * Draws the title bar with the Label and box.
	 */
	private void drawTitleBox()
	{
		int length = Draw.getStringSize("Control Management").width;
		Draw.setColor(METRO.__metroBlue);
		Draw.Rect(20, 15, _windowWidth - 40, 60);
		Color c = new Color((int)(METRO.__metroBlue.getRed() * 1.8f),
			(int)(METRO.__metroBlue.getGreen() * 1.3f),
			255); // lighter metro-blue
		Draw.setColor(c);
		Draw.Rect(22, 17, _windowWidth - 44, 56);

		// Take "METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2" to center the label
		length = Draw.getStringSize("METRO lines").width;
		Draw.setColor(METRO.__metroRed);
		Draw.String("METRO lines", (_windowWidth - length) / 2, 25);
		Draw.Line((_windowWidth - length) / 2 - 5, 40,
			(_windowWidth + length) / 2 + 5, 40);

		length = Draw.getStringSize("Control Management").width;
		Draw.String("Control Management", (_windowWidth - length) / 2, 50);
		Draw.Line((_windowWidth - length) / 2 - 5, 65,
			(_windowWidth + length) / 2 + 5, 65);
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
	}

	/**
	 * Draws the color bar including border and preview field next to it.
	 */
	private void drawColorBar()
	{
		if(_colorBar.getClickedColor() != null)
		{
			Fill.setColor(_colorBar.getClickedColor());
			Fill.Rect(new Rectangle(_areaOffset.x + _windowWidth - 40, _areaOffset.y + 520, 20, 20));
		}
		Draw.setColor(Color.darkGray);
		Draw.Rect(new Rectangle(_areaOffset.x + _windowWidth - 40, _areaOffset.y + 520, 20, 20));
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
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
		else if(!isHovered())// if select tool is not enabled, hide/close the whole line view when the mouse is outside of it
		{
			close();
			return;
		}

		// when the mouse is out of TrainLineView, get the new line from the select tool and add it to the overseer
		if(screenX <= METRO.__SCREEN_SIZE.width - _windowWidth && _lineSelectToolEnabled)
		{
			Logger.__debug("Add line to observer");
			TrainLine line = _lineSelectTool.getTrainLine();
			_trainManagementService.addLine(line); // this will only change something when line is valid
		}
	}

	public void keyDown(int keyCode)
	{
		_lineSelectTool.setName(_lineNameField.getText());
	}

	public void mouseScrolled(int amount)
	{
		String msg = _lineSelectTool.setColor(_colorBar.getClickedColor());
		if(!msg.equals("")) _messageLabel.setText(msg);
	}

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
	}

	@Override
	public void close()
	{
		_lineSelectTool.close();
		_panel.close();
		super.close();
		
		setChanged();
		notifyObservers(); // notify about close
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

			if(_oldLine != null) // when creating a new line, _oldLine is null
			{
				_trainManagementService.addLine(_oldLine);
				_lineList.setText(_oldLine.getName());
			}
			_lineList.setState(true);
			reset();
		}
	}

	/**
	 * @return Gets the area offset.
	 */
	public Point getAreaOffset()
	{
		return _areaOffset;
	}

	/**
	 * @return The current line select tool.
	 */
	public LineSelectTool getLineSelectTool()
	{
		return _lineSelectTool;
	}

	/**
	 * @return The panel where all controls are on.
	 */
	public AbstractContainer getBackgroundPanel()
	{
		Contract.EnsureNotNull(_panel);

		return _panel;
	}

	/**
	 * @return The value of the {@code _lineSelectToolEnabled} flag.
	 */
	public boolean isLineSelectToolEnabled()
	{
		return _lineSelectToolEnabled;
	}
}
