package metro.TrainManagement.Trains;

/**
 * Trains have all same properties but their values are different. This interface makes it easier to manage all trains.
 * 
 * @author hauke
 *
 */

public interface Train
{
	/**
	 * @return The model designation of the train.
	 */
	public String getName();
}
