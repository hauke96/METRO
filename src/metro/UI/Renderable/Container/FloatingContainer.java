package metro.UI.Renderable.Container;

import metro.UI.ContainerRegistrationService;

public abstract class FloatingContainer extends AbstractContainer
{
	public FloatingContainer()
	{
	}
	
	@Override
	protected void registerContainerInRenderer(ContainerRegistrationService registrationService)
	{
		registrationService.register(this);
	}
}
