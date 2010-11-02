package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.Component;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lobobrowser.gui.FramePanel;

/**
 *
 * @author Marcus Whybrow
 */
public class WebPageTabContent extends FramePanel implements TabContent //implements HyperlinkListener
{
	public void goTo(String address)
	{
		try
		{
			navigate(address);
		}
		catch (MalformedURLException ex)
		{
			System.err.println("Malformed URL: " + address);
		}
	}

	public Component getContent()
	{
		return this;
	}
}
