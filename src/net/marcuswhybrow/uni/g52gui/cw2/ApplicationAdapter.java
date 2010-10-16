package net.marcuswhybrow.uni.g52gui.cw2;

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationEvent;
import javax.swing.JOptionPane;

/**
 * It's very important that this class is *never* used if on an operating sytem
 * other Mac OS. This is becuase imports are made which are only availble on Mac
 * OS.
 *
 * @author Marcus Whybrow
 */
public class ApplicationAdapter extends com.apple.eawt.ApplicationAdapter
{
	/**
	 * A static method which allows all of the Mac type stuff to remain inside
	 * of this class alone and no where else.
	 */
	public static void setup()
	{
		Application macApplication = Application.getApplication();
		macApplication.addApplicationListener(new ApplicationAdapter());
		macApplication.setEnabledPreferencesMenu(true);
		macApplication.setDockIconImage(Browser.get().getIcon());
	}

	@Override
	public void handleQuit(ApplicationEvent e)
	{
		Browser.get().close();
	}

	@Override
	public void handleAbout(ApplicationEvent e)
	{
		// tell the system we're handling this, so it won't display
		// the default system "about" dialog after ours is shown.
		e.setHandled(true);
		JOptionPane.showMessageDialog(null, "Show About dialog here");
	}

	@Override
	public void handlePreferences(ApplicationEvent e)
	{
		Browser.get().showPreferences();
	}
}
