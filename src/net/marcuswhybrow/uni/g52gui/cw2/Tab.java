package net.marcuswhybrow.uni.g52gui.cw2;

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
