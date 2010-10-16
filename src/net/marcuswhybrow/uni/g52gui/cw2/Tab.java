package net.marcuswhybrow.uni.g52gui.cw2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 *
 * @author Marcus Whybrow
 */
public class Tab extends JScrollPane implements HyperlinkListener, Reopenable
{
	private Tabs tabs;
	private String title = "New Tab";
	private String address = "";
	private JEditorPane viewPort = new JEditorPane();
	private boolean isClosed = false;

	public Tab(Tabs tabs)
	{
		this(tabs, null);
	}

	public Tab(Tabs tabs, String address)
	{
		super();
		this.tabs = tabs;

		viewPort.setEditable(false);
		if (address != null)
			goTo(address);
		viewPort.addHyperlinkListener(this);
		viewPort.setContentType("text/html;text/css");
		getViewport().add(viewPort);
	}

	public void close()
	{
		tabs.closeTab(this);
		isClosed = true;
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
			try
			{
				viewPort.setPage(address);
			}
			catch (IOException ex)
			{
				System.err.println("Error getting page " + address);
			}
			this.address = address;
			title = address;
		}
	}

	public void hyperlinkUpdate(HyperlinkEvent e)
	{
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		{
			if (e instanceof HTMLFrameHyperlinkEvent)
			{
				HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent) e;
				HTMLDocument doc = (HTMLDocument) viewPort.getDocument();
				doc.processHTMLFrameHyperlinkEvent(evt);
			}
			else
			{
				tabs.getWindow().goTo(e.getURL().toString(), this);
			}
		}
	}

	public void reopen()
	{
		System.out.println("reopening tab");
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
}
