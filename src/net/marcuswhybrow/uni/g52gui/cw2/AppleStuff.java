package net.marcuswhybrow.uni.g52gui.cw2;

import com.apple.eawt.AppEvent.PreferencesEvent;
import com.apple.eawt.Application;
import com.apple.eawt.PreferencesHandler;
import net.marcuswhybrow.uni.g52gui.cw2.menu.MenuBar;

/**
 *
 * @author marcus
 */
public class AppleStuff
{
	public static void go()
	{
		com.apple.eawt.Application app = Application.getApplication();
		app.setDockIconImage(Browser.get().getIcon());
		app.setDefaultMenuBar(new MenuBar());
		app.setPreferencesHandler(new PreferencesHandler() {
			public void handlePreferences(PreferencesEvent pe)
			{
				Settings.get().showSettings();
			}
		});
	}
}
