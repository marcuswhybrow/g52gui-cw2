package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

/**
 *
 * @author marcus
 */
public class RootFolder extends Folder
{
	public RootFolder(String name)
	{
		super(name);
		root = true;
	}

	@Override
	public void setName(String name)
	{
		// do nothing and fail silently
	}
}
