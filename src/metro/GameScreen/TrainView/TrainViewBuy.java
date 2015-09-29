package metro.GameScreen.TrainView;

import java.awt.Point;
import java.awt.Rectangle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.GameScreen.GameScreen;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;

public class TrainViewBuy extends GameScreen
{
	private Button _backButton; // to create a new train
	
	public TrainViewBuy(final Point areaOffset, final int windowWidth)
	{
		_backButton = new Button(new Rectangle(areaOffset.x + 20, 450, (windowWidth - 40) / 3 - 10, 20), "Back");
		_backButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				setChanged();
				notifyObservers(new TrainViewMain(areaOffset, windowWidth));
			}
		});
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		_backButton.draw();
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
