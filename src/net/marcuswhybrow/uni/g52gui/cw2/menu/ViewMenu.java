package net.marcuswhybrow.uni.g52gui.cw2.menu;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.KeyStroke;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.Settings;
import net.marcuswhybrow.uni.g52gui.cw2.SettingsChangeListener;

/**
 *
 * @author marcus
 */
public class ViewMenu extends Menu implements SettingsChangeListener
{
	private JCheckBoxMenuItem alwaysShowBookmarksBar;
	public ViewMenu()
	{
		super("View");

		Settings.get().addSettingsChangeListener(this);

		alwaysShowBookmarksBar = new JCheckBoxMenuItem("Always Show Bookmarks Bar");
		alwaysShowBookmarksBar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		alwaysShowBookmarksBar.setActionCommand(alwaysShowBookmarksBar.getText());
		alwaysShowBookmarksBar.addActionListener(Browser.get());
		alwaysShowBookmarksBar.setSelected(Settings.get().getAlwaysShowBookmarksBar());
		add(alwaysShowBookmarksBar);

		addSeparator();
		addMenuItem("Stop", KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Reload This Page", KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addSeparator();
		addMenuItem("Enter Full Screen", KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//		addMenuItem("Actual Size", KeyStroke.getKeyStroke(KeyEvent.VK_0, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//		addMenuItem("Zoom In", KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//		addMenuItem("Zoom Out", KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addSeparator();
		addMenuItem("View Source", KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	public void settingHasChanged(String settingName, Object oldValue, Object newValue)
	{
		if ("always show bookmarks bar".equals(settingName))
			alwaysShowBookmarksBar.setSelected((Boolean) newValue);
	}
}
