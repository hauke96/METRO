package metro.GameScreen.MainView.NotificationView;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.WindowControls.List;

/**
 * This is a kind of log where game-concerned events pop up and are shown.
 * 
 * @author hauke
 *
 */
public class NotificationArea extends GameScreen implements NotificationSubscriber
{
	private boolean _isActive,
		_isExpanded;
	private int _height,
		_width,
		_headerHeight;
	private Color _backGroundColor;
	private List _entryList;

	private final static NotificationArea __INSTANCE = new NotificationArea();

	/**
	 * Creates a notification area with no entries.
	 */
	private NotificationArea()
	{
		_backGroundColor = new Color(255, 255, 255, 165);
		_height = 250;
		_headerHeight = 25;
		_width = METRO.__SCREEN_SIZE.width;
		_isExpanded = true;
		_entryList = new List(new Rectangle(0, METRO.__SCREEN_SIZE.height - _height + _headerHeight, _width - 5, _height - METRO.__titleBarHeight + _headerHeight),
			new ArrayList<String>(), null, true);
		registerControl(_entryList);
		_entryList.setDecoration(false);
		_entryList.setTransparency(165);
		_entryList.setStickiness(true);
		addMessage("Game started", NotificationType.GAME_INFO);
		NotificationServer.subscribe(this);
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
	 * @param newWidth The new width in pixel.
	 */
	public void setWidth(int newWidth)
	{
		_width = newWidth;
		_entryList.setSize(new Rectangle(0, METRO.__SCREEN_SIZE.height - _height + _headerHeight, _width - 5, _height - METRO.__titleBarHeight + _headerHeight));
	}

	@Override
	public void addMessage(String message, NotificationType type)
	{
		// TODO add entry-types to the list control to control e.g. the color and translate NotificationTypes into ListEntryTypes.
		_entryList.addElement(message);
	}

	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		Fill.setColor(_backGroundColor);
		Fill.Rect(0, METRO.__SCREEN_SIZE.height - _height, _width, _height);

		if(_isExpanded)
		{
			_entryList.draw();
		}

		Draw.setColor(METRO.__metroBlue);
		Draw.Line(0, METRO.__SCREEN_SIZE.height - _height, _width, METRO.__SCREEN_SIZE.height - _height);
		Draw.Line(0, METRO.__SCREEN_SIZE.height - _height + _headerHeight, _width, METRO.__SCREEN_SIZE.height - _height + _headerHeight);

		int length = Draw.getStringSize("Notifications:").width;
		Draw.setColor(METRO.__metroRed);
		Draw.String("Notifications:", 15, METRO.__SCREEN_SIZE.height - _height + 5);
		Draw.Line(13, METRO.__SCREEN_SIZE.height - _height + 21, 15 + length, METRO.__SCREEN_SIZE.height - _height + 21);

		Draw.Image(METRO.__iconSet,
			new Rectangle(_width - _headerHeight, METRO.__SCREEN_SIZE.height - _height, _headerHeight, _headerHeight),
			new Rectangle(0, 228 + (_isExpanded ? _headerHeight : 0), _headerHeight, _headerHeight));
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		if(METRO.__SCREEN_SIZE.height - _height <= screenY
			&& screenY <= METRO.__SCREEN_SIZE.height - _height + _headerHeight
			&& screenX <= _width)
		{
			_isExpanded ^= true; // flip state of boolean
			_height = _isExpanded ? 250 : METRO.__titleBarHeight + _headerHeight; // TODO change 47 to "_height + METRO.__windowTitleHeight()" or something
			_entryList.setState(_isExpanded);
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
