package metro.AppContext;

import java.util.HashMap;
import java.util.Map;

import metro.Common.Game.GameState;
import metro.Common.Game.Settings;
import metro.Common.Technical.Contract;

/**
 * This class is used to implement dependency injection. All services are created here.
 * 
 * @author hauke
 *
 */
public class ServiceLocator
{
	private interface ServiceInstanceCreator<T>
	{
		T resolve();
	}

	private static Map<Class, Object> _registeredServices;
	private static boolean _serviceLocatorInitialized;

	/**
	 * Initializes the service locator. This is needed to resolve the services.
	 */
	public static void init()
	{
		_registeredServices = new HashMap<>();
		_serviceLocatorInitialized = true;

		registerAll();
	}

	private static void registerAll()
	{
		register(GameState.class, () -> new GameState());
		register(Settings.class, () -> new Settings());
	}

	private static <T> void register(Class<T> clazz, ServiceInstanceCreator<T> t)
	{
		Contract.Require(_serviceLocatorInitialized);

		_registeredServices.put(clazz, t);
	}

	public static <T> T get(Class<T> clazz)
	{
		Contract.Require(_serviceLocatorInitialized);

		return (T)_registeredServices.get(clazz);
	}
}
