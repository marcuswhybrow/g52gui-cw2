package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.ComboBoxItem;
import net.marcuswhybrow.uni.g52gui.cw2.Page;
import net.marcuswhybrow.uni.g52gui.cw2.Utils;

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

		this.setLayout(new FlowLayout());

		add(new JLabel("Bookmark Added!"));
		title = new JTextField(page.getTitle());
		add(title);

		combo = Utils.getComboBoxForBookmarks();

		add(combo);

		JButton done = new JButton("Done");
		done.addActionListener(this);
		add(done);
		add(new JButton("Remove"));

		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae)
	{
		Folder folder = ((ComboBoxItem) combo.getSelectedItem()).getFolder();
		page.setTitle(title.getText());
		folder.addChild(new Bookmark(page));
		this.dispose();
	}
}
