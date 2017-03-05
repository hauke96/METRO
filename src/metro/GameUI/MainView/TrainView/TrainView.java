package metro.GameUI.MainView.TrainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.Common.Technical.Contract;
import metro.GameUI.Common.ToolView;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Trains.Train;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Container.Panel;
import metro.UI.Renderable.Controls.Canvas;

/**
 * The dialog to buy, sell and manage trains.
 * 
 * @author hauke
 *
 */
public class TrainView extends ToolView
{
	private int _windowWidth;
	private Point _areaOffset; // to get the (0,0)-coordinate very easy
	private TrainViewMain _trainViewMain;
	private TrainViewBuy _trainViewBuy;
	private Panel _panel;
	private TrainManagementService _trainManagementService;

	/**
	 * Creates a new TrainLineView.
	 * @param toolWidth The width of this tool. 
	 * @param trainManagementService The train management service.
	 */
	public TrainView(int toolWidth, TrainManagementService trainManagementService)
	{
		_windowWidth = toolWidth;
		_trainManagementService = trainManagementService;
		
		_areaOffset = new Point(METRO.__SCREEN_SIZE.width - _windowWidth, 40);
		_trainViewMain = new TrainViewMain(getAreaOffset(), _windowWidth, _trainManagementService);
		_trainViewBuy = new TrainViewBuy(getAreaOffset(), _windowWidth, _trainManagementService);

		_panel = new Panel(new Rectangle(_areaOffset.x, _areaOffset.y, _windowWidth, METRO.__SCREEN_SIZE.height));
		_panel.setDrawBorder(true);

		Canvas canvas = new Canvas(new Point(_panel.getPosition().x, _panel.getPosition().y + 20));
		canvas.setPainter(() -> draw());

		_panel.add(_trainViewMain.getPanel());
		_panel.add(_trainViewBuy.getPanel());
		_panel.add(canvas);

		registerObserver();
	}

	private void registerObserver()
	{
		_trainViewBuy.getBuyButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				if(_trainViewMain.getLineList().getSelectedIndex() == -1)
				{
					_trainViewBuy.getMessageLabel().setText("Please select the line this train should drive on.");
				}
				else if(_trainViewBuy.getTrainList().getSelectedIndex() == -1)
				{
					_trainViewBuy.getMessageLabel().setText("Please select the train model you want to buy.");
				}
				else if(METRO.__gameState.getMoney() < _trainManagementService.getTemplateTrain(_trainViewBuy.getTrainList().getSelectedText()).getPrice())
				{
					_trainViewBuy.getMessageLabel().setText("You have not enough money.");
				}
				else
				{
					System.out.println(_trainViewMain.getLineList().getSelectedText());
					Train train = new Train(_trainManagementService.getTemplateTrain(_trainViewBuy.getTrainList().getSelectedText()));
					train.setLine(_trainManagementService.getLine(_trainViewMain.getLineList().getSelectedText()));
					_trainManagementService.addTrain(train);
					_trainViewBuy.getMessageLabel().setText("");

					// find next number for train
					String trainName = train.getName();
					int minNumber = 0;
					while(minNumber < 1000 && _trainManagementService.getTrainByName(trainName + " (" + minNumber + ")") != null)
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

	private void draw()
	{
		drawTitleBox();
		_trainViewBuy.updateGameScreen();
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

	@Override
	public boolean mouseClicked(int screenX, int screenY, int mouseButton)
	{
		return false;
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
	public AbstractContainer getBackgroundPanel()
	{
		Contract.EnsureNotNull(_panel);

		return _panel;
	}
}
