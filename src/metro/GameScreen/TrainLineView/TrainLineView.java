package metro.GameScreen.TrainLineView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.GameScreen.TrainInteractionTool;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Lines.TrainLineOverseer;
import metro.WindowControls.Button;
import metro.WindowControls.ColorBar;
import metro.WindowControls.InputField;
import metro.WindowControls.Label;
import metro.WindowControls.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The TrainLineView is a dialog to manage the train lines in METRO. It allows the user to create, modify and remove lines.
 * 
 * @author hauke
 *
 */
public class TrainLineView extends GameScreen implements TrainInteractionTool
{
	private int _windowWidth;
	private List _lineList;
	private Button _createLineButton, // to create a new train line
		_editLineButton, // to change railway nodes of train line
		_removeLineButton, // to remove a train line
		_saveButton; // to save settings/changes
	private boolean _visible, // true: TrainLineView will be displayed
		_lineSelectToolEnabled, // if enabled, the user can select nodes
		_isClosed,
		_editMode; // true when user edits a line
	private Point _areaOffset; // to get the (0,0)-coordinate very easy
	private Point2D _mapOffset;
	private TrainLineSelectTool _lineSelectTool;
	private InputField _lineNameField;
	private Label _lineNameFieldLabel,
		_messageLabel;
	private ColorBar _colorBar;

	/**
	 * Creates a new TrainLineView.
	 * 
	 * @param mapOffset The offset of the map (for correct mouse clicks)
	 */
	public TrainLineView(Point2D mapOffset)
	{
		_visible = true;
		_lineSelectToolEnabled = false;
		_isClosed = false;
		_windowWidth = 400;
		_mapOffset = mapOffset;
		_lineSelectTool = new TrainLineSelectTool();

		_areaOffset = new Point(METRO.__SCREEN_SIZE.width - _windowWidth, 0);
		_lineList = new List(new Rectangle(_areaOffset.x + 20, 130, _windowWidth - 40, 300),
			null, null, true);
		_createLineButton = new Button(new Rectangle(_areaOffset.x + 20, 450, (_windowWidth - 40) / 3 - 10, 20), "Create line");
		_editLineButton = new Button(new Rectangle(_areaOffset.x + 12 + (_windowWidth / 3), 450, (_windowWidth - 40) / 3 - 10, 20), "Edit line");
		_removeLineButton = new Button(new Rectangle(_areaOffset.x + 4 + (_windowWidth / 3) * 2, 450, (_windowWidth - 40) / 3 - 10, 20), "Remove line");

		_lineNameField = new InputField(new Rectangle(_areaOffset.x + 95, 490, _windowWidth - 175, 20));
		_lineNameField.setState(false);
		_lineNameFieldLabel = new Label("Line Name", new Point(_areaOffset.x + 20, 493));
		_lineNameFieldLabel.setState(false);
		_colorBar = new ColorBar(new Rectangle(_areaOffset.x + 20, 520, _windowWidth - 70, 20), null, 0.9f, 0.8f);
		_colorBar.setState(false);
		_saveButton = new Button(new Rectangle(_areaOffset.x + (_windowWidth / 2) - 75, METRO.__SCREEN_SIZE.height - _areaOffset.y - 60, 150, 20), "Save");
		_saveButton.setState(false);

		_messageLabel = new Label("", new Point(_areaOffset.x + 20, METRO.__SCREEN_SIZE.height - _areaOffset.y - 30));
		_messageLabel.setColor(METRO.__metroRed);
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
		if(!_visible) resetControls();
	}

	/**
	 * Checks if a button has clicked and executes necessary methods.
	 * 
	 * @param screenX The x-position on the screen
	 * @param screenY The y-position on the screen
	 * @param mouseButton The number of the button like Buttons.LEFT
	 * @return True when one of the buttons has been clicked.
	 */
	private boolean checkButtonClicks(int screenX, int screenY, int mouseButton)
	{
		//TODO: ActionObserver implementation
		boolean buttonClicked = false;

		if(_createLineButton.isPressed(screenX, screenY))
		{
			createLineButton_action();
			buttonClicked = true;
		}
		else if(_editLineButton.isPressed(screenX, screenY))
		{
			editLineButton_action();
			buttonClicked = true;
		}
		else if(_removeLineButton.isPressed(screenX, screenY))
		{
			removeLineButton_action();
			buttonClicked = true;
		}
		else if(_saveButton.isPressed(screenX, screenY))
		{
			saveButton_action();
			buttonClicked = true;
		}

		return buttonClicked;
	}

	/**
	 * Check if the color bar has been clicked and sets message and the color of the select tool.
	 * 
	 * @param screenX The x-position on the screen
	 * @param screenY The y-position on the screen
	 * @param mouseButton The number of the button like Buttons.LEFT
	 * @return True when the color bar has been clicked.
	 */
	private boolean checkColorBarClick(int screenX, int screenY, int mouseButton)
	{
		boolean barClicked = false;
		if(_colorBar.clickOnControlElement())
		{
			String msg = _lineSelectTool.setColor(_colorBar.getClickedColor());
			if(!msg.equals("")) _messageLabel.setText(msg);
			barClicked = true;
		}
		return barClicked;
	}

