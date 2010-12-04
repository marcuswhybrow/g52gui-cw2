package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Bookmark;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.BookmarkItem;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.BookmarkMenuItem;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Folder;

/**
 *
 * @author marcus
 */
public class MenuBar extends JMenuBar
{

	public MenuBar()
	{
		super();
		Menu menu = new Menu("File");
		menu.addMenuItem("New Tab", KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("New Window", KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Re-open Closed Tab", KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Open Location", KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addSeparator();
		menu.addMenuItem("Close Window", KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Close Tab", KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		add(menu);

		menu = new Menu("Edit");

		menu.addMenuItem("Undo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Redo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addSeparator();
		menu.addMenuItem("Cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Copy URL", KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Past", KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		if (Browser.get().getOperatingSystem() != Browser.OperatingSystem.MAC)
		{
			menu.addSeparator();
			menu.addMenuItem("Preferences");
		}

		add(menu);

		menu = new Menu("View");
		
		JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem("Always Show Bookmarks Bar");
		checkbox.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		checkbox.setActionCommand(checkbox.getText());
		checkbox.addActionListener(Browser.get());
		menu.add(checkbox);
		
		menu.addSeparator();
		menu.addMenuItem("Stop", KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Reload This Page", KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addSeparator();
		menu.addMenuItem("Enter Full Screen", KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Actual Size", KeyStroke.getKeyStroke(KeyEvent.VK_0, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Zoom In", KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Zoom Out", KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addSeparator();
		menu.addMenuItem("View Source", KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		add(menu);

		menu = new Menu("History");

		menu.addMenuItem("Home", KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Back", KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Forward", KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		menu.addSeparator();
		JMenuItem title = new JMenuItem("Most Visited"); title.setEnabled(false);
		menu.add(title);
		// most visited URLs will be listed here

		menu.addSeparator();
		title = new JMenuItem("Resently Closed"); title.setEnabled(false);
		menu.add(title);
		// recently visited URLs will be listed here
		menu.addSeparator();

		menu.addMenuItem("Show Full HIstory", KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		add(menu);

		menu = new Menu("Bookmarks");

		menu.addMenuItem("Bookmark Manager", KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Bookmark This Page...", KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Bookmark All Tabs...", KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addSeparator();
		// bookmarks bar bookmarks go here
		menu.addSeparator();

		Menu otherBookmarks = new Menu("Other Bookmarks");

		Folder f = Browser.get().getOtherBookmarksBookmarks();
//		System.out.println(f);
		for (BookmarkItem item : f.getChildren())
			addItem(otherBookmarks, item);
		menu.add(otherBookmarks);
		
		add(menu);

		menu = new Menu("Window");
		menu.addMenuItem("Minimise", KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addSeparator();
		menu.addMenuItem("Select Next Tab", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addMenuItem("Select Previous Tab", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.addSeparator();
		// list of available windows
		add(menu);
	}

	private void addItem(Menu menu, BookmarkItem item)
	{
		if (item instanceof Folder)
		{
			Folder folder = (Folder) item;
			Menu newMenu = new Menu(folder.getName());
			menu.add(newMenu);
			BookmarkItem[] children = folder.getChildren();
			if (children.length > 0)
				for (BookmarkItem newItem : children)
					addItem(newMenu, newItem);
			else
			{
				JMenuItem empty = new JMenuItem("Empty");
				empty.setEnabled(false);
				newMenu.add(empty);
			}
		}
		else
		{
			System.out.println("Create BookmarkMenuItem: " + ((Bookmark) item).getTitle());
			menu.add(new BookmarkMenuItem((Bookmark) item));
		}
	}
}
