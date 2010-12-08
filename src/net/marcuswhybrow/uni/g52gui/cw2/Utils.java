package net.marcuswhybrow.uni.g52gui.cw2;

import com.ucware.icontools.IconTools;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;

/**
 *
 * @author marcus
 */
public class Utils
{
	public static Icon getFavIconForAddress(String address)
	{
		if (address == null)
			return null;

		URL url = null;
		try
		{
			url = new URL(address);
		}
		catch (MalformedURLException ex)
		{
			return null;
		}
		
		try
		{
			// This link could be modified to somehow ask the page where its
			// favicon.ico file is, however for now I am assuming it is at the
			// standard location
			Icon[] icons = IconTools.readIcons(new URL("http", url.getHost(), "/favicon.ico"));
			return icons[icons.length - 1];
		}
		catch (MalformedURLException ex) {
			System.err.println("favIcon error in URL");
		}
		catch (IOException ex) {
			System.err.println("favIcon error in IO");
		}
		catch (IllegalArgumentException iaex)
		{
			System.err.println("favIcon error was not an Icon");
		}
		
		return null;
	}
}
