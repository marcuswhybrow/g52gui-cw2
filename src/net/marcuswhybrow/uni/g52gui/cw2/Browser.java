package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Bookmark;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.BookmarkItem;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Folder;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.RootFolder;
import org.lobobrowser.main.PlatformInit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Marcus Whybrow
 */
public class Browser implements ActionListener
{
	private static Browser browser = null;

	private ArrayList<Window> windows = new ArrayList<Window>();
	private Stack<Reopenable> closedItems = new Stack<Reopenable>();
	private Window activeWindow = null;

	private Folder bookmarksBar;
	private Folder otherBookmarks;

	private Preferences preferences;

	private Image icon;

	private Browser() {}

	private void setup()
	{
		try
		{
			// Initialise logging such that only warnings are printed out
			PlatformInit.getInstance().initLogging(false);

			// Required for extentions to work
			PlatformInit.getInstance().init(false, false);
		}
		catch (Exception ex)
		{
			System.err.println("Something when wrong with Lobo");
		}

		try
		{
			icon = ImageIO.read(getClass().getClassLoader().getResource("assets/icon.png"));
		}
		catch (IOException ex)
		{
			System.err.println("Couldn't find the Browser icon image");
		}

		// Mac Specific Stuff
		if (System.getProperty("mrj.version") != null)
		{
//			new ApplicationAdapter().setup();
		}
		
		openWindow();

		preferences = new Preferences();

		bookmarksBar = readFromFile("bookmarks_bar.xml");
		otherBookmarks = readFromFile("other_bookmarks.xml");

		if (bookmarksBar == null)
			bookmarksBar = new RootFolder("Bookmarks Bar");
		if (otherBookmarks == null)
			otherBookmarks = new RootFolder("Other Bookmarks");
	}

	public Folder getBookmarksBarBookmarks()
	{
		return bookmarksBar;
	}

	public Folder getOtherBookmarksBookmarks()
	{
		return otherBookmarks;
	}

	private static Folder readFromFile(String filePath)
	{
		Document doc = null;
		try
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filePath));
		}
		catch (ParserConfigurationException ex)
		{
			System.err.println("parser configuration error");
		}
		catch (SAXException ex)
		{
			System.err.println("SAX error");
		}
		catch (IOException ex)
		{
			System.err.println("IO error");
		}

		if (doc != null)
		{
			doc.getDocumentElement().normalize();
			return (Folder) convert(doc.getDocumentElement());
		}
		return null;
	}

	private static void writeToFile(Folder folder, String filePath)
	{
		Document doc = null;
		try
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		}
		catch (ParserConfigurationException ex)
		{
			System.err.println("Some sort of XML error");
		}

		if (doc != null)
		{
			doc.appendChild(folder.convertToNode(doc));
			try
			{
				TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(new File(filePath)));
			}
			catch (TransformerException ex)
			{
				System.err.print("XML transformer error");
			}
		}
	}

	private static BookmarkItem convert(Element element)
	{
		if (element.getNodeName().equals(Folder.getRootFolderName()))
		{
			Folder rootFolder = new RootFolder(element.getAttribute("name"));
			NodeList nodes = element.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++)
				rootFolder.addChild(convert((Element) nodes.item(i)));
			return rootFolder;
		}
		else if (element.getNodeName().equals(Bookmark.getXmlElementName()))
			return new Bookmark(element.getAttribute("address"), element.getAttribute("title"));
		else if (element.getNodeName().equals(Folder.getXmlElementName()))
		{
			Folder folder = new Folder(element.getAttribute("name"));
			NodeList nodes = element.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++)
				folder.addChild(convert((Element) nodes.item(i)));
			return folder;
		}
		return null;
	}

	public static synchronized Browser get()
	{
		if (browser == null)
		{
			browser = new Browser();
			browser.setup();
		}
		return browser;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	public void openWindow()
	{
		Window window = new Window();
		windows.add(window);
		setActiveWindow(window);
	}

	public void setActiveWindow(Window window)
	{
		activeWindow = window;
	}

	public void windowHasClosed(Window window)
	{
		windowHasClosed(window, true);
	}

	public void windowHasClosed(Window window, boolean putOnClosedItemStack)
	{
		windows.remove(window);
		if (putOnClosedItemStack)
			addClosedItem(window);
		if (windows.size() == 0)
			close();
	}

	public void close()
	{
		Browser.writeToFile(bookmarksBar, "bookmarks_bar.xml");
		Browser.writeToFile(otherBookmarks, "other_bookmarks.xml");
		System.exit(0);
	}

	public void addClosedItem(Reopenable item)
	{
		closedItems.push(item);
	}

	public void openLastClosedItem()
	{
		Reopenable item;

		while (!closedItems.isEmpty())
		{
			item = closedItems.pop();
			if (item.isClosed())
			{
				System.out.println("Opening item: " + item.toString());
				item.reopen();
				break;
			}
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() != null)
		{
			System.out.println(e.getActionCommand());
			
			if (e.getActionCommand().equals("New Window"))
				Browser.get().openWindow();
			else if (e.getActionCommand().equals("New Tab"))
				activeWindow.getTabs().openWebPageTab();
			else if (e.getActionCommand().equals("Close Window"))
				activeWindow.close();
			else if (e.getActionCommand().equals("Close Tab"))
				activeWindow.getTabs().getActiveTab().close(); 
			else if (e.getActionCommand().equals("Re-open Closed Tab"))
				openLastClosedItem();
			else if (e.getActionCommand().equals("Open Location"))
				activeWindow.getTabs().getActiveTab().getAddressBar().requestFocus();
			else if (e.getActionCommand().equals("Bookmark Manager"))
				activeWindow.getTabs().openBookmarkManagerTab();
		}
	}

	public void showPreferences()
	{
		preferences.setVisible(true);
	}

	public Image getIcon()
	{
		return icon;
	}

	public Window getActiveWindow()
	{
		return activeWindow;
	}

	public static void main(String[] args)
	{
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Browser");

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			System.err.println("Unable to set look and feel");
		}

		try
		{
			// Set the tree leaf icon in a JTree to be the same as the folder icon
			UIDefaults def = UIManager.getLookAndFeelDefaults();
			// def.put("Tree.leafIcon", def.get("Tree.closedIcon"));
			def.put("Tree.closedIcon", def.get("Tree.leafIcon"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Browser.get();
	}
}
