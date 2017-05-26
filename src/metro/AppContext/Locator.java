package metro.AppContext;

import java.util.HashMap;
import java.util.Map;

import juard.Contract;
import metro.Common.Game.GameState;
import metro.Common.Game.Settings;
import metro.GameUI.MainView.MainView;
import metro.GameUI.MainView.NotificationView.NotificationArea;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.TrainManagement.TrainLineDrawingService;
import metro.TrainManagement.TrainManagementService;
import metro.UI.ContainerRegistrationService;

/**
 * This class is used to implement dependency injection. All services are created here.
 * 
 * @author hauke
 *
 */
public class Locator
{
	private interface ServiceInstanceCreator<T>
	{
		T resolve();
	}
	
	private static Map<Class<?>, ServiceInstanceCreator<?>>	_registeredServices;
	private static Map<Class<?>, Object>					_initializedServices;
	private static boolean									_serviceLocatorInitialized;
	
	/**
	 * Initializes the service locator. This is needed to resolve the services.
	 */
	static
	{
		_registeredServices = new HashMap<>();
		_initializedServices = new HashMap<>();
		_serviceLocatorInitialized = true;
		
		registerAll();
	}
	
	private static void registerAll()
	{
		register(GameState.class, () -> new GameState());
		register(Settings.class, () -> new Settings());
		register(PlayingField.class, () -> new PlayingField(
		        get(GameState.class), get(TrainManagementService.class)));
		register(NotificationArea.class, () -> new NotificationArea());
		
		register(TrainManagementService.class, () -> new TrainManagementService(
		        get(GameState.class), get(TrainLineDrawingService.class)));
		register(TrainLineDrawingService.class, () -> new TrainLineDrawingService(
		        get(GameState.class)));
		register(ContainerRegistrationService.class, () -> new ContainerRegistrationService());
		
		register(MainView.class, () -> new MainView(
		        get(GameState.class), get(TrainManagementService.class), get(PlayingField.class), get(NotificationArea.class)));
	}
	
	private static <T> void register(Class<T> clazz, ServiceInstanceCreator<T> t)
	{
		Contract.Require(_serviceLocatorInitialized);
		
		_registeredServices.put(clazz, t);
	}
	
	/**
	 * Will resolve an object of the desired type.
	 * 
	 * @param clazz
	 *            The type of the objects as class-object.
	 * @return The desired object.
	 */
	public static <T> T get(Class<T> clazz)
	{
		Contract.Require(_serviceLocatorInitialized);
		
		if (_initializedServices.containsKey(clazz))
		{
			@SuppressWarnings ("unchecked") // we will either get null or the correct type
			T service = (T) _initializedServices.get(clazz);
			return service;
		}
		
		// TODO exception if type not registered.
		ServiceInstanceCreator<?> serviceCreator = _registeredServices.get(clazz);
		
		@SuppressWarnings ("unchecked") // we will either get null or the correct type
		T resolvedService = (T) serviceCreator.resolve();
		_initializedServices.put(clazz, resolvedService);
		
		return resolvedService;
	}
}
