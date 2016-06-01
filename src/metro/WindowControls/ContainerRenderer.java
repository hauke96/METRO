package metro.WindowControls;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ContainerRenderer implements CloseObserver
{
	interface Notifier
	{
		void notifyRenderable(Container container);
	}

	private List<Window> _listOfWindows;
	private List<Container> _listOfRenderables;
	
	public ContainerRenderer()
	{
		_listOfRenderables = new LinkedList<Container>();
		_listOfWindows = new LinkedList<Window>();
	}
	
	public void registerRenderer(Container renderer)
	{
		_listOfRenderables.add(renderer);
	}

	
	public void notifyDraw()
	{
		generalNotifying((Container container) -> container.onDraw());
	}
	
	public void notifyMouseClick(int screenX, int screenY, int button)
	{
		generalNotifying((Container container) -> container.onMouseClick(screenX, screenY, button));
	}
	
	public void notifyMouseScrolled(int amount)
	{
		generalNotifying((Container container) -> container.onMouseScrolled(amount));
	}
	
	public void notifyKeyPressed(int keyCode)
	{
		generalNotifying((Container container) -> container.onKeyPressed(keyCode));
	}
	
	public void notifyKeyUp(int keyCode)
	{
		generalNotifying((Container container) -> container.onKeyUp(keyCode));
	}
	
	private void generalNotifying(Notifier notifyFunction)
	{
		//TODO determine on which control the focus is  
		for(Container container : _listOfRenderables)
		{
			notifyFunction.notifyRenderable(container);
		}
		
		for(Window window:_listOfWindows)
		{
			notifyFunction.notifyRenderable(window);
		}
	}

	@Override
	public void closed(Container container)
	{
		_listOfRenderables.remove(container);
	}
}