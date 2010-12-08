package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;

/**
 *
 * @author marcus
 */
public class BookmarkMenuItem extends JMenuItem implements ActionListener
{
	private Bookmark bookmark = null;

	public BookmarkMenuItem(Bookmark bookmark)
	{
		super(bookmark.getPage().getTitle(), bookmark.getPage().getFavIcon());
		this.bookmark = bookmark;
		this.addActionListener(this);
	}

//	@Override
//	public String getText()
//	{
//		return bookmark != null ? bookmark.getPage().getAddress() : null;
//	}

	public void actionPerformed(ActionEvent ae)
	{
		Browser.get().getActiveWindow().getTabs().openWebPageTab(bookmark.getPage().getAddress());
	}
}
