package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JPanel;
import net.marcuswhybrow.uni.g52gui.cw2.BrowserPage;
import net.marcuswhybrow.uni.g52gui.cw2.Page;
import net.marcuswhybrow.uni.g52gui.cw2.visual.AddressBar;
import net.marcuswhybrow.uni.g52gui.cw2.visual.Reopenable;
import net.marcuswhybrow.uni.g52gui.cw2.Settings;
import net.marcuswhybrow.uni.g52gui.cw2.visual.ToolBar;
import net.marcuswhybrow.uni.g52gui.cw2.visual.Window;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.BookmarkManagerTabContent;
import net.marcuswhybrow.uni.g52gui.cw2.history.History;
import net.marcuswhybrow.uni.g52gui.cw2.visual.BookmarksBar;

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
	private ToolBar toolBar;
	private BookmarksBar bookmarksBar;

	private TabButton tabButton;

	public enum TabType {WEB_PAGE, NEW_TAB_PAGE, BOOKMARK_MANAGER_PAGE, HISTORY_PAGE, VIEW_SOURCE};

	private ArrayList<Page> history = new ArrayList<Page>();
	private int currentLocation;

	private TabType type;

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
		this.type = type;
		
		setLayout(new BorderLayout());

		JPanel toolBars = new JPanel(new BorderLayout());

		toolBar = new ToolBar(this);
		toolBars.add(toolBar, BorderLayout.NORTH);

		bookmarksBar = new BookmarksBar(this);
		toolBars.add(bookmarksBar, BorderLayout.SOUTH);
		
		add(toolBars, BorderLayout.NORTH);

		this.setTabButton(new TabButton(this, ""));

		switch (type)
		{
			case WEB_PAGE:
				this.addAddressToHistory(address);
				this.content = new WebPageTabContent(this, address);
				break;
			case NEW_TAB_PAGE:
				this.content = new NewTabTabContent(this);
				this.setTitle("New Tab");
				break;
			case BOOKMARK_MANAGER_PAGE:
				this.addPageToHistory(new BrowserPage(BrowserPage.Type.BOOKMARKS));
				this.content = new BookmarkManagerTabContent(this);
				break;
			case HISTORY_PAGE:
				this.addPageToHistory(new BrowserPage(BrowserPage.Type.HISTORY));
				this.content = new HistoryTabContent(this);
				break;
			case VIEW_SOURCE:
				this.content = new SourceCodeTabContent(this, address);
		}

		if (history.size() > 0)
		{
			this.setTitle(this.getCurrentLocation().getTitle());
			this.tabButton.setIcon(this.getCurrentLocation().getFavIcon());
		}

		add((Component) this.content, BorderLayout.CENTER);
	}

	private void setTabButton(TabButton tabButton)
	{
		this.tabButton = tabButton;
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
		return this.toolBar.getAddressBar();
	}

	public String getTitle()
	{
		return title;
	}

	public TabType getTabType()
	{
		return this.type;
	}

	public void goTo(String address)
	{
		this.addAddressToHistory(address);
		this.goToWithoutHistory(address);
	}

	public void goToWithoutHistory(String address)
	{
		if (address != null)
		{
			toolBar.getAddressBar().updateAddress(address.toString());

			if (address.startsWith("http://"))
			{
				if (! (content instanceof WebPageTabContent))
					setTabContent(new WebPageTabContent(this));

				((WebPageTabContent) content).goTo(address);

				title = address.toString().substring(7);
			}
			else if (address.startsWith("browser://bookmarks"))
			{
				if (! (content instanceof BookmarkManagerTabContent))
					setTabContent(new BookmarkManagerTabContent(this));
				title = "Bookmark Manager";
			}
			else if (address.startsWith("browser://history"))
			{
				if (! (content instanceof HistoryTabContent))
					setTabContent(new HistoryTabContent(this));
				title = "History";
			}
			else if (address.startsWith("view-source:"))
				if (address.length() > 11)
					if (! (content instanceof SourceCodeTabContent))
						setTabContent(new SourceCodeTabContent(this, "http://".concat(address.substring(12))));
		}
	}

	public void close()
	{
		tabs.closeTab(this);
		isClosed = true;
	}

	public void reopen()
	{
		this.setTabButton(tabButton);
		tabs.openTab(this);
		
		Window window = tabs.getWindow();
		if (window.isClosed())
			window.reopen();
	}

	public boolean isClosed()
	{
		return isClosed;
	}

	@Override
	public String toString()
	{
		return title + (isClosed ? " is closed" : " is open");
	}

	public TabButton getTabButton()
	{
		return this.tabButton;
	}

	public void setTitle(String title)
	{
		if (tabs.getActiveTab() == this)
			tabs.getWindow().setTitle(getCurrentLocation() != null ? this.getCurrentLocation().getTitle() : "New Tab");
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
		this.moveToPointInHistory(this.currentLocation);
	}

	public void back()
	{
		this.moveToPointInHistory(this.currentLocation - 1);
	}

	public void forward()
	{
		this.moveToPointInHistory(this.currentLocation + 1);
	}

	public ToolBar getToolBar()
	{
		return this.toolBar;
	}

	public Window getWindow()
	{
		return this.tabs.getWindow();
	}



	// History

	public void removeFutureLocationsFromHistory()
	{
		while(this.currentLocation < this.history.size() - 1)
			this.history.remove(this.currentLocation + 1);
	}

	public void addAddressToHistory(String address)
	{
		if (address != null)
		{
			this.removeFutureLocationsFromHistory();

			if (address.toString().startsWith("http://"))
				this.addPageToHistory(new Page(address));
			else if (address.startsWith("browser://bookmarks"))
				this.addPageToHistory(new BrowserPage(BrowserPage.Type.BOOKMARKS));
			else if (address.startsWith("browser://history"))
				this.addPageToHistory(new BrowserPage(BrowserPage.Type.HISTORY));
		}
	}

	public void addPageToHistory(Page page)
	{
		History.get().addHistoryEntry(page);
		this.history.add(page);
		this.currentLocation = history.size() - 1;

		// This much can be assumed
		this.toolBar.setBackEnabled(history.size() > 1);
		this.toolBar.setForwardEnabled(false);
	}

	public int getHistorySize()
	{
		return this.history.size();
	}

	public void moveToPointInHistory(int index)
	{
		if (index >= 0 && index < this.history.size())
		{
			this.currentLocation = index;

			Page page = history.get(index);

			if (page instanceof BrowserPage)
			{
				BrowserPage browserPage = (BrowserPage) page;
				switch (browserPage.getType())
				{
					case BOOKMARKS:
						setTabContent(new BookmarkManagerTabContent(this));
						this.setTitle("Bookmarks Manager");
						break;
					case HISTORY:
						setTabContent(new HistoryTabContent(this));
						this.setTitle("History");
						break;
				}
				tabButton.setIcon(browserPage.getFavIcon());
			}
			else
			{
				if (! (this.content instanceof WebPageTabContent))
					this.setTabContent(new WebPageTabContent(this));
				this.goToWithoutHistory(history.get(index).getAddress());
			}

			if (this.currentLocation < this.history.size() - 1)
				toolBar.setForwardEnabled(true);
			else
				toolBar.setForwardEnabled(false);

			if (this.currentLocation > 0)
				toolBar.setBackEnabled(true);
			else
				toolBar.setBackEnabled(false);
		}
	}

	public Page getCurrentLocation()
	{
		try
		{
			return this.history.get(this.currentLocation);
		}
		catch (IndexOutOfBoundsException e) {}

		return null;
	}

	public BookmarksBar getBookmarksBar()
	{
		return this.bookmarksBar;
	}
}
