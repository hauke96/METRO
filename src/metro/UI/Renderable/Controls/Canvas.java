package metro.UI.Renderable.Controls;

import java.awt.Point;
import java.awt.Rectangle;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
import metro.Common.Technical.Contract;
import metro.UI.Renderable.ControlElement;

/**
 * This control allows drawing on a plain canvas.
 * 
 * @author hauke
 *
 */
public class Canvas extends ControlElement
{
	public interface CanvasPainter
	{
		public void paint();
	}
	
	private CanvasPainter _painter;
	
	public Canvas(Point position)
	{
		setPosition(position);
	}
	
	public void setPainter(CanvasPainter newPainter)
	{
		_painter = newPainter;
	}

	@Override
	protected void draw()
	{
		Contract.RequireNotNull(_painter);

		Draw.setOffset(_area.x, _area.y);
		Fill.setOffset(_area.x, _area.y);
		_painter.paint();
	}

	@Override
	public boolean mouseClicked(int screenX, int screenY, int button)
	{
		return false;
	}

	@Override
	public void mouseReleased(int screenX, int screenY, int button)
	{
	}

	@Override
	public void moveElement(Point position)
	{
		setPosition(position);
	}

	public void setPosition(Point position)
	{
		Contract.RequireNotNull(position);
		
		position.translate(METRO.__getXOffset(), METRO.__getYOffset());
		super.setPosition(position);
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public void keyPressed(int keyCode)
	{
	}

	@Override
	public void keyUp(int keyCode)
	{
	}
}	