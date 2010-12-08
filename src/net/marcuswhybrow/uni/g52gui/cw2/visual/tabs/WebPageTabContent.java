package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.history.History;
import net.marcuswhybrow.uni.g52gui.cw2.Page;
import net.marcuswhybrow.uni.g52gui.cw2.Utils;
import net.marcuswhybrow.uni.g52gui.cw2.visual.AddressBar;
import net.marcuswhybrow.uni.g52gui.cw2.visual.StatusBar;
import net.marcuswhybrow.uni.g52gui.cw2.visual.ToolBar;
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

	private ArrayList<Page> history = new ArrayList<Page>();
	private int currentLocation;

	private URL lastURL;

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
				this.addAddressToHistory(new URL(address));

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
			catch (MalformedURLException ex){}
		}
		else
			this.pane = new EditorPane();
		
		this.pane.setEditable(false);
		this.setViewportView(pane);
		this.pane.addHyperlinkListener(this);
		this.pane.addPropertyChangeListener(this);

		ToolBar tb = this.tab.getToolBar();
		AddressBar ab = tb.getAddressBar();
		ab.setText(address);
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

	private void addAddressToHistory(URL address)
	{
		this.removeFutureLocationsFromHistory();
		this.history.add(new Page(address));
		this.currentLocation = history.size() - 1;
	}

	public void goTo(URL location)
	{
		this.lastURL = location;
		this.addAddressToHistory(location);

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
				Page page = history.get(index);
				this.pane.setPage(page.getAddress().toString());
				this.tab.getToolBar().getAddressBar().setText(page.getAddress().toString());
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

	public Page getCurrentLocation()
	{
		System.out.println(history.size());
		System.out.println(this.currentLocation);
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
		Page page;
		switch (this.state)
		{
			case EMPTY:
				break;
			case WAITING:
				page = this.getCurrentLocation();
				if (page != null)
				{
					Window w = this.getWindow();
					StatusBar sb = w.getStatusBar();
					String s = "Waiting for " + page.getAddress().getHost();
					sb.setImportantText(s);
				}
				break;
			case LOADING:
				page = this.getCurrentLocation();
				if (page != null)
					this.getWindow().getStatusBar().setImportantText("Transfering data from " + page.getAddress().getHost());
				this.tab.getTabButton().setIcon(Browser.get().getTabLoadingIcon());
				break;
			case DONE:
				page = this.getCurrentLocation();
				this.getWindow().getStatusBar().doneWithImportantText();
				page.setTitle(this.getWebPageTitle());
				this.tab.setTitle(this.getShortenedWebPageTitle());
				this.tab.getTabButton().setIcon(page.getFavIcon());
				History.get().addHistoryEntry(page);
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
