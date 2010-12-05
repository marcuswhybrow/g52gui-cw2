package net.marcuswhybrow.uni.g52gui.cw2.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;

/**
 *
 * @author marcus
 */
public class Menu extends JMenu
{
	public Menu(String text)
	{
		super(text);
	}

	public void addMenuItem(String text, KeyStroke keyStroke)
	{
		addMenuItem(new JMenuItem(text), keyStroke);
	}

	public void addMenuItem(String text)
	{
		addMenuItem(new JMenuItem(text), null);
	}

	private void addMenuItem(JMenuItem menuItem, KeyStroke keyStroke)
	{
		menuItem.setAccelerator(keyStroke);
		menuItem.setActionCommand(menuItem.getText());
		menuItem.addActionListener(Browser.get());
		add(menuItem);
	}
}
