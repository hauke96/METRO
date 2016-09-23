package metro.GameScreen.MainView.LineView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Point;

import org.junit.Test;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.UI.ContainerRegistrationService;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderer.BasicContainerRenderer;

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
		new BasicContainerRenderer();
		METRO.__SCREEN_SIZE = new Dimension(1920, 1080);
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
	
	/**
	 * Creates a line select tool and checks for observers and correct flags.
	 */
	@Test
	public void testCreateLineSelectTool()
	{
		LineSelectTool tool = view.getLineSelectTool();
		assertTrue(tool != null);
		assertTrue(tool.countObservers() > 0);
		assertFalse(view.isLineSelectToolEnabled());
		
		view.createLineSelectTool();
		
		tool = view.getLineSelectTool();
		assertTrue(tool != null);
		assertTrue(tool.countObservers() > 0);
		assertTrue(view.isLineSelectToolEnabled());
	}
}
