package metro.GameUI.MainView.Toolbar;

import java.awt.Point;

import juard.Contract;
import metro.Common.Technical.Event;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Controls.Button;

/**
 * The controller class for the Toolbar.
 * 
 * @author hauke
 *
 */
public class ToolbarTool
{
	private ToolbarView	_view;
	private Button		_selectedButton;
	
	public Event	StationPlacingToolSelected	= new Event();
	public Event	TrackPlacingToolSelected	= new Event();
	public Event	LineViewToolSelected		= new Event();
	public Event	TrainViewToolSelected		= new Event();
	
	/**
	 * Creates the controller and the view of the toolbar.
	 * 
	 * @param viewWidth
	 *            The width of the toolbar.
	 * @param viewHeight
	 *            The height of the toolbar.
	 */
	public ToolbarTool(int viewWidth, int viewHeight)
	{
		_view = new ToolbarView(viewWidth, viewHeight);
		_selectedButton = null;
		
		registerObervers();
	}
	
	private void registerObervers()
	{
		_view.getBuildStationButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				if (_selectedButton == arg) return;
				
				StationPlacingToolSelected.fireEvent();
				selectButton(_view.getBuildStationButton());
			}
		});
		
		_view.getBuildTracksButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				if (_selectedButton == arg) return;
				
				TrackPlacingToolSelected.fireEvent();
				selectButton(_view.getBuildTracksButton());
			}
		});
		_view.getShowTrainListButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				if (_selectedButton == arg) return;
				
				LineViewToolSelected.fireEvent();
				selectButton(_view.getShowTrainListButton());
			}
		});
		_view.getCreateNewTrainButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				if (_selectedButton == arg) return;
				
				TrainViewToolSelected.fireEvent();
				selectButton(_view.getCreateNewTrainButton());
			}
		});
	}
	
	/**
	 * Will select the given button. Is {@code null} is passed, all buttons will be deselected.
	 * Selecting means, that the given button will be e.g. a bit lower or greater then the others.
	 *
	 * @param button
	 *            Button to select.
	 */
	public void selectButton(Button button)
	{
		if (_selectedButton != null)
		{
			resetButtonPosition(_selectedButton);
		}
		
		// Move the selected button a bit down:
		if (button != null)
		{
			button.setPosition(new Point(button.getPosition().x, _view.getSelectedButtonYPosition()));
		}
		
		_selectedButton = button;
	}
	
	private void resetButtonPosition(Button button)
	{
		Contract.RequireNotNull(button);
		
		button.setPosition(new Point(button.getPosition().x, _view.getButtonYPosition()));
	}
	
	/**
	 * Moves the view of this control above of the given container.
	 * 
	 * @param container
	 *            The container that should be below the toolbar.
	 */
	public void setAboveOf(AbstractContainer container)
	{
		_view.setAboveOf(container);
	}
}
