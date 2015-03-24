package Game;

import Game.CityTravelerSpot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * GameScreen with the city view. It Shows the population density and basic player information.
 */

/**
 * @author Hauke
 *
 */
public class GameScreen_CityView implements GameScreen
{
	public static GameScreen _trainGameScreen;
	public static Point2D _offset = new Point2D.Float(0, 0);
	
	private List<CityTravelerSpot> _travelerSpots = new ArrayList<CityTravelerSpot>();
	private Point _oldMousePos; // Mouse position from last frame
	private Point2D _lastFrameOffset = new Point2D.Float(0, 0); // offset of last frame
	private boolean _dragMode = false;
	private int _lastSelectedSpot, 
		_lastSelectedLayer; // selected things since last frame
	private BufferedImage _lastSpotFrame; // the image, rendered since last change

	/**
	 * Constructor to load all the important stuff
	 */
	public GameScreen_CityView()
	{
		_travelerSpots.add(new CityTravelerSpot(new Point(500, 500), 7));
		_travelerSpots.add(new CityTravelerSpot(new Point(700, 700), 5));
		_travelerSpots.add(new CityTravelerSpot(new Point(550, 800), 6));
		_travelerSpots.add(new CityTravelerSpot(new Point(200, 650), 8));
		
		_lastSpotFrame = new BufferedImage(METRO.__SCREEN_SIZE.width, METRO.__SCREEN_SIZE.height, BufferedImage.TYPE_INT_ARGB);
	}

	/* (non-Javadoc)
	 * @see GameScreen#update(java.awt.Graphics2D)
	 */
	@Override
	public void update(SpriteBatch sp)
	{
		if(_dragMode)
		{
			_offset = new Point2D.Float((float)_offset.getX() + (METRO.__mousePosition.x - _oldMousePos.x),
				(float)_offset.getY() + (METRO.__mousePosition.y - _oldMousePos.y));
		}
		_oldMousePos = METRO.__mousePosition;
		
		drawCircles(sp);	
		
	}
	/**
	 * Draws the traveler-circles.
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
		//TODO: Recreate drawing stuff for circles
//		if(_lastSelectedSpot != selectedSpotNumber 
//			|| _lastSelectedLayer != selectedLayerNumber
//			|| !_lastFrameOffset.equals(_offset)) // only re-render if something changed
//		{
//			Graphics2D g2d = _lastSpotFrame.createGraphics();
//			g2d.setColor(Color.white);
//			g2d.fillRect(0, 0, _lastSpotFrame.getWidth(), _lastSpotFrame.getHeight());
//			g2d.setFont(METRO.__stdFont);
//			// draw all the circles
//			for(int i = 0; i < 10; i++)
//			{
//				for(int k = 0; k < _travelerSpots.size(); k++)
//				{
//					if(_travelerSpots.get(k).getStrength() <= i) continue;
//					_travelerSpots.get(k).draw(g2d, i, i == selectedLayerNumber, k == selectedSpotNumber, true); // i==selectedLayerNumber means: if i is the selected circle level -> draw it different
//				}
//				for(int k = 0; k < _travelerSpots.size(); k++)
//				{
//					if(_travelerSpots.get(k).getStrength() <= i) continue;
//					_travelerSpots.get(k).draw(g2d, i, i == selectedLayerNumber, k == selectedSpotNumber); // i==selectedLayerNumber means: if i is the selected circle level -> draw it different
//				}
//			}
//			_lastSelectedLayer = selectedLayerNumber;
//			_lastSelectedSpot = selectedSpotNumber;
//			_lastFrameOffset = _offset;
//		}
//		g.drawImage(_lastSpotFrame, 0, 0, null);
//		
//		if(selectedLayerNumber != -1) // if there's a selected circle 
//		{
//			int gray = 55 * selectedLayerNumber;
//			if(gray > 255) gray = 255;
//			g.setColor(new Color(0, 0, 200));
//			g.drawString(selectedLayerNumber + "", METRO.__mousePosition.x - g.getFontMetrics(METRO.__stdFont).stringWidth(selectedLayerNumber + "") / 2 - 1, 
//				METRO.__mousePosition.y + g.getFontMetrics(METRO.__stdFont).getHeight() / 4 + 1);
//		}
	}

	/* (non-Javadoc)
	 * @see GameScreen#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			_dragMode = true;
		}
		if(METRO.__viewPortButton_Train.isPressed(METRO.__mousePosition.x, METRO.__mousePosition.y))
		{
			METRO.__currentGameScreen = _trainGameScreen;
			METRO.__viewPortButton_City.setPosition(new Point(METRO.__SCREEN_SIZE.width / 2 - 200, -15));
			METRO.__viewPortButton_Train.setPosition(new Point(METRO.__SCREEN_SIZE.width / 2, -5));
		}
	}

	/* (non-Javadoc)
	 * @see GameScreen#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			_dragMode = false;
		}
	}
	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			METRO.__close();
		}
	}
}
