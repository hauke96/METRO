package metro.GameScreen.MainView.LineView;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author hauke
 */
public class LineViewTest
{
	LineView view;

	/**
	 * Simply creates the line view object
	 */
	public LineViewTest()
	{
		view = new LineView();
	}

	/**
	 * Tests the visibility of the line view.
	 */
	@Test
	public void testVisibility()
	{
		assertTrue(view.isActive());
		
		view.setVisibility(false);
		
		assertFalse(view.isActive());
	}
}
