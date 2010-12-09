package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import net.marcuswhybrow.uni.g52gui.cw2.Page;

/**
 *
 * @author marcus
 */
public class SourceCodeTabContent extends JScrollPane implements ITabContent
{
	private String sourceCode = "";
	private URL url;
	private Tab tab;

	public SourceCodeTabContent(Tab tab, String address)
	{
		this.tab = tab;
		this.tab.setTitle("view-source:".concat(address.substring(7)));
		
		try
		{
			url = new URL(address);
		}
		catch (MalformedURLException ex) {
			System.err.println("Malformed");
		}
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String str;
			while((str = in.readLine()) != null)
				sourceCode = sourceCode.concat(str.concat("\n"));
			in.close();
		}
		catch (IOException ex) {
			System.err.println("IO");
		}

		JTextPane textPane = new JTextPane();
		textPane.setText(sourceCode);

		this.setViewportView(textPane);
	}
}
