package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

/**
 *
 * @author Marcus Whybrow
 */
public class AddressBar extends JTextField implements ActionListener
{
	/** The window this address bar belongs to */
	private Tab tab;

	public AddressBar(Tab tab)
	{
		super();
		addActionListener(this);

		this.tab = tab;
	}

	public void actionPerformed(ActionEvent e)
	{
		String address = getText();

		if (! address.contains("://") && address.contains("."))
		{
			address = "http://".concat(address);
			setText(address);
		}
    	tab.goTo(address);
	}

	public void updateAddress(String address)
	{
		setText(address);
	}
}
