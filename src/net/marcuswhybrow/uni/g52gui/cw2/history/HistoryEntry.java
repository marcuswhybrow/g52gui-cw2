package net.marcuswhybrow.uni.g52gui.cw2.history;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.marcuswhybrow.uni.g52gui.cw2.BrowserPage;
import net.marcuswhybrow.uni.g52gui.cw2.Page;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

	public HistoryEntry(Page page, String date)
	{
		this.page = page;

		SimpleDateFormat f = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyy");
		try
		{
			this.date = f.parse(date);
		}
		catch (ParseException ex)
		{
			System.err.println("Failed to parse a date of a History entry");
		}
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

	public Node convertToNode(Document doc)
	{
		Element entry;
		entry = doc.createElement("entry");

		if (page instanceof BrowserPage)
			entry.setAttribute("browserPageType", ((BrowserPage) page).getType().name());
		else
		{
			entry.setAttribute("title", page.getTitle());
			entry.setAttribute("address", page.getAddress());
		}
		
		entry.setAttribute("date", date.toString());
		return entry;
	}
}
