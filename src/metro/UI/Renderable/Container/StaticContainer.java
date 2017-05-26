package metro.UI.Renderable.Container;

import metro.UI.ContainerRegistrationService;

/**
 * A static container is a normal {@link AbstractContainer} but can not be moved by the user.
 * 
 * @author hauke
 *
 */
public abstract class StaticContainer extends AbstractContainer
{
	/**
	 * Creates a new basic static container.
	 */
	public StaticContainer()
	{
	}
	
	@Override
	protected void registerContainerInRenderer(ContainerRegistrationService registrationService)
	{
		registrationService.register(this);
	}
}
