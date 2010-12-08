package net.marcuswhybrow.uni.g52gui.cw2.menu;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

/**
 *
 * @author marcus
 */
public class WindowMenu extends Menu
{
	public WindowMenu()
	{
		super("Window");
		addMenuItem("Minimise", KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addSeparator();
		addMenuItem("Select Next Tab", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Select Previous Tab", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addSeparator();
	}
}
