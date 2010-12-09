package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Marcus Whybrow
 */
public interface IBookmarkItem
{
	public Node convertToNode(Document doc);

	public void setParent(Folder folder);

	public String printOut();

	public void delete() throws Folder.CannotDeleteRootFolderExcetpion;
}
