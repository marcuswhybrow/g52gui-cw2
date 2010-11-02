package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author Marcus Whybrow
 */
public abstract class Tab extends JPanel implements Reopenable
{
	protected Tabs tabs;
	protected String title;
	protected boolean isClosed;
	protected String address;
	
	private JPanel mainArea;
	private AddressBar addressBar;

	public Tab(Tabs tabs)
	{
		this.tabs = tabs;
		setLayout(new BorderLayout());
		mainArea = new JPanel();
		addressBar = new AddressBar(this);
		add(addressBar, BorderLayout.NORTH);
		add(mainArea, BorderLayout.CENTER);
	}

	public JPanel getMainArea()
	{
		return mainArea;
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
		// does nothing by default
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
