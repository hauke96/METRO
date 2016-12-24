package metro.GameUI.MainView.NotificationView;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.badlogic.gdx.Input.Buttons;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
import metro.Common.Technical.Contract;
import metro.UI.Renderable.ActionObserver;
import metro.UI.Renderable.Container.AbstractContainer;
import metro.UI.Renderable.Container.Panel;
import metro.UI.Renderable.Controls.Canvas;
import metro.UI.Renderable.Controls.List;

/**
 * This is a kind of log where game-concerned events pop up and are shown.
 * 
 * @author hauke
 *
 */
public class NotificationArea implements NotificationSubscriber
{
	private boolean _isExpanded;
	private int _height,
		_width,
		_headerHeight;
	private Color _backGroundColor;
	private List _entryList;
	private Canvas _canvas;
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
			METRO.__SCREEN_SIZE.height - _height,
			_width - METRO.__getXOffset(),
			_height - METRO.__titleBarHeight));
		_panel.setDrawBorder(true, METRO.__metroBlue);

		_entryList = new List(new Rectangle(0,
			METRO.__SCREEN_SIZE.height - _height + _headerHeight,
			_width - METRO.__getXOffset() - 4,
			_height - _headerHeight - METRO.__titleBarHeight),
			new ArrayList<String>(), true);

		_entryList.setDecoration(false);
		_entryList.setTransparency(165);
		_entryList.setStickiness(true);

		_canvas = new Canvas(new Point(0, METRO.__SCREEN_SIZE.height - _height));
		_canvas.setPainter(() -> draw());
		_canvas.register(new ActionObserver()
		{
			public void clickedOnControl(Object arg)
			{
				Contract.Require(arg instanceof Integer);

				if(isInArea(METRO.__mousePosition.x, METRO.__mousePosition.y))
				{
					mouseClicked(METRO.__mousePosition.x, METRO.__mousePosition.y, (int)arg);
				}
			};
		});

		_panel.add(_entryList);
		_panel.add(_canvas);

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

		Rectangle oldPanelArea = (Rectangle)_panel.getArea().clone();
		oldPanelArea.setSize(_width - METRO.__getXOffset(), oldPanelArea.height);
		_panel.setArea(oldPanelArea);

		_entryList
			.setArea(new Rectangle(0, METRO.__SCREEN_SIZE.height - _height + _headerHeight, _width - METRO.__getXOffset() - 4, _height - METRO.__titleBarHeight + _headerHeight));
	}

	@Override
	public void addMessage(String message, NotificationType type)
	{
		// TODO add entry-types to the list control to control e.g. the color and translate NotificationTypes into ListEntryTypes.
		_entryList.addElement(message);
	}

	/**
	 * Draws the control.
	 */
	public void draw()
	{
		Fill.setColor(_backGroundColor);
		Fill.Rect(0, 0, _width, _headerHeight);

		Draw.setColor(METRO.__metroBlue);
		Draw.Line(0, 0, _width, 0);

		// TODO replace this by a label
		int length = Draw.getStringSize("Notifications:").width;
		Draw.setColor(METRO.__metroRed);
		Draw.String("Notifications:", 15, 5);
		Draw.Line(13, 21, 15 + length, 21);

		Draw.Image(METRO.__iconSet,
			new Rectangle(_width - _headerHeight, 0, _headerHeight, _headerHeight),
			new Rectangle(0, 228 + (_isExpanded ? _headerHeight : 0), _headerHeight, _headerHeight));
	}

	/**
	 * Reacts to a mouse click with the given position and button.
	 * It'll only have an effect when the click in inside the control.
	 * 
	 * @param screenX The x-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param mouseButton The mouse button.
	 */
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		Contract.Require(isInArea(screenX, screenY));

		if(mouseButton == Buttons.LEFT)
		{
			_isExpanded ^= true; // flip state of boolean
			_height = _isExpanded ? 250 : METRO.__titleBarHeight + _headerHeight + 5;
			_entryList.setState(_isExpanded);
			_entryList.setVisibility(_isExpanded);
			_panel.setPosition(new Point(0, METRO.__SCREEN_SIZE.height - _height));
		}
	}

	/**
	 * Checks if the given coordinates are inside this control.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return True when point is inside, false otherwise.
	 */
	public boolean isInArea(int x, int y)
	{
		return y >= METRO.__SCREEN_SIZE.height - _height
			&& y <= METRO.__SCREEN_SIZE.height - _height + _headerHeight
			&& x <= _width;
	}
	
	public AbstractContainer getBackgroundPanel()
	{
		return _panel;
	}
}
