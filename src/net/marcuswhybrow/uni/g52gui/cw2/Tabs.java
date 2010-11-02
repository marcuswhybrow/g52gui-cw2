package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.Component;
import java.util.ArrayList;
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
		openWebPageTab("http://google.com");

		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		addChangeListener(this);
	}

	public void openWebPageTab()
	{
		openWebPageTab("");
	}

	public void openWebPageTab(String url)
	{
		Tab tab = new Tab(this, new WebPageTabContent());
		openGenericTab(tab);
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
		setSelectedComponent((Component) tab);
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
