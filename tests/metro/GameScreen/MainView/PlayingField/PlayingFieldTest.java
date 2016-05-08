package metro.GameScreen.MainView.PlayingField;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

/**
 * @author hauke
 */
public class PlayingFieldTest
{
	private PlayingField field;

	/**
	 * Creates the playing field.
	 */
	public PlayingFieldTest()
	{
		field = PlayingField.getInstance();
	}

	/**
	 * Checks the active and hovered values.
	 */
	@Test
	public void testActiveAndHovered()
	{
		assertFalse(field.isActive());
		assertFalse(field.isHovered());
	}

	/**
	 * Tests the initial state of the map offset.
	 */
	@Test
	public void testInitialMapOffset()
	{
		assertEquals(new Point(0,0), field.getMapOffset());
	}
	
	/**
	 * Tests the initial state of the selected node.
	 */
	@Test
	public void testInitialSelectedNode()
	{
		assertEquals(new Point(-1,-1), field.getSelectedNode());
	}
}
