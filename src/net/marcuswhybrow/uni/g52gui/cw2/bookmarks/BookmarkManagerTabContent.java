package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.ComboBoxItem;
import net.marcuswhybrow.uni.g52gui.cw2.Page;
import net.marcuswhybrow.uni.g52gui.cw2.Utils;
import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.Tab;
import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.TabContent;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Folder.CannotDeleteRootFolderExcetpion;

/**
 *
 * @author Marcus Whybrow
 */
public class BookmarkManagerTabContent extends JSplitPane implements TabContent
{
	private FolderTree bookmarksTree;
	private JList bookmarksList;
	private ContextMenu contextMenu;
	private Tab tab;

	public BookmarkManagerTabContent(Tab tab)
	{
		this.tab = tab;
		
		contextMenu = new ContextMenu();

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		DefaultMutableTreeNode bookmarksBarBookmarks = convertToNode(Browser.get().getBookmarksBarBookmarks());
		DefaultMutableTreeNode otherBookmarksBookmarks = convertToNode(Browser.get().getOtherBookmarksBookmarks());
		rootNode.add(bookmarksBarBookmarks);
		rootNode.add(otherBookmarksBookmarks);

		bookmarksTree = new FolderTree(rootNode);
		bookmarksTree.setSelectionPath(new TreePath(bookmarksBarBookmarks.getPath()));
		setLeftComponent(new JScrollPane(bookmarksTree));

		bookmarksList = new FolderContents(bookmarksTree.getSelectedFolder());
		setRightComponent(new JScrollPane(bookmarksList));

		setDividerLocation(200);
	}

	public Component getContent()
	{
		return this;
	}

	public void setTab(Tab tab)
	{
		this.tab = tab;
	}

	private class FolderContents extends JList implements ListCellRenderer
	{
		public FolderContents(Folder folder)
		{
			super(folder != null ? folder.getChildren() : null);
			setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			this.setCellRenderer(this);

			this.addMouseListener(new BookmarksListMouseAdapter(tab, bookmarksTree));
			this.addKeyListener(new BookmarksListKeyAdapter(tab, this, bookmarksTree));
		}

		public Component getListCellRendererComponent(JList jlist, Object o, int index, boolean isSelected, boolean cellHasFocus)
		{
			JLabel label = null;
			if (o instanceof Folder)
				label = new JLabel(((Folder) o).getName(), Browser.get().getFolderIcon(), JLabel.LEFT);
			else if (o instanceof Bookmark)
			{
				Bookmark bookmark = (Bookmark) o;
				label = new JLabel(bookmark.getPage().getTitle(), bookmark.getPage().getFavIcon(), JLabel.LEFT);
			}

			if (label != null)
			{
				label.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
			}

			if (isSelected)
			{
				label.setForeground(Color.BLUE);
			}

			return label;
		}

		private class BookmarksListMouseAdapter extends MouseAdapter
		{
			private Tab tab;
			private FolderTree bookmarksTree;

			public BookmarksListMouseAdapter(Tab tab, FolderTree bookmarksTree)
			{
				this.tab = tab;
				this.bookmarksTree = bookmarksTree;
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				JList list = (JList) e.getSource();
				if(e.getClickCount() == 2)
				{
					int index = list.locationToIndex(e.getPoint());
					ListModel listModel = list.getModel();

					BookmarkItem item = (BookmarkItem) listModel.getElementAt(index);
					list.ensureIndexIsVisible(index);

					if (item instanceof Bookmark)
					{
						Bookmark bookmark = (Bookmark) item;
						tab.goTo(bookmark.getPage().getAddress());
					}
					else if (item instanceof Folder)
						selectNodeUnderSelectedNodeByFolder((Folder) item);
				}
			}
		}

		private void selectNodeUnderSelectedNodeByFolder(Folder folder)
		{
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) bookmarksTree.getSelectionPath().getLastPathComponent();

			for (int i = 0; i < dmtn.getChildCount(); i++)
			{
				DefaultMutableTreeNode dmtnChild = (DefaultMutableTreeNode) dmtn.getChildAt(i);
				BookmarkItem compItem = (BookmarkItem) (dmtnChild).getUserObject();
				if (compItem instanceof Folder && ((Folder) compItem).equals(folder))
				{
					bookmarksTree.setSelectionPath(new TreePath(dmtnChild.getPath()));
					bookmarksTree.scrollPathToVisible(new TreePath(dmtnChild.getPath()));
					break;
				}
			}
		}

