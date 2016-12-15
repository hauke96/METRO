package metro.GameUI.MainView.TrainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
import metro.Common.Technical.Contract;
import metro.GameUI.Screen.GameScreen;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Trains.Train;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.Container.Panel;

/**
 * The dialog to buy, sell and manage trains.
 * 
 * @author hauke
 *
 */
public class TrainView extends GameScreen
{
	private int _windowWidth;
	private boolean _isActive;
	private Point _areaOffset; // to get the (0,0)-coordinate very easy
	private TrainViewMain _trainViewMain;
	private TrainViewBuy _trainViewBuy;
	private Panel _panel;

	/**
	 * Creates a new TrainLineView.
	 */
	public TrainView()
	{
		_windowWidth = GameState.getInstance().getToolViewWidth();
		_areaOffset = new Point(METRO.__SCREEN_SIZE.width - _windowWidth, 40);
		_trainViewMain = new TrainViewMain(getAreaOffset(), _windowWidth);
		_trainViewBuy = new TrainViewBuy(getAreaOffset(), _windowWidth);

		_panel = new Panel(new Rectangle(_areaOffset.x, _areaOffset.y, _windowWidth, METRO.__SCREEN_SIZE.height));
		_panel.setDrawBorder(true);
		_panel.add(_trainViewMain.getPanel());
		_panel.add(_trainViewBuy.getPanel());

		registerObserver();
	}

	private void registerObserver()
	{
		_trainViewBuy.getBuyButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				TrainManagementService trainManagementService = TrainManagementService.getInstance();
				if(_trainViewMain.getLineList().getSelected() == -1)
				{
					_trainViewBuy.getMessageLabel().setText("Please select the line this train should drive on.");
				}
				else if(_trainViewBuy.getTrainList().getSelected() == -1)
				{
					_trainViewBuy.getMessageLabel().setText("Please select the train model you want to buy.");
				}
				else if(METRO.__gameState.getMoney() < trainManagementService.getTemplateTrain(_trainViewBuy.getTrainList().getText()).getPrice())
				{
					_trainViewBuy.getMessageLabel().setText("You have not enough money.");
				}
				else
				{
					Train train = new Train(trainManagementService.getTemplateTrain(_trainViewBuy.getTrainList().getText()));
					train.setLine(trainManagementService.getLine(_trainViewMain.getLineList().getText()));
					trainManagementService.addTrain(train);
					_trainViewBuy.getMessageLabel().setText("");

					// find next number for train
					String trainName = train.getName();
					int minNumber = 0;
					while(minNumber < 1000 && trainManagementService.getTrainByName(trainName + " (" + minNumber + ")") != null)
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
				_trainViewBuy.setTrain(entry); // just sets the entry to view the values of the train
			}
		});
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		drawTitleBox();
		_trainViewMain.updateGameScreen(g);
		_trainViewBuy.updateGameScreen(g);
	}

	/**
	 * Draws the title bar with the Label and box.
	 */
	private void drawTitleBox()
	{
		int length = Draw.getStringSize("Control Management").width;
		Draw.Rect(METRO.__SCREEN_SIZE.width - _windowWidth + 20, getAreaOffset().y + 15, _windowWidth - 40, 60);
		Color c = new Color((int)(METRO.__metroBlue.getRed() * 1.8f),
			(int)(METRO.__metroBlue.getGreen() * 1.3f),
			255); // lighter metro-blue
		Draw.setColor(c);
		Draw.Rect(METRO.__SCREEN_SIZE.width - _windowWidth + 22, getAreaOffset().y + 17, _windowWidth - 44, 56);

		// Take "METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2" to center the label
		length = Draw.getStringSize("METRO trains").width;
		Draw.setColor(METRO.__metroRed);
		Draw.String("METRO trains", METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2, getAreaOffset().y + 25);
		Draw.Line(METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2 - 5, getAreaOffset().y + 40,
			METRO.__SCREEN_SIZE.width - (_windowWidth - length) / 2 + 5, getAreaOffset().y + 40);

		length = Draw.getStringSize("Control Management").width;
		Draw.String("Control Management", METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2, getAreaOffset().y + 50);
		Draw.Line(METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2 - 5, getAreaOffset().y + 65,
			METRO.__SCREEN_SIZE.width - (_windowWidth - length) / 2 + 5, getAreaOffset().y + 65);
	}

	/**
	 * Sets the visibility of the TrainLineView. The visibility will also effect the usability (e.g. mouse click).
	 * 
	 * @param visible True will make this visible, false will make it invisible.
	 */
	public void setVisibility(boolean visible)
	{
		_isActive = visible;
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(mouseButton == Buttons.RIGHT)
		{
			_isActive = false;
			setChanged();
			notifyObservers(); // notify about close
		}
	}

	@Override
	public boolean isActive()
	{
		return _isActive;
	}

	@Override
	public void close()
	{
		_trainViewBuy.close();
		_trainViewMain.close();
		_panel.close();
		super.close();
	}

	@Override
	public boolean isHovered()
	{
		return METRO.__mousePosition.x > getAreaOffset().x; // we don't need to check y-coord. because we use the full screen height.
	}

	/**
	 * @return Gets the area offset.
	 */
	public Point getAreaOffset()
	{
		return _areaOffset;
	}

	/**
	 * @return The background panel all controls are on.
	 */
	public Panel getBackgroundPanel()
	{
		Contract.EnsureNotNull(_panel);
		return _panel;
	}
}
