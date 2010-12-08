package net.marcuswhybrow.uni.g52gui.cw2.visual;

import net.marcuswhybrow.uni.g52gui.cw2.menu.MenuBar;
import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.Tabs;
import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.Tab;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;

/**
 *
 * @author Marcus Whybrow
 */
public class Window extends JFrame implements WindowListener, Reopenable, Frame
{
	private Tabs tabs;
	private StatusBar statusBar;
	
	public Window()
	{
		this.statusBar = new StatusBar(this);
		this.tabs = new Tabs(this);

		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());
		
		pane.add(tabs, BorderLayout.CENTER);
		pane.add(statusBar, BorderLayout.SOUTH);

		setTitle("Window");
		pack();

		setJMenuBar(new MenuBar());

//		setLayout(new BorderLayout());

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		this.setSize(800, 600);

		Window window = Browser.get().getActiveWindow();

		if (window != null)
		{
			this.setLocationRelativeTo(Browser.get().getActiveWindow());
			Point location = this.getLocation();
			this.setLocation(new Point(location.x + 10, location.y + 10));
		}
		else
			this.setLocationRelativeTo(null);

		this.setIconImage(Browser.get().getIcon());

//		this.tabs.openWebPageTab();
		this.setVisible(true);
//		this.tabs.getActiveTab().goTo(Settings.get().getHomePage());
//		this.tabs.openNewTab();
	}

	public void goTo(String url)
	{
		goTo(url, tabs.getActiveTab());
	}
	
	public void goTo(String url, Tab tab)
	{
		tab.goToWithoutHistory(url);
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
		Browser.get().setActiveFrame(this);
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

	public void openNewTab()
	{
		this.tabs.openNewTab();
	}
}
