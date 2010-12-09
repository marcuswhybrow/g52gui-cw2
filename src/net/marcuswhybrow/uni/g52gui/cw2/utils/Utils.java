package net.marcuswhybrow.uni.g52gui.cw2.utils;

import com.ucware.icontools.IconTools;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JComboBox;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.IBookmarkItem;
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

		return new JComboBox(getComboBoxItemsForBookmarks(null));
	}

	public static Object[] getComboBoxItemsForBookmarks(IBookmarkItem excludeMe)
	{
		ArrayList<ComboBoxItem> options = new ArrayList<ComboBoxItem>();

		options.add(new ComboBoxItem(Browser.get().getBookmarksBarBookmarks()));

		for (IBookmarkItem item : Browser.get().getBookmarksBarBookmarks().getChildren())
			if (item != excludeMe)
				addItem(options, item, 1);

		options.add(new ComboBoxItem(Browser.get().getOtherBookmarksBookmarks()));

		for (IBookmarkItem item : Browser.get().getOtherBookmarksBookmarks().getChildren())
			if (item != excludeMe)
				addItem(options, item, 1);

		return options.toArray();
	}

	public static Object[] getComboBoxItemsForBookmarks()
	{
		return getComboBoxItemsForBookmarks(null);
	}

	private static void addItem(ArrayList<ComboBoxItem> options, IBookmarkItem item, int depth)
	{
		if (item instanceof Folder)
		{
			Folder folder = (Folder) item;

			String prefix = "";
			for (int i = 0; i < depth; i++)
				prefix = prefix.concat("    ");
			options.add(new ComboBoxItem(folder, prefix.concat(folder.getName())));

			for (IBookmarkItem bi : folder.getChildren())
				addItem(options, bi, depth + 1);
		}
	}
}
