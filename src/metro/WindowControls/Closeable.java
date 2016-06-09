package metro.WindowControls;

abstract class Closeable extends ControlElement
{
	public abstract void notifyAboutClose();
	
	abstract void registerCloseObserver(CloseObserver observer);
	
	abstract void removeCloseObserver(CloseObserver observer);
}
