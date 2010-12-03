package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
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

	private ArrayList<URL> history = new ArrayList<URL>();
	private int currentLocation;

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

	private void removeFutureLocationsFromHistory()
	{
		while(this.currentLocation < this.history.size() - 1)
			this.history.remove(this.currentLocation + 1);
	}

	public void goTo(URL location)
	{
		this.removeFutureLocationsFromHistory();
		this.history.add(location);
		this.currentLocation = history.indexOf(location);

		try
		{
			pane.setPage(location);

			this.tab.getToolBar().getAddressBar().setText(location.toString());

			this.tab.getToolBar().setForwardEnabled(false);
			this.tab.getToolBar().setBackEnabled(this.history.size() > 1 ? true : false);
		}
		catch (IOException ex)
		{
			System.err.println("Malformed URL: " + location.toString());
		}

		System.out.println(this.history);
	}

	public void goTo(String address)
	{
		try
		{
			this.goTo(new URL(address));
		}
		catch (MalformedURLException ex)
		{
			System.err.println("Could not convert a string to a URL");
		}
	}

	private void refreshHTMLPane() throws IOException
	{
		Document doc = this.pane.getDocument();
		doc.putProperty(Document.StreamDescriptionProperty, null);

		this.pane.setPage(this.history.get(this.currentLocation));
	}

	public void moveToPointInHistory(int index)
	{
		if (index >= 0 && index < this.history.size())
		{
			try
			{
				if (this.currentLocation == index)
					this.refreshHTMLPane();
				else
				{
					this.currentLocation = index;
					URL location = history.get(index);
					this.pane.setPage(location);
					this.tab.getToolBar().getAddressBar().setText(location.toString());
				}
			}
			catch (IOException ex)
			{
				System.err.println("History movement failed");
			}

			if (this.currentLocation < this.history.size() - 1)
				this.tab.getToolBar().setForwardEnabled(true);
			else
				this.tab.getToolBar().setForwardEnabled(false);

			if (this.currentLocation > 0)
				this.tab.getToolBar().setBackEnabled(true);
			else
				this.tab.getToolBar().setBackEnabled(false);
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
		if (title == null)
			title = "Untitled Document";
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
				this.tab.setTitle("loading ...");
				break;
			case DONE:
				System.out.println("done");
				this.tab.setTitle(this.getWebPageTitle());
				break;
		}
	}

	public void refresh()
	{
		System.out.println(this.currentLocation);
		this.moveToPointInHistory(this.currentLocation);
		System.out.println(this.currentLocation);
	}

	public void back()
	{
		System.out.println(this.currentLocation);
		this.moveToPointInHistory(this.currentLocation - 1);
		System.out.println(this.currentLocation);
	}

	public void forward()
	{
		System.out.println(this.currentLocation);
		this.moveToPointInHistory(this.currentLocation + 1);
		System.out.println(this.currentLocation);
	}
}
