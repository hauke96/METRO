package metro.GameUI.MainView;

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import org.junit.Test;

import com.badlogic.gdx.Input.Buttons;

import metro.GameUI.MainView.StationPlacingTool;

/**
 * @author hauke
 */
public class StationPlacingToolTest
{
	private StationPlacingTool _tool;
	private boolean _notified; 
	
	/**
	 * Creates a new station placing tool and sets the notified variable to false.
	 */
	public StationPlacingToolTest()
	{
		_tool = new StationPlacingTool();
		_notified = false;
	}
	
	/**
	 * Check the hover getter.
	 */
	@Test
	public void testHovered()
	{
		assertFalse(_tool.isHovered());
	}
	
	/**
	 * Check the active state getter.
	 */
	@Test
	public void testIsActive()
	{
		assertTrue(_tool.isActive());
	}
	
	/**
	 * Check if the state is not active after right clicking.
	 */
	@Test
	public void testActiveAfterRightClick()
	{
		_tool.mouseClicked(0, 0, Buttons.RIGHT);
		assertFalse(_tool.isActive());
	}
	
	/**
	 * Checks if the observer works on right clicking.
	 */
	@Test
	public void testObserverByRightClick()
	{
		_tool.addObserver(new Observer()
		{
			@Override
			public void update(Observable o, Object arg)
			{
				assertEquals(o, _tool);
				_notified = true;
			}
		});
		
		_tool.mouseClicked(0, 0, Buttons.RIGHT);
		
		assertTrue(_notified);
	}
}
