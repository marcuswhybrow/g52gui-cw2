package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Folder.CannotDeleteRootFolderExcetpion;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Marcus Whybrow
 */
public class Bookmark implements BookmarkItem
{
	private String address;
	private String title;
	private Folder parent;

	public Bookmark(String address)
	{
		this(address, null);
	}

	public Bookmark(String address, String title)
	{
		this(address, title, null);
	}

	public Bookmark(String address, String title, Folder parent)
	{	
		this.address = address;
		this.title = title != null ? title : address;

		if (parent != null)
			parent.addChild(this);
	}

	public String getAddress()
	{
		return address;
	}

	public String getTitle()
	{
		return title;
	}

	public Node convertToNode(Document doc)
	{
		Element bookmark = doc.createElement(getXmlElementName());
		bookmark.setAttribute("address", address);
		bookmark.setAttribute("title", title);
		return bookmark;
	}

	public void setParent(Folder folder)
	{
		// do nothing
	}

	public String printOut()
	{
		return address + " - " + title;
	}

	@Override
	public String toString()
	{
		return title;
	}

	public static String getXmlElementName()
	{
		return "bookmark";
	}

	public void delete()
	{
		parent.removeChild(this);
	}
}
