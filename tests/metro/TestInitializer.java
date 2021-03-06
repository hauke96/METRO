package metro;

import java.awt.Dimension;

import juard.injection.Locator;
import metro.TrainManagement.TrainManagementService;
import metro.UI.ContainerRegistrationService;
import metro.UI.Renderer.BasicContainerRenderer;

/**
 * This class initializes the most basic things, like renderer and METRO-fields.
 * It may happen, that fields or services are not initialized, just add them here.
 * 
 * @author hauke
 *
 */
public class TestInitializer
{
	/**
	 * Initializes the most basic stuff, in this case the resolution and renderer.
	 */
	public static void init()
	{
		METRO.__SCREEN_SIZE = new Dimension(1920, 1080);
		
		Locator.get(TrainManagementService.class).init();
		
		// Initializes the renderer
		new BasicContainerRenderer(Locator.get(ContainerRegistrationService.class));
	}
}
