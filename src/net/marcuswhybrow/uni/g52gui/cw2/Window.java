package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 *
 * @author Marcus Whybrow
 */
public class Window extends JFrame implements WindowListener, HyperlinkListener, ActionListener, ItemListener
{
	ArrayList<Tab> tabs = new ArrayList<Tab>();
	JEditorPane viewPort;
	JTextField addressBar;

	Browser browser;
	
	public Window()
	{
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());

		String url = "http://google.com";

		addressBar = new JTextField(url);
		addressBar.addActionListener(this);
		pane.add(addressBar, BorderLayout.NORTH);

		setTitle("Window");
		pack();

		setJMenuBar(new MenuBar(this));

		setVisible(true);

//		setLayout(new BorderLayout());
		try
		{
			viewPort = new JEditorPane(url);
		}
		catch (IOException ex)
		{
			System.err.println("Problemmo");
		}
		viewPort.setEditable(false);
		viewPort.addHyperlinkListener(this);
//		viewPort.setEnabled(false);
		viewPort.setContentType("text/html;text/css");

		pane.add(new JScrollPane(viewPort), BorderLayout.CENTER);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
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
		Browser.get().windowHasClosed(this);
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

	public void hyperlinkUpdate(HyperlinkEvent e)
	{
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		{
			if (e instanceof HTMLFrameHyperlinkEvent)
			{
				HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
				HTMLDocument doc = (HTMLDocument)viewPort.getDocument();
				doc.processHTMLFrameHyperlinkEvent(evt);
			}
			else
			{
				try
				{
					viewPort.setPage(e.getURL());
					addressBar.setText(e.getURL().toString());
				}
				catch (Throwable t)
				{
					System.out.println("Unable to Move to " + e.getURL());
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		System.out.println(e.toString());
		String location  = addressBar.getText();
    	try {
    		viewPort.setPage(location);
	    }
	    catch (Exception ex) {
	    	System.err.println("Unable to Move to " + location);
	    }
	}

	public void itemStateChanged(ItemEvent e)
	{
		System.out.println(e.toString());
	}
}
