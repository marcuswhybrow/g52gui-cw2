package net.marcuswhybrow.uni.g52gui.cw2;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author marcus
 */
public class History implements Iterable<HistoryEntry>
{
	private static History h = null;

	private ArrayList<HistoryEntry> history;

	private History() {}

	private void setup()
	{
		history = new ArrayList<HistoryEntry>();
	}

	public static History get()
	{
		if (h == null)
		{
			h = new History();
			h.setup();
		}
		return h;
	}

	public void addHistoryEntry(String title, URL address)
	{
		history.add(new HistoryEntry(title, address));
	}

	public Iterator<HistoryEntry> iterator()
	{
		return history.iterator();
	}
}
