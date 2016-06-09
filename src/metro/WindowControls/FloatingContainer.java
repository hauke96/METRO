package metro.WindowControls;

public abstract class FloatingContainer extends Container
{
	public FloatingContainer()
	{
	}
	
	@Override
	protected void registerContainerInRenderer(ContainerRegistrationService registrationService)
	{
		registrationService.registerFloatingContainer(this);
	}
}
