package net.marcuswhybrow.uni.g52gui.cw2.menu;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Bookmark;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.IBookmarkItem;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.BookmarkMenuItem;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Folder;
import net.marcuswhybrow.uni.g52gui.cw2.listeners.FolderChangeListener;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.FolderMenuItem;

/**
 *
 * @author marcus
 */
public class BookmarksMenu extends Menu implements FolderChangeListener
{
	public BookmarksMenu()
	{
		super("Bookmarks");

		Browser.get().getBookmarksBarBookmarks().addFolderChangeListener(this);
		Browser.get().getOtherBookmarksBookmarks().addFolderChangeListener(this);

		this.rebuild();
	}

	private void rebuild()
	{
		this.removeAll();

		addMenuItem("Bookmark Manager", KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.ALT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Bookmark This Page...", KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addMenuItem("Bookmark All Tabs...", KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		addSeparator();

		for (IBookmarkItem item : Browser.get().getBookmarksBarBookmarks().getChildren())
			addItem(this, item);

		addSeparator();

		Menu otherBookmarks = new Menu("Other Bookmarks");

		Folder f = Browser.get().getOtherBookmarksBookmarks();
		for (IBookmarkItem item : f.getChildren())
			addItem(otherBookmarks, item);
		add(otherBookmarks);

		this.validate();
	}

	private void addItem(JMenu menu, IBookmarkItem item)
	{
		if (item instanceof Folder)
		{
			Folder folder = (Folder) item;
			FolderMenuItem newMenu = new FolderMenuItem(folder);
			menu.add(newMenu);
			IBookmarkItem[] children = folder.getChildren();
			if (children.length > 0)
				for (IBookmarkItem newItem : children)
					addItem(newMenu, newItem);
			else
			{
				JMenuItem empty = new JMenuItem("Empty");
				empty.setEnabled(false);
				newMenu.add(empty);
			}
		}
		else
			menu.add(new BookmarkMenuItem((Bookmark) item));
	}

	public void notifyFolderHasChanged(Folder folder)
	{
		this.rebuild();
	}
}
