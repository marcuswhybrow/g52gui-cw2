package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 *
 * @author Marcus Whybrow
 */
public class Window extends JFrame implements WindowListener
{
	Tabs tabs = new Tabs(this);
	AddressBar addressBar = new AddressBar(this);
	
	public Window()
	{
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());
		
		pane.add(addressBar, BorderLayout.NORTH);
		pane.add(tabs, BorderLayout.CENTER);

		setTitle("Window");
		pack();

		setJMenuBar(new MenuBar(this));

		setVisible(true);

//		setLayout(new BorderLayout());

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
	}

	public void goTo(String url)
	{
		goTo(url, tabs.getActiveTab());
	}
	
	public void goTo(String url, Tab tab)
	{
		tab.goTo(url);
	}

	public void windowOpened(WindowEvent e)
	{
		// do nothing
	}

	public void windowClosing(WindowEvent e)
	{
		// do nothing
	}

	public void windowClosed(WindowEvent e)
	{
		close();
	}

	public void windowIconified(WindowEvent e)
	{
		// do nothing
	}

	public void windowDeiconified(WindowEvent e)
	{
		// do nothing
	}

	public void windowActivated(WindowEvent e)
	{
		// do nothing
	}

	public void windowDeactivated(WindowEvent e)
	{
		// do nothing
	}

	public void close()
	{
		Browser.get().windowHasClosed(this);
	}
}
