package Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Draws stuff like money, view-buttons, etc. onto the screen.
 */

/**
 * @author hauke
 *
 */
public class ScreenInfoDrawer implements GameScreen {

	/* (non-Javadoc)
	 * @see GameScreen#update(java.awt.Graphics2D)
	 */
	@Override
	public void update(SpriteBatch sp)
	{
		sp.draw(METRO.__viewPortButton_Texture, METRO.__SCREEN_SIZE.width / 2 - 40, 45, METRO.__SCREEN_SIZE.width / 2 + 40, 85); // the "view" sign under the two buttons
		METRO.__viewPortButton_City.draw(sp);
		METRO.__viewPortButton_Train.draw(sp);
		drawPlayerInfos(sp);
	}
	/**
	 * Draws all significant player infos onto the upper left corner.
	 * @param g The graphic handle to draw on
	 */
	private void drawPlayerInfos(SpriteBatch g)
	{
		//TOOD: recreate drawing stuff for the player infos
		// clear area
//		g.setColor(Color.white);
//		g.fillRect(0, 0, 150, 26);
//		
//		g.setColor(METRO.__metroBlue);
//		// Draw a   _|
//		g.drawLine(0, 26, 150, 26);
//		g.drawLine(150, 0, 150, 26);
//		
//		// draw amount of money like 935.258.550 $
//		g.setFont(METRO.__stdFont);
//		g.setColor(METRO.__metroRed);
//		g.drawString("$", 5, 22);
//		g.setColor(METRO.__metroBlue);
//		String str = " = " + String.format("%,d", METRO.__money) + " $";
//		str = str.replace(".", ". ");
//		g.drawString(str, 12, 22); // converts 12345 to 12.345
	}

	/* (non-Javadoc)
	 * @see GameScreen#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) 
	{
	}

	/* (non-Javadoc)
	 * @see GameScreen#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e){}
	public void keyPressed(KeyEvent e){}
}
