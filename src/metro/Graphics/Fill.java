package metro.Graphics;

import com.badlogic.gdx.Gdx;
import java.awt.Color;
import java.awt.Rectangle;

import metro.METRO;
import metro.Common.Game.Settings;

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
	private static ShapeRenderer __shapeRenderer = new ShapeRenderer();
	private static int __xOffset, __yOffset;
	private static float __r, __g, __b, __a; // current color values

	/**
	 * Sets the offset for drawing things.
	 * 
	 * @param offsetX The offset for the x-coordinates.
	 * @param offsetY The offset for the y-coordinates.
	 */
	public static void setOffset(int offsetX, int offsetY)
	{
		__xOffset = offsetX;
		__yOffset = offsetY;
	}

	/**
	 * Initializes the screen for drawing on it. It also stops the spriteBatch for a transparent background.
	 */
	private static void init(SpriteBatch spriteBatch)
	{
		spriteBatch.end();
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

		__shapeRenderer.setProjectionMatrix(METRO.__camera.combined);
		__shapeRenderer.begin(ShapeType.Filled);
		__shapeRenderer.setColor(__r, __g, __b, __a);
	}

	/**
	 * Resets the screen and the spriteBatch to the default values so that METRO can use it normally. The drawing has finished at this point.
	 */
	private static void reset(SpriteBatch spriteBatch)
	{
		__shapeRenderer.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);
		spriteBatch.begin();
	}

	/**
	 * Sets the current color. Everything after this will be drawn in this color.
	 * 
	 * @param color Color to set.
	 */
	public static void setColor(Color color)
	{
		__r = color.getRed() / 255f;
		__g = color.getGreen() / 255f;
		__b = color.getBlue() / 255f;
		__a = color.getAlpha() / 255f;
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
		init(METRO.__spriteBatch);
		__shapeRenderer.circle(x + diameter / 2 + __xOffset,
			y + diameter / 2 + __yOffset,
			diameter / 2,
			Integer.parseInt(Settings.getInstance().getOld("amount.segments").toString()));
		reset(METRO.__spriteBatch);
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
		Rect(x, y, width, height, METRO.__spriteBatch);
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
		init(spriteBatch);
		__shapeRenderer.rect(x + __xOffset, y + __yOffset, width, height);
		reset(spriteBatch);
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
		init(spriteBatch);
		__shapeRenderer.rectLine(x1 + __xOffset, y1 + __yOffset, x2 + __xOffset, y2 + __yOffset, w);
		reset(spriteBatch);
	}
}
