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
public class Page
{
	private URL address;
	private String title;
	private Icon favIcon;

	public Page(URL address, String title)
	{
		this(address, title, null);
	}

	public Page(URL address, String title, Icon favIcon)
	{
		this.address = address;
		this.title = title;
		this.favIcon = favIcon;

		if (this.favIcon == null)
			this.favIcon = Utils.getFavIconForURL(address);
	}

	public URL getAddress()
	{
		return address;
	}

	public String getTitle()
	{
		return title;
	}

	public Icon getFavIcon()
	{
		return favIcon;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Page other = (Page) obj;
		if (this.address != other.address && (this.address == null || !this.address.equals(other.address)))
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 89 * hash + (this.address != null ? this.address.hashCode() : 0);
		return hash;
	}
}
