package net.marcuswhybrow.uni.g52gui.cw2;

import com.ucware.icontools.IconTools;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Icon;

/**
 *
 * @author marcus
 */
public class VisitCountEntry implements Comparable<VisitCountEntry>
{
	private Page page;
	private int count;

	public VisitCountEntry(Page page, int count)
	{
		this.page = page;
		this.count = count;
	}

	public int compareTo(VisitCountEntry t)
	{
		int diff = this.getCount() - t.getCount();

		if (diff < 0)
			return 1;
		if (diff > 0)
			return -1;
		return 0;
	}

	public int getCount()
	{
		return this.count;
	}

	public Page getPage()
	{
		return this.page;
	}
}
