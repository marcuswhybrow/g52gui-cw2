package net.marcuswhybrow.uni.g52gui.cw2.menu;

import javax.swing.JMenuBar;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;

/**
 *
 * @author marcus
 */
public class MenuBar extends JMenuBar
{
	public MenuBar()
	{
		add(new FileMenu());
		if (Browser.get().getOperatingSystem() != Browser.OperatingSystem.MAC)
			add(new EditMenu());
		add(new ViewMenu());
		add(new HistoryMenu());
		add(new BookmarksMenu());
		add(new WindowMenu());
	}
}
