package metro.UI.Renderable.Controls;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import metro.UI.Renderable.ControlElement;

/**
 * A {@code ColorFormattedLabel} is a label which can display text with multiple colors specified by a color string.
 * Note that there're no line breaks for a color formatted label.
 * The color strings are colored by %c where c is a color letter:
 * 	- %k - black
 * 	- %r - metro red
 * 	- %b - metro blue
 * 	- %w - white
 *  
 * @author hauke
 *
 */
public class ColorFormattedLabel extends ControlElement
{
	Set<Label> _setOfLabels;
	
	public ColorFormattedLabel(String text)
	{
		_setOfLabels = new HashSet<Label>();
		
		//TODO parse input text
	}

	@Override
	protected void draw()
	{
		//TODO draw labels
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
	public void moveElement(Point offset)
	{
		//TODO move all elements
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
