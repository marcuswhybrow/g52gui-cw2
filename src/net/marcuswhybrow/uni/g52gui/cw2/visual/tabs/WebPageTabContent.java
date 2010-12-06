package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.Tab;
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
import net.marcuswhybrow.uni.g52gui.cw2.history.History;
import net.marcuswhybrow.uni.g52gui.cw2.visual.Window;

/**
 *
 * @author Marcus Whybrow
 */
public class WebPageTabContent extends JScrollPane implements TabContent, HyperlinkListener, PropertyChangeListener
{
	private JEditorPane pane;
	private Tab tab;

	public static enum State {EMPTY, WAITING, LOADING, DONE};
	private State state = State.EMPTY;

	private ArrayList<URL> history = new ArrayList<URL>();
	private int currentLocation;

	public WebPageTabContent(Tab tab)
	{
		this(tab, null);
	}

	public WebPageTabContent(Tab tab, String address)
	{
		this.tab = tab;

		if (address != null)
		{
			try
			{
				this.pane = new EditorPane(address);
			}
			catch (IOException ioe)
			{
				System.err.println(ioe);
				this.pane = new EditorPane();
			}
		}
		else
			this.pane = new EditorPane();
		
		this.pane.setEditable(false);
		this.setViewportView(pane);
		this.pane.addHyperlinkListener(this);
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
			this.getWindow().getStatusBar().setText(e.getURL().toString());
		}
		else if (e.getEventType() == HyperlinkEvent.EventType.EXITED)
		{
			this.getWindow().getStatusBar().clear();
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
		this.currentLocation = history.size() - 1;

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

	public void moveToPointInHistory(int index)
	{
		if (index >= 0 && index < this.history.size())
		{
			try
			{
				this.currentLocation = index;
				URL location = history.get(index);
				this.pane.setPage(location);
				this.tab.getToolBar().getAddressBar().setText(location.toString());
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

	public URL getCurrentLocation()
	{
		try
		{
			return this.history.get(this.currentLocation);
		}
		catch (IndexOutOfBoundsException e) {}

		return null;
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

	public String getShortenedWebPageTitle()
	{
		String title = getWebPageTitle();
		return title.length() >= 25 ? title.substring(0, 25) + " ..." : title;
	}

	public String getWebPageTitle()
	{

		String title = (String) this.pane.getDocument().getProperty("title");
		if (title == null)
			title = (String) this.pane.getDocument().getProperty("TITLE");
		if (title == null)
			title = "Untitled Document";
		return title;
	}

	public void setState(State state)
	{
		this.state = state;
		URL l;
		switch (this.state)
		{
			case EMPTY:
				break;
			case WAITING:
				l = this.getCurrentLocation();
				if (l != null)
					this.getWindow().getStatusBar().setImportantText("Waiting for " + l.getHost());
				break;
			case LOADING:
				l = this.getCurrentLocation();
				if (l != null)
					this.getWindow().getStatusBar().setImportantText("Transfering data from " + l.getHost());
				this.tab.setTitle("loading ...");
				break;
			case DONE:
				this.getWindow().getStatusBar().doneWithImportantText();
				this.tab.setTitle(this.getShortenedWebPageTitle());
				History.get().addHistoryEntry(this.getWebPageTitle(), this.pane.getPage());
				break;
		}
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

	public Window getWindow()
	{
		return this.tab.getWindow();
	}

	private class EditorPane extends JEditorPane
	{
		private void allowCurrentPageToRefresh()
		{
			this.getDocument().putProperty(Document.StreamDescriptionProperty, null);
		}

		public EditorPane()
		{
			super();
		}

		public EditorPane(String address) throws IOException
		{
			super(address);
		}

		public EditorPane(URL address) throws IOException
		{
			super(address);
		}

		@Override
		public void setPage(URL url) throws IOException
		{
			setState(State.WAITING);
			this.allowCurrentPageToRefresh();
			super.setPage(url);
		}
	}
}
