package net.marcuswhybrow.uni.g52gui.cw2.visual;

import java.awt.event.KeyEvent;
import net.marcuswhybrow.uni.g52gui.cw2.menu.MenuBar;
import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.Tabs;
import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.Tab;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.JComponent;
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
	private Window originalWindow;

	private ArrayList<WindowChangeListener> windowChangeListeners = new ArrayList<WindowChangeListener>();
	
	
	public Window()
	{
		setup(null);
		this.setVisible(true);
	}

	public Window(Window window)
	{
		this.originalWindow = window;
		setup(window);
	}
	
	public void setup(Window window)
	{
		if (window != null)
		{
			this.setUndecorated(true);
			window.getTabs().setWindow(window);
		}

		this.statusBar = new StatusBar(this);
		this.tabs = window != null ? window.getTabs() : new Tabs(this);



		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());
		
		pane.add(tabs, BorderLayout.CENTER);
		pane.add(statusBar, BorderLayout.SOUTH);

		setTitle(window != null ? window.getTitle() : "Window");
		pack();

		setJMenuBar(new MenuBar());

//		setLayout(new BorderLayout());

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		this.setSize(800, 600);

		Window w = Browser.get().getActiveWindow();

		if (w != null)
		{
			this.setLocationRelativeTo(Browser.get().getActiveWindow());
			Point location = this.getLocation();
			this.setLocation(new Point(location.x + 10, location.y + 10));
		}
		else
			this.setLocationRelativeTo(null);

		this.setIconImage(Browser.get().getIcon());

		this.setVisible(true);
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

	@Override
	public void setTitle(String title)
	{
		super.setTitle(title);
		this.notifyWindowChangeListeners();
	}


	public void addWindowChangeListener(WindowChangeListener wcl)
	{
		this.windowChangeListeners.add(wcl);
	}

	public void removeWindowChangeListener(WindowChangeListener wcl)
	{
		this.windowChangeListeners.remove(wcl);
	}

	private void notifyWindowChangeListeners()
	{
		for (WindowChangeListener wcl : windowChangeListeners)
			wcl.windowHasChanged();
	}


	public Window getOriginalWindow()
	{
		return this.originalWindow;
	}

	public void setTabs(Tabs tabs)
	{
		this.tabs = tabs;
		this.remove(tabs);
		this.add(tabs, BorderLayout.CENTER);
		tabs.setWindow(this);
		this.validate();
	}
}
