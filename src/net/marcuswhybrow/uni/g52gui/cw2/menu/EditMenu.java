package net.marcuswhybrow.uni.g52gui.cw2.menu;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;

/**
 *
 * @author marcus
 */
public class EditMenu extends Menu
{
	public EditMenu()
	{
		super("Edit");

		addMenuItem("Undo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Redo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addSeparator();
		addMenuItem("Cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Copy URL", KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Past", KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		if (Browser.get().getOperatingSystem() != Browser.OperatingSystem.MAC)
		{
			addSeparator();
			addMenuItem("Preferences");
		}
	}
}
