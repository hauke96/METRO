package metro.Graphics;

import com.badlogic.gdx.Gdx;
import java.awt.Color;
import java.awt.Rectangle;

import metro.METRO;
import metro.Settings;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Provides lots of methods to fill several simple forms like circles, rectangles, ...
 * The Color is set for all following Fill-operations, so set the color and all following Fill-operations will use this color.
 * 
 * @author hauke
 *
 */

public class Fill
{
	private static ShapeRenderer shapeRenderer = new ShapeRenderer();
	private static float r, g, b, a; // current color values

	/**
	 * Initializes the screen for drawing on it. It also stops the spriteBatch for a transparent background.
	 */
	private static void init()
	{
		METRO.__spriteBatch.end();
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

		shapeRenderer.setProjectionMatrix(METRO.__camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(r, g, b, a);
	}

	/**
	 * Resets the screen and the spriteBatch to the default values so that METRO can use it normally. The drawing has finished at this point.
	 */
	private static void reset()
	{
		shapeRenderer.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);
		METRO.__spriteBatch.begin();
	}

	/**
	 * Sets the current color. Everything after this will be drawn in this color.
	 * 
	 * @param color Color to set.
	 */
	public static void setColor(Color color)
	{
		r = color.getRed() / 255f;
		g = color.getGreen() / 255f;
		b = color.getBlue() / 255f;
		a = color.getAlpha() / 255f;
	}

	/**
	 * Draws a simple circle.
	 * 
	 * @param x x-Position (upper left corner of circle)
	 * @param y y-Position (upper left corner of circle)
	 * @param diameter diameter=width=height of circle
	 */
	public static void Circle(int x, int y, int diameter)
	{
		init();
		shapeRenderer.circle(x + diameter / 2, y + diameter / 2, diameter / 2, Integer.parseInt(Settings.getOld("amount.segments").toString()));
		reset();
	}

	/**
	 * Draws a rectangle.
	 * 
	 * @param x x-coordinate (left upper corner)
	 * @param y y-coordinate (left upper corner)
	 * @param width Width of rectangle.
	 * @param height Height of rectangle.
	 */
	public static void Rect(int x, int y, int width, int height)
	{
		init();
		shapeRenderer.rect(x, y, width, height);
		reset();
	}

	/**
	 * Draws a rectangle onto a given spriteBatch.
	 * 
	 * @param x x-coordinate (left upper corner)
	 * @param y y-coordinate (left upper corner)
	 * @param width Width of rectangle.
	 * @param height Height of rectangle.
	 * @param spriteBatch The spriteBatch to use here.
	 */
	public static void Rect(int x, int y, int width, int height, SpriteBatch spriteBatch)
	{
		init();
		shapeRenderer.rect(x, y, width, height);
		reset();
	}

	/**
	 * Fills a rectangle with the given awt.Rectangle onto the METRO.__spriteBatch.
	 * 
	 * @param rect The rectangle to draw.
	 */
	public static void Rect(Rectangle rect)
	{
		Rect(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * Fills a rectangle with the given awt.Rectangle onto a given spriteBatch.
	 * 
	 * @param rect The rectangle to draw.
	 * @param spriteBatch The spriteBatch to use here.
	 */
	public static void Rect(Rectangle rect, SpriteBatch spriteBatch)
	{
		Rect(rect.x, rect.y, rect.width, rect.height, spriteBatch);
	}

	/**
	 * Draws a thick line from one coordinate pair to another onto the given sprite batch.
	 * 
	 * @param x1 First x-coordinate.
	 * @param y1 First y-coordinate.
	 * @param x2 Second x-coordinate.
	 * @param y2 Second y-coordinate.
	 * @param w The width of the line
	 * @param spriteBatch The spriteBatch to draw on.
	 */
	public static void Line(int x1, int y1, int x2, int y2, int w, SpriteBatch spriteBatch)
	{
		init();
		shapeRenderer.rectLine(x1, y1, x2, y2, w);
		reset();
	}
}
