package metro.GameUI.MainView;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import org.junit.Test;

import metro.METRO;
import metro.Common.Game.GameState;
import metro.GameUI.MainView.TrackPlacingTool;

public class TrackPlacingToolTest
{
	private TrackPlacingTool _tool;
	private boolean _notified;

	public TrackPlacingToolTest()
	{
		_tool = new TrackPlacingTool();
		_notified = false;
		METRO.__gameState = GameState.getInstance();
	}

	/**
	 * Checks the initial active state.
	 */
	@Test
	public void testIsActive()
	{
		assertTrue(_tool.isActive());
	}

	/**
	 * Creates no internal node and checks if the observer IS notified on right click.
	 */
	@Test
	public void testRightClickWithoutNode()
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
		
		_tool.rightClick(100, 100, new Point());
		
		assertTrue(_notified);
		assertFalse(_tool.isActive());
	}

	/**
	 * Creates internally a node and checks if the observer IS NOT notified on right click.
	 */
	@Test
	public void testRightClickWithNode()
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
		
		// get node internally:
		_tool.leftClick(100, 100, new Point());
		
		// click to exit placing mode and NOT to close the tool!
		_tool.rightClick(100, 100, new Point());
		
		// when the internal creation of the node and the left click routine
		// worked as expected, there should be no notification about a close.
		assertFalse(_notified);
		assertTrue(_tool.isActive());
	}

	/**
	 * Creates internally a node and checks if the observer IS notified on right click also after two left clicks (so after creating a track).
	 */
	@Test
	public void testRightClickWithNodeAfterTwoLeftClicks()
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

		// get node internally:
		_tool.leftClick(100, 100, new Point());
		
		// place node internally:
		_tool.leftClick(200, 150, new Point());
		
		// right click to close
		_tool.rightClick(300, 200, new Point());

		// when the placing was correct, the closing on right click should work.
		assertTrue(_notified);
		assertFalse(_tool.isActive());
	}
}
