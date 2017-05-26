package metro.UI.Renderable.Container;

import metro.UI.ContainerRegistrationService;

/**
 * A floating container is a normal {@link AbstractContainer} but is able to be moved by the user.
 * 
 * @author hauke
 *
 */
public abstract class FloatingContainer extends AbstractContainer
{
	/**
	 * Creates a new basic floating container.
	 */
	public FloatingContainer()
	{
	}
	
	@Override
	protected void registerContainerInRenderer(ContainerRegistrationService registrationService)
	{
		registrationService.register(this);
	}
}
