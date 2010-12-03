package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.BookmarkManagerTabContent;

/**
 *
 * @author Marcus Whybrow
 */
public class Tabs extends JTabbedPane implements ChangeListener
{
	/** The window these tabs belong to */
	private Window window;
	private ArrayList<Tab> tabs;
	private Tab activeTab;

	public Tabs(Window window)
	{
		this.window = window;
		tabs = new ArrayList<Tab>();
		openWebPageTab();

		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		addChangeListener(this);
	}

	public void openWebPageTab()
	{
		openWebPageTab(null);
	}

	public void openWebPageTab(String url)
	{
		Tab tab = new Tab(this, new WebPageTabContent());
		openGenericTab(tab);
		if (url != null)
			tab.goTo(url);
	}


	public void openBookmarkManagerTab()
	{
		Tab tab = new Tab(this, new BookmarkManagerTabContent());
		openGenericTab(tab);
	}

	public void openGenericTab(Tab tab)
	{
		tabs.add(tab);
		TabButton tabButton = new TabButton(tab, tab.title);
		tab.setTabButton(tabButton);

		addTab(null, tab);
		setTabComponentAt(indexOfComponent(tab), tabButton);

		setActiveTab(tab);
	}

	public void replaceTab(Tab oldTab, Tab newTab)
	{
		TabButton tabButton = (TabButton) getTabComponentAt(indexOfComponent(oldTab));
		oldTab.close();
		openGenericTab(newTab);
		tabButton.setTab(newTab);
	}

	public void setActiveTab(Tab tab)
	{
		try
		{
			setSelectedComponent((Component) tab);
		}
		catch (IllegalArgumentException e)
		{
			System.err.println("Tried to set '" + tab.toString() + "' to the active tab but it didn't exist");
		}
		activeTab = tab;
	}

	public void closeTab(Tab tab)
	{
		tabs.remove(tab);
		remove(tab);
		Browser.get().addClosedItem(tab);
		if (tabs.size() == 0)
			window.close(true);
	}

	public Window getWindow()
	{
		return window;
	}

	public Tab getActiveTab()
	{
		return activeTab;
	}

	public void stateChanged(ChangeEvent e)
	{
		activeTab = (Tab) getSelectedComponent();
	}
}
