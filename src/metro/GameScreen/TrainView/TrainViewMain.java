package metro.GameScreen.TrainView;

import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.TrainManagement.Lines.TrainLineOverseer;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;
import metro.WindowControls.List;

public class TrainViewMain extends GameScreen
{
	private int _windowWidth;
	private List _trainList,
		_lineList;
	private Button _moveTrainButton, // to change a train
		_sellTrainButton; // to remove a train
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
		
		_lineList = new List(new Rectangle(_areaOffset.x + 20, 130, _windowWidth - 300, 250),
			null, null, true);
		_trainList = new List(new Rectangle(_areaOffset.x + 121, 130, _windowWidth - 141, 250),
			null, null, true);
		
		_moveTrainButton = new Button(new Rectangle(_areaOffset.x + 12 + (_windowWidth / 3), 400, (_windowWidth - 40) / 3 - 10, 20), "Move train");
		_sellTrainButton = new Button(new Rectangle(_areaOffset.x + 4 + (_windowWidth / 3) * 2, 400, (_windowWidth - 40) / 3 - 10, 20), "Sell train");

		addButtonObserver();
	}

	/**
	 * Creates all needed observers for the buttons.
	 */
	private void addButtonObserver()
	{
		_moveTrainButton.register(new ActionObserver()
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
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		drawListBox();
		drawButtons();
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
		_moveTrainButton.draw();
		_sellTrainButton.draw();
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

		_moveTrainButton.setState(false);
		_sellTrainButton.setState(false);
	}

	@Override
	public void reset()
	{
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
