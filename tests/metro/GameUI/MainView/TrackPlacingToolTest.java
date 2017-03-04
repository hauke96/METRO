package metro.GameUI.MainView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import org.junit.Test;

import metro.METRO;
import metro.AppContext.Locator;
import metro.Common.Game.GameState;
import metro.Common.Technical.ModifiableBoolean;
import metro.GameUI.MainView.PlayingField.PlayingField;

public class TrackPlacingToolTest
{
	private TrackPlacingTool _tool;

	public TrackPlacingToolTest()
	{
		GameState gameState = Locator.get(GameState.class);
		PlayingField playingField = Locator.get(PlayingField.class);
		
		_tool = new TrackPlacingTool(gameState, playingField);
		METRO.__gameState = gameState;
	}

	/**
	 * Creates no internal node and checks if the observer IS notified on right click.
	 */
	@Test
	public void testRightClickWithoutNode()
	{
		ModifiableBoolean notified = new ModifiableBoolean();

		_tool.addObserver(new Observer()
		{
			@Override
			public void update(Observable o, Object arg)
			{
				assertEquals(o, _tool);
				notified.set(true);
			}
		});

		_tool.rightClick(100, 100, new Point());

		assertTrue(notified.value());
	}

	/**
	 * Creates internally a node and checks if the observer IS NOT notified on right click.
	 */
	@Test
	public void testRightClickWithNode()
	{
		ModifiableBoolean notified = new ModifiableBoolean();

		_tool.addObserver(new Observer()
		{
			@Override
			public void update(Observable o, Object arg)
			{
				assertEquals(o, _tool);
				notified.set(true);
			}
		});

		// get node internally:
		_tool.leftClick(100, 100, new Point());

		// click to exit placing mode and NOT to close the tool!
		_tool.rightClick(100, 100, new Point());

		// when the internal creation of the node and the left click routine
		// worked as expected, there should be no notification about a close.
		assertFalse(notified.value());
	}

	/**
	 * Creates internally a node and checks if the observer IS notified on right click also after two left clicks (so after creating a track).
	 */
	@Test
	public void testRightClickWithNodeAfterTwoLeftClicks()
	{
		ModifiableBoolean notified = new ModifiableBoolean();

		_tool.addObserver(new Observer()
		{
			@Override
			public void update(Observable o, Object arg)
			{
				assertEquals(o, _tool);
				notified.set(true);
			}
		});

		// get node internally:
		_tool.leftClick(100, 100, new Point());

		// place node internally:
		_tool.leftClick(200, 150, new Point());

		// right click to close
		_tool.rightClick(300, 200, new Point());

		// when the placing was correct, the closing on right click should work.
		assertTrue(notified.value());
	}
}
