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
		_lineNameFieldButton; // to edit train line name
	private boolean _visible, // true: TrainLineView will be displayed
		_lineSelectToolEnabled, // if enabled, the user can select nodes
		_isClosed;
	private Point _areaOffset; // to get the (0,0)-coordinate very easy
	private Point2D _mapOffset;
	private TrainLineSelectTool _lineSelectTool;
	private InputField _trainLineNameField;
	private Label _trainLineNameFieldLabel;

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

		_lineNameFieldButton = new Button(new Rectangle(_areaOffset.x + _windowWidth - 70, 490, 50, 20), "OK");
		_lineNameFieldButton.setState(false);
		_trainLineNameField = new InputField(new Rectangle(_areaOffset.x + 95, 490, _windowWidth - 175, 20));
		_trainLineNameField.setState(false);
		_trainLineNameFieldLabel = new Label("Line Name", new Point(_areaOffset.x + 20, 493));
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

		// Draw Color bar
		for(int i = 0; i < 255; i++)
		{
			Color color = Color.getHSBColor((float)i / (float)255, 0.61f, 1.0f);
			Draw.setColor(color);
			Draw.Line(i + 300, 0, i + 300, 20);
		}
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
		_trainLineNameFieldLabel.draw();
		_trainLineNameField.draw();
		_lineNameFieldButton.draw();
	}

	/**
	 * Sets the visibility of the TrainLineView. The visibility will also effect the usability (e.g. mouse click).
	 * 
	 * @param visible True will make this visible, false will make it invisible.
	 */
	public void setVisibility(boolean visible)
	{
		_visible = visible;
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(!_visible || _lineList.clickOnControlElement()) return;

		// "create new train"/"finish" line has been pressed
		if(_createLineButton.isPressed(screenX, screenY)) createLineButton_action();
		// TODO: If clause for edit button
		// TODO: If clause for remove button
		// TODO: If clause for ok button

		// when no control was clicked and mouse out of T.L.View, forward it to the SelectTool
		if(screenX <= METRO.__SCREEN_SIZE.width - _windowWidth && _lineSelectToolEnabled)
		{
			// the list of nodes in the selectTool has been updated, so get the new line and save it in the overseer
			TrainLine line = _lineSelectTool.getTrainLine();
			TrainLineOverseer.addLine(line); // only change something when line is valid
		}
	}

	/**
	 * Will be executed after a click on the "Create Line" / "Finish" button
	 */
	private void createLineButton_action()
	{
		if(!_lineSelectToolEnabled)
		{
			_lineSelectTool = new TrainLineSelectTool(); // create clean select tool
		}
		else
		{
			TrainLine line = _lineSelectTool.getTrainLine();
			if(line != null) // only change something when line is valid
			{
				TrainLineOverseer.addLine(line);
				_lineList.addElement(line.getName());
			}
		}

		_lineSelectToolEnabled = !_lineSelectToolEnabled;
		resetControls();
	}

	private void resetControls()
	{
		if(_lineSelectToolEnabled) _createLineButton.setText("Finish");
		else _createLineButton.setText("Create Line");
		// switch controls on/off depending on the enable state of the selector tool
		_editLineButton.setState(!_lineSelectToolEnabled);
		_removeLineButton.setState(!_lineSelectToolEnabled);
		_trainLineNameField.setState(_lineSelectToolEnabled);
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
		if(!_visible) return;
		_lineList.mouseScrolled(amount);
	}

	@Override
	public void draw(SpriteBatch sp, Point2D offset)
	{
	}

	@Override
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		if(_lineSelectTool != null) _lineSelectTool.leftClick(screenX, screenY, _mapOffset); // add node to list
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
