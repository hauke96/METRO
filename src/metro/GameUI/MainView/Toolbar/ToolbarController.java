package metro.GameUI.MainView.Toolbar;

import java.awt.Point;

import metro.Common.Technical.Contract;
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
public class ToolbarController
{
	private ToolbarView _view;
	private Button _selectedButton;

	public Event StationPlacingToolSelected = new Event();
	public Event TrackPlacingToolSelected = new Event();
	public Event LineViewToolSelected = new Event();
	public Event TrainViewToolSelected = new Event();

	/**
	 * Creates the controller and the view of the toolbar.
	 * 
	 * @param viewWidth The width of tools to align buttons.
	 */
	public ToolbarController(int viewWidth)
	{
		_view = new ToolbarView(viewWidth);
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
				if(_selectedButton == arg) return;

				selectButton(_view.getBuildStationButton());
				StationPlacingToolSelected.fireEvent();
			}
		});

		_view.getBuildTracksButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				if(_selectedButton == arg) return;

				selectButton(_view.getBuildTracksButton());
				TrackPlacingToolSelected.fireEvent();
			}
		});
		_view.getShowTrainListButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				if(_selectedButton == arg) return;

				selectButton(_view.getShowTrainListButton());
				LineViewToolSelected.fireEvent();
			}
		});
		_view.getCreateNewTrainButton().register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				if(_selectedButton == arg) return;

				selectButton(_view.getCreateNewTrainButton());
				TrainViewToolSelected.fireEvent();
			}
		});
	}

	/**
	 * Will select the given button. Is {@code null} is passed, all buttons will be deselected.
	 * Selecting means, that the given button will be e.g. a bit lower or greater then the others.
	 *
	 * @param button Button to select.
	 */
	public void selectButton(Button button)
	{
		if(_selectedButton != null)
		{
			resetButtonPosition(_selectedButton);
		}

		// Move the selected button a bit down:
		if(button != null)
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
	 * @param container The container that should be below the toolbar.
	 */
	public void setAboveOf(AbstractContainer container)
	{
		_view.setAboveOf(container);
	}
}
