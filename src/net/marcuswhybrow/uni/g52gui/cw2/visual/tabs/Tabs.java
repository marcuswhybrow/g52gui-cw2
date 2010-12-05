package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.Settings;
import net.marcuswhybrow.uni.g52gui.cw2.visual.Window;
import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.Tab.TabType;
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

		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		addChangeListener(this);

		openNewTab();
	}

	public void openWebPageTab()
	{
		openWebPageTab(null);
	}

	public void openWebPageTab(String url)
	{
		openGenericTab(new Tab(this, url));
	}

	public void openNewTabTab()
	{
		openGenericTab(new Tab(this, TabType.NEW_TAB_PAGE));
	}


	public void openNewTab()
	{
		switch (Settings.get().getNewTabState())
		{
			case USE_HOME_PAGE:
				openWebPageTab(Settings.get().getHomePage());
				break;
			case NOT_SET:
			case USE_NEW_TAB_PAGE:
				openNewTabTab();
				break;
		}
	}

	public void openBookmarkManagerTab()
	{
		openGenericTab(new Tab(this, TabType.BOOKMARK_MANAGER_PAGE));
	}

	public void openGenericTab(Tab tab)
	{
		tabs.add(tab);
		TabButton tabButton = new TabButton(tab, tab.title);
		tab.setTabButton(tabButton);

		this.addTab(null, tab);
		this.setTabComponentAt(indexOfComponent(tab), tabButton);

		this.setActiveTab(tab);
	}

	public void replaceTab(Tab oldTab, Tab newTab)
	{
		TabButton tabButton = (TabButton) getTabComponentAt(indexOfComponent(oldTab));
		oldTab.close();
		openGenericTab(newTab);
		tabButton.setTab(newTab);
	}

	public void selectNextTab()
	{
		int nextTabIndex = this.tabs.indexOf(this.getActiveTab()) + 1;
		this.setActiveTab(this.tabs.get(nextTabIndex));
	}

	public void selectPreviousTab()
	{
		int nextTabIndex = this.tabs.indexOf(this.getActiveTab()) - 1;
		this.setActiveTab(this.tabs.get(nextTabIndex));
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
