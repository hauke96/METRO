package metro.UI.Renderable.Container;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import juard.injection.Locator;
import metro.Common.Technical.Exceptions.ContainerPositioningConflict;
import metro.UI.ContainerRegistrationService;
import metro.UI.Renderer.BasicContainerRenderer;

/**
 * @author hauke
 */
public class BasicContainerRendererTest
{
	private StaticContainer	_containerAbove,
	        _containerBelow,
	        _containerMiddle,
	        _containerOther,
	        _containerConflictedWithAbove;
	private DummyRenderer	_containerRenderer;
	
	private class DummyRenderer extends BasicContainerRenderer
	{
		public DummyRenderer(ContainerRegistrationService registrationService)
		{
			super(registrationService);
		}
		
		public List<AbstractContainer> getStaticContainer()
		{
			return _listOfStaticContainer;
		}
	}
	
	private class DummyAbstractContainer extends StaticContainer
	{
		@Override
		public void close()
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void moveElement(Point offset)
		{
			throw new UnsupportedOperationException();
		}
	}
	
	@Before
	public void constructControls()
	{
		ContainerRegistrationService registrationService = Locator.get(ContainerRegistrationService.class);
		_containerRenderer = new DummyRenderer(registrationService);
		
		_containerAbove = new DummyAbstractContainer();
		_containerMiddle = new DummyAbstractContainer();
		_containerBelow = new DummyAbstractContainer();
		_containerOther = new DummyAbstractContainer();
		_containerConflictedWithAbove = new DummyAbstractContainer();
		
		_containerAbove.setAboveOf(_containerMiddle);
		_containerAbove.setAboveOf(_containerBelow);
		_containerMiddle.setAboveOf(_containerBelow);
	}
	
	@Test
	public void testMiddleAboveAbove()
	{
		assertTrue(_containerAbove.isAbove(_containerMiddle));
		
		assertTrue(_containerAbove.compareTo(_containerMiddle) == 1);
		
		// Check if they're really in the right order:
		
		List<AbstractContainer> listOfContainer = _containerRenderer.getStaticContainer();
		int indexMiddle = listOfContainer.indexOf(_containerMiddle);
		int indexAbove = listOfContainer.indexOf(_containerAbove);
		
		assertTrue(indexMiddle < indexAbove);
	}
	
	@Test
	public void testAboveAboveBelow()
	{
		assertTrue(_containerAbove.isAbove(_containerBelow));
		
		assertTrue(_containerAbove.compareTo(_containerBelow) == 1);
		
		// Check if they're really in the right order:
		
		List<AbstractContainer> listOfContainer = _containerRenderer.getStaticContainer();
		int indexBelow = listOfContainer.indexOf(_containerBelow);
		int indexAbove = listOfContainer.indexOf(_containerAbove);
		
		assertTrue(indexBelow < indexAbove);
	}
	
	@Test
	public void testMiddleAboveBelow()
	{
		assertTrue(_containerMiddle.isAbove(_containerBelow));
		
		assertTrue(_containerMiddle.compareTo(_containerBelow) == 1);
		
		// Check if they're really in the right order:
		
		List<AbstractContainer> listOfContainer = _containerRenderer.getStaticContainer();
		int indexBelow = listOfContainer.indexOf(_containerBelow);
		int indexMiddle = listOfContainer.indexOf(_containerMiddle);
		
		assertTrue(indexBelow < indexMiddle);
	}
	
	@Test
	public void testNobodyAboveOther()
	{
		assertFalse(_containerAbove.isAbove(_containerOther));
		assertFalse(_containerMiddle.isAbove(_containerOther));
		assertFalse(_containerBelow.isAbove(_containerOther));
		
		assertTrue(_containerAbove.compareTo(_containerOther) == 0);
		assertTrue(_containerMiddle.compareTo(_containerOther) == 0);
		assertTrue(_containerBelow.compareTo(_containerOther) == 0);
		
		// Check if they're really in the right order:
		
		List<AbstractContainer> listOfContainer = _containerRenderer.getStaticContainer();
		int indexBelow = listOfContainer.indexOf(_containerBelow);
		int indexMiddle = listOfContainer.indexOf(_containerMiddle);
		int indexAbove = listOfContainer.indexOf(_containerAbove);
		int indexOther = listOfContainer.indexOf(_containerOther);
		
		assertTrue(indexBelow < indexOther);
		assertTrue(indexMiddle < indexOther);
		assertTrue(indexAbove < indexOther);
	}
	
	@Test (expected = ContainerPositioningConflict.class)
	public void testAddingConflictingFailed()
	{
		_containerAbove.setAboveOf(_containerConflictedWithAbove);
		
		// Generate conflict
		_containerConflictedWithAbove.setAboveOf(_containerAbove);
	}
	
	@Test (expected = ContainerPositioningConflict.class)
	public void testComparingConflictingFailed()
	{
		_containerAbove.setAboveOf(_containerConflictedWithAbove);
		
		// force conflict
		_containerConflictedWithAbove.getContainerBelow().add(_containerAbove);
		
		// generate exception by comparing them
		_containerAbove.compareTo(_containerConflictedWithAbove);
	}
}
