package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import com.ucware.icontools.IconTools;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import net.marcuswhybrow.uni.g52gui.cw2.History;
import net.marcuswhybrow.uni.g52gui.cw2.HistoryEntry;

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

		JPanel wrapper = new JPanel();
		wrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		List newList = Arrays.asList(History.get().getArrayList().toArray().clone());
		Collections.reverse(newList);

		JList list = new JList(newList.toArray());
		list.setCellRenderer(this);

		wrapper.setLayout(new BorderLayout());
		wrapper.add(list, BorderLayout.CENTER);
		wrapper.setBackground(Color.WHITE);
		this.setViewportView(wrapper);
	}

	public Component getContent()
	{
		return this;
	}

	public void refresh()
	{
		this.tab.setTabContent(new HistoryTabContent(tab));
	}

	public void back()
	{
		// do nothing
	}

	public void forward()
	{
		// do nothing
	}

	public Component getListCellRendererComponent(JList jlist, Object o, int index, boolean isSelected, boolean cellHasFocus)
	{
		HistoryEntry item = (HistoryEntry) o;

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
		label.setText(item.getTitle());

		Icon favIcon = item.getFavIcon();
		if (favIcon != null)
			label.setIcon(favIcon);
		panel.add(label);

		return panel;
	}
}
