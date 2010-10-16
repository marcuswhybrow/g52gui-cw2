package net.marcuswhybrow.uni.g52gui.cw2;

import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author Marcus Whybrow
 */
public class Tabs extends JTabbedPane
{
	/** The window these tabs belong to */
	private Window window;
	private ArrayList<Tab> tabs;
	private Tab activeTab;

	public Tabs(Window window)
	{
		this.window = window;
		tabs = new ArrayList<Tab>();
		openTab("http://google.com");
	}

	public Tab openTab()
	{
		return openTab(null);
	}

	public Tab openTab(String url)
	{
		Tab tab = new Tab(this);
		tabs.add(tab);
		if (url != null)
		{
			addTab(url, new JScrollPane(tab));
			window.goTo(url, tab);
		}
		else
		{
			addTab("New Tab", new JScrollPane(tab));
		}
		activeTab = tab;
		return tab;
	}

	public void closeTab(Tab tab)
	{
		tabs.remove(tab);
		// remove tab visually
		if (tabs.size() == 0)
			window.close();
	}

	public Window getWindow()
	{
		return window;
	}

	public Tab getActiveTab()
	{
		return activeTab;
	}
}
