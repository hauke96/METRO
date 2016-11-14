package metro.UI.Renderable.Controls;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import metro.METRO;
import metro.Common.Graphics.Draw;
import metro.Common.Technical.Contract;

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
	private Set<Label> _allLabels;

	private static Map<String, Color> _colorMap;

	static
	{
		_colorMap = new HashMap<String, Color>();

		// Fill with values listed in the class doc
		_colorMap.put("k", Color.black);
		_colorMap.put("r", METRO.__metroRed);
		_colorMap.put("b", METRO.__metroBlue);
		_colorMap.put("w", Color.white);
	}

	private ColorFormattedLabel(String text, Point position)
	{
		super(text, position);
		_allLabels = new HashSet<Label>();

		parseInput();
	}

	/**
	 * Creates a new color formatted label. The color-codes can be found in the class doc.
	 * 
	 * @param text The text the the label.
	 * @param position The position of the label.
	 * @return A new color formatted label.
	 */
	public static ColorFormattedLabel newLabel(String text, Point position)
	{
		Contract.RequireNotNull(text);
		Contract.RequireNotNull(position);
		return new ColorFormattedLabel(text, position);
	}

	/**
	 * Parses the input string and creates all the labels.
	 */
	private void parseInput()
	{
		// Only get the % without \ at the beginning but leave spaces and other character at the end of a splitted word.
		String[] splittedText = _text.split("(?<=[^\\\\])(?=%[krbw])");

		Point position = (Point)_area.getLocation().clone();

		for(String subText : splittedText)
		{
			Color color = Color.black;

			// Check if this block really begins with a color definition
			if(subText.startsWith("%"))
			{
				color = _colorMap.get("" + subText.charAt(1));
				subText = subText.substring(2); // remove %[color-code] delimiter
			}

			Label label = new Label(subText, position);
			label.setColor(color);
			label.underlined(_underlined);

			// Translate to place the next label right to this one
			int width = Draw.getStringSize(subText).width;
			position.translate(width, 0);

			_allLabels.add(label);
		}
	}

	@Override
	public void draw()
	{
		for(Label label : _allLabels)
		{
			label.draw();
		}
	}
	
	@Override
	public void underlined(boolean underlined)
	{
		_underlined = underlined;
		for(Label label : _allLabels)
		{
			label.underlined(_underlined);
		}
	}
	
	@Override
	public void setUnderlineColor(Color color)
	{
		for(Label label : _allLabels)
		{
			label.setUnderlineColor(color);
		}
	}
}
