package net.marcuswhybrow.uni.g52gui.cw2;

import com.ucware.icontools.IconTools;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Icon;

/**
 *
 * @author marcus
 */
public class HistoryEntry
{
	private URL address;
	private Date date;
	private String title;
	private Icon favIcon = null;

	public HistoryEntry(String title, URL address)
	{
		this.title = title;
		this.address = address;
		this.date = new Date();

		this.favIcon = Utils.getFavIconForURL(address);
	}

	public String getDateString()
	{
		return new SimpleDateFormat("EEE d MMM yyyy").format(this.getDate());
	}

	public String getTimeString()
	{
		return new SimpleDateFormat("HH:mm:ss").format(this.getDate());
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

	public Icon getFavIcon()
	{
		return this.favIcon;
	}

	@Override
	public String toString()
	{
		return title;
	}
}