		private class BookmarksListKeyAdapter extends KeyAdapter
		{
			private Tab tab;
			private JList list;
			private FolderTree bookmarksTree;

			public BookmarksListKeyAdapter(Tab tab, JList list, FolderTree bookmarksTree)
			{
				this.tab = tab;
				this.list = list;
				this.bookmarksTree = bookmarksTree;
			}

			@Override
			public void keyPressed(KeyEvent ke)
			{
				ListModel listModel = list.getModel();
				int index = list.getSelectedIndex();
				BookmarkItem item = (BookmarkItem) listModel.getElementAt(index);
				list.ensureIndexIsVisible(index);

				if (ke.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if (item instanceof Bookmark)
					{
						Bookmark bookmark = (Bookmark) item;
						tab.goTo(bookmark.getPage().getAddress());
					}
					else if (item instanceof Folder)
						selectNodeUnderSelectedNodeByFolder((Folder) item);
				}
			}
		}
	}

	private class FolderTree extends JTree implements TreeSelectionListener
	{
		public FolderTree(MutableTreeNode node)
		{
			super(node);

			// Expand initial rows
			// TODO make memory based
			expandRow(1);
			expandRow(0);
			setRootVisible(false);

			setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			addMouseListener(new MouseAdapter()
			{
				@Override
				public void mousePressed(MouseEvent e)
				{
					TreePath selectedPath = bookmarksTree.getPathForLocation(e.getX(), e.getY());
					if (selectedPath != null)
					{
						if (e.getButton() == MouseEvent.BUTTON3)
						{
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedPath.getPath()[selectedPath.getPathCount() - 1];
							if (e.isPopupTrigger())
							{
								bookmarksTree.setSelectionPath(selectedPath);
								contextMenu.show(e.getComponent(), e.getX(), e.getY(), node);
							}
						}
					}
				}
			});

			addTreeSelectionListener(this);
		}

		public DefaultMutableTreeNode getSelectedNode()
		{
			TreePath path = getSelectionPath();
			if (path != null)
				return (DefaultMutableTreeNode) path.getLastPathComponent();
			return null;
		}

		public Folder getSelectedFolder()
		{
			TreePath path = getSelectionPath();
			if (path != null)
				return (Folder) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
			return null;
		}

		public void valueChanged(TreeSelectionEvent e)
		{
			setRightComponent(new JScrollPane(new FolderContents(bookmarksTree.getSelectedFolder())));
		}
	}

	private class ContextMenu extends JPopupMenu implements ActionListener
	{
		private DefaultMutableTreeNode node;
		private Folder folder;

		private JMenuItem openAllBookmarks = new JMenuItem("Open All Bookmarks");
		private JMenuItem openAllBookmarksInNewWindow = new JMenuItem("Open All Bookmarks in New Window");

		private JMenuItem rename = new JMenuItem("Rename");
		private JMenuItem edit = new JMenuItem("Edit");

//		private JMenuItem cut = new JMenuItem("Cut");
//		private JMenuItem copy = new JMenuItem("Copy");
//		private JMenuItem paste = new JMenuItem("Paste");

		private JMenuItem delete = new JMenuItem("Delete");

		private JMenuItem addPage = new JMenuItem("Add Page...");
		private JMenuItem addFolder = new JMenuItem("Add Folder...");

		public ContextMenu()
		{
			addMenuItem(openAllBookmarks);
			addMenuItem(openAllBookmarksInNewWindow);
			
			addSeparator();
			addMenuItem(rename);
			addMenuItem(edit);

			addSeparator();
//			addMenuItem(cut);
//			addMenuItem(copy);
//			addMenuItem(paste);

			addSeparator();
			addMenuItem(delete);

			addSeparator();
			addMenuItem(addPage);
			addMenuItem(addFolder);

//			paste.setEnabled(false);
		}

		private void addMenuItem(JMenuItem menuItem)
		{
			if (menuItem != null)
			{
				menuItem.setActionCommand(menuItem.getText());
				menuItem.addActionListener(this);
				add(menuItem);
			}
		}

		public DefaultMutableTreeNode getNode()
		{
			return node;
		}

		public void show(Component component, int x, int y, DefaultMutableTreeNode node)
		{
			show(component, x, y);

			this.node = node;
			this.folder = (Folder) node.getUserObject();
			if (folder.isRoot())
			{
				delete.setEnabled(false);
				rename.setEnabled(false);
				edit.setEnabled(false);
//				cut.setEnabled(false);
//				copy.setEnabled(false);
			}
		}

		@Override
		public void show(Component component, int x, int y)
		{
			delete.setEnabled(true);
			rename.setEnabled(true);
			edit.setEnabled(true);
//			cut.setEnabled(true);
//			copy.setEnabled(true);
			super.show(component, x, y);
		}


		public void actionPerformed(ActionEvent e)
		{
			if (e.getActionCommand() != null)
			{
				String actionCommand = e.getActionCommand();

				if ("Edit".equals(actionCommand))
				{
					Object[] choices = Utils.getComboBoxItemsForBookmarks(folder);
					ComboBoxItem i = (ComboBoxItem) JOptionPane.showInputDialog(null, "Choose A New Folder...", "Move A Folder", JOptionPane.QUESTION_MESSAGE, null, choices, choices[1]);

					if (i != null)
					{
						Folder newFolder = i.getFolder();
						Folder oldParent = folder.getParent();
						folder.moveTo(newFolder);

//						// Refresh
//						DefaultTreeModel dtm = (DefaultTreeModel) bookmarksTree.getModel();
//						dtm.nodeChanged(node.getParent());

						// Hack way of gettings the page to refresh
						tab.refresh();
					}
				}
				if (actionCommand.equals("Delete"))
				{

					// Get the parent node before deleting it
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

					// delete the node from the UI
					((DefaultTreeModel)bookmarksTree.getModel()).removeNodeFromParent(node);

					// delete the node from data structure
					try
					{
						folder.delete();
					}
					catch (CannotDeleteRootFolderExcetpion ex)
					{
						System.err.println("Attempted to delete a RootFolder (shouldn't be possible)");
					}

					// Select the parent node in the UI
					bookmarksTree.setSelectionPath(new TreePath(parent.getPath()));
				}
				else if (actionCommand.equals("Add Folder..."))
				{
					if (node != null && folder != null)
					{
						// Create a new Folder and it it to the existing folder
						Folder newFolder = new Folder(JOptionPane.showInputDialog("New Folder Name"));
						folder.addChild(newFolder);

						// The new node for the JTree
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newFolder);

						// Actually add the node to the JTree
						((DefaultTreeModel) bookmarksTree.getModel()).insertNodeInto(newNode, node, node.getChildCount());

						// Select the new node and ensure the user can see it
						TreePath path = new TreePath(newNode.getPath());
						bookmarksTree.setSelectionPath(path);
						bookmarksTree.scrollPathToVisible(path);
					}
					else
					{
						System.err.println("Context Menu is Malfunctioning");
					}
				}
				else if (actionCommand.equals("Rename"))
				{
					String newName = JOptionPane.showInputDialog("New Folder Name", folder.getName());
					if (newName != null)
					{
						folder.setName(newName);
						((DefaultTreeModel) bookmarksTree.getModel()).nodeChanged(node);
					}
				}
				else if (actionCommand.equals("Add Page..."))
				{
					String url = JOptionPane.showInputDialog("URL");
					Bookmark bookmark = new Bookmark(new Page(url), folder);
				}
			}
		}
	}


	private DefaultMutableTreeNode convertToNode(Folder folder)
	{
		return convertToNode(folder, true, -1);
	}

	private DefaultMutableTreeNode convertToNode(Bookmark bookmark)
	{
		return new DefaultMutableTreeNode(bookmark);
	}

	private DefaultMutableTreeNode convertToNode(Folder folder, boolean excludeFiles, int depth)
	{
		if (folder == null)
			return null;
		
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(folder);

		// Stop adding children if the depth becomes 0,
		// thus if the depth started at -1 (since it always decrements) it will
		// carry on forever
		if (depth < 0 || depth >= 1)
		{
			for (BookmarkItem child : folder.getChildren())
			{
				if (! excludeFiles && child instanceof Bookmark)
					node.add(convertToNode((Bookmark) child));

				if (child instanceof Folder)
					node.add(convertToNode((Folder) child, excludeFiles, --depth));
			}
		}
		return node;
	}
}
