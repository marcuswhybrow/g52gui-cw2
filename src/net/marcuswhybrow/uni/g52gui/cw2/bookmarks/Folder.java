package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Marcus Whybrow
 */
public class Folder implements BookmarkItem
{
	private Folder parent;
	private String name;
	private ArrayList<BookmarkItem> children = new ArrayList<BookmarkItem>();
	private boolean root = false;

	private static String rootFolderName = "bookmarks";

	public Folder()
	{
		root = true;
		name = rootFolderName;
	}

	public Folder(String name)
	{
		this(name == null ? "New Untitled Folder" : name, null);
	}

	public Folder(String name, Folder parent)
	{
		this.name = name;

		if (parent != null)
			parent.addChild(this);
	}

	public void setParent(Folder folder)
	{
		this.parent = folder;
	}

	public Node convertToNode(Document doc)
	{
		Element folder;
		if (isRoot())
			folder = doc.createElement(getRootFolderName());
		else
		{
			folder = doc.createElement(getXmlElementName());
			folder.setAttribute("name", name);
		}
		for (BookmarkItem item : children)
			folder.appendChild(item.convertToNode(doc));
		return folder;
	}

	@Override
	public String toString()
	{
		String output = name + " (  ";
		for (BookmarkItem item : children)
			output += item.toString() + ", ";
		return output.substring(0, output.length() - 2) + "  )";
	}

	public void addChild(BookmarkItem item)
	{
		if (item != null)
		{
			children.add(item);
			item.setParent(this);
		}
	}

	public boolean isRoot()
	{
		return root;
	}

	public static String getRootFolderName()
	{
		return rootFolderName;
	}

	public static String getXmlElementName()
	{
		return "folder";
	}
}
