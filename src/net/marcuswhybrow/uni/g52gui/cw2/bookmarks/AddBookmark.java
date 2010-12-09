package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.marcuswhybrow.uni.g52gui.cw2.utils.ComboBoxItem;
import net.marcuswhybrow.uni.g52gui.cw2.Page;
import net.marcuswhybrow.uni.g52gui.cw2.utils.Utils;

/**
 *
 * @author marcus
 */
public class AddBookmark extends JFrame implements ActionListener
{
	private Page page;
	private ArrayList<ComboBoxItem> options;
	private JComboBox combo;
	private JTextField title;

	public AddBookmark(Page page)
	{
		super("Add Bookmark");
		
		this.page = page;

		JPanel wrapper = new JPanel();
		wrapper.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		wrapper.setLayout(new GridLayout(3,1));

		title = new JTextField(page.getTitle());
		wrapper.add(title);

		combo = Utils.getComboBoxForBookmarks();
//		combo.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

		wrapper.add(combo);

		JPanel buttons = new JPanel(new GridLayout(1,2));

		JButton button = new JButton("Add");
		button.addActionListener(this);
		buttons.add(button);

		button = new JButton("Cancel");
		button.addActionListener(this);
		buttons.add(button);

//		buttons.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

		wrapper.add(buttons);

		this.add(wrapper);

		this.setMinimumSize(new Dimension(100, 0));
		this.pack();
		this.setResizable(false);

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae)
	{
		if ("Add".equals(ae.getActionCommand()))
		{
			Folder folder = ((ComboBoxItem) combo.getSelectedItem()).getFolder();
			page.setTitle(title.getText());
			folder.addChild(new Bookmark(page));
			this.dispose();
		}
		else if ("Cancel".equals(ae.getActionCommand()))
			this.dispose();
	}
}
