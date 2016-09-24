package metro.GameUI.MainView.StatisticView;

import java.awt.Point;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import metro.METRO;
import metro.TrainManagement.Trains.Train;

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
	protected final Train __TRAIN; // no setter/getter because this is a protected use-as-it-is value

	protected AbstractTrainStatisticView(Point size, Train train)
	{
		setSize(size);
		__TRAIN = train;
	}

	/**
	 * @return The size of the area.
	 */
	protected Point getSize()
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
	}

	/**
	 * Draws the view with all information depending on the subclass.
	 * 
	 * @param sp The sprite batch to draw on.
	 * @param position The position of the view in pixel coordinates.
	 */
	public void draw(SpriteBatch sp, Point position)
	{
		METRO.__spriteBatch.end();
		METRO.__spriteBatch.begin();

		// Create scissor to draw only in the area of the statistic view.
		com.badlogic.gdx.math.Rectangle scissors = new com.badlogic.gdx.math.Rectangle();
		com.badlogic.gdx.math.Rectangle clipBounds = new com.badlogic.gdx.math.Rectangle(position.x + METRO.__getXOffset(), position.y + METRO.__getYOffset(),
			_size.x + 1,
			_size.y + 1);
		ScissorStack.calculateScissors((Camera)METRO.__camera, METRO.__spriteBatch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);

		drawView(sp);

		ScissorStack.popScissors();
	}

	/**
	 * Draws the content of the statistic view.
	 * 
	 * @param sp The sprite batch to draw on.
	 */
	public abstract void drawView(SpriteBatch sp);
}
