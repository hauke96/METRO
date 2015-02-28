package WindowControls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import Game.*;

/**
 * 
 * @author hauke
 *
 */
public class Window 
{
	private Point _position,
		_size;
	private String _title = "";
	
	public Window(String title, Point position, Point size)
	{
		_title = title;
		_position = position;
		_size = size;
		_size.y += 20; // for title
	}
	public void draw(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(_position.x, _position.y, _size.x, _size.y);
		g.setColor(METRO.__metroBlue);
		g.drawRect(_position.x, _position.y, _size.x, _size.y);
		g.drawRect(_position.x, _position.y, _size.x, 20);
	}
}
