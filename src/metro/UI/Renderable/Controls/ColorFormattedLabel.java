package metro.UI.Renderable.Controls;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import metro.Graphics.Draw;

/**
 * A {@code ColorFormattedLabel} is a label which can display text with multiple colors specified by a color string.
 * Note that there're no line breaks for a color formatted label.
 * The color strings are colored by %c where c is a color letter:
 * - %k - black (default)
 * - %r - metro red
 * - %b - metro blue
 * - %w - white
 * 
 * @author hauke
 *
 */
public class ColorFormattedLabel extends Label
{
	private Set<Label> _setOfLabels;
	private Point _position;
	private String _text;

	private static Map<String, Color> _colorMap = new HashMap<String, Color>();

	private ColorFormattedLabel(String text, Point position)
	{
		super(text, position);
		_setOfLabels = new HashSet<Label>();

		parseInput();
	}

	/**
	 * Creates a new color formatted label. The color-codes can be found in the class doc.
	 * 
	 * @param text The text the the label.
	 * @param position The position of the label.
	 * @return A new color formatted label.
	 */
	public ColorFormattedLabel newLabel(String text, Point position)
	{
		// TODO Contract text != null
		return new ColorFormattedLabel(text, position);
	}

	/**
	 * Parses the input string and creates all the labels.
	 */
	private void parseInput()
	{
		_text.replace("%", "\0%");
		String[] splittedText = _text.split("\0%[krbw]");
		Point position = (Point)_position.clone();

		for(String subText : splittedText)
		{
			Color color = Color.black;

			// Check if this block really begins with a color definition
			if(subText.charAt(0) == '%')
			{
				subText = subText.substring(2); // remove %[color-code] delimiter
				color = _colorMap.get(subText.charAt(0));
			}

			Label label = new Label(subText, position);
			label.setColor(color);

			int width = Draw.getStringSize(subText).width;
			position.translate(width, 0);
		}
	}

	@Override
	protected void draw()
	{
		for(Label label : _setOfLabels)
		{
			label.draw();
		}
	}
}
