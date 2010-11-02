package net.marcuswhybrow.uni.g52gui.cw2.bookmarks;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import net.marcuswhybrow.uni.g52gui.cw2.Browser;
import net.marcuswhybrow.uni.g52gui.cw2.Tab;
import net.marcuswhybrow.uni.g52gui.cw2.Tabs;
import net.marcuswhybrow.uni.g52gui.cw2.bookmarks.Folder.CannotDeleteRootFolderExcetpion;

/**
 *
 * @author Marcus Whybrow
 */
public class BookmarkManagerTab extends Tab
{
	private JSplitPane splitPane;
	private FolderTree bookmarksTree;
	private JTree bookmarksList;
	private ContextMenu contextMenu;

	private JScrollPane bookmarksListScrollPane = new JScrollPane();

	public BookmarkManagerTab(Tabs tabs)
	{
		super(tabs);

		address = "";
		title = "Bookmark Manager";

		contextMenu = new ContextMenu();

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		DefaultMutableTreeNode bookmarksBarBookmarks = convertToNode(Browser.get().getBookmarksBarBookmarks());
		DefaultMutableTreeNode otherBookmarksBookmarks = convertToNode(Browser.get().getOtherBookmarksBookmarks());
		rootNode.add(bookmarksBarBookmarks);
		rootNode.add(otherBookmarksBookmarks);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		bookmarksTree = new FolderTree(rootNode);
		bookmarksTree.setSelectionPath(new TreePath(bookmarksBarBookmarks.getPath()));
		splitPane.setLeftComponent(bookmarksTree);

		bookmarksList = new FolderContents(bookmarksTree.getSelectedFolder());
		splitPane.setRightComponent(bookmarksList);

		splitPane.setDividerLocation(200);

		JPanel mainArea = getMainArea();
		mainArea.setLayout(new BorderLayout());
		mainArea.add(splitPane, BorderLayout.CENTER);
	}

	private class FolderContents extends JTree
	{
		public FolderContents(Folder folder)
		{
			super(convertToNode(folder, false /* don't exlude things which arn't folders */, 1 /* Only 1 level of items */));
			setRootVisible(false);
			setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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

		public Folder getSelectedFolder()
		{
			TreePath path = getSelectionPath();
			if (path != null)
				return (Folder) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
			return null;
		}

		public void valueChanged(TreeSelectionEvent e)
		{
			splitPane.setRightComponent(new JScrollPane(new FolderContents(bookmarksTree.getSelectedFolder())));
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

		private JMenuItem cut = new JMenuItem("Cut");
		private JMenuItem copy = new JMenuItem("Copy");
		private JMenuItem paste = new JMenuItem("Paste");

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
			addMenuItem(cut);
			addMenuItem(copy);
			addMenuItem(paste);

			addSeparator();
			addMenuItem(delete);

			addSeparator();
			addMenuItem(addPage);
			addMenuItem(addFolder);

			paste.setEnabled(false);
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
				cut.setEnabled(false);
				copy.setEnabled(false);
			}
		}

		@Override
		public void show(Component component, int x, int y)
		{
			delete.setEnabled(true);
			rename.setEnabled(true);
			edit.setEnabled(true);
			cut.setEnabled(true);
			copy.setEnabled(true);
			super.show(component, x, y);
		}


		public void actionPerformed(ActionEvent e)
		{
			if (e.getActionCommand() != null)
			{
				String actionCommand = e.getActionCommand();
				System.out.println(actionCommand);

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
						System.out.println("Attempted to delete a RootFolder (shouldn't be possible)");
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
					folder.setName(JOptionPane.showInputDialog("New Folder Name", folder.getName()));
					((DefaultTreeModel) bookmarksTree.getModel()).nodeChanged(node);
				}
				else if (actionCommand.equals("Add Page..."))
				{
//					Bookmark bookmark = new Bookmark()
//					folder.addChild(folder);
				}
			}
		}
	}


	private DefaultMutableTreeNode convertToNode(Folder folder)
	{
		return convertToNode(folder, true, -1);
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
				if (excludeFiles)
				{
					if (child instanceof Folder)
						node.add(convertToNode((Folder) child, excludeFiles, --depth));
				}
				else
					node.add(convertToNode((Folder) child));
			}
		}
		return node;
	}
}
