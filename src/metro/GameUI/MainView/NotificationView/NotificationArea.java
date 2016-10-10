package metro.GameUI.MainView.NotificationView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameUI.Screen.GameScreen;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.UI.Renderable.Container.Panel;
import metro.UI.Renderable.Controls.List;

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
	private Panel _panel;

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

		_panel = new Panel(new Rectangle(0,
			METRO.__SCREEN_SIZE.height - _height + _headerHeight,
			_width - 5,
			_height - METRO.__titleBarHeight + _headerHeight));

		_entryList = new List(new Rectangle(0,
			METRO.__SCREEN_SIZE.height - _height + _headerHeight,
			_width - 5,
			_height - METRO.__titleBarHeight + _headerHeight),
			new ArrayList<String>(), true);

		_entryList.setDecoration(false);
		_entryList.setTransparency(165);
		_entryList.setStickiness(true);
		addMessage("Game started", NotificationType.GAME_INFO);
		NotificationServer.subscribe(this);

		_panel.add(_entryList);
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

		Rectangle oldPanelArea = (Rectangle)_panel.getArea().clone();
		oldPanelArea.setSize(oldPanelArea.height, _width - 5);
		_panel.setArea(oldPanelArea);
		
		_entryList.setArea(new Rectangle(0, METRO.__SCREEN_SIZE.height - _height + _headerHeight, _width - 5, _height - METRO.__titleBarHeight + _headerHeight));
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
		if(screenY >= METRO.__SCREEN_SIZE.height - _height
			&& screenY <= METRO.__SCREEN_SIZE.height - _height + _headerHeight
			&& screenX <= _width)
		{
			_isExpanded ^= true; // flip state of boolean
			_height = _isExpanded ? 250 : METRO.__titleBarHeight + _headerHeight;
			_entryList.setState(_isExpanded);
			_panel.setPosition(new Point(_entryList.getPosition().x, METRO.__SCREEN_SIZE.height - _height + _headerHeight));
		}
	}

	@Override
	public boolean isActive()
	{
		return _isActive;
	}

	@Override
	public boolean isHovered()
	{
		return false;
	}
}
