package metro.Game;
import java.awt.Color;
import java.awt.Rectangle;

import metro.Graphics.Draw;
import metro.Graphics.Fill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Draws stuff like money, view-buttons, etc. onto the screen.
 * @author hauke
 *
 */
public class ScreenInfoDrawer extends GameScreen {

	/* (non-Javadoc)
	 * @see GameScreen#update(java.awt.Graphics2D)
	 */
	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		Draw.Image(METRO.__viewPortButton_Texture, 
			new Rectangle(METRO.__SCREEN_SIZE.width / 2 - 40, 45, 79, 40), 
			new Rectangle(161, 60, 79, 40));
		METRO.__viewPortButton_City.draw();
		METRO.__viewPortButton_Train.draw();
		drawPlayerInfos(sp);
	}
	
	/**
	 * Draws all significant player infos onto the upper left corner.
	 * @param g The graphic handle to draw on
	 */
	private void drawPlayerInfos(SpriteBatch sp)
	{
		// clear area
		Fill.setColor(Color.white);
		Fill.Rect(0, 0, 150, 26);
		// Draw a  _|
		Draw.setColor(METRO.__metroBlue);
		Draw.Line(0, 26, 150, 26);
		Draw.Line(150, 0, 150, 26);
		
		// draw amount of money like 935.258.550 $
		Draw.setColor(METRO.__metroRed);
		Draw.String("$", 5, 7);
		
		String str = " = " + String.format("%,d", METRO.__money);
		str = str.replace(".", ". ");
		Draw.setColor(METRO.__metroBlue);
		Draw.String(str, 13, 7);
	}

	/* (non-Javadoc)
	 * @see GameScreen#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
	}

	/* (non-Javadoc)
	 * @see GameScreen#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(int mouseButton){}
	public void keyDown(int keyCode){}
	@Override
	public void mouseScrolled(int amount){}
}
