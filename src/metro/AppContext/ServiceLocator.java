package metro.AppContext;

import juard.injection.Locator;
import metro.Common.Game.GameState;
import metro.Common.Game.Settings;
import metro.GameUI.MainView.MainView;
import metro.GameUI.MainView.StationPlacingTool;
import metro.GameUI.MainView.TrackPlacingTool;
import metro.GameUI.MainView.LineView.LineView;
import metro.GameUI.MainView.NotificationView.NotificationArea;
import metro.GameUI.MainView.PlayingField.PlayingField;
import metro.GameUI.MainView.TrainView.TrainView;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.TrainLines.TrainLineManagementService;
import metro.UI.ContainerRegistrationService;

/**
 * This class is used to implement dependency injection. All services are created here.
 * 
 * @author hauke
 *
 */
public class ServiceLocator
{
	public static void registerAll()
	{
		Locator.register(GameState.class, () -> new GameState());
		Locator.register(Settings.class, () -> new Settings());
		Locator.register(PlayingField.class, () -> new PlayingField(
		        Locator.get(GameState.class), Locator.get(TrainManagementService.class)));
		Locator.register(NotificationArea.class, () -> new NotificationArea());
		
		Locator.register(TrainManagementService.class, () -> new TrainManagementService(
		        Locator.get(GameState.class), Locator.get(TrainLineManagementService.class)));
		Locator.register(TrainLineManagementService.class, () -> new TrainLineManagementService(
		        Locator.get(GameState.class)));
		Locator.register(ContainerRegistrationService.class, () -> new ContainerRegistrationService());
		
		Locator.register(MainView.class, () -> new MainView(
		        Locator.get(GameState.class), Locator.get(PlayingField.class), Locator.get(NotificationArea.class)));
		Locator.register(StationPlacingTool.class, () -> new StationPlacingTool(Locator.get(GameState.class), Locator.get(PlayingField.class), Locator.get(TrainManagementService.class)));
		Locator.register(TrackPlacingTool.class, () -> new TrackPlacingTool(Locator.get(GameState.class), Locator.get(PlayingField.class)));
		Locator.register(LineView.class, () -> new LineView(Locator.get(GameState.class), Locator.get(PlayingField.class), Locator.get(TrainManagementService.class)));
		Locator.register(TrainView.class, () -> new TrainView(Locator.get(GameState.class), Locator.get(TrainManagementService.class)));
	}
}
