package net.marcuswhybrow.uni.g52gui.cw2;

import javax.swing.Icon;

/**
 *
 * @author marcus
 */
public class BrowserPage extends Page
{
	public static enum Type {HISTORY, BOOKMARKS, VIEW_SOURCE};
	private Type type;

	public BrowserPage(Type type)
	{
		this.type = type;

		switch (type)
		{
			case BOOKMARKS:
				this.setTitle("Bookmarks Manager");
				break;
			case HISTORY:
				this.setTitle("History");
				break;
		}
	}

	public Type getType()
	{
		return this.type;
	}

	@Override
	public String getTitle()
	{
		switch (this.type)
		{
			case HISTORY:
				return "History";
			case BOOKMARKS:
				return "Bookmarks Manager";
			default:
				return "";
		}
	}
	
	@Override
	public String getAddress()
	{
		switch (this.type)
		{
			case HISTORY:
				return "browser://history";
			case BOOKMARKS:
				return "browser://bookmarks";
			default:
				return "";
		}
	}

	@Override
	public String toString()
	{
		return this.getType().name();
	}

	@Override
	public Icon getFavIcon()
	{
		switch (this.type)
		{
			case BOOKMARKS:
				return Browser.get().getBookmarksIcon();
			case HISTORY:
				return Browser.get().getHistoryIcon();
			default:
				return Browser.get().getTabFavIcon();
		}
	}
}
