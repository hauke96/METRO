package metro.GameUI.MainView.LineView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Point;

import org.junit.Test;

import metro.METRO;
import metro.AppContext.Locator;
import metro.Common.Game.GameState;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.TrainManagement.TrainManagementService;
import metro.UI.ContainerRegistrationService;
import metro.UI.Renderer.BasicContainerRenderer;

/**
 * @author hauke
 */
public class LineViewTest
{
	private LineView	view;
	private int			_toolViewWidth;
	
	/**
	 * Simply creates the line view object
	 */
	public LineViewTest()
	{
		new BasicContainerRenderer(Locator.get(ContainerRegistrationService.class));
		METRO.__SCREEN_SIZE = new Dimension(1920, 1080);
		
		_toolViewWidth = Locator.get(GameState.class).getToolViewWidth();
		PlayingField field = Locator.get(PlayingField.class);
		TrainManagementService trainManagementService = Locator.get(TrainManagementService.class);
		
		view = new LineView(_toolViewWidth, field, trainManagementService);
	}
	
	/**
	 * Check if the initial state of the area offset is correct.
	 */
	@Test
	public void testGetAreaOffset()
	{
		assertEquals(
		        new Point(METRO.__SCREEN_SIZE.width - _toolViewWidth, 40), view.getAreaOffset());
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
		assertTrue(tool == null);
		
		view.createLineSelectTool();
		tool = view.getLineSelectTool();
		
		assertTrue(tool != null);
		assertTrue(tool.CloseEvent.countObservers() > 0);
		assertTrue(view.isLineSelectToolEnabled());
	}
}
