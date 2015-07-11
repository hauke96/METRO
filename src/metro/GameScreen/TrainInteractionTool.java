package metro.GameScreen;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Every class that can edit trains, lines, stations, ... should implement this interface to be able to be used.
 * 
 * @author hauke
 *
 */
public interface TrainInteractionTool
{
	/**
	 * Draws content of this tool.
	 * 
	 * @param sp The SpriteBatch.
	 * @param offset An offset for the click (e.g. for the map-offset)
	 */
	public void draw(SpriteBatch sp, Point2D offset);

	/**
	 * Does things when the left mouse button was clicked.
	 * 
	 * @param screenX The x-position of the click.
	 * @param screenY The y-position of the click.
	 * @param offset An offset for the click (e.g. for the map-offset)
	 */
	public void leftClick(int screenX, int screenY, Point2D offset);

	/**
	 * Does things when the right mouse button was clicked.
	 * 
	 * @param screenX The x-position of the click.
	 * @param screenY The y-position of the click.
	 * @param offset An offset for the click (e.g. for the map-offset)
	 */
	public void rightClick(int screenX, int screenY, Point2D offset);
}
