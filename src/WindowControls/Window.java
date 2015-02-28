package WindowControls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
	private ArrayList<ControlElement> _elementList = new ArrayList<ControlElement>();
	
	public Window(String title, Point position, Point size)
	{
		_title = title;
		_position = position;
		_size = size;
		_size.y += 20; // for title
	}
	public void draw(Graphics g)
	{
		BufferedImage bufferedImage = new BufferedImage(_size.x, _size.y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		
		//Clear background
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, _size.x - 1, _size.y - 1);
		//Draw Border
		g2d.setColor(METRO.__metroBlue);
		g2d.drawRect(0, 0, _size.x - 1, _size.y - 1);
		g2d.drawRect(0, 0, _size.x - 1, 20);
		g2d.drawRect(_size.x - 20, 0, _size.x - 1, 20); // close box
//		TODO create fancy close-cross for window
//		TODO draw title of window
		
		for(ControlElement cElement : _elementList)
		{
			cElement.draw(g2d);
		}
		
		g.drawImage(bufferedImage, _position.x, _position.y, null);
	}
	public void update()
	{
		for(ControlElement cElement : _elementList)
		{
			cElement.update();
		}
	}
	public void addControlElement(ControlElement cElement)
	{
		_elementList.add(cElement);
	}
}
