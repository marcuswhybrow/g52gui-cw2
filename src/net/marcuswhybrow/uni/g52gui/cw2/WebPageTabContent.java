package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 *
 * @author Marcus Whybrow
 */
public class WebPageTabContent extends JScrollPane implements TabContent, HyperlinkListener, PropertyChangeListener
{
	private JEditorPane pane = new JEditorPane();
	private Tab tab;
	public static enum State {EMPTY, LOADING, DONE};
	private State state = State.EMPTY;

	public WebPageTabContent()
	{
		pane.setEditable(false);
		this.setViewportView(pane);
		pane.addHyperlinkListener(this);
		this.pane.addPropertyChangeListener(this);
	}

	public void hyperlinkUpdate(HyperlinkEvent e)
	{
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		{
			JEditorPane source = (JEditorPane) e.getSource();
			if (e instanceof HTMLFrameHyperlinkEvent)
			{
				HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
				HTMLDocument doc = (HTMLDocument)source.getDocument();
				doc.processHTMLFrameHyperlinkEvent(evt);
			}
			else
			{
				System.out.println(tab);
				this.tab.goTo(e.getURL().toString());
			}
		}
		else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED)
		{
			//
		}
		else if (e.getEventType() == HyperlinkEvent.EventType.EXITED)
		{
			//
		}
	}

	public void propertyChange(PropertyChangeEvent pce)
	{
		if ("document".equals(pce.getPropertyName()))
			this.setState(WebPageTabContent.State.LOADING);
		else if ("page".equals(pce.getPropertyName()))
			this.setState(WebPageTabContent.State.DONE);
	}

	public void goTo(String address)
	{
		try
		{
			pane.setPage(address);
		}
		catch (IOException ex)
		{
			System.err.println("Malformed URL: " + address);
		}
	}

	public Component getContent()
	{
		return this;
	}

	public void setTab(Tab tab)
	{
		this.tab = tab;
	}

	public Tab getTab()
	{
		return this.tab;
	}

	public String getWebPageTitle()
	{
		String title = (String) this.pane.getDocument().getProperty("title");
		if (title == null)
			title = (String) this.pane.getDocument().getProperty("TITLE");
		return title.length() >= 25 ? title.substring(0, 25) + " ..." : title;
	}

	public void setState(State state)
	{
		this.state = state;
		switch (this.state)
		{
			case EMPTY:
				break;
			case LOADING:
				System.out.println("loading");
				this.tab.setTitle("loading ...");
				break;
			case DONE:
				System.out.println("done");
				this.tab.setTitle(this.getWebPageTitle());
				break;
		}
	}
}
