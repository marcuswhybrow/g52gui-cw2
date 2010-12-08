package net.marcuswhybrow.uni.g52gui.cw2.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.Page;

/**
 *
 * @author marcus
 */
public class PageMenuItem extends JMenuItem implements ActionListener
{
	private Page page;

	public PageMenuItem(Page page)
	{
		super(page.getTitle());
		this.setIcon(page.getFavIcon());
		this.page = page;
		this.addActionListener(this);
	}

	public Page getPage()
	{
		return this.page;
	}

	public void actionPerformed(ActionEvent ae)
	{
		Browser.get().getActiveWindow().getTabs().openWebPageTab(this.page.getAddress().toString());
	}
}
