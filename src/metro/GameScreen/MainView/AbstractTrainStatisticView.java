package metro.GameScreen.MainView;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This abstract statistic view is used to show statistic information in a given area.
 * There's no special information about different train models or any images about the train.
 * 
 * @author hauke
 *
 */
abstract public class AbstractTrainStatisticView
{
	private Point _size;

	protected AbstractTrainStatisticView(Point size)
	{
		setSize(size);
	}

	/**
	 * @return The size of the area.
	 */
	public Point getSize()
	{
		return _size;
	}

	/**
	 * Sets the size of the view.
	 * 
	 * @param size The new size of the view.
	 */
	public void setSize(Point size)
	{
		_size = size;
		// TODO: create scissor to only draw in the given area
	}

	/**
	 * Draws the view with all information depending on the subclass.
	 * 
	 * @param sp The sprite batch to draw on.
	 */
	public abstract void draw(SpriteBatch sp);
}
