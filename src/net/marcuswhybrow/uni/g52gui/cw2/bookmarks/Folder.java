package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

import net.marcuswhybrow.uni.g52gui.cw2.listeners.FolderChangeListener;
import java.util.ArrayList;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Marcus Whybrow
 */
public class Folder implements IBookmarkItem
{
	protected Folder parent;
	protected String name;
	protected ArrayList<IBookmarkItem> children = new ArrayList<IBookmarkItem>();
	protected boolean root = false;

	private static String rootFolderName = "bookmarks";
	private ArrayList<FolderChangeListener> listeners;

	public Folder()
	{
		this("New Untitled Folder");
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

		this.listeners = new ArrayList<FolderChangeListener>();
	}

	public void setParent(Folder parent)
	{
		this.parent = parent;
	}

	public void moveTo(Folder parent)
	{
		Folder oldParent = this.parent;
		if (this.parent != null)
			this.parent.removeChild(this);
		this.parent = parent;
		if (this.parent != null)
			this.parent.addChild(this);

		this.hasChanged();
		oldParent.hasChanged();
	}

	public Folder getParent()
	{
		return this.parent;
	}

	public Node convertToNode(Document doc)
	{
		Element folder;
		
		if (isRoot())
			folder = doc.createElement(getRootFolderName());
		else
			folder = doc.createElement(getXmlElementName());

		folder.setAttribute("name", name);
		
		for (IBookmarkItem item : children)
			folder.appendChild(item.convertToNode(doc));
		return folder;
	}

	public String printOut()
	{
		String output = name + " (  ";
		for (IBookmarkItem item : children)
			output += item.printOut() + ", ";
		return output.substring(0, output.length() - 2) + "  )";
	}

	@Override
	public String toString()
	{
		return name;
	}

	public void addChild(IBookmarkItem item)
	{
		if (item != null)
		{
			children.add(item);
			item.setParent(this);
			this.hasChanged();
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
		this.hasChanged();
	}

	public IBookmarkItem[] getChildren()
	{
		IBookmarkItem[] items = new IBookmarkItem[children.size()];
		return children.toArray(items);
	}

	void removeChild(IBookmarkItem item)
	{
		children.remove(item);
		this.hasChanged();
	}

	public void delete() throws CannotDeleteRootFolderExcetpion
	{
		if (!isRoot())
		{
			parent.removeChild(this);
		}
		else
			throw new CannotDeleteRootFolderExcetpion();
	}

	public class CannotDeleteRootFolderExcetpion extends Exception
	{
		public CannotDeleteRootFolderExcetpion()
		{
			super();
		}
	}

	public void hasChanged()
	{
		if (this.parent != null)
			this.parent.hasChanged();

		for (FolderChangeListener fcl : listeners)
			fcl.notifyFolderHasChanged(this);
	}

	public void addFolderChangeListener(FolderChangeListener fcl)
	{
		listeners.add(fcl);
	}

	public void removeFolderChangeListener(FolderChangeListener fcl)
	{
		listeners.remove(fcl);
	}
}
