package metro.GameScreen.MainView.StatisticView;

import java.awt.Point;

import metro.TrainManagement.Trains.Train;

/**
 * A detailed train statistic view shows the basic information (s. {@link BasicTrainStatisticView}) but also has an image above the content.
 * 
 * @author hauke
 *
 */
public class DetailedTrainStatisticView extends BasicTrainStatisticView
{

	/**
	 * Creates a more detailed version of the basic
	 * 
	 * @param size The size of the view.
	 * @param train The train which information should be displayed.
	 */
	public DetailedTrainStatisticView(Point size, Train train)
	{
		super(size, train); // TODO crop the size a bit because we want to draw around the basic view
	}

}
