package net.marcuswhybrow.uni.g52gui.cw2.menu;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

/**
 *
 * @author marcus
 */
public class FileMenu extends Menu
{
	public FileMenu()
	{
		super("File");
		addMenuItem("New Tab", KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("New Window", KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Re-open Closed Tab", KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Open Location", KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addSeparator();
		addMenuItem("Close Window", KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Close Tab", KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
}
