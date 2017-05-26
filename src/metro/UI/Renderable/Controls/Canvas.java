package metro.UI.Renderable.Controls;

import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import juard.Contract;
import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.Common.Graphics.Fill;
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
	
	@Deprecated
	public Canvas(Point position)
	{
		setPosition(position);
		
		_area.height = METRO.__SCREEN_SIZE.height;
		_area.width = METRO.__SCREEN_SIZE.width;
	}
	
	public Canvas(Rectangle area)
	{
		_area = area;
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
		
		com.badlogic.gdx.math.Rectangle scissors = new com.badlogic.gdx.math.Rectangle();
		com.badlogic.gdx.math.Rectangle clipBounds = new com.badlogic.gdx.math.Rectangle(_area.x + METRO.__getXOffset(), _area.y + METRO.__getYOffset(), _area.width
		        + 1, _area.height + 1);
		
		ScissorStack.calculateScissors((Camera) METRO.__camera, METRO.__spriteBatch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);
		
		_painter.paint();
		
		ScissorStack.popScissors();
		
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
		
		Point position = (Point) getPosition().clone();
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
