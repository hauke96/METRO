package metro.GameScreen.MainView.CityView;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * GameScreen with the city view. It Shows the population density and basic player information.
 * This class is used by the TrainView-class as "background image".
 * 
 * @author Hauke
 * 
 */

public class CityView extends GameScreen
{
	/**
	 * The offset of the city view win pixel.
	 */
	public static Point2D __offset = new Point2D.Float(0, 0);

	private List<CityTravelerSpot> _travelerSpots;
	private Point _oldMousePos; // Mouse position from last frame
	private boolean _dragMode;
	private int _selectedLayerNumber;
	private boolean _enableMouseSelection; // When false, there'll be no circle highlighting

	/**
	 * Constructor to load all the important stuff.
	 */
	public CityView()
	{
		_travelerSpots = new ArrayList<CityTravelerSpot>();
		_dragMode = false;
		_enableMouseSelection = true;

		_travelerSpots.add(new CityTravelerSpot(new Point(500, 500), 7));
		_travelerSpots.add(new CityTravelerSpot(new Point(700, 700), 5));
		_travelerSpots.add(new CityTravelerSpot(new Point(550, 850), 6));
		_travelerSpots.add(new CityTravelerSpot(new Point(484, 714), 5));
		_travelerSpots.add(new CityTravelerSpot(new Point(200, 650), 8));
		_travelerSpots.add(new CityTravelerSpot(new Point(-90, 150), 6));
		_travelerSpots.add(new CityTravelerSpot(new Point(200, 450), 5));
		_travelerSpots.add(new CityTravelerSpot(new Point(242, 497), 8));
		_travelerSpots.add(new CityTravelerSpot(new Point(1214, 320), 3));
		_travelerSpots.add(new CityTravelerSpot(new Point(1103, 402), 4));
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		if(_dragMode)
		{
			__offset = new Point2D.Float((float)__offset.getX() + (METRO.__mousePosition.x - _oldMousePos.x), (float)__offset.getY()
				+ (METRO.__mousePosition.y - _oldMousePos.y));
		}
		_oldMousePos = METRO.__mousePosition;

		draw(sp);
	}

	/**
	 * Draws the traveler-circles.
	 * 
	 * @param sp The SpriteBatch to draw on
	 */
	private void draw(SpriteBatch sp)
	{
		_selectedLayerNumber = -1;
		if(_enableMouseSelection)
		{
			// get the selected circle number
			for(int i = 0; i < 10; ++i) // go through all layers
			{
				boolean isLayerSelected = false;
				for(int k = 0; !isLayerSelected && k < _travelerSpots.size(); ++k) // go through all spots
				{
					isLayerSelected = _travelerSpots.get(k).isMouseInCircle(i); // if mouse is in ANY circle of this layer
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
			for(int k = 0; k < _travelerSpots.size(); ++k)
			{
				if(_travelerSpots.get(k).getStrength() <= i) continue;
				_travelerSpots.get(k).draw(sp, i, i == _selectedLayerNumber, true); // i==selectedLayerNumber means: if i is the selected circle level -> draw it different
			}
			for(int k = 0; k < _travelerSpots.size(); ++k)
			{
				if(_travelerSpots.get(k).getStrength() <= i) continue;
				_travelerSpots.get(k).draw(sp, i, i == _selectedLayerNumber, false); // i==selectedLayerNumber means: if i is the selected circle level -> draw it different
			}
		}
	}

	/**
	 * Draws the number of the selected Traveler Spot above the mouse.
	 * 
	 * @param sp The spriteBatch
	 * @param cursorDotPosition The position of the cursor dot from the Grid.
	 */
	public void drawNumbers(SpriteBatch sp, Point cursorDotPosition)
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

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(mouseButton == Buttons.MIDDLE)
		{
			_dragMode = true;
		}
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
		if(mouseButton == Buttons.MIDDLE)
		{
			_dragMode = false;
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

	@Override
	public void keyDown(int keyCode)
	{
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void reset()
	{
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
