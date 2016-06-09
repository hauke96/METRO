package metro.WindowControls;

public abstract class StaticContainer extends Container
{
	public StaticContainer()
	{
		register(this);
	}
	
	@Override
	protected void registerContainerInRenderer()
	{
		register(this);
	}
}
