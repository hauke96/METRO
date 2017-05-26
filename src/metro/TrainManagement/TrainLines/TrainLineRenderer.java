package metro.TrainManagement.TrainLines;

import java.util.List;

import metro.TrainManagement.Nodes.RailwayNode;

/**
 * This interface gives you the opportunity to render lines or just a list of nodes.
 * 
 * @author hauke
 *
 */
public interface TrainLineRenderer
{
	/**
	 * Draws all the train lines.
	 * 
	 * @param lines
	 *            A list of valid Train lines.
	 */
	void render(List<TrainLine> lines);
	
	/**
	 * Draws lines between these nodes as they are ordered in the list.
	 * 
	 * @param nodes
	 *            A ordered list of nodes.
	 */
	void renderRaw(List<RailwayNode> nodes);
}
