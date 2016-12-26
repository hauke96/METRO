package metro.GameUI.MainView.TrainView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Point;

import org.junit.Test;

import metro.METRO;
import metro.Common.Game.GameState;

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
		METRO.__SCREEN_SIZE = new Dimension(1920, 1080);
		view = new TrainView();
	}

	/**
	 * Check if the initial state of the area offset is correct.
	 */
	@Test
	public void testGetAreaOffset()
	{
		assertEquals(
			new Point(METRO.__SCREEN_SIZE.width - GameState.getInstance().getToolViewWidth(), 40),
			view.getAreaOffset());
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
