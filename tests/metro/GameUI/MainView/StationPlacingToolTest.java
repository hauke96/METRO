package metro.GameUI.MainView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Input.Buttons;

import metro.TestInitializer;
import juard.injection.Locator;
import metro.Common.Game.GameState;
import metro.Common.Technical.ModifiableBoolean;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.TrainStations.TrainStation;

/**
 * @author hauke
 */
public class StationPlacingToolTest
{
	private StationPlacingTool		_tool;
	private boolean					_notified;
	private TrainManagementService	_trainManagementService;
	
	/**
	 * Creates a new station placing tool and sets the notified variable to false.
	 */
	@Before
	public void init()
	{
		TestInitializer.init();
		
		_trainManagementService = Locator.get(TrainManagementService.class);
		_tool = new StationPlacingTool(Locator.get(GameState.class), Locator.get(PlayingField.class), _trainManagementService);
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
		
		_tool.CloseEvent.add(() -> closed.set(true));
		_tool.mouseClicked(0, 0, Buttons.RIGHT);
		
		assertTrue(closed.value());
	}
	
	/**
	 * Checks if the observer works on right clicking.
	 */
	@Test
	public void testObserverByRightClick()
	{
		_tool.CloseEvent.add(() ->
		{
			_notified = true;
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
		List<TrainStation> stations = _trainManagementService.getStations();
		assertEquals(0, stations.size());
		
		_tool.mouseClicked(100, 100, Buttons.LEFT);
		
		assertEquals(1, _trainManagementService.getStations().size());
	}
	
	/**
	 * Tests if a left-click adds a station.
	 */
	@Test
	public void testDoubleClickOnScreenProducesNoDuplicates()
	{
		List<TrainStation> stations = _trainManagementService.getStations();
		assertEquals(0, stations.size());
		
		_tool.mouseClicked(100, 100, Buttons.LEFT);
		_tool.mouseClicked(100, 100, Buttons.LEFT);
		
		assertEquals(1, _trainManagementService.getStations().size());
	}
}
