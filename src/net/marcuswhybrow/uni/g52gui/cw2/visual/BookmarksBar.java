package net.marcuswhybrow.uni.g52gui.cw2.visual;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.Settings;
import net.marcuswhybrow.uni.g52gui.cw2.listeners.SettingsChangeListener;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Bookmark;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.IBookmarkItem;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.BookmarkMenuItem;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Folder;
import net.marcuswhybrow.uni.g52gui.cw2.listeners.FolderChangeListener;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.FolderMenuItem;
import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.Tab;

/**
 *
 * @author marcus
 */
public class BookmarksBar extends JPanel implements SettingsChangeListener, FolderChangeListener
{
	private Tab tab;

	public BookmarksBar(Tab tab)
	{
		this.tab = tab;
		Settings.get().addSettingsChangeListener(this);
		Browser.get().getBookmarksBarBookmarks().addFolderChangeListener(this);
		Browser.get().getOtherBookmarksBookmarks().addFolderChangeListener(this);

		setVisible(Settings.get().getAlwaysShowBookmarksBar());
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		rebuild();
	}

	public void settingHasChanged(String settingName, Object oldValue, Object newValue)
	{
		if ("always show bookmarks bar".equals(settingName))
			setVisible((Boolean) newValue);
	}

	public void rebuild()
	{
		this.removeAll();
		for (IBookmarkItem item : Browser.get().getBookmarksBarBookmarks().getChildren())
		{
			if (item instanceof Bookmark)
				add(new BookmarksBarBookmark((Bookmark) item));
			else if (item instanceof Folder)
				add(new BookmarksBarFolder((Folder) item));
		}
		this.validate();
	}

	public void notifyFolderHasChanged(Folder folder)
	{
		this.rebuild();
	}

	private class BookmarksBarBookmark extends JButton implements ActionListener
	{
		private Bookmark bookmark = null;

		public BookmarksBarBookmark(Bookmark bookmark)
		{
			super(bookmark.getPage().getTitle(), bookmark.getPage().getFavIcon());
			this.bookmark = bookmark;

			this.setBorder(BorderFactory.createEmptyBorder(5, 10, 7, 10));

			this.addMouseListener(new BookmarksBarBookmarkMouseAdapter(this));
			this.addActionListener(this);
		}

		public void actionPerformed(ActionEvent ae)
		{
			Browser.get().getActiveWindow().getTabs().getActiveTab().goTo(bookmark.getPage().getAddress());
		}

		private class BookmarksBarBookmarkMouseAdapter extends MouseAdapter
		{
			private BookmarksBarBookmark bbb;

			public BookmarksBarBookmarkMouseAdapter(BookmarksBarBookmark bbb)
			{
				this.bbb = bbb;
			}

			@Override
			public void mouseEntered(MouseEvent me)
			{
				super.mouseEntered(me);
//				wrapper.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				bbb.setBackground(Color.RED);
			}

			@Override
			public void mouseExited(MouseEvent me)
			{
				super.mouseExited(me);
//				wrapper.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
				bbb.setBackground(null);
			}

			@Override
			public void mousePressed(MouseEvent me)
			{
				super.mousePressed(me);
				bbb.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
			}

			@Override
			public void mouseReleased(MouseEvent me)
			{
				super.mouseReleased(me);
				bbb.setBorder(BorderFactory.createEmptyBorder(5, 10, 7, 10));
			}
		}
	}

	private class BookmarksBarFolder extends JButton
	{
		private Folder folder;
		private FolderPopupMenu popupMenu;

		public BookmarksBarFolder(Folder folder)
		{
			super(folder.getName(), Browser.get().getFolderIcon());
			this.folder = folder;

			popupMenu = new FolderPopupMenu(folder);

			this.setBorder(BorderFactory.createEmptyBorder(5, 10, 7, 10));
			this.setComponentPopupMenu(popupMenu);
			this.addMouseListener(new BookmarksBarFolderMouseAdapter(this));
		}

		private class BookmarksBarFolderMouseAdapter extends MouseAdapter
		{
			private BookmarksBarFolder bbf;

			public BookmarksBarFolderMouseAdapter(BookmarksBarFolder bbf)
			{
				this.bbf = bbf;
			}
			
			@Override
			public void mouseClicked(MouseEvent me)
			{
				super.mouseClicked(me);
				popupMenu.show(bbf, 10, bbf.getHeight() - 5);
			}
		}

		private class FolderPopupMenu extends JPopupMenu
		{
			private Folder folder;

			public FolderPopupMenu(Folder folder)
			{
				this.folder = folder;

				if (this.folder.getChildren().length > 0)
					for (IBookmarkItem item : this.folder.getChildren())
						this.addItem(this, item);
				else
				{
					JMenuItem item = new JMenuItem("Empty");
					item.setEnabled(false);
					this.add(item);
				}
			}

			private void addItem(JComponent menu, IBookmarkItem item)
			{
				if (item instanceof Folder)
				{
					Folder f = (Folder) item;
					FolderMenuItem newMenu = new FolderMenuItem(f);
					menu.add(newMenu);
					IBookmarkItem[] children = f.getChildren();
					if (children.length > 0)
						for (IBookmarkItem newItem : f.getChildren())
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
		}
	}
}
