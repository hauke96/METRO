package metro.GameUI.MainView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Input.Buttons;

import metro.TestInitializer;
import metro.Common.Technical.ModifiableBoolean;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Trains.TrainStation;

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
	@Before
	public void init()
	{
		TestInitializer.init();

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
	 * Check if the state is not active after right clicking.
	 */
	@Test
	public void testActiveAfterRightClick()
	{
		ModifiableBoolean closed = new ModifiableBoolean();

		_tool.addObserver((o, arg) -> closed.set(true));
		_tool.mouseClicked(0, 0, Buttons.RIGHT);

		assertTrue(closed.value());
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

	/**
	 * Tests if a left-click adds a station.
	 */
	@Test
	public void testClickOnScreenAddsStation()
	{
		List<TrainStation> stations = TrainManagementService.getInstance().getStations();
		assertEquals(0, stations.size());

		_tool.mouseClicked(100, 100, Buttons.LEFT);

		assertEquals(1, stations.size());
	}

	/**
	 * Tests if a left-click adds a station.
	 */
	@Test
	public void testDoubleClickOnScreenProducesNoDuplicates()
	{
		List<TrainStation> stations = TrainManagementService.getInstance().getStations();
		assertEquals(0, stations.size());

		_tool.mouseClicked(100, 100, Buttons.LEFT);
		_tool.mouseClicked(100, 100, Buttons.LEFT);

		assertEquals(1, stations.size());
	}
}
