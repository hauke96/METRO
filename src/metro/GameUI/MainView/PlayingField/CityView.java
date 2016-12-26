package metro.GameUI.MainView.PlayingField;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.GameUI.Screen.ToolView;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Trains.TravelerSpot;

/**
 * GameScreen with the city view. It Shows the population density and basic player information.
 * This class is used by the TrainView-class as "background image".
 * 
 * @author Hauke
 * 
 */

public class CityView extends ToolView
{
	private int _selectedLayerNumber;
	private boolean _enableMouseSelection; // When false, there'll be no circle highlighting

	/**
	 * Constructor to load all the important stuff.
	 */
	public CityView()
	{
		_enableMouseSelection = true;
	}

	/**
	 * Draws the city view. This method does not draw the number of the selected circle over the cursor (s. {@link #drawNumbers(Point)}).
	 * @param offset The current map offset.
	 */
	public void updateGameScreen(Point offset)
	{
		ArrayList<TravelerSpot> travelerSpots = TrainManagementService.getInstance().getTravelerSpots();

		_selectedLayerNumber = -1;
		if(_enableMouseSelection)
		{
			// get the selected circle number
			for(int i = 0; i < 10; ++i) // go through all layers
			{
				boolean isLayerSelected = false;
				for(int k = 0; !isLayerSelected && k < travelerSpots.size(); ++k) // go through all spots
				{
					isLayerSelected = travelerSpots.get(k).isMouseInCircle(i, offset); // if mouse is in ANY circle of this layer
					if(isLayerSelected)
					{
						_selectedLayerNumber = i;
					}
				}
			}
		}
		// draw all the circles
		for(int i = 0; i < 10; ++i)
		{
			for(int k = 0; k < travelerSpots.size(); ++k)
			{
				if(travelerSpots.get(k).getStrength() <= i) continue;
				travelerSpots.get(k).draw(i, i == _selectedLayerNumber, true, offset); // i==selectedLayerNumber means: if i is the selected circle level -> draw it different
			}
			for(int k = 0; k < travelerSpots.size(); ++k)
			{
				if(travelerSpots.get(k).getStrength() <= i) continue;
				travelerSpots.get(k).draw(i, i == _selectedLayerNumber, false, offset); // i==selectedLayerNumber means: if i is the selected circle level -> draw it different
			}
		}
	}

	/**
	 * Draws the number of the selected Traveler Spot above the mouse.
	 * @param cursorDotPosition The position of the cursor dot from the Grid.
	 */
	public void drawNumbers(Point cursorDotPosition)
	{
		Point p = new Point(cursorDotPosition.x - METRO.__mousePosition.x,
			cursorDotPosition.y - METRO.__mousePosition.y);
		if(_selectedLayerNumber != -1) // if there's a selected circle, draw the number at cursor position
		{
			Draw.setColor(new Color(63, 114, 161));
			Draw.String(_selectedLayerNumber + "",
				METRO.__mousePosition.x - Draw.getStringSize(_selectedLayerNumber + "").width / 2 + p.x,
				METRO.__mousePosition.y + Draw.getStringSize(_selectedLayerNumber + "").height / 4 - 30 + p.y);
		}
	}

	/**
	 * Enables the highlighting of the city circles when a mouse hovers one.
	 */
	public void enableCircleHighlighting()
	{
		_enableMouseSelection = true;
	}

	/**
	 * Disables the highlighting of the city circles when a mouse hovers one.
	 */
	public void disableCircleHighlighting()
	{
		_enableMouseSelection = false;
	}

	/**
	 * @return True when a circle is selected. It depends on whether the highlighting (=mouse interaction) is enabled.
	 */
	@Override
	public boolean isHovered()
	{
		return _selectedLayerNumber != -1;
	}
}
