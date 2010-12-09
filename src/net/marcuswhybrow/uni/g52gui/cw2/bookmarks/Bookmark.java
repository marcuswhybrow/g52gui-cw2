package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.Page;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Marcus Whybrow
 */
public class Bookmark implements IBookmarkItem
{
	private Page page;
	private Folder parent;

	public Bookmark(Page page)
	{
		this(page, null);
	}

	public Bookmark(Page page, Folder parent)
	{	
		this.page = page;

		if (parent != null)
			parent.addChild(this);
	}

	public Page getPage()
	{
		return page;
	}

	public Node convertToNode(Document doc)
	{
		Element bookmark = doc.createElement(getXmlElementName());
		bookmark.setAttribute("address", page.getAddress());
		bookmark.setAttribute("title", page.getTitle());
		return bookmark;
	}

	public void setParent(Folder folder)
	{
		// do nothing
	}

	public String printOut()
	{
		return page.getAddress() + " - " + page.getTitle();
	}

	@Override
	public String toString()
	{
		return page.getTitle();
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
