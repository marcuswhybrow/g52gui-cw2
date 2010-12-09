package net.marcuswhybrow.uni.g52gui.cw2.utils;

import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Folder;

/**
 *
 * @author marcus
 */
public class ComboBoxItem
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
