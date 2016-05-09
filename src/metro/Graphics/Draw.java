package metro.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import metro.METRO;
import metro.Settings;

/**
 * Provides lots of methods to draw lines, rects, circles, ... onto the screen
 * The Color is set for all following Draw-operations, so set the color and all following Draw-operations will use this color.
 * 
 * @author hauke
 *
 */

public class Draw
{
	private static ShapeRenderer __shapeRenderer;
	private static int __xOffset, __yOffset, __lineGap = 8;
	private static float __r, __g, __b, __a; // current color values
	private static BitmapFont __stdFont;

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
	 * Sets the default font of the Drawer. Every string that's drawn by this class will use this font.
	 * 
	 * @param font The new font of all texts.
	 */
	public static void setFont(BitmapFont font)
	{
		__stdFont = font;
	}

	/**
	 * Initializes the screen for drawing on it. It also stops the spriteBatch for a transparent background.
	 */
	private static void init()
	{
		METRO.__spriteBatch.end();
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

		if(__shapeRenderer == null)
		{
			__shapeRenderer = new ShapeRenderer();
		}
		__shapeRenderer.setProjectionMatrix(METRO.__camera.combined);
		__shapeRenderer.begin(ShapeType.Line);
		__shapeRenderer.setColor(__r, __g, __b, __a);
	}

	/**
	 * Resets the screen and the spriteBatch to the default values so that METRO can use it normally. The drawing has finished at this point.
	 */
	private static void reset()
	{
		__shapeRenderer.end();
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
		init();
		__shapeRenderer.circle(x + diameter / 2 + __xOffset,
			y + diameter / 2 + __yOffset,
			diameter / 2,
			Integer.parseInt(Settings.getInstance().getOld("amount.segments").toString()));
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
		Rect(x, y, width, height, 1, spriteBatch);
	}

	/**
	 * Draws a rectangle on a specific sprite batch.
	 * 
	 * @param x x-coordinate (left upper corner)
	 * @param y y-coordinate (left upper corner)
	 * @param width Width of rectangle.
	 * @param height Height of rectangle.
	 * @param w Width of the lines
	 * @param spriteBatch The sprite batch to draw on.
	 */
	public static void Rect(int x, int y, int width, int height, int w, SpriteBatch spriteBatch)
	{
		width -= w;
		height -= w;
		int wh = w / 2;
		Line(x + wh, y + wh, x + wh, y + height + wh, w, spriteBatch); // left
		Line(x, y + height + wh, x + width, y + height + wh, w, spriteBatch); // bottom
		Line(x + wh + width, y, x + wh + width, y + height + 1, w, spriteBatch); // right
		Line(x, y + wh, x + width, y + wh, w, spriteBatch); // top
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
		Line(x1, y1, x2, y2, 1, METRO.__spriteBatch);
	}

	/**
	 * Draws a line with a specified width from one coordinate pair to another. This uses the METRO.__spriteBatch as default.
	 * 
	 * @param x1 First x-coordinate.
	 * @param y1 First y-coordinate.
	 * @param x2 Second x-coordinate.
	 * @param y2 Second y-coordinate.
	 * @param w The width of the line.
	 */
	public static void Line(int x1, int y1, int x2, int y2, int w)
	{
		Line(x1, y1, x2, y2, w, METRO.__spriteBatch);
	}

	/**
	 * Draws a line from one coordinate pair to another. This uses the METRO.__spriteBatch as default.
	 * 
	 * @param x1 First x-coordinate.
	 * @param y1 First y-coordinate.
	 * @param x2 Second x-coordinate.
	 * @param y2 Second y-coordinate.
	 */
	public static void Line(double x1, double y1, double x2, double y2)
	{
		Line((int)x1, (int)y1, (int)x2, (int)y2, 1, METRO.__spriteBatch);
	}

	/**
	 * Draws a line from one coordinate pair to another onto the given sprite batch.
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
		spriteBatch.end();
		spriteBatch.begin();
		if(x1 != x2 && y1 != y2) // -> diagonal line -> shapeRenderer for antialising
		{
			Fill.setColor(new Color(__r, __g, __b, __a));
			Fill.Line(x1, y1, x2, y2, w, spriteBatch);
		}
		else
		// -> straight line -> texture for non-antialised drawing
		{
			x1 += __xOffset;
			x2 += __xOffset;
			y1 += __yOffset;
			y2 += __yOffset;

			spriteBatch.setColor(__r, __g, __b, __a);
			int offset = -w / 2;
			for(int i = 0; i <= w; i++)
			{
				spriteBatch.draw(METRO.__iconSet.getTexture(),
					x1,
					y1,
					x2 - x1 != 0 ? x2 - x1 + 1 : x2 - x1 + offset + i,
					y2 - y1 != 0 ? y2 - y1 : y2 - y1 + offset + i,
					20, 0, 1, 1,
					false,
					false);
			}
		}
		spriteBatch.setColor(1, 1, 1, 1);
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
		String(text, x, y, __stdFont);
	}

	/**
	 * Draws a string with the __stdFont (default font).
	 * 
	 * @param text The text you want to draw.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param spriteBatch The sprite batch to draw on.
	 */
	public static void String(String text, int x, int y, SpriteBatch spriteBatch)
	{
		String(text, x, y, spriteBatch, __stdFont);
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
		String(text, x, y, METRO.__spriteBatch, font);
	}

