package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import java.awt.BorderLayout;
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
	protected String address;
	
	private TabContent content;
	private AddressBar addressBar;
	private ToolBar toolBar;

	private TabButton tabButton;

	public enum TabType {WEB_PAGE, NEW_TAB_PAGE, BOOKMARK_MANAGER_PAGE};

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
		}
		setLayout(new BorderLayout());
//		addressBar = new AddressBar(this);
		toolBar = new ToolBar(this);
//		add(addressBar, BorderLayout.NORTH);
		add(toolBar, BorderLayout.NORTH);
		add(this.content.getContent(), BorderLayout.CENTER);

		tabButton = new TabButton(this, title);
		tabs.addTab(null, this);
		tabs.setTabComponentAt(tabs.indexOfComponent(this), tabButton);
	}

	private void updateTabButtonTitle()
	{
		tabButton.setTitle(title);
	}

	public TabContent getTabContent()
	{
		return content;
	}

	public void setTabContent(TabContent tabContent)
	{
		if (this.content != null)
			remove(this.content.getContent());
		this.content = tabContent;
		this.content.setTab(this);
		add(this.content.getContent(), BorderLayout.CENTER);
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

	public String getAddress()
	{
		return address;
	}

	public void goTo(String address)
	{
		if (address != null)
		{
			toolBar.getAddressBar().updateAddress(address);

			// The address bar can change the address (for example add http://)
			this.address = toolBar.getAddressBar().getText();

			
			if (address.startsWith("http://"))
			{
				if (! (content.getContent() instanceof WebPageTabContent))
					setTabContent(new WebPageTabContent(this));
				
				((WebPageTabContent) content.getContent()).goTo(address);

				title = address.substring(7);
			}
			else if (address.startsWith("browser://bookmarks"))
			{
				if (! (content.getContent() instanceof BookmarkManagerTabContent))
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
		tabs.openGenericTab(this);
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
