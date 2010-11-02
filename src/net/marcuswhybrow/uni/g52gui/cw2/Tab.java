package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.BorderLayout;
import javax.swing.JPanel;
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

	private TabButton tabButton;

	public Tab(Tabs tabs, TabContent tabContent)
	{
		this.tabs = tabs;
		setLayout(new BorderLayout());
		this.content = tabContent;
		addressBar = new AddressBar(this);
		add(addressBar, BorderLayout.NORTH);
		add(tabContent.getContent(), BorderLayout.CENTER);

		if (tabContent instanceof BookmarkManagerTabContent)
			title = "Bookmark Manager";
		else
			title = "New Tab";

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
		remove(this.content.getContent());
		this.content = tabContent;
		add(this.content.getContent(), BorderLayout.CENTER);
		validate();
	}

	public AddressBar getAddressBar()
	{
		return addressBar;
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
		// assumes HTTP
		if (address != null)
		{
			addressBar.updateAddress(address);

			// The address bar can change the address (for example add http://)
			address = addressBar.getText();
			
			if (address.startsWith("http://"))
			{
				if (! (content.getContent() instanceof WebPageTabContent))
					setTabContent(new WebPageTabContent());
				
				((WebPageTabContent) content.getContent()).goTo(address);

				title = address.substring(7);
				updateTabButtonTitle();
			}
			else if (address.startsWith("browser://bookmarks"))
			{
				if (! (content.getContent() instanceof BookmarkManagerTabContent))
					setTabContent(new BookmarkManagerTabContent());
				
				title = "Bookmark Manager";
				updateTabButtonTitle();
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
}