	/**
	 * Checks if the message label has been clicked and removes its content if needed.
	 * 
	 * @param screenX The x-position on the screen
	 * @param screenY The y-position on the screen
	 * @param mouseButton The number of the button like Buttons.LEFT
	 * @return True when label has been clicked.
	 */
	private boolean checkMessageLabelClick(int screenX, int screenY, int mouseButton)
	{
		boolean labelClicked = false;
		if(_messageLabel.clickOnControlElement())
		{
			_messageLabel.setText("");
			labelClicked = true;
		}
		return labelClicked;
	}

	/**
	 * Will be executed after a click on the "Create Line" button. This will unlock/lock all necessary controls.
	 */
	private void createLineButton_action()
	{
		if(_lineSelectToolEnabled) return;

		_lineSelectTool = new TrainLineSelectTool(); // create clean select tool
		if(_colorBar.getClickedColor() != null) _lineSelectTool.setColor(_colorBar.getClickedColor());
		_lineSelectToolEnabled = true;
		resetControls();
	}

	/**
	 * Creates a new train line and saves it correctly. If something went wrong, an error message will be shown.
	 */
	private void saveButton_action()
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
			TrainLineOverseer.addLine(line);
			if(!_editMode) _lineList.addElement(_lineNameField.getText());
			_lineSelectToolEnabled = false;
			resetControls();
		}
	}

	/**
	 * Removes the whole selected line.
	 */
	private void removeLineButton_action()
	{
		TrainLineOverseer.removeLine(_lineList.getText(_lineList.getSelected()));
		_lineList.remove(_lineList.getSelected());

	}

	/**
	 * Fills controls with information about line so that the player can edit it.
	 */
	private void editLineButton_action()
	{
		if(_lineList.getSelected() == -1) return;
		Color color = TrainLineOverseer.getColor(_lineList.getText(_lineList.getSelected()));
		TrainLine line = TrainLineOverseer.getLine(_lineList.getText(_lineList.getSelected()));

		if(color == null || line == null)
		{
			_messageLabel.setText("An erorr occured while reading data :(");
			return;
		}

		_editMode = true;

		_lineNameField.setState(true);
		_lineNameField.setText(_lineList.getText(_lineList.getSelected()));
		_colorBar.setState(true);
		_colorBar.setValue(color);
		_createLineButton.setState(false);
		_editLineButton.setState(false);
		_removeLineButton.setState(false);
		_saveButton.setState(true);

		_lineSelectToolEnabled = true;
		_lineSelectTool.setLine(line);
		_lineSelectTool.setState(true);
	}

	/**
	 * Resets all controls to their default values.
	 */
	private void resetControls()
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

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(!_visible || _lineList.clickOnControlElement()) return;

		// set lineNameField as inactive when mouse NOT clicked on it
		if(_lineNameField.clickOnControlElement()) _lineNameField.select();
		else _lineNameField.disselect();

		boolean controlClicked = false;

		controlClicked |= checkButtonClicks(screenX, screenY, mouseButton);
		controlClicked |= checkColorBarClick(screenX, screenY, mouseButton);
		controlClicked |= checkMessageLabelClick(screenX, screenY, mouseButton);
		// "create new train"/"finish" line has been pressed

		// when no control was clicked and mouse out of TrainLineView, forward it to the SelectTool
		if(!controlClicked && screenX <= METRO.__SCREEN_SIZE.width - _windowWidth && _lineSelectToolEnabled)
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
	public void draw(SpriteBatch sp, Point2D offset)
	{
	}

	@Override
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		if(!_visible) return;
		if(_lineSelectTool != null && _lineSelectToolEnabled && screenX <= METRO.__SCREEN_SIZE.width - _windowWidth)
		{
			_lineSelectTool.leftClick(screenX, screenY, _mapOffset); // add node to list
		}
	}

	@Override
	public void rightClick(int screenX, int screenY, Point2D offset)
	{
		if(!_visible) return;
		if(_lineSelectToolEnabled)
		{
			_lineSelectTool.rightClick(screenX, screenY, offset); // if enables, than do something with it
			_lineSelectToolEnabled = !_lineSelectTool.isClosed(); // after right click
			if(!_lineSelectToolEnabled) // tool closed itself
			{
				TrainLine line = _lineSelectTool.getTrainLine();
				TrainLineOverseer.removeLine(line);
				_lineSelectTool = null;
				resetControls();
			}
		}
		else
		{
			_isClosed = true; // of not, close the TrainLineView
			_visible = false;
		}
	}

	@Override
	public boolean isClosed()
	{
		return _isClosed;
	}
}
