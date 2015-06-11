package metro.GameScreen;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import metro.METRO;
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
	public static GameScreen _trainGameScreen;
	public static Point2D _offset = new Point2D.Float(0, 0);

	private List<CityTravelerSpot> _travelerSpots = new ArrayList<CityTravelerSpot>();
	private Point _oldMousePos; // Mouse position from last frame
	private boolean _dragMode = false;

	/**
	 * Constructor to load all the important stuff
	 */
	public CityView()
	{
		_travelerSpots.add(new CityTravelerSpot(new Point(500, 500), 7));
		_travelerSpots.add(new CityTravelerSpot(new Point(700, 700), 5));
		_travelerSpots.add(new CityTravelerSpot(new Point(550, 850), 6));
		_travelerSpots.add(new CityTravelerSpot(new Point(200, 650), 8));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GameScreen#update(java.awt.Graphics2D)
	 */
	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		if(_dragMode)
		{
			_offset = new Point2D.Float((float)_offset.getX() + (METRO.__mousePosition.x - _oldMousePos.x), (float)_offset.getY()
				+ (METRO.__mousePosition.y - _oldMousePos.y));
		}
		_oldMousePos = METRO.__mousePosition;

		drawCircles(sp);
	}

	/**
	 * Draws the traveler-circles.
	 * 
	 * @param g The graphic handle to draw on
	 */
	private void drawCircles(SpriteBatch sp)
	{
		int selectedSpotNumber = -1;
		int selectedLayerNumber = -1; // the circle, the mouse is in. -1 means: Mouse out of any circle.

		// get the selected circle number
		for(int i = 0; i < 10; i++) // go through all layers
		{
			boolean isLayerSelected = false;
			for(int k = 0; !isLayerSelected && k < _travelerSpots.size(); k++) // go through all spots
			{
				isLayerSelected = _travelerSpots.get(k).isMouseInCircle(i); // if mouse is in ANY circle of this layer
				if(isLayerSelected)
				{
					selectedLayerNumber = i;
					selectedSpotNumber = k;
				}
			}
		}
		// draw all the circles
		for(int i = 0; i < 10; i++)
		{
			for(int k = 0; k < _travelerSpots.size(); k++)
			{
				if(_travelerSpots.get(k).getStrength() <= i) continue;
				_travelerSpots.get(k).draw(sp, i, i == selectedLayerNumber, k == selectedSpotNumber, true); // i==selectedLayerNumber means: if i is the selected circle level -> draw it different
			}
			for(int k = 0; k < _travelerSpots.size(); k++)
			{
				if(_travelerSpots.get(k).getStrength() <= i) continue;
				_travelerSpots.get(k).draw(sp, i, i == selectedLayerNumber, k == selectedSpotNumber); // i==selectedLayerNumber means: if i is the selected circle level -> draw it different
			}
		}

		if(selectedLayerNumber != -1) // if there's a selected circle
		{
			Draw.setColor(new Color(0, 0, 200));
			// Draw.String(selectedLayerNumber + "", METRO.__mousePosition.x - g.getFontMetrics(METRO.__stdFont).stringWidth(selectedLayerNumber + "") / 2 - 1,
			// METRO.__mousePosition.y + g.getFontMetrics(METRO.__stdFont).getHeight() / 4 + 1);
			Draw.String(selectedLayerNumber + "", METRO.__mousePosition.x - Draw.getStringSize(selectedLayerNumber + "").width / 2, METRO.__mousePosition.y
				+ Draw.getStringSize(selectedLayerNumber + "").height / 4 - 10);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GameScreen#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(mouseButton == Buttons.MIDDLE)
		{
			_dragMode = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GameScreen#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(int mouseButton)
	{
		if(mouseButton == Buttons.MIDDLE)
		{
			_dragMode = false;
		}
	}

	@Override
	public void keyDown(int keyCode)
	{
		// if (keyCode == Keys.ESCAPE)
		// {
		// METRO.__close();
		// }
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}
}
