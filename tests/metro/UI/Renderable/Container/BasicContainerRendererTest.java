package metro.UI.Renderable.Container;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import metro.Exceptions.ContainerPositioningConflict;
import metro.UI.ContainerRegistrationService;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Container.FloatingContainer;
import metro.UI.Renderer.BasicContainerRenderer;

/**
 * @author hauke
 */
public class BasicContainerRendererTest
{
	FloatingContainer _containerAbove,
		_containerBelow,
		_containerMiddle,
		_containerOther,
		_containerConflictedWithAbove;
	private BasicContainerRenderer _containerRenderer;
	
	private class DummyAbstractContainer extends FloatingContainer
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
		_containerRenderer = new BasicContainerRenderer();
		ContainerRegistrationService registrationService = new ContainerRegistrationService();
		registrationService.setRenderer(_containerRenderer);
		AbstractContainer.setContainerRegistrationService(registrationService);
		
		_containerAbove = new DummyAbstractContainer();
		_containerMiddle = new DummyAbstractContainer();
		_containerBelow = new DummyAbstractContainer();
		_containerOther = new DummyAbstractContainer();
		_containerConflictedWithAbove = new DummyAbstractContainer();

		registrationService.register(_containerAbove);
		registrationService.register(_containerMiddle);
		registrationService.register(_containerBelow);
		registrationService.register(_containerOther);
		registrationService.register(_containerConflictedWithAbove);

		_containerAbove.setAboveOf(_containerMiddle);
		_containerAbove.setAboveOf(_containerBelow);
		_containerMiddle.setAboveOf(_containerBelow);
	}
	
	@Test
	public void testMiddleAboveAbove()
	{
		assertTrue(_containerAbove.isAbove(_containerMiddle));
		
		assertTrue(_containerAbove.compareTo(_containerMiddle) == 1);
	}
	
	@Test
	public void testAboveAboveBelow()
	{
		assertTrue(_containerAbove.isAbove(_containerBelow));
		
		assertTrue(_containerAbove.compareTo(_containerBelow) == 1);
	}
	
	@Test
	public void testMiddleAboveBelow()
	{
		assertTrue(_containerMiddle.isAbove(_containerBelow));
		
		assertTrue(_containerMiddle.compareTo(_containerBelow) == 1);
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
	}
	
	@Test(expected=ContainerPositioningConflict.class)
	public void testAddingConflictingFailed()
	{
		_containerAbove.setAboveOf(_containerConflictedWithAbove);
		
		// Generate conflict
		_containerConflictedWithAbove.setAboveOf(_containerAbove);
	}
	
	@Test(expected=ContainerPositioningConflict.class)
	public void testComparingConflictingFailed()
	{
		_containerAbove.setAboveOf(_containerConflictedWithAbove);
		
		// force conflict
		_containerConflictedWithAbove.getContainerBelow().add(_containerAbove);
		
		// generate exception by comparing them
		_containerAbove.compareTo(_containerConflictedWithAbove);
	}
}
