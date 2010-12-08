package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import net.marcuswhybrow.uni.g52gui.cw2.visual.AddressBar;
import net.marcuswhybrow.uni.g52gui.cw2.visual.Reopenable;
import net.marcuswhybrow.uni.g52gui.cw2.Settings;
import net.marcuswhybrow.uni.g52gui.cw2.visual.ToolBar;
import net.marcuswhybrow.uni.g52gui.cw2.visual.Window;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.BookmarkManagerTabContent;

/**
 *
 * @author Marcus Whybrow
 */
public class Tab extends JPanel implements Reopenable
{
	protected Tabs tabs;
	protected String title;
	protected boolean isClosed;
	
	private TabContent content;
	private AddressBar addressBar;
	private ToolBar toolBar;

	private TabButton tabButton;

	public enum TabType {WEB_PAGE, NEW_TAB_PAGE, BOOKMARK_MANAGER_PAGE, HISTORY_PAGE};

	public Tab(Tabs tabs, String address)
	{
		this(tabs, TabType.WEB_PAGE, address);
	}

	public Tab(Tabs tabs, TabType type)
	{
		this(tabs, type, null);
	}

	public Tab(Tabs tabs, TabType type, String address)
	{
		this.tabs = tabs;
		
		setLayout(new BorderLayout());
		toolBar = new ToolBar(this);
		add(toolBar, BorderLayout.NORTH);

		switch (type)
		{
			case WEB_PAGE:
				this.content = new WebPageTabContent(this, address);
				this.title = "New Tab";
				break;
			case NEW_TAB_PAGE:
				this.content = new NewTabTabContent(this);
				this.title = "New Tab";
				break;
			case BOOKMARK_MANAGER_PAGE:
				this.content = new BookmarkManagerTabContent(this);
				this.title = "Bookmark Manager";
				break;
			case HISTORY_PAGE:
				this.content = new HistoryTabContent(this);
				this.title = "History";
				break;
		}

		add((Component) this.content, BorderLayout.CENTER);

		tabButton = new TabButton(this, title);
		tabs.addTab(null, this);
		tabs.setTabComponentAt(tabs.indexOfComponent(this), tabButton);
	}

	private void updateTabButtonTitle()
	{
		tabButton.setTitle(title);

//		if (content instanceof WebPageTabContent)
//			tabButton.setIcon(((WebPageTabContent) content).getCurrentLocation().getFavIcon());
	}

	public TabContent getTabContent()
	{
		return content;
	}

	public void setTabContent(TabContent tabContent)
	{
		if (this.content != null)
			remove((Component) this.content);
		this.content = tabContent;
		add((Component) this.content, BorderLayout.CENTER);
		validate();
	}

	public AddressBar getAddressBar()
	{
//		return addressBar;
		return this.toolBar.getAddressBar();
	}

	public String getTitle()
	{
		return title;
	}

	public void goTo(String address)
	{
		try
		{
			goTo(new URL(address));
		}
		catch (MalformedURLException ex) {}
	}

	public void goTo(URL address)
	{
		if (address != null)
		{
			toolBar.getAddressBar().updateAddress(address.toString());

			if (address.toString().startsWith("http://"))
			{
				if (! (content instanceof WebPageTabContent))
					setTabContent(new WebPageTabContent(this));

				((WebPageTabContent) content).goTo(address);

				title = address.toString().substring(7);
			}
			else if (address.toString().startsWith("browser://bookmarks"))
			{
				if (! (content instanceof BookmarkManagerTabContent))
					setTabContent(new BookmarkManagerTabContent(this));

				title = "Bookmark Manager";
			}
		}
	}

	public void close()
	{
		tabs.closeTab(this);
		isClosed = true;
	}

	public void reopen()
	{
		tabs.openTab(this);
		Window window = tabs.getWindow();
		if (window.isClosed())
			window.reopen();
		isClosed = false;
	}

	public boolean isClosed()
	{
		return isClosed;
	}

	public String toString()
	{
		return title + (isClosed ? " is closed" : " is open");
	}

	public void setTabButton(TabButton tabButton)
	{
		this.tabButton = tabButton;
	}

	public TabButton getTabButton()
	{
		return this.tabButton;
	}

	public void setTitle(String title)
	{
		this.title = title;
		this.updateTabButtonTitle();
	}

	public void home()
	{
		if (! (this.content instanceof WebPageTabContent))
			this.setTabContent(new WebPageTabContent(this));

		this.goTo(Settings.get().getHomePage());
	}

	public void refresh()
	{
		this.content.refresh();
	}

	public void back()
	{
		this.content.back();
	}

	public void forward()
	{
		this.content.forward();
	}

	public ToolBar getToolBar()
	{
		return this.toolBar;
	}

	public Window getWindow()
	{
		return this.tabs.getWindow();
	}
}
