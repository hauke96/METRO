package metro.WindowControls;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This renderer can't do any special and is the default one for container controls.
 * 
 * @author hauke
 *
 */
public class BasicContainerRenderer extends ContainerRendererObservable implements ContainerRenderable
{

	@Override
	public void onDraw(SpriteBatch sp, Point mapOffset)
	{
		notifyDraw(sp, mapOffset);
	}

	@Override
	public void onMouseClick(int screenX, int screenY, int button)
	{
		notifyMouseClick(screenX, screenY, button);
	}

	@Override
	public void onMouseScrolled(int amount)
	{
		notifyMouseScrolled(amount);
	}

	@Override
	public void onKeyPressed(int keyCode)
	{
		notifyKeyPressed(keyCode);
	}

	@Override
	public void onKeyUp(int keyCode)
	{
		notifyKeyUp(keyCode);
	}

}
