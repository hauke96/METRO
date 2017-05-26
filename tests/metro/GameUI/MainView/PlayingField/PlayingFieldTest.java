package metro.GameUI.MainView.PlayingField;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import metro.AppContext.Locator;
import metro.GameUI.MainView.PlayingField.PlayingField;

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
		field = Locator.get(PlayingField.class);
	}
	
	/**
	 * Tests the initial state of the map offset.
	 */
	@Test
	public void testInitialMapOffset()
	{
		assertEquals(new Point(0, 0), field.getMapOffset());
	}
	
	/**
	 * Tests the initial state of the selected node.
	 */
	@Test
	public void testInitialSelectedNode()
	{
		assertEquals(new Point(-1, -1), field.getSelectedNode());
	}
}
