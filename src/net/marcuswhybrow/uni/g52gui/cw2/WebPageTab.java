package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import org.lobobrowser.gui.FramePanel;

/**
 *
 * @author Marcus Whybrow
 */
public class WebPageTab extends Tab //implements HyperlinkListener
{
//	private JEditorPane viewPort = new JEditorPane();
	private FramePanel viewPort = new FramePanel();
//	private JScrollPane scrollPane = new JScrollPane();

	public WebPageTab(Tabs tabs)
	{
		this(tabs, null);
	}

	public WebPageTab(Tabs tabs, String address)
	{
		super();
		this.tabs = tabs;

//		viewPort.setEditable(false);
		if (address != null)
			goTo(address);
//		viewPort.addHyperlinkListener(this);
//		viewPort.setContentType("text/html;text/css");

		setLayout(new BorderLayout());
		add(viewPort, BorderLayout.CENTER);
//		scrollPane.getViewport().add(viewPort);
	}

	@Override
	public void goTo(String address)
	{
		if (address != null)
		{
			try
			{
				viewPort.navigate(address);
				this.address = address;
				title = address;
			}
			catch (MalformedURLException ex)
			{
				System.err.println("Error getting page " + address);
			}
		}
	}

//	public void hyperlinkUpdate(HyperlinkEvent e)
//	{
//		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
//		{
//			if (e instanceof HTMLFrameHyperlinkEvent)
//			{
//				HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent) e;
//				HTMLDocument doc = (HTMLDocument) viewPort.getgetDocument();
//				doc.processHTMLFrameHyperlinkEvent(evt);
//			}
//			else
//			{
//				tabs.getWindow().goTo(e.getURL().toString(), this);
//			}
//		}
//	}
}
