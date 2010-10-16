package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

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

	public static void main(String[] args)
	{
		Browser.get();
	}
}
