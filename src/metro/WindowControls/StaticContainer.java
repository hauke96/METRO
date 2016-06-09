package metro.WindowControls;

public abstract class StaticContainer extends Container
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
