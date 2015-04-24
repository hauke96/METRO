package metro.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import metro.Game.METRO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Provides lots of methods to draw lines, rects, circles, ... onto the screen
 * The Color is set for all following Draw-operations, so set the color and all following Draw-operations will use this color.
 * 
 * @author hauke
 *
 */

public class Draw
{
	private static ShapeRenderer shapeRenderer = new ShapeRenderer();
	private static float r, g, b, a; // current color values

	/**
	 * Initializes the screen for drawing on it. It also stops the spriteBatch for a transparent background.
	 * 
	 * @param shape Defines the shape type of the object that should be drawn.
	 */
	private static void init()
	{
		METRO.__spriteBatch.end();
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

		shapeRenderer.setProjectionMatrix(METRO.__camera.combined);
		shapeRenderer.begin(ShapeType.Line);
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
		shapeRenderer.circle(x + diameter / 2, y + diameter / 2, diameter / 2, 64);
		reset();
	}

	/**
	 * Draws a rectangle onto the METRO.__spriteBatch.
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
	 * Draws a rectangle on a specific sprite batch.
	 * 
	 * @param x x-coordinate (left upper corner)
	 * @param y y-coordinate (left upper corner)
	 * @param width Width of rectangle.
	 * @param height Height of rectangle.
	 * @param spriteBatch The sprite batch to draw on.
	 */
	public static void Rect(int x, int y, int width, int height, SpriteBatch spriteBatch)
	{
		spriteBatch.setColor(r, g, b, a);
		spriteBatch.draw(METRO.__iconSet.getTexture(), x, y, 1, height, 20, 0, 1, 1, false, false); // left
		spriteBatch.draw(METRO.__iconSet.getTexture(), x, y + height, width, 1, 20, 0, 1, 1, false, false); // bottom
		spriteBatch.draw(METRO.__iconSet.getTexture(), x + width, y, 1, height, 20, 0, 1, 1, false, false); // right
		spriteBatch.draw(METRO.__iconSet.getTexture(), x, y, width, 1, 20, 0, 1, 1, false, false); // top
		spriteBatch.setColor(1, 1, 1, 1);
	}

