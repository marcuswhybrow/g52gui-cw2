package net.marcuswhybrow.uni.g52gui.cw2.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.KeyStroke;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.WindowsChangeListener;
import net.marcuswhybrow.uni.g52gui.cw2.visual.Window;

/**
 *
 * @author marcus
 */
public class WindowMenu extends Menu implements WindowsChangeListener
{
	public WindowMenu()
	{
		super("Window");

		Browser.get().addWindowsChangeListener(this);
	}

	private void rebuild()
	{
		this.removeAll();
		addMenuItem("Minimise", KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addSeparator();
		addMenuItem("Select Next Tab", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Select Previous Tab", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addSeparator();

		for (Window window : Browser.get().getWindows())
			add(new WindowMenuItem(this, window));

		this.validate();
	}

	public void windowsHaveChanged(ArrayList<Window> windows)
	{
		this.rebuild();
	}

	private class WindowMenuItem extends JCheckBoxMenuItem implements ActionListener
	{
		private Window window;
		private WindowMenu windowMenu;

		public WindowMenuItem(WindowMenu windowMenu, Window window)
		{
			super(window.getTitle());
			this.window = window;
			this.windowMenu = windowMenu;

			if (windowIsActive())
				this.setSelected(true);

			this.addActionListener(this);
		}

		private boolean windowIsActive()
		{
			return Browser.get().getActiveWindow() == this.window;
		}

		public void actionPerformed(ActionEvent ae)
		{
			if (windowIsActive())
				this.setSelected(true);
			else
			{
				Browser.get().setActiveFrame(this.window);
				windowMenu.rebuild();
			}
		}
	}
}
