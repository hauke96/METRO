package metro.TrainManagement.Trains;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

import metro.Common.Technical.Logger;

/**
 * A train template is a final train. There are also less available properties e.g. has a train template no line and no current passengers.
 * 
 * Info: For more information about lines just look into the {@code TrainLine} class ;)
 * 
 * @author hauke
 *
 */

public class TrainTemplate extends Observable
{
	protected String _name, _modelName, _manufacturer;
	protected int _price, _costs, _maxPassengers;
	protected float _costsFactor, _speed;

	// Values that may be used very often:
	protected static Map<String, TextureRegion> _textures = new HashMap<>();
	protected static Map<String, TextureRegion> _titleTextures = new HashMap<>();
	protected static Point _textureScale = new Point(1, 1);

	/**
	 * Creates a new train template with the following properties.
	 * Every data for trains are in the ./data/trains.txt file and will be read & parsed by the GameState class.
	 * This is not a actual train, for more information about trains look into the {@code train} class.
	 * 
	 * @param name The name of this train like "CT-1 (3)". This name can be anything.
	 * @param modelName The model name of the train.
	 * @param manufacturer Manufacturer of the train.
	 * @param price Price for buying the train.
	 * @param costs Costs per month.
	 * @param costsFactor Monthly increase of the costs.
	 * @param passengers Maximum amount of passengers per train.
	 * @param speed The speed in nodes per second.
	 */
	public TrainTemplate(String name, String modelName, String manufacturer, int price, int costs, float costsFactor, int passengers, float speed)
	{
		_name = name;
		_modelName = modelName;
		_manufacturer = manufacturer;
		_price = price;
		_costs = costs;
		_costsFactor = costsFactor;
		_maxPassengers = passengers;
		_speed = speed;

		loadTextures();
	}

	private void loadTextures()
	{
		if(!_textures.containsKey(_name) && Gdx.files != null)
		{
			loadModelTextures();
			loadTitleTextures();
		}
	}

	/**
	 * Load the normal texture for this train. This will be used to display the train one the playing field.
	 */
	private void loadTitleTextures()
	{
		try
		{
			TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("textures/Trains_" + _modelName + ".png")));
			texture.flip(false, true);
			texture.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			_textures.put(_modelName, texture);

			Logger.__info("Succesfully loaded image for train " + _modelName + ".");
		}
		catch(GdxRuntimeException ex)
		{
			Logger.__error("Error while loading trains title image!", ex);
		}
	}

	/**
	 * Load the title image for this train that'll be displayed by the train buy dialog.
	 */
	private void loadModelTextures()
	{
		Logger.__info("Loading train title image");
		try
		{
			FileHandle handle = Gdx.files.internal("textures/Trains_" + _modelName + "_big.png");
			Texture texture = new Texture(handle);
			TextureRegion textureRegion = new TextureRegion(texture);
			textureRegion.flip(false, true);
			textureRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			_titleTextures.put(_modelName, textureRegion);

			Logger.__info("Succesfully loaded title image for train " + _modelName + ".");
		}
		catch(GdxRuntimeException ex)
		{
			Logger.__error("Error while loading title image!", ex);
		}
	}

	/**
	 * @return The texture for this train.
	 */
	protected TextureRegion getTexture()
	{
		return _textures.get(_modelName);
	}

	/**
	 * @return The title texture for this train.
	 */
	protected TextureRegion getTitleTexture()
	{
		return _titleTextures.get(_modelName);
	}

	/**
	 * @return The model designation of the train.
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * @return Gets the name of the manufacturer of this train.
	 */
	public String getManufacturer()
	{
		return _manufacturer;
	}

	/**
	 * @return Gets the price this train costs.
	 */
	public int getPrice()
	{
		return _price;
	}

	/**
	 * @return Gets the monthly costs of this train without any consideration of the age.
	 */
	public int getCosts()
	{
		return _costs;
	}

	/**
	 * @return Gets the monthly increase of this train.
	 */
	public float getCostFactor()
	{
		return _costsFactor;
	}

	/**
	 * @return Gets the maximum amount of passengers that can travel with this train.
	 */
	public int getMaxPassenger()
	{
		return _maxPassengers;
	}

	/**
	 * @return Gets the speed in nodes per second.
	 */
	public float getSpeed()
	{
		return _speed;
	}

	/**
	 * @return The name of the train model. This is not the real name of the train itself, just the name of the model.
	 */
	public String getModelName()
	{
		return _modelName;
	}

	/**
	 * @return The title image of the train model.
	 */
	public TextureRegion getTitleImage()
	{
		return _titleTextures.get(_modelName);
	}
}
