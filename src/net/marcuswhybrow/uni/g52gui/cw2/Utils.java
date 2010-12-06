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
public class Utils
{
	public static Icon getFavIconForURL(URL address)
	{
		try
		{
			// This link could be modified to somehow ask the page where its
			// favicon.ico file is, however for now I am assuming it is at the
			// standard location
			Icon[] icons = IconTools.readIcons(new URL("http", address.getHost(), "/favicon.ico"));
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
