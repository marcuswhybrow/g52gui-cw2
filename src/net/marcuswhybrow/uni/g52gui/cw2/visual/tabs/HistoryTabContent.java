package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.BrowserPage;
import net.marcuswhybrow.uni.g52gui.cw2.history.History;
import net.marcuswhybrow.uni.g52gui.cw2.history.HistoryEntry;

/**
 *
 * @author marcus
 */
public class HistoryTabContent extends JScrollPane implements TabContent, ListCellRenderer
{
	private Tab tab;
	
	public HistoryTabContent(Tab tab)
	{
		this.tab = tab;
		this.tab.getTabButton().setIcon(Browser.get().getHistoryIcon());

		JPanel wrapper = new JPanel();
		wrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		List newList = Arrays.asList(stripHistoryPages(History.get().getArrayList()).toArray());
		Collections.reverse(newList);

		JList list = new JList(newList.toArray());
		list.setCellRenderer(this);
		list.addMouseListener(new HistoryListMouseAdapter(tab));
		list.addKeyListener(new HistoryListKeyAdapter(tab, list));

		wrapper.setLayout(new BorderLayout());
		wrapper.add(list, BorderLayout.CENTER);
		wrapper.setBackground(Color.WHITE);
		this.setViewportView(wrapper);
	}

	private ArrayList<HistoryEntry> stripHistoryPages(ArrayList<HistoryEntry> historyEnteries)
	{
		ArrayList<HistoryEntry> entries = new ArrayList<HistoryEntry>();

		for (HistoryEntry entry : historyEnteries)
			if (entry.getPage() instanceof BrowserPage && ((BrowserPage) entry.getPage()).getType() == BrowserPage.Type.HISTORY)
				continue;
			else
				entries.add(entry);
		
		return entries;
	}

	public Component getListCellRendererComponent(JList jlist, Object o, int index, boolean isSelected, boolean cellHasFocus)
	{
		HistoryEntry item = (HistoryEntry) o;

//		if (item.getPage() instanceof BrowserPage && ((BrowserPage) item.getPage()).getType() == BrowserPage.Type.HISTORY)
//			return null;

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setBackground(Color.WHITE);

		if (index > 0)
			panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		JLabel date = new JLabel(item.getDateString());
		date.setMinimumSize(new Dimension(100, 0));
		date.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		panel.add(date);

		JLabel time = new JLabel(item.getTimeString());
		time.setMinimumSize(new Dimension(50, 0));
		time.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		panel.add(time);

		JLabel label = new JLabel();
		label.setIconTextGap(12);
		label.setText(item.getPage().getTitle());
		label.setIcon(item.getPage().getFavIcon());
		panel.add(label);

		if (isSelected)
		{
//			label.setFont(new Font("", Font.PLAIN, 13));
			date.setForeground(Color.BLUE);
			time.setForeground(Color.BLUE);
			label.setForeground(Color.BLUE);
		}

		return panel;
	}

	private class HistoryListMouseAdapter extends MouseAdapter
	{
		private Tab tab;

		public HistoryListMouseAdapter(Tab tab)
		{
			this.tab = tab;
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			JList list = (JList) e.getSource();
			if(e.getClickCount() == 2)
			{
				int index = list.locationToIndex(e.getPoint());
				ListModel listModel = list.getModel();

				HistoryEntry entry = (HistoryEntry) listModel.getElementAt(index);
				list.ensureIndexIsVisible(index);
				tab.goTo(entry.getPage().getAddress());
			}
		}
	}

	private class HistoryListKeyAdapter extends KeyAdapter
	{
		private Tab tab;
		private JList list;

		public HistoryListKeyAdapter(Tab tab, JList list)
		{
			this.tab = tab;
			this.list = list;
		}

		@Override
		public void keyPressed(KeyEvent ke)
		{
			ListModel listModel = list.getModel();
			int index = list.getSelectedIndex();
			HistoryEntry entry = (HistoryEntry) listModel.getElementAt(index);
			list.ensureIndexIsVisible(index);
			if (ke.getKeyCode() == KeyEvent.VK_ENTER)
				tab.goTo(entry.getPage().getAddress());
		}
	}
}
