package net.marcuswhybrow.uni.g52gui.cw2;

import java.net.URL;
import java.util.Date;

/**
 *
 * @author marcus
 */
public class HistoryEntry
{
	private URL address;
	private Date date;
	private String title;

	public HistoryEntry(String title, URL address)
	{
		this.title = title;
		this.address = address;
		this.date = new Date();
	}

	public String getTitle()
	{
		return title;
	}

	public URL getAddress()
	{
		return address;
	}

	public Date getDate()
	{
		return date;
	}
}
