package net.marcuswhybrow.uni.g52gui.cw2;

import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Icon;

/**
 *
 * @author marcus
 */
public class Page
{
	private String address;
	private String title;
	private Icon favIcon;

	public Page()
	{
		this(null, null, null);
	}

	public Page(String address)
	{
		this(address, address != null ? address.toString() : null, null);
	}

	public Page(String address, String title)
	{
		this(address, title, null);
	}

	public Page(String address, String title, Icon favIcon)
	{
		this.address = address;
		this.title = title;
		this.favIcon = favIcon;

		if (this.favIcon == null)
			this.favIcon = Utils.getFavIconForAddress(address);
	}

	public String getHost()
	{
		try
		{
			return new URL(this.address).getHost();
		}
		catch (MalformedURLException ex) {}
		return null;
	}

	public String getAddress()
	{
		return address;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setFavIcon(Icon favIcon)
	{
		this.favIcon = favIcon;
	}

	public Icon getFavIcon()
	{
		return favIcon != null ? favIcon : Browser.get().getTabFavIcon();
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

	@Override
	public String toString()
	{
		return address != null ? this.address.toString() : super.toString();
	}
}
