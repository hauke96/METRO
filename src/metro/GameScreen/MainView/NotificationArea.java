package metro.GameScreen.MainView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;

/**
 * This is a kind of log where game-concerned events pop up and are shown.
 * 
 * @author hauke
 *
 */
public class NotificationArea extends GameScreen
{
	private boolean _isActive,
		_isExpanded;
	private int _height,
		_width;
	private LinkedList<String> _times, // the time of an entry
		_entries; // the entry itself
	private Color _backGroundColor;

	private final static NotificationArea __INSTANCE = new NotificationArea();

	/**
	 * Creates a notification area with no entries.
	 */
	private NotificationArea()
	{
		_times = new LinkedList<>();
		_entries = new LinkedList<>();
		_backGroundColor = new Color(255, 255, 255, 125);
		_height = 250;
		_width = METRO.__SCREEN_SIZE.width;
		_isExpanded = true;
	}

	/**
	 * @return The instance of the game state. There can only be one instance per game.
	 */
	public static NotificationArea getInstance()
	{
		return __INSTANCE;
	}

	/**
	 * Sets the width of the notification area. It still begins at (0,SCREEN_HEIGHT - height) but may not end at the screens edge.
	 * 
	 * @param newWidth The net width in pixel.
	 */
	public void setWidth(int newWidth)
	{
		_width = newWidth;
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		Fill.setColor(_backGroundColor);
		Fill.Rect(0, METRO.__SCREEN_SIZE.height - _height, _width, _height);

		Draw.setColor(METRO.__metroBlue);
		Draw.Line(0, METRO.__SCREEN_SIZE.height - _height, _width, METRO.__SCREEN_SIZE.height - _height);
		Draw.Line(0, METRO.__SCREEN_SIZE.height - _height + 25, _width, METRO.__SCREEN_SIZE.height - _height + 25);

		int length = Draw.getStringSize("Notifications:").width;
		Draw.setColor(METRO.__metroRed);
		Draw.String("Notifications:", 15, METRO.__SCREEN_SIZE.height - _height + 5);
		Draw.Line(13, METRO.__SCREEN_SIZE.height - _height + 21, 15 + length, METRO.__SCREEN_SIZE.height - _height + 21);

		Draw.Image(METRO.__iconSet,
			new Rectangle(_width - 25, METRO.__SCREEN_SIZE.height - _height, 25, 25),
			new Rectangle(0, 228 + (_isExpanded ? 25 : 0), 25, 25));

		// if(_isExpanded)
		// {
		// TODO draw entries and times
		// }
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(METRO.__SCREEN_SIZE.height - _height <= screenY && screenY <= METRO.__SCREEN_SIZE.height - _height + 25)
		{
			_isExpanded ^= true; // flip state of boolean
			_height = _isExpanded ? 250 : 47; // TODO change 47 to "_height + METRO.__windowTitleHeight()" or something
		}
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
	}

	@Override
	public void keyDown(int keyCode)
	{
	}

	@Override
	public void mouseScrolled(int amount)
	{
		// TODO scroll through entries
	}

	@Override
	public boolean isActive()
	{
		return _isActive;
	}

	@Override
	public void reset()
	{
	}

	@Override
	public boolean isHovered()
	{
		return false;
	}
}
