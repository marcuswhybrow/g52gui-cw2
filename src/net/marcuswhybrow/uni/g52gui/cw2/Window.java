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
public class Window extends JFrame implements WindowListener, Reopenable
{
	private Tabs tabs = new Tabs(this);
	private StatusBar statusBar = new StatusBar(this);
	
	public Window()
	{
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());
		
		pane.add(tabs, BorderLayout.CENTER);
		pane.add(statusBar, BorderLayout.SOUTH);

		setTitle("Window");
		pack();

		setJMenuBar(new MenuBar(this));

		setVisible(true);

//		setLayout(new BorderLayout());

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		this.setSize(300, 200);
	}

	public void goTo(String url)
	{
		goTo(url, tabs.getActiveTab());
	}
	
	public void goTo(String url, Tab tab)
	{
		tab.goTo(url);
	}

	public Tabs getTabs()
	{
		return tabs;
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
		Browser.get().setActiveWindow(this);
	}

	public void windowDeactivated(WindowEvent e)
	{
		// do nothing
	}

	public void close()
	{
		close(false);
	}

	/**
	 * 
	 *
	 * @param cascadeClose True if the window was closed as the result of a all
	 * tabs being closed and not the window being closed directly.
	 */
	public void close(boolean cascadeClose)
	{
		setVisible(false);
		if (cascadeClose)
			Browser.get().windowHasClosed(this, false /* Don't put window on the closed items stack */);
		else
			Browser.get().windowHasClosed(this);
	}

	public void reopen()
	{
		System.out.println("opening window");
		setVisible(true);
	}

	public boolean isClosed()
	{
		return !isVisible();
	}

	public StatusBar getStatusBar()
	{
		return this.statusBar;
	}
}
