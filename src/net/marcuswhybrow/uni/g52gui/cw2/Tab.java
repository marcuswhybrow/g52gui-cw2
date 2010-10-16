package net.marcuswhybrow.uni.g52gui.cw2;

import java.io.IOException;
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
public class Tab extends JEditorPane implements HyperlinkListener
{
	private Tabs tabs;

	public Tab(Tabs tabs)
	{
		this(tabs, null);
	}

	public Tab(Tabs tabs, String url)
	{
		super();
		this.tabs = tabs;

		setEditable(false);
		if (url != null)
			goTo(url);
		addHyperlinkListener(this);
		setContentType("text/html;text/css");
	}

	public void goTo(String url)
	{
		System.out.println("setting tab to " + url);
		try
		{
			setPage(url);
		}
		catch (IOException ex)
		{
			System.err.println("Error getting page " + url);
		}
	}

	public void hyperlinkUpdate(HyperlinkEvent e)
	{
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		{
			if (e instanceof HTMLFrameHyperlinkEvent)
			{
				HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent) e;
				HTMLDocument doc = (HTMLDocument) getDocument();
				doc.processHTMLFrameHyperlinkEvent(evt);
			}
			else
			{
				tabs.getWindow().goTo(e.getURL().toString(), this);
			}
		}
	}
}
