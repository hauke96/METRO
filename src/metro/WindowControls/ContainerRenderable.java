package metro.WindowControls;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ContainerRenderable
{
	//TODO implement on... callback methods here that are also in the ControlElement class.
	
	public void onDraw(SpriteBatch sp, Point mapOffset);
	
	public void onMouseClick(int screenX, int screenY, int button);
	
	public void onMouseScrolled(int amount);
	
	public void onKeyPressed(int keyCode);
	
	public void onKeyUp(int keyCode);
}
