/**
 * Provides a Button with basic functions.
 */
package WindowControls;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

import Game.METRO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * @author Hauke
 *
 */
public class Button implements ControlElement
{
	private TextureRegion _texture;
	private Rectangle _position,
		_positionOnImage;
	private Window _windowHandle;
	private boolean hasBeenClicked = false; // true if control has been clicked since last check
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();

	/**
	 * Creates a new Button.
	 * @param position The position on the screen/window (absolute).
	 * @param positionOnImage The position of the button texture on an image (absolute).
	 * @param texture The button texture.
	 */
	public Button(Rectangle position, Rectangle positionOnImage, TextureRegion texture)
	{
		this(position, positionOnImage, texture, null);
	}
	/**
	 * Creates a new Button.
	 * @param position The position on the screen/window (absolute).
	 * @param positionOnImage The position of the button texture on an image (absolute).
	 * @param texture The button texture.
	 * @param window The window it should be on.
	 */
	public Button(Rectangle position, Rectangle positionOnImage, TextureRegion texture, Window window)
	{
		_texture = texture;
		_position = position;
		_positionOnImage = positionOnImage;
		_windowHandle = window;
		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
	}
	/**
	 * Creates a new button.
	 * @param position The position on the screen/window (absolute).
	 * @param text The text of the button.
	 * @param window The window it should be on.
	 */
	public Button(Rectangle position, String text, Window window)
	{
		//TODO: Recreate creation stuff for buttons
//		BufferedImage bufferedImage = new BufferedImage(position.width, position.height, BufferedImage.TYPE_INT_ARGB);
//		
//		Graphics2D g2d = bufferedImage.createGraphics();
//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2d.setColor(METRO.__metroBlue);
//		g2d.setFont(METRO.__stdFont);
//
//		g2d.drawString(text, position.width / 2 - g2d.getFontMetrics(METRO.__stdFont).stringWidth(text) / 2, 
//			g2d.getFontMetrics(METRO.__stdFont).getHeight() - 5);
//		
//		g2d.drawRect(0, 0, position.width - 1, position.height - 1);
//
//		_texture = bufferedImage;
		
		_position = position;
		_positionOnImage = new Rectangle(0, 0, _position.width, _position.height);
		_windowHandle = window;
		if(_windowHandle != null) _windowHandle.addControlElement(this); // there won't be any doubles, don't worry ;)
	}
	/**
	 * Creates a new button.
	 * @param position The position on screen (absolute).
	 * @param text The text of the button.
	 */
	public Button(Rectangle position, String text)
	{
		this(position, text, null);
	}
	/**
	 * Checks if the mouse is in the button area or has been clicked a while ago.
	 * @param x x-coordinate of mouse.
	 * @param y y-coordinate of mouse.
	 * @return True if mouse is in area or Button has been pressed.
	 */
	public boolean isPressed(int x, int y)
	{
		if(x >= _position.x 
			&& x <= _position.x + _position.width
			&& y >= _position.y
			&& y <= _position.y + _position.height
			|| hasBeenClicked)
		{
			hasBeenClicked = false;
			return true;
		}
		return false;
	}
	/**
	 * Returns if the button has been clicked since last frame.
	 * @return True is button has been pressed.
	 */
	public boolean isPressed()
	{
		boolean temp = hasBeenClicked;
		hasBeenClicked = false;
		return temp;
	}
	/**
	 * Draws the button with its text/texture.
	 */
	public void draw(SpriteBatch sp)
	{
//		sp.draw(_texture.getTexture(), 
//				_position.x, 
//				_position.y, 
//				_position.width, 
//				_position.height, 
//				_positionOnImage.x, 
//				_positionOnImage.y, 
//				_positionOnImage.width, 
//				_positionOnImage.height, 
//				false, true); 
		//TASK!: finish button drawing
		METRO.__spriteBatch.end();
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0, 0, 0, 1);
		shapeRenderer.rect(0, 0, 100, 100);
		shapeRenderer.circle(50, 50, 20);
		shapeRenderer.end();
		
		Gdx.gl.glDisable(GL30.GL_BLEND);
		METRO.__spriteBatch.begin();
	}
	/**
	 * Sets the position of the button.
	 */
	public void setPosition(Point newPosition)
	{
		_position = new Rectangle(newPosition.x, newPosition.y, _position.width, _position.height);
	}
	/**
	 * Returns the position of the button as point.
	 */
	public Point getPosition()
	{
		return new Point(_position.x, _position.y);
	}
	/**
	 * Check if mouse is on the button and sets its clicked-flag to true (isPressed() would return true now).
	 */
	public boolean clickOnControlElement()
	{
		Point mPos = MouseInfo.getPointerInfo().getLocation();
		
		if(mPos.x >= _position.x + _windowHandle.getPosition().x
			&& mPos.x <= _position.x + _position.width + _windowHandle.getPosition().x
			&& mPos.y >= _position.y + _windowHandle.getPosition().y
			&& mPos.y <= _position.y + _position.height + _windowHandle.getPosition().y)
		{
			hasBeenClicked = true;
			return true;
		}
		return false;
	}
	public void update() 
	{
	}
}