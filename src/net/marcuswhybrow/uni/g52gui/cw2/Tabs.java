package net.marcuswhybrow.uni.g52gui.cw2;

import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

	public Tabs(Window window)
	{
		this.window = window;
		tabs = new ArrayList<Tab>();
		closedTabs = new ArrayList<Tab>();
		openTab("http://google.com");

		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		addChangeListener(this);
	}

	public void openTab()
	{
		openTab("");
	}

	public void openTab(Tab tab)
	{
		tabs.add(tab);
		addTab(null, tab);
		setTabComponentAt(indexOfComponent(tab), new TabButton(tab));
		setActiveTab(tab);
	}

	public void openTab(String url)
	{
		Tab tab = new Tab(this);
		tabs.add(tab);

		addTab(null, tab);
		setTabComponentAt(indexOfComponent(tab), new TabButton(tab));

		if (url != null)
			window.goTo(url, tab);
		setActiveTab(tab);
	}

	public void setActiveTab(Tab tab)
	{
		setSelectedComponent(tab);
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
		window.activeTabHasChanged();
	}
}