	/**
	 * Draws a text onto the sprite batch.
	 * 
	 * @param text The text you want to draw.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param spriteBatch The sprite batch to draw on
	 * @param font The font of the text
	 */
	public static void String(String text, int x, int y, SpriteBatch spriteBatch, BitmapFont font)
	{
		int stringHeight = (int)font.getCache().getBounds().height, vOffset = 0;
		String[] lines = text.split("\n"); // split by new line

		for(String line : lines)
		{
			font.setColor(__r, __g, __b, __a);
			font.draw(spriteBatch, line, x + __xOffset, y + vOffset + __yOffset);

			vOffset += stringHeight + __lineGap; // y-pos for next line
		}

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
		int stringHeight = (int)__stdFont.getCache().getBounds().height, vOffset = 0, rowCount = 0;
		String[] segments = text.split("\n"); // split by new line

		for(String segment : segments)
		{
			String[] subSegments = segment.split(" "); // each word
			String line = "";

			// recunstruct string with length < area width
			for(int i = 0; i < subSegments.length; ++i)
			{
				if(Draw.getStringSize(line + " " + subSegments[i]).width >= width) // if next addition would be out of area
				{
					Draw.String(line, x, y + vOffset);
					vOffset += stringHeight + __lineGap; // y-pos for next line
					line = subSegments[i] + " "; // choose first char for next line
					++rowCount;
				}
				else
				// if new addition is in area
				{
					line += subSegments[i] + " ";
				}
			}
			Draw.String(line, x, y + vOffset);
			vOffset += stringHeight + __lineGap; // y-pos for next line
			++rowCount;
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
		return getStringSize(text, __stdFont);
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
		if(font == null) return new Dimension();
		TextBounds bounds = font.getBounds(text);
		return new Dimension((int)bounds.width, ((int)bounds.height + __lineGap) * amountOfLines(text) - __lineGap);
	}

	/**
	 * Gets the amount of lines of the given text.
	 * 
	 * @param text Some text.
	 * @return The amount of lines of the given text.
	 */
	private static int amountOfLines(String text)
	{
		String[] array = text.split("\n");
		return array.length;
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

		for(String segment : segments)
		{
			String[] subSegments = segment.split(" "); // each word
			String line = "";

			// recunstruct string with length < area width
			for(int i = 0; i < subSegments.length; ++i)
			{
				if(Draw.getStringSize(line + " " + subSegments[i]).width >= width) // if next addition would be out of area
				{
					++rowCount;
					line = subSegments[i] + " "; // choose first char for next line
				}
				else
				// if new addition is in area
				{
					line += subSegments[i] + " ";
				}
			}
			++rowCount;
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
		Image(image, new Rectangle(x + __xOffset, y + __yOffset, image.getRegionWidth(), image.getRegionHeight()));
	}

	/**
	 * Draws an image to the sprite batch.
	 * 
	 * @param image The TextureRegion (i call it "image" ;) ) that should be draw.
	 * @param position The position as Rectangle of the image with x, y, width and height.
	 */
	public static void Image(TextureRegion image, Rectangle position)
	{
		METRO.__spriteBatch.draw(image, position.x + __xOffset, position.y + __yOffset, position.width, position.height);
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
		METRO.__spriteBatch.draw(image.getTexture(), position.x + __xOffset, position.y + __yOffset, position.width, position.height, areaOnImage.x, areaOnImage.y,
			areaOnImage.width,
			areaOnImage.height, false, true);
	}

	/**
	 * Draws a rotated part of a texture.
	 * 
	 * @param image The TextureRectangle (i call it "image" ;) ) from which an area should be drawn.
	 * @param position The position of the image on the screen.
	 * @param areaOnImage The area of the image on the given texture.
	 * @param rotationCenterX The center of rotation.
	 * @param rotationCenterY The center of rotation.
	 * @param rotation The rotation in clockwise direction.
	 */
	public static void Image(TextureRegion image, Rectangle position, Rectangle areaOnImage, int rotationCenterX, int rotationCenterY, float rotation)
	{
		METRO.__spriteBatch.draw(image.getTexture(),
			position.x + __xOffset,
			position.y + __yOffset,
			rotationCenterX,
			rotationCenterY,
			position.width,
			position.height,
			1,
			1,
			rotation,
			areaOnImage.x,
			areaOnImage.y,
			areaOnImage.width,
			areaOnImage.height,
			false,
			true);
	}
}