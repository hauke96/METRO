package metro.WindowControls;

public abstract class FloatingContainer extends Container
{
	public FloatingContainer()
	{
		registerFloatingContainer(this);
	}
	
	@Override
	protected void registerContainerInRenderer()
	{
		registerFloatingContainer(this);
	}
}
