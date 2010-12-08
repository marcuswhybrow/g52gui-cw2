package net.marcuswhybrow.uni.g52gui.cw2;

import com.ucware.icontools.IconTools;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JComboBox;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.BookmarkItem;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Folder;

/**
 *
 * @author marcus
 */
public class Utils
{
	public static Icon getFavIconForAddress(String address)
	{
		if (address == null)
			return null;

		URL url = null;
		try
		{
			url = new URL(address);
		}
		catch (MalformedURLException ex)
		{
			return null;
		}
		
		try
		{
			// This link could be modified to somehow ask the page where its
			// favicon.ico file is, however for now I am assuming it is at the
			// standard location
			Icon[] icons = IconTools.readIcons(new URL("http", url.getHost(), "/favicon.ico"));
			return icons[icons.length - 1];
		}
		catch (MalformedURLException ex) {
			System.err.println("favIcon error in URL");
		}
		catch (IOException ex) {
			System.err.println("favIcon error in IO");
		}
		catch (IllegalArgumentException iaex)
		{
			System.err.println("favIcon error was not an Icon");
		}
		
		return null;
	}

	public static JComboBox getComboBoxForBookmarks()
	{
		ArrayList<ComboBoxItem> options = new ArrayList<ComboBoxItem>();

		options.add(new ComboBoxItem(Browser.get().getBookmarksBarBookmarks()));

		for (BookmarkItem item : Browser.get().getBookmarksBarBookmarks().getChildren())
			addItem(options, item, 1);

		options.add(new ComboBoxItem(Browser.get().getOtherBookmarksBookmarks()));

		for (BookmarkItem item : Browser.get().getOtherBookmarksBookmarks().getChildren())
			addItem(options, item, 1);

		return new JComboBox(options.toArray());
	}

	private static void addItem(ArrayList<ComboBoxItem> options, BookmarkItem item, int depth)
	{
		if (item instanceof Folder)
		{
			Folder folder = (Folder) item;

			String prefix = "";
			for (int i = 0; i < depth; i++)
				prefix = prefix.concat("    ");
			options.add(new ComboBoxItem(folder, prefix.concat(folder.getName())));

			for (BookmarkItem bi : folder.getChildren())
				addItem(options, bi, depth + 1);
		}
	}

	private static class ComboBoxItem
	{
		private Folder folder;
		private String titleOverride;

		public ComboBoxItem(Folder folder)
		{
			this(folder, null);
		}

		public ComboBoxItem(Folder folder, String titleOverride)
		{
			this.folder = folder;
			this.titleOverride = titleOverride;
		}

		@Override
		public String toString()
		{
			return titleOverride == null ? this.folder.getName() : titleOverride;
		}

		public Folder getFolder()
		{
			return this.folder;
		}
	}
}