	/**
	 * Draws a rectangle with the given awt.Rectangle onto the METRO.__spriteBatch.
	 * 
	 * @param rect The rectangle to draw.
	 */
	public static void Rect(Rectangle rect)
	{
		Rect(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * Draws a rectangle with the given awt.Rectangle onto a given spriteBatch.
	 * 
	 * @param rect The rectangle to draw.
	 * @param spriteBatch The spriteBatch to use here.
	 */
	public static void Rect(Rectangle rect, SpriteBatch spriteBatch)
	{
		Rect(rect.x, rect.y, rect.width, rect.height, spriteBatch);
	}

	/**
	 * Draws a line from one coordinate pair to another. This uses the METRO.__spriteBatch as default.
	 * 
	 * @param x1 First x-coordinate.
	 * @param y1 First y-coordinate.
	 * @param x2 Second x-coordinate.
	 * @param y2 Second y-coordinate.
	 */
	public static void Line(int x1, int y1, int x2, int y2)
	{
		Line(x1, y1, x2, y2, METRO.__spriteBatch);
	}

	/**
	 * Draws a line from one coordinate pair to another onto the given sprite batch.
	 * 
	 * @param x1 First x-coordinate.
	 * @param y1 First y-coordinate.
	 * @param x2 Second x-coordinate.
	 * @param y2 Second y-coordinate.
	 * @param spriteBatch The spriteBatch to draw on.
	 */
	public static void Line(int x1, int y1, int x2, int y2, SpriteBatch spriteBatch)
	{
		spriteBatch.end();
		spriteBatch.begin();
		if (x1 != x2 && y1 != y2) // -> diagonal line -> shapeRenderer for antialising
		{
			init();
			shapeRenderer.line(x1, y1, x2, y2);
			reset();
		}
		else // -> straight line -> texture for non-antialised drawing
		{
			spriteBatch.setColor(r, g, b, a);
			spriteBatch.draw(METRO.__iconSet.getTexture(), x1, y1, x2 - x1 + 1, y2 - y1 + 1, 20, 0, 1, 1, false, false);
			spriteBatch.setColor(1, 1, 1, 1);
		}
	}

	/**
	 * Draws a string with the __stdFont (default font).
	 * 
	 * @param text The text you want to draw.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public static void String(String text, int x, int y)
	{
		String(text, x, y, METRO.__stdFont);
	}

	/**
	 * Draws a text onto the sprite batch.
	 * 
	 * @param text The text you want to draw.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param font The font of the text
	 */
	public static void String(String text, int x, int y, BitmapFont font)
	{
		font.setColor(r, g, b, a);
		font.draw(METRO.__spriteBatch, text, x, y);
	}

	/**
	 * Draws a multi-line text with automatic line breaks.
	 * 
	 * @param text The text to draw.
	 * @param x The x-coord.
	 * @param y The y-coord.
	 * @param width The maximum width of the area where the text should be.
	 * @return Amount of lines/rows.
	 */
	public static int String(String text, int x, int y, int width)
	{
		int stringHeight = Draw.getStringSize(text).height, vOffset = 0, rowCount = 0;
		String[] segments = text.split("\n"); // split by new line

		for (String segment : segments)
		{
			String[] subSegments = segment.split(" "); // each word
			String line = "";

			// recunstruct string with length < area width
			for (int i = 0; i < subSegments.length; i++)
			{
				if (Draw.getStringSize(line + " " + subSegments[i]).width >= width) // if next addition would be out of area
				{
					Draw.String(line, x, y + vOffset);
					vOffset += stringHeight + 8; // y-pos for next line
					line = subSegments[i] + " "; // choose first char for next line
					rowCount++;
				}
				else
				// if new addition is in area
				{
					line += subSegments[i] + " ";
				}
			}
			Draw.String(line, x, y + vOffset);
			vOffset += stringHeight + 8; // y-pos for next line
			rowCount++;
		}

		return rowCount;
	}

	/**
	 * Calculates the size of a string when it's drawn onto the screen. This method usess the __stdFont (defualt font).
	 * 
	 * @param text The text whose size you want.
	 * @return The size as a Dimension (width, height).
	 */
	public static Dimension getStringSize(String text)
	{
		return getStringSize(text, METRO.__stdFont);
	}

	/**
	 * Calculates the size of a string when it's drawn onto the screen using the parameter "font".
	 * 
	 * @param text The text whose size you want.
	 * @param font The font that should be used to measure the size.
	 * @return The size as a Dimension (width, height).
	 */
	public static Dimension getStringSize(String text, BitmapFont font)
	{
		TextBounds bounds = font.getBounds(text);
		return new Dimension((int) bounds.width, (int) bounds.height);
	}

	/**
	 * Calculates the amount of lines needed to display the given string in the given space.
	 * 
	 * @param text The string that should be displayed.
	 * @param width The maximum width in pixel of the string.
	 * @return The amount of lines needed.
	 */
	public static int getStringLines(String text, int width)
	{
		int rowCount = 0;
		String[] segments = text.split("\n"); // split by new line

		for (String segment : segments)
		{
			String[] subSegments = segment.split(" "); // each word
			String line = "";

			// recunstruct string with length < area width
			for (int i = 0; i < subSegments.length; i++)
			{
				if (Draw.getStringSize(line + " " + subSegments[i]).width >= width) // if next addition would be out of area
				{
					rowCount++;
					line = subSegments[i] + " "; // choose first char for next line
				}
				else
				// if new addition is in area
				{
					line += subSegments[i] + " ";
				}
			}
			rowCount++;
		}

		return rowCount;
	}

	/**
	 * Draws an image with its original size to the spriteBatch.
	 * 
	 * @param image The TextureRegion (i call it "image" ;) ) that should be drawn.
	 * @param x The x-coordinate (upper left corner)
	 * @param y The y-coordinate (upper left corner)
	 */
	public static void Image(TextureRegion image, int x, int y)
	{
		Image(image, new Rectangle(x, y, image.getRegionWidth(), image.getRegionHeight()));
	}

	/**
	 * Draws an image to the sprite batch.
	 * 
	 * @param image The TextureRegion (i call it "image" ;) ) that should be draw.
	 * @param x The x-coordinate (upper left corner)
	 * @param y The y-coordinate (upper left corner)
	 * @param width The width of the area for the image (to scale it)
	 * @param height The height of the area for the image (to scale it)
	 */
	public static void Image(TextureRegion image, Rectangle position)
	{
		METRO.__spriteBatch.draw(image, position.x, position.y, position.width, position.height);
	}

	/**
	 * Draws an area of an image on the sprite batch.
	 * 
	 * @param image The TextureRectangle (i call it "image" ;) ) from which an area should be drawn.
	 * @param position The position of the image
	 * @param areaOnImage The area on the image that should be drawn.
	 */
	public static void Image(TextureRegion image, Rectangle position, Rectangle areaOnImage)
	{
		METRO.__spriteBatch.draw(image.getTexture(), position.x, position.y, position.width, position.height, areaOnImage.x, areaOnImage.y, areaOnImage.width,
				areaOnImage.height, false, true);
	}
}