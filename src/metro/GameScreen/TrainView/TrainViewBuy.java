package metro.GameScreen.TrainView;

import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;
import metro.WindowControls.List;

public class TrainViewBuy extends GameScreen
{
	private List _modelList; // all available train models
	private int _windowWidth;
	private Point _areaOffset;
	private Button _buyButton;

	public TrainViewBuy(Point areaOffset, int windowWidth)
	{
		_windowWidth = windowWidth;
		_areaOffset = areaOffset;
		
		_buyButton = new Button(new Rectangle(_areaOffset.x + 130, 680, 250, 20), "Buy");
		_modelList = new List(new Rectangle(_areaOffset.x + 20, 460, 100, 240), null, true);
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		Draw.setColor(METRO.__metroRed);
		
		String text = "Available train models:";
		int length = Draw.getStringSize(text).width;
		Draw.String(text, METRO.__SCREEN_SIZE.width - _windowWidth + 25, 440);
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth + 25, 455,
			METRO.__SCREEN_SIZE.width - _windowWidth + 25 + length, 455);
		
		Draw.String("INFOS ABOUT SELECTED TRAIN HERE!", _areaOffset.x + 170, 530, 160);
		
		_modelList.draw();
		_buyButton.draw();
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
		return false;
	}

	@Override
	public void reset()
	{
	}
}
