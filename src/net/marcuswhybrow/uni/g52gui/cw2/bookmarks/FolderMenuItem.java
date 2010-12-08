package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

import javax.swing.JMenu;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;

/**
 *
 * @author marcus
 */
public class FolderMenuItem extends JMenu
{
	private Folder folder;

	public FolderMenuItem(Folder folder)
	{
		super(folder.getName());
		this.folder = folder;

		this.setIcon(Browser.get().getFolderIcon());
	}
}
