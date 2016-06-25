package metro.UI.Renderable.Container;

import metro.UI.ContainerRegistrationService;

public abstract class StaticContainer extends AbstractContainer
{
	public StaticContainer()
	{
	}
	
	@Override
	protected void registerContainerInRenderer(ContainerRegistrationService registrationService)
	{
		registrationService.register(this);
	}
}
