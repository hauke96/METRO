package metro.GameUI.MainView.TrainView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Point;

import org.junit.Test;

import com.badlogic.gdx.Input.Buttons;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.GameUI.MainView.TrainView.TrainView;

/**
 * @author hauke
 */
public class TrainViewTest
{
	private TrainView view;

	/**
	 * Sets the screen dimensions and creates a fresh train view.
	 */
	public TrainViewTest()
	{
		METRO.__SCREEN_SIZE = new Dimension(1920,1080);
		view = new TrainView();
	}

	/**
	 * Checks the visibility/activity property of the control.
	 */
	@Test
	public void testVisibilitySetter()
	{
		view.setVisibility(true);
		assertEquals(true, view.isActive());
		view.setVisibility(false);
		assertEquals(false, view.isActive());
	}
	
	/**
	 * Check if the initial state of the area offset is correct.
	 */
	@Test
	public void testGetAreaOffset()
	{
		assertEquals(
			new Point(METRO.__SCREEN_SIZE.width-GameState.getInstance().getToolViewWidth(), 45), 
			view.getAreaOffset());
	}

	/**
	 * Check is the active state of the view changed to false when the left mouse button is clicked.
	 */
	@Test
	public void testMouseClickActive()
	{
		view.setVisibility(false);
		view.mouseClicked(0, 0, Buttons.RIGHT);
		assertFalse(view.isActive());

		view.setVisibility(true);
		view.mouseClicked(0, 0, Buttons.LEFT);
		assertTrue(view.isActive());
		view.mouseClicked(0, 0, Buttons.MIDDLE);
		assertTrue(view.isActive());
		view.mouseClicked(0, 0, Buttons.BACK);
		assertTrue(view.isActive());
		view.mouseClicked(0, 0, Buttons.FORWARD);
		assertTrue(view.isActive());
	}

	/**
	 * Check if the hover-detection works properly.
	 */
	@Test
	public void testIsHovered()
	{
		METRO.__mousePosition = new Point(0, 0);
		assertFalse(view.isHovered());
		
		METRO.__mousePosition = new Point(view.getAreaOffset().x, 0);
		assertFalse(view.isHovered());
		
		METRO.__mousePosition = new Point(view.getAreaOffset().x + 1, 0);
		assertTrue(view.isHovered());
	}
}
