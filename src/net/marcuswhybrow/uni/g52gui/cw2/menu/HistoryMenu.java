package net.marcuswhybrow.uni.g52gui.cw2.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.BrowserPage;
import net.marcuswhybrow.uni.g52gui.cw2.history.History;
import net.marcuswhybrow.uni.g52gui.cw2.history.HistoryChangeListener;
import net.marcuswhybrow.uni.g52gui.cw2.history.HistoryEntry;
import net.marcuswhybrow.uni.g52gui.cw2.history.VisitCountEntry;

/**
 *
 * @author marcus
 */
public class HistoryMenu extends Menu implements HistoryChangeListener
{
	private ArrayList<VisitCountEntry> visitCount = null;

	public HistoryMenu()
	{
		super("History");

		this.rebuildMenu();

		History.get().addHistoryChangeListener(this);
	}

	private void rebuildMenu()
	{
		this.removeAll();

		this.addMenuItem("Home", KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.addMenuItem("Back", KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.addMenuItem("Forward", KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		this.addSeparator();

		JMenuItem title = new JMenuItem("Most Visited");
		title.setEnabled(false);
		this.add(title);

		if (visitCount != null)
		{
			int number = 0;
			for (VisitCountEntry entry : visitCount)
			{
				// Ignore browser pages (such as history and bookmarks manager)
				if (entry.getPage() instanceof BrowserPage)
					continue;
				
				String pageTitle = entry.getPage().getTitle();
				if (pageTitle.length() > 30)
					pageTitle = pageTitle.substring(0, 30) + " ...";
				this.add(new PageMenuItem(entry.getPage()));

				number ++;
				if (number >= 9)
					break;
			}
		}
		
		this.addSeparator();

		title = new JMenuItem("Resently Closed");
		title.setEnabled(false);
		this.add(title);
		
		this.addSeparator();

		this.addMenuItem("Show Full History", KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	public void historyHasChanged(ArrayList<HistoryEntry> history)
	{
		// do nothing (not interested in this event).
	}

	public void visitCountHasChanged(ArrayList<VisitCountEntry> visitCount)
	{
		this.visitCount = visitCount;

		for (VisitCountEntry e : visitCount.size() > 9 ? visitCount.subList(0, 8) : visitCount)
			System.out.println(e.getPage().getTitle());
		this.rebuildMenu();
	}
}
