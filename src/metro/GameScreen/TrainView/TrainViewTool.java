package metro.GameScreen.TrainView;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface TrainViewTool
{
	/**
	 * Draws content of this tool.
	 * 
	 * @param sp The SpriteBatch.
	 */
	public void draw(SpriteBatch sp, Point2D offset);

	/**
	 * Does things when the left mouse button was clicked.
	 * 
	 * @param screenX The x-position of the click.
	 * @param screenY The y-position of the click.
	 */
	public void leftClick(int screenX, int screenY, Point2D offset);

	/**
	 * Does things when the right mouse button was clicked.
	 * 
	 * @param screenX The x-position of the click.
	 * @param screenY The y-position of the click.
	 */
	public void rightClick(int screenX, int screenY, Point2D offset);
}
