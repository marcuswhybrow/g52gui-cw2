package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 *
 * @author marcus
 */
public class NewTabTabContent extends JScrollPane implements ITabContent
{
	private Tab tab = null;

	public NewTabTabContent(Tab tab)
	{
		this.tab = tab;
	}

	public Component getContent()
	{
		return this;
	}

	public void setTab(Tab tab)
	{
		this.tab = tab;
	}

	public void refresh()
	{
		// do nothing
	}

	public void back()
	{
		// do nothing
	}

	public void forward()
	{
		// do nothing
	}

}
