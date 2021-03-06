package net.marcuswhybrow.uni.g52gui.cw2.visual;

import java.awt.Color;
import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.Tab;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JToolBar;
import net.marcuswhybrow.uni.g52gui.cw2.Settings;

/**
 *
 * @author marcus
 */
public class ToolBar extends JToolBar implements ActionListener
{
	private AddressBar addressBar;
	private JButton backButton;
	private JButton forwardButton;
	private JButton refreshButton;
	private JButton homeButton;

	private Tab tab;

	public ToolBar(Tab tab) {

		this.tab = tab;

		addressBar = new AddressBar(tab);
		backButton = new JButton("back");
		forwardButton = new JButton("forward");
		refreshButton = new JButton("refresh");
		homeButton = new JButton("home");

		homeButton.setVisible(Settings.get().getShowHomeButton());

		this.add(backButton);
		this.add(forwardButton);
		this.add(refreshButton);
		this.add(homeButton);
		this.add(addressBar);

		this.backButton.setEnabled(false);
		this.forwardButton.setEnabled(false);

		this.homeButton.addActionListener(this);
		this.backButton.addActionListener(this);
		this.forwardButton.addActionListener(this);
		this.refreshButton.addActionListener(this);

		this.setFloatable(false);
	}

	public void actionPerformed(ActionEvent ae)
	{
		if ("home".equals(ae.getActionCommand()))
			this.tab.home();
		else if ("refresh".equals(ae.getActionCommand()))
			this.tab.refresh();
		else if ("back".equals(ae.getActionCommand()))
			this.tab.back();
		else if ("forward".equals(ae.getActionCommand()))
			this.tab.forward();
	}

	public AddressBar getAddressBar()
	{
		return this.addressBar;
	}

	public void setBackEnabled(boolean bool)
	{
		this.backButton.setEnabled(bool);
	}

	public void setForwardEnabled(boolean bool)
	{
		this.forwardButton.setEnabled(bool);
	}

	public void setHomeEnabled(boolean bool)
	{
		this.homeButton.setEnabled(bool);
	}

	public void setRefreshEnabled(boolean bool)
	{
		this.refreshButton.setEnabled(bool);
	}


	public void setHomeButtonVisible(Boolean bool)
	{
		this.homeButton.setVisible(bool);
	}
}
