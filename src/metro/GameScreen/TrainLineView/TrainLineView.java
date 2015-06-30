package metro.GameScreen.TrainLineView;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.Graphics.Fill;
import metro.WindowControls.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TrainLineView extends GameScreen
{
	private int _windowWidth;
	private List _lineList;

	public TrainLineView()
	{
		_windowWidth = 400;
		_lineList = new List(new Rectangle(METRO.__SCREEN_SIZE.width - _windowWidth + 20, 130, _windowWidth - 40, 300), 
			null, null, true);
		Random rand = new Random(23934965L);
		for(int i = 0; i < 20 ; i++)
		{
			_lineList.addElement("Zahl: " + rand.nextInt());
		}
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		drawBackground();
		drawTitleBox();
		drawListBox();
	}

	private void drawBackground()
	{
		Fill.setColor(Color.white);
		Fill.Rect(METRO.__SCREEN_SIZE.width - _windowWidth, 0, _windowWidth, METRO.__SCREEN_SIZE.height);
		Draw.setColor(METRO.__metroBlue);
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth, 0, METRO.__SCREEN_SIZE.width - _windowWidth, METRO.__SCREEN_SIZE.height);
	}

	private void drawTitleBox()
	{
		int length = Draw.getStringSize("Control Management").width;
		Draw.Rect(METRO.__SCREEN_SIZE.width - _windowWidth + 20, 15, _windowWidth - 40, 60);
		Color c = new Color((int)(METRO.__metroBlue.getRed() * 1.8f), 
			(int)(METRO.__metroBlue.getGreen() * 1.3f), 
			255); // lighter metro-blue
		Draw.setColor(c);
		Draw.Rect(METRO.__SCREEN_SIZE.width - _windowWidth + 22, 17, _windowWidth - 44, 56);

		//Take "METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2" to center the label
		length = Draw.getStringSize("METRO lines").width;
		Draw.setColor(METRO.__metroRed);
		Draw.String("METRO lines", METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2, 25);
		Draw.Line(METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2 - 5, 40,
			METRO.__SCREEN_SIZE.width - (_windowWidth - length) / 2 + 5, 40);

		length = Draw.getStringSize("Control Management").width;
		Draw.String("Control Management", METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2, 50);
		Draw.Line(METRO.__SCREEN_SIZE.width - (_windowWidth + length) / 2 - 5, 65,
			METRO.__SCREEN_SIZE.width - (_windowWidth - length) / 2 + 5, 65);
	}

	private void drawListBox()
	{
		Draw.setColor(METRO.__metroRed);
		int length = Draw.getStringSize("List of your lines:").width;
		Draw.String("List of your lines:", METRO.__SCREEN_SIZE.width - _windowWidth + 20, 110);
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth + 20, 125,
			METRO.__SCREEN_SIZE.width - _windowWidth + 20 + length, 125);
		_lineList.draw();
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
		_lineList.clickOnControlElement();
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
		_lineList.mouseScrolled(amount);
	}
}
