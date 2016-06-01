package metro.WindowControls;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ContainerRendererObservable
{
	interface Notifier
	{
		void notifyRenderable(ContainerRenderable renderable);
	}
	
	private List<ContainerRenderable> _listOfRenderables;
	
	public ContainerRendererObservable()
	{
		_listOfRenderables = new LinkedList<ContainerRenderable>();
	}
	
	public void registerRenderer(ContainerRenderable renderer)
	{
		_listOfRenderables.add(renderer);
	}

	
	public void notifyDraw(SpriteBatch sp, Point mapOffset)
	{
	}
	
	public void notifyMouseClick(int screenX, int screenY, int button)
	{
		generalNotifying((ContainerRenderable renderable) -> renderable.onMouseClick(screenX, screenY, button));
	}
	
	public void notifyMouseScrolled(int amount)
	{
		generalNotifying((ContainerRenderable renderable) -> renderable.onMouseScrolled(amount));
	}
	
	public void notifyKeyPressed(int keyCode)
	{
		generalNotifying((ContainerRenderable renderable) -> renderable.onKeyPressed(keyCode));
	}
	
	public void notifyKeyUp(int keyCode)
	{
		generalNotifying((ContainerRenderable renderable) -> renderable.onKeyUp(keyCode));
	}
	
	private void generalNotifying(Notifier notifyFunction)
	{
		for(ContainerRenderable renderable : _listOfRenderables)
		{
			notifyFunction.notifyRenderable(renderable);
		}
	}
}