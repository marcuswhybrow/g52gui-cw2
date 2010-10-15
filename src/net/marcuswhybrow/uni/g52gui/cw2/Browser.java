package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author Marcus Whybrow
 */
public class Browser implements ActionListener
{
	private static Browser browser = null;

	ArrayList<Window> windows = new ArrayList<Window>();

	private Browser() {}

	private void setup()
	{
		// Mac specifc stuff to get the menu bar to integrate with the OS
		// and to give a more user friendly program name
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Browser");
		
		windows.add(new Window());
	}

	public static synchronized Browser get()
	{
		if (browser == null)
		{
			browser = new Browser();
			browser.setup();
		}
		return browser;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	public Window openWindow()
	{
		Window window = new Window();
		windows.add(window);
		return window;
	}

	public void windowHasClosed(Window window)
	{
		windows.remove(window);
		if (windows.size() == 0)
			System.exit(0);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() != null)
		{
			if (e.getActionCommand().equals("New Window"))
				Browser.get().openWindow();
		}
	}

	public static void main(String[] args)
	{
		Browser.get();
	}
}
