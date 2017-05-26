package metro.GameUI.MainView.TrainView;

import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.GameUI.Common.ToolView;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Trains.Train;
import metro.TrainManagement.Trains.TrainLine;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.Container.Panel;
import metro.UI.Renderable.Controls.Button;
import metro.UI.Renderable.Controls.Label;
import metro.UI.Renderable.Controls.List;

/**
 * This dialog part shows the lines and the trains attached to them.
 * The player can't buy a train with this dialog but can sell one.
 * 
 * @author hauke
 *
 */
public class TrainViewMain extends ToolView
{
	private int						_windowWidth;
	private List					_trainList,
	        _lineList;
	private Button					_moveTrainButton,						// to change a train
	        _sellTrainButton;												// to remove a train
	private Point					_areaOffset;							// to get the (0,0)-coordinate very easy
	private String					_movedTrain;
	private TrainManagementService	_trainManagementService;
	private Panel					_panel;
	private Label					_yourLinesLabel,
	        _yourTrainsLabel;
	
	/**
	 * Creates a new main view for the train screen.
	 * 
	 * @param areaOffset
	 *            The offset of the screen area.
	 * @param windowWidth
	 *            The window width.
	 * @param trainManagementService
	 *            The train management service.
	 */
	public TrainViewMain(Point areaOffset, int windowWidth, TrainManagementService trainManagementService)
	{
		_windowWidth = windowWidth;
		_areaOffset = areaOffset;
		_trainManagementService = trainManagementService;
		
		// TODO WTF a train as String?
		_movedTrain = "";
		
		_panel = new Panel(new Rectangle());
		_panel.setDrawBorder(false);
		
		_lineList = new List(new Rectangle(_areaOffset.x + 20, _areaOffset.y + 130, _windowWidth - 300, 230), true);
		_lineList.register(new ActionObserver()
		{
			@Override
			public void selectionChanged(String entry)
			{
				if (isInMoveMode())
				{
					TrainLine selectedLine = _trainManagementService.getLine(entry);
					Train selectedTrain = _trainManagementService.getTrainByName(_movedTrain);
					
					selectedTrain.setLine(selectedLine);
					stopMoveMode();
				}
				
				_trainList.clear();
				TrainLine line = _trainManagementService.getLine(_lineList.getSelectedText());
				for (Train train : _trainManagementService.getTrains())
				{
					if (train.getLine().equals(line)) _trainList.addElement(train.getName());
				}
			}
		});
		
		_trainList = new List(new Rectangle(_areaOffset.x + 121, _areaOffset.y + 130, _windowWidth - 141, 230), true);
		
		fillLineList();
		
		_yourLinesLabel = new Label("Your Lines:", new Point(METRO.__SCREEN_SIZE.width - _windowWidth + 25, _areaOffset.y + 110));
		_yourLinesLabel.setColor(METRO.__metroRed);
		_yourLinesLabel.setUnderline(true);
		_yourTrainsLabel = new Label("Your Trains:", new Point(METRO.__SCREEN_SIZE.width - _windowWidth + 125, _areaOffset.y + 110));
		_yourTrainsLabel.setColor(METRO.__metroRed);
		_yourTrainsLabel.setUnderline(true);
		
		_moveTrainButton = new Button(new Rectangle(_areaOffset.x + 12 + (_windowWidth / 3), _areaOffset.y + 380, (_windowWidth - 40) / 3 - 10, 20), "Move train");
		_sellTrainButton = new Button(new Rectangle(_areaOffset.x + 4 + (_windowWidth / 3) * 2, _areaOffset.y + 380, (_windowWidth - 40) / 3 - 10, 20), "Sell train");
		
		addButtonObserver();
		
		_panel.add(_lineList);
		_panel.add(_trainList);
		_panel.add(_moveTrainButton);
		_panel.add(_sellTrainButton);
		_panel.add(_yourLinesLabel);
		_panel.add(_yourTrainsLabel);
	}
	
	/**
	 * Fills the line list with the lines.
	 */
	private void fillLineList()
	{
		// Fill line list with all lines:
		java.util.List<TrainLine> lineList = _trainManagementService.getLines();
		for (TrainLine line : lineList)
		{
			_lineList.addElement(line.getName());
		}
		
		if (lineList.size() > 0)
		{
			_lineList.setSelectedEntry(0); // simply select the first entry
		}
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
				startMoveMode();
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
	
	/**
	 * Removes the whole selected line.
	 */
	private void sellTrainButton_action()
	{
		_trainManagementService.removeTrain(_trainList.getSelectedText());
		_trainList.removeElement(_trainList.getSelectedIndex());
		_trainList.setSelectedEntry(0);
	}
	
	/**
	 * Fills controls with information about line so that the player can edit it.
	 * Also disabled the buttons for editing and selling trains and sets the {@code _movedTrain} variable.
	 */
	void startMoveMode()
	{
		if (_trainList.getSelectedIndex() == -1) return;
		
		_moveTrainButton.setState(false);
		_sellTrainButton.setState(false);
		
		_movedTrain = _trainList.getSelectedText();
	}
	
	/**
	 * Sets the move mode to {@code false}, so that no trains can be moved to other lines.
	 */
	void stopMoveMode()
	{
		_moveTrainButton.setState(false);
		_sellTrainButton.setState(false);
		
		_movedTrain = "";
	}
	
	/**
	 * @return The list with all trains of the selected line.
	 */
	List getTrainList()
	{
		return _trainList;
	}
	
	/**
	 * @return The list with all lines.
	 */
	List getLineList()
	{
		return _lineList;
	}
	
	/**
	 * @return Gets the panel of this screen.
	 */
	Panel getPanel()
	{
		return _panel;
	}
	
	/**
	 * @return True when the TrainViewMain is in move mode.
	 */
	public boolean isInMoveMode()
	{
		return !_movedTrain.isEmpty();
	}
	
	@Override
	public boolean isHovered()
	{
		return METRO.__mousePosition.x > _areaOffset.x
		        && METRO.__mousePosition.y < 400;
	}
	
	/**
	 * @return The train that should be moved.
	 */
	public String getTrainToMove()
	{
		return _movedTrain;
	}
	
	@Override
	public boolean mouseClicked(int screenX, int screenY, int mouseButton)
	{
		return false;
	}
}
