package metro.GameUI.MainMenu;

import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import metro.METRO;
import metro.UI.Renderable.Container.Window;
import metro.UI.Renderable.Controls.Button;
import metro.UI.Renderable.Controls.Label;

/**
 * The welcome window with all important information.
 * 
 * @author hauke
 *
 */
public class WelcomeWindow extends Window
{
	/**
	 * Creates the default welcome window with the title image.
	 * 
	 * @param titleImageTexture
	 *            The title image.
	 */
	public WelcomeWindow(TextureRegion titleImageTexture)
	{
		super("Welcome to METRO - v" + METRO.__VERSION, new Rectangle(50, 100, // same y-pos as title image
				500, 530));
		
		Button button = new Button(
				new Rectangle((500 - (int) (titleImageTexture.getRegionWidth() * 0.4f)) / 2, (260 - (int) (titleImageTexture.getRegionWidth() * 0.4f))
						/ 2, (int) (titleImageTexture.getRegionWidth() * 0.4f), (int) (titleImageTexture.getRegionHeight()
								* 0.4f)), new Rectangle(0, 0, titleImageTexture.getRegionWidth(), titleImageTexture.getRegionHeight()), titleImageTexture);
		
		Label label = new Label("METRO stands for \"Master of established transport railway operators\" and is a simple Subway/Rapid-Transit and economic simulator.\n\n"
				+ "For all changes take a look into the 'changelog.txt'\n"
				+ "New main-features of v" + METRO.__VERSION + ":\n\n"
				+ " * Trains\n"
				+ "     - Have own speed\n"
				+ "     - Changing direction when reaching the end of a line\n\n"
				+ "And now: Have fun and earn money ;)", new Point(20, 100), 450);
		
		add(button);
		add(label);
	}
}
