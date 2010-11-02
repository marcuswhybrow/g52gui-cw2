package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.BookmarkManagerTab;

/**
 *
 * @author Marcus Whybrow
 */
public class Tabs extends JTabbedPane implements ChangeListener
{
	/** The window these tabs belong to */
	private Window window;
	private ArrayList<Tab> tabs;
	private ArrayList<Tab> closedTabs;
	private Tab activeTab;
	private BookmarkManagerTab bookmarkManagerTab;

	public Tabs(Window window)
	{
		this.window = window;
		tabs = new ArrayList<Tab>();
		closedTabs = new ArrayList<Tab>();
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
		WebPageTab tab = new WebPageTab(this);
		openGenericTab(tab);
		if (url != null)
			window.goTo(url, tab);
	}


	public void openBookmarkManagerTab()
	{
		if (bookmarkManagerTab == null)
		{
			bookmarkManagerTab = new BookmarkManagerTab(this);
			openGenericTab(bookmarkManagerTab);
		}
		else
		{
			if (bookmarkManagerTab.isClosed)
				bookmarkManagerTab.reopen();
			setActiveTab(bookmarkManagerTab);
		}
	}

	public void openGenericTab(Tab tab)
	{
		tabs.add(tab);
		addTab(null, tab);
		setTabComponentAt(indexOfComponent(tab), new TabButton(tab));
		setActiveTab(tab);
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
