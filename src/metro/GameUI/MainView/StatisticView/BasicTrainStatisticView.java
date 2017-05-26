package metro.GameUI.MainView.StatisticView;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.TrainManagement.Trains.Train;

/**
 * A basic train statistic view is a view that shows information like name, model, earned money, driven distance and some other properties of a train.
 * 
 * @author hauke
 *
 */
public class BasicTrainStatisticView extends AbstractTrainStatisticView
{
	
	/**
	 * Creates a new train statistic view that just shows the basic information about the train.
	 * 
	 * @param size
	 *            The size of the view.
	 * @param train
	 *            The train which information should be displayed.
	 */
	public BasicTrainStatisticView(Point size, Train train)
	{
		super(size, train);
	}
	
	@Override
	public void drawView(SpriteBatch sp)
	{
	}
}
