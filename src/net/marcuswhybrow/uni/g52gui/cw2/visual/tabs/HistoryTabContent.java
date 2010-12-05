package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 *
 * @author marcus
 */
public class HistoryTabContent extends JScrollPane implements TabContent
{
	private Tab tab;
	
	public HistoryTabContent(Tab tab)
	{
		this.tab = tab;
		this.setViewportView(new JLabel("History"));
	}

	public Component getContent()
	{
		return this;
	}

	public void setTab(Tab tab)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void refresh()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void back()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void forward()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
