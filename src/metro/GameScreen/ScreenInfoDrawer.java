package metro.GameScreen;

import java.awt.Color;

import metro.METRO;
import metro.Graphics.Draw;
import metro.Graphics.Fill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Draws stuff like money, etc. onto the screen.
 * 
 * @author hauke
 *
 */
public class ScreenInfoDrawer extends GameScreen
{
	@Override
	public void updateGameScreen(SpriteBatch sp)
	{
		drawPlayerInfos(sp);
	}

	/**
	 * Draws all significant player info onto the upper left corner.
	 * 
	 * @param sp The SpriteBatch to draw on.
	 */
	private void drawPlayerInfos(SpriteBatch sp)
	{
		// clear area
		Fill.setColor(Color.white);
		Fill.Rect(0, 0, 150, 26);
		// Draw a _|
		Draw.setColor(METRO.__metroBlue);
		Draw.Line(0, 26, 150, 26);
		Draw.Line(150, 0, 150, 26);

		// draw amount of money like 935.258.550 $
		Draw.setColor(METRO.__metroRed);
		Draw.String("$", 5, 7);

		String str = " = " + String.format("%,d", METRO.__gameState.getMoney());
		str = str.replace(".", ". ");
		Draw.setColor(METRO.__metroBlue);
		Draw.String(str, 13, 7);
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
	}

	@Override
	public void keyDown(int keyCode)
	{
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void reset()
	{
	}
}
