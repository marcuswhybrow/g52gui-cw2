package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Marcus Whybrow
 */
public class Browser implements ActionListener
{
	private static Browser browser = null;

	private ArrayList<Window> windows = new ArrayList<Window>();
	private Stack<Reopenable> closedItems = new Stack<Reopenable>();
	private Window activeWindow = null;

	private Preferences preferences;

	private Image icon;

	private Browser() {}

	private void setup()
	{
		try
		{
			icon = ImageIO.read(getClass().getClassLoader().getResource("assets/icon.png"));
		}
		catch (IOException ex)
		{
			System.err.println("Couldn't find the Browser icon image");
		}

		// Mac Specific Stuff
		if (System.getProperty("mrj.version") != null)
		{
			ApplicationAdapter.setup();
		}
		
		windows.add(new Window());

		preferences = new Preferences();
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
		activeWindow = window;
		return window;
	}

	public void setActiveWindow(Window window)
	{
		activeWindow = window;
	}

	public void windowHasClosed(Window window)
	{
		windowHasClosed(window, true);
	}

	public void windowHasClosed(Window window, boolean putOnClosedItemStack)
	{
		windows.remove(window);
		if (putOnClosedItemStack)
			addClosedItem(window);
		if (windows.size() == 0)
			System.exit(0);
	}

	public void addClosedItem(Reopenable item)
	{
		closedItems.push(item);
	}

	public void openLastClosedItem()
	{
		Reopenable item;

		while (!closedItems.isEmpty())
		{
			item = closedItems.pop();
			if (item.isClosed())
			{
				item.reopen();
				break;
			}
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() != null)
		{
			System.out.println(e.getActionCommand());
			
			if (e.getActionCommand().equals("New Window"))
				Browser.get().openWindow();
			else if (e.getActionCommand().equals("New Tab"))
				activeWindow.getTabs().openTab();
			else if (e.getActionCommand().equals("Close Window"))
				activeWindow.close();
			else if (e.getActionCommand().equals("Close Tab"))
				activeWindow.getTabs().getActiveTab().close();
			else if (e.getActionCommand().equals("Re-open Closed Tab"))
				openLastClosedItem();
			else if (e.getActionCommand().equals("Open Location"))
				activeWindow.getAddressBar().requestFocus();
		}
	}

	public void showPreferences()
	{
		preferences.setVisible(true);
	}

	public Image getIcon()
	{
		return icon;
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			System.err.println("Unable to set look and feel");
		}

		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Browser");
		Browser.get();
	}
}
