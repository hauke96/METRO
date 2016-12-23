package metro.UI.Renderable.Controls;

import java.awt.Point;

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
		Draw.setOffset(METRO.__getXOffset(), METRO.__getYOffset());
		Fill.setOffset(METRO.__getXOffset(), METRO.__getYOffset());
	}

	@Override
	public boolean mouseClicked(int screenX, int screenY, int button)
	{
		notifyClickOnControl(button);
		return false;
	}

	@Override
	public void mouseReleased(int screenX, int screenY, int button)
	{
	}

	@Override
	public void moveElement(Point offset)
	{
		Contract.RequireNotNull(offset);

		Point position = (Point)getPosition().clone();
		position.translate(offset.x, offset.y);
		super.setPosition(position);
	}

	@Override
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