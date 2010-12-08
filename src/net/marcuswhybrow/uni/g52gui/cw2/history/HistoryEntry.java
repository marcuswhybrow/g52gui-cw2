package net.marcuswhybrow.uni.g52gui.cw2.history;

import com.ucware.icontools.IconTools;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Icon;
import net.marcuswhybrow.uni.g52gui.cw2.Page;
import net.marcuswhybrow.uni.g52gui.cw2.Utils;

/**
 *
 * @author marcus
 */
public class HistoryEntry
{
	private Page page;
	private Date date;

	public HistoryEntry(Page page)
	{
		this.page = page;
		this.date = new Date();
	}

	public String getDateString()
	{
		return new SimpleDateFormat("EEE d MMM yyyy").format(this.getDate());
	}

	public String getTimeString()
	{
		return new SimpleDateFormat("HH:mm:ss").format(this.getDate());
	}

	public Page getPage()
	{
		return page;
	}

	public Date getDate()
	{
		return date;
	}

	@Override
	public String toString()
	{
		return page.getTitle();
	}
}
