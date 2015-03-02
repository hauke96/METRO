package WindowControls;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
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
		//Draw head bar of window
		g2d.setColor(METRO.__metroBlue);
		g2d.fillRect(0,  0, _size.x - 20, 20);
		//Draw Border
		g2d.drawRect(0, 0, _size.x - 1, _size.y - 1);
		g2d.drawRect(0, 0, _size.x - 1, 20);
		g2d.drawRect(_size.x - 20, 0, _size.x - 1, 20); // close box
		//Draw Window title
		g2d.setColor(Color.white);
		g2d.setFont(METRO.__STDFONT);
		g2d.drawString(_title, (_size.x - 20) / 2 - g2d.getFontMetrics(METRO.__STDFONT).stringWidth(_title) / 2, g2d.getFontMetrics(METRO.__STDFONT).getHeight() + 1);
		//Close cross
		g2d.drawImage(METRO.__iconSet, _size.x - 20, 0, _size.x, 20, 0, 0, 20, 20, null);
		
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
	/**
	 * Returns true is mouse is in window, false if not and also false if in window but e.g. on a button.
	 * @return boolean True/false if mouse is in window.
	 */
	public boolean isMouseOnWindow()
	{
		Point mPos = MouseInfo.getPointerInfo().getLocation();
		
		return mPos.x >= _position.x
			&& mPos.x <= _position.x + _size.x
			&& mPos.y >= _position.y
			&& mPos.y <= _position.y + _size.y;
			//&& !isOnWindow; // if mouse is on NO control element (only on the window area)
				
	}
	public void mousePressed(MouseEvent e)
	{
	}
	public boolean closeIfNeeded(MouseEvent e)
	{
		if(e.getPoint().x >= _position.x + _size.x - 20
			&& e.getPoint().x <= _position.x + _size.x
			&& e.getPoint().y >= _position.y
			&& e.getPoint().y <= _position.y + 20)
		{
			METRO.__closeWindow(this); // closes this window and removes it from the memory
			return true;
		}
		return false;
	}
}
