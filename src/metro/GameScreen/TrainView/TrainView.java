package metro.GameScreen.TrainView;

import java.awt.Color;
import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.TrainManagement.Lines.TrainLineOverseer;
import metro.TrainManagement.Trains.Train;
import metro.WindowControls.ActionObserver;

/**
 * The dialog to buy, sell and manage trains.
 * 
 * @author hauke
 *
 */
public class TrainView extends GameScreen// implements TrainInteractionTool
{
	private int _windowWidth;
	private boolean _visible;
	private Point _areaOffset; // to get the (0,0)-coordinate very easy
	private TrainViewMain _trainViewMain;
	private TrainViewBuy _trainViewBuy;

	/**
	 * Creates a new TrainLineView.
	 */
	public TrainView()
	{
		_windowWidth = 400;
		_areaOffset = new Point(METRO.__SCREEN_SIZE.width - _windowWidth, 0);
		_trainViewMain = new TrainViewMain(_areaOffset, _windowWidth);
		_trainViewBuy = new TrainViewBuy(_areaOffset, _windowWidth);

		registerObserver();
	}

	private void registerObserver()
	{
		_trainViewBuy.getBuyButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				if(_trainViewMain.getLineList().getSelected() == -1)
				{
					_trainViewBuy.getMessageLabel().setText("Please select the line this train should drive on.");
				}
				else if(_trainViewBuy.getTrainList().getSelected() == -1)
				{
					_trainViewBuy.getMessageLabel().setText("Please select the train model you want to buy.");
				}
				else
				{
					Train train = new Train(METRO.__gameState.getTemplateTrain(_trainViewBuy.getTrainList().getText()));
					train.setLine(TrainLineOverseer.getLine(_trainViewMain.getLineList().getText()));
					METRO.__gameState.addTrain(train);
					_trainViewBuy.getMessageLabel().setText("");

					// find next number for train
					String trainName = train.getName();
					int minNumber = 0;
					while(minNumber < 1000 && METRO.__gameState.getTrainByName(trainName + " (" + minNumber + ")") != null)
					{
						++minNumber;
					}

					if(minNumber == 1000) // maximum number of trains from a specific type
					{
						_trainViewBuy.getMessageLabel().setText("You have to much trains of this type!");
					}
					else
					{
						train.setName(train.getName() + " (" + minNumber + ")");
						_trainViewMain.getTrainList().addElement(train.getName());
					}
				}
			}
		});

		_trainViewMain.getTrainList().register(new ActionObserver()
		{
			@Override
			public void selectionChanged(String entry)
			{
				_trainViewBuy.setTrain(entry);
			}
		});
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		drawBackground();
		drawTitleBox();
		_trainViewMain.updateGameScreen(g);
		_trainViewBuy.updateGameScreen(g);
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
		length = Draw.getStringSize("METRO trains").width;
		Draw.setColor(METRO.__metroRed);
		Draw.String("METRO trains", METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2, 25);
		Draw.Line(METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2 - 5, 40,
			METRO.__SCREEN_SIZE.width - (_windowWidth - length) / 2 + 5, 40);

		length = Draw.getStringSize("Control Management").width;
		Draw.String("Control Management", METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2, 50);
		Draw.Line(METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2 - 5, 65,
			METRO.__SCREEN_SIZE.width - (_windowWidth - length) / 2 + 5, 65);
	}

	/**
	 * Sets the visibility of the TrainLineView. The visibility will also effect the usability (e.g. mouse click).
	 * 
	 * @param visible True will make this visible, false will make it invisible.
	 */
	public void setVisibility(boolean visible)
	{
		_visible = visible;
		if(!_visible)
		{
			_trainViewMain.reset();
			_trainViewBuy.reset();
		}
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
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		// TODO close this screen by right click via the observer pattern
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public boolean isActive()
	{
		return _visible;
	}

	@Override
	public void reset()
	{
	}

	@Override
	public void close()
	{
		_trainViewBuy.close();
		_trainViewMain.close();
		super.close();
	}
}
