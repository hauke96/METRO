package metro.GameScreen.LineView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.GameScreen.MainView.MainView;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Lines.TrainLineOverseer;
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
public class LineView extends GameScreen
{
	private int _windowWidth;
	private List _lineList;
	private Button _createLineButton, // to create a new train line
		_editLineButton, // to change railway nodes of train line
		_removeLineButton, // to remove a train line
		_saveButton; // to save settings/changes
	private boolean _visible, // true: TrainLineView will be displayed
		_lineSelectToolEnabled, // if enabled, the user can select nodes
		_editMode, // true when user edits a line
		_controlClicked;
	private Point _areaOffset; // to get the (0,0)-coordinate very easy
	private LineSelectTool _lineSelectTool;
	private InputField _lineNameField;
	private Label _lineNameFieldLabel,
		_messageLabel;
	private String _oldLineName; // when the user edits a line name, the old name of it has to be saved to correctly update the line list
	private ColorBar _colorBar;

	/**
	 * Creates a new TrainLineView.
	 */
	public LineView()
	{
		_visible = true;
		_lineSelectToolEnabled = false;
		_controlClicked = false;
		_windowWidth = 400;
		_lineSelectTool = new LineSelectTool();

		_areaOffset = new Point(METRO.__SCREEN_SIZE.width - _windowWidth, 0);
		_lineList = new List(new Rectangle(_areaOffset.x + 20, 130, _windowWidth - 40, 300),
			null, null, true);
		registerControl(_lineList);
		fillLineList();

		_createLineButton = new Button(new Rectangle(_areaOffset.x + 20, 450, (_windowWidth - 40) / 3 - 10, 20), "Create line");
		registerControl(_createLineButton);
		_editLineButton = new Button(new Rectangle(_areaOffset.x + 12 + (_windowWidth / 3), 450, (_windowWidth - 40) / 3 - 10, 20), "Edit line");
		registerControl(_editLineButton);
		_removeLineButton = new Button(new Rectangle(_areaOffset.x + 4 + (_windowWidth / 3) * 2, 450, (_windowWidth - 40) / 3 - 10, 20), "Remove line");
		registerControl(_removeLineButton);

		_lineNameField = new InputField(new Rectangle(_areaOffset.x + 95, 490, _windowWidth - 175, 20));
		_lineNameField.setState(false);
		registerControl(_lineNameField);
		_lineNameFieldLabel = new Label("Line Name", new Point(_areaOffset.x + 20, 493));
		_lineNameFieldLabel.setState(false);
		registerControl(_lineNameFieldLabel);
		_colorBar = new ColorBar(new Rectangle(_areaOffset.x + 20, 520, _windowWidth - 70, 20), null, 0.9f, 0.8f);
		_colorBar.setState(false);
		registerControl(_colorBar);
		_saveButton = new Button(new Rectangle(_areaOffset.x + (_windowWidth / 2) - 75, METRO.__SCREEN_SIZE.height - _areaOffset.y - 60, 150, 20), "Save");
		_saveButton.setState(false);
		registerControl(_saveButton);

		_messageLabel = new Label("", new Point(_areaOffset.x + 20, METRO.__SCREEN_SIZE.height - _areaOffset.y - 30));
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
		for(TrainLine line : METRO.__gameState.getLines())
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

				_lineSelectTool = new LineSelectTool(); // create clean select tool
				if(_colorBar.getClickedColor() != null) _lineSelectTool.setColor(_colorBar.getClickedColor());
				_lineSelectToolEnabled = true;
				_controlClicked = true;
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
				if(_lineList.getSelected() == -1) return;
				
				_oldLineName = _lineList.getText();
				
				Color color = TrainLineOverseer.getColor(_oldLineName);
				TrainLine line = TrainLineOverseer.getLine(_oldLineName);

				if(color == null || line == null)
				{
					_messageLabel.setText("An erorr occured while reading data :(");
					return;
				}

				_editMode = true;

				_lineNameField.setState(true);
				_lineNameField.setText(_oldLineName);
				_lineNameFieldLabel.setState(true);
				_colorBar.setState(true);
				_colorBar.setValue(color);
				_createLineButton.setState(false);
				_editLineButton.setState(true);
				_removeLineButton.setState(true);
				_saveButton.setState(true);

				_lineSelectTool = new LineSelectTool(line); // create clean select tool
				_lineSelectToolEnabled = true;
				
				_lineList.setState(false);

				_controlClicked = true;
				
				METRO.__gameState.getLines().remove(line);
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
				if(_lineList.getSelected() < 0 || _lineList.getSelected() >= METRO.__gameState.getLines().size()) return; // out of bounds

				TrainLineOverseer.removeLine(_lineList.getText());
				METRO.__gameState.getLines().remove(_lineList.getSelected());
				_lineList.remove(_lineList.getSelected());
				_lineSelectToolEnabled = false;
				_controlClicked = true;
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
				if(!_lineSelectToolEnabled) return;

				TrainLine line = _lineSelectTool.getTrainLine();

				// if something went wrong:
				if(_lineList.contains(_lineNameField.getText()) && !_editMode) // when edit mode, then don't change the list
				{
					_messageLabel.setText("The line \"" + _lineNameField.getText() + "\" already exists.");
				}
				else if(_lineNameField.getText().equals(""))
				{
					_messageLabel.setText("Please enter a name!");
				}
				else if(!line.isValid())
				{
					_messageLabel.setText("Train Line is not valid!");
				}
				else if(line.isValid()) // only change something when line and name are valid
				{
					_lineList.remove(_lineList.getIndex(_oldLineName));
					line.setName(_lineNameField.getText());
					TrainLineOverseer.addLine(line);
					METRO.__gameState.getLines().add(line);
					_lineList.addElement(_lineNameField.getText());
					_lineSelectToolEnabled = false;
					
					reset();
					_lineList.setState(true);
				}
				_controlClicked = true;
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
				String msg = _lineSelectTool.setColor(_colorBar.getClickedColor());
				if(!msg.equals("")) _messageLabel.setText(msg);
				_controlClicked = true;
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
				_controlClicked = true;
			}
		});
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		if(!_visible) return;
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
			Fill.Rect(new Rectangle(_areaOffset.x + _windowWidth - 40, 520, 20, 20));
		}
		Draw.setColor(Color.darkGray);
		Draw.Rect(new Rectangle(_areaOffset.x + _windowWidth - 40, 520, 20, 20));
	}

	/**
	 * Sets the visibility of the TrainLineView. The visibility will also effect the usability (e.g. mouse click).
	 * 
	 * @param visible True will make this visible, false will make it invisible.
	 */
	public void setVisibility(boolean visible)
	{
		_visible = visible;
		if(_lineSelectToolEnabled)
		{
			TrainLineOverseer.removeLine(_lineSelectTool.getTrainLine());
			_lineSelectToolEnabled = false;
		}
		if(!_visible) reset();
	}

	/**
	 * Checks if a control has been clicked since last call.
	 * 
	 * @return True when one of the control has been clicked since last call.
	 */
	private boolean hasControlClicked()
	{
		return _controlClicked;
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(!_visible) return;
		if(mouseButton == Buttons.RIGHT)
		{
			// LineSelectTool-Stuff:
			if(_lineSelectToolEnabled)
			{
				_lineSelectTool.rightClick(screenX, screenY, MainView._mapOffset); // if enables, than do something with the select tool
				_lineSelectToolEnabled = !_lineSelectTool.isActive(); // turn activation after right click
				if(!_lineSelectToolEnabled) // tool closes itself
				{
					TrainLine line = _lineSelectTool.getTrainLine();
					TrainLineOverseer.removeLine(line);
					reset();
					_lineSelectTool = null;
				}
			}
			else // if select tool is not enabled, hide/close the whole line view
			{
				_visible = false;
			}
		}
		else if(mouseButton == Buttons.LEFT)
		{
			// if select tool exists and mouse is in the area of the select tool, forward click to tool
			if(_lineSelectTool != null && _lineSelectToolEnabled && screenX <= METRO.__SCREEN_SIZE.width - _windowWidth)
			{
				_lineSelectTool.leftClick(screenX, screenY, MainView._mapOffset); // add node to list
			}
		}

		// when no control was clicked and mouse out of TrainLineView, get line the new from the select tool
		if(!hasControlClicked() && screenX <= METRO.__SCREEN_SIZE.width - _windowWidth && _lineSelectToolEnabled)
		{
			// the list of nodes in the selectTool has been updated, so get the new line and save it in the overseer
			TrainLine line = _lineSelectTool.getTrainLine();
			TrainLineOverseer.addLine(line); // this will only change something when line is valid
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
		if(!_visible) return;
		_lineList.mouseScrolled(amount);
		_colorBar.mouseScrolled(amount);
		String msg = _lineSelectTool.setColor(_colorBar.getClickedColor());
		if(!msg.equals("")) _messageLabel.setText(msg);
	}

	@Override
	public boolean isActive()
	{
		return _visible;
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

		_lineSelectTool.setState(false);
	}
}
