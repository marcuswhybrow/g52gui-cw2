package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.event.ActionEvent;
import net.marcuswhybrow.uni.g52gui.cw2.visual.Frame;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.util.prefs.Preferences;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import net.marcuswhybrow.uni.g52gui.cw2.visual.Window;
import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.Tab;

/**
 *
 * @author Marcus Whybrow
 */
public class Settings extends JFrame implements WindowListener, Frame
{
	private static Settings settings;

	private Preferences prefs;
	private JPanel wrapper;
	private BoxLayout wrapperBoxLayout;
	private ArrayList<Section> sections;

	public enum NewTabState {USE_NEW_TAB_PAGE, USE_HOME_PAGE, NOT_SET};

	private ArrayList<SettingsChangeListener> changeListeners;

	private Settings() {
		super("Preferences");
	}

	private void setup()
	{
		changeListeners = new ArrayList<SettingsChangeListener>();

		this.addWindowListener(this);
		this.sections = new ArrayList<Section>();

		prefs = Preferences.userRoot().node("/net/marcuswhybrow/uni/g52gui/cw2");

		this.wrapper = new JPanel();
		this.wrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
		this.wrapperBoxLayout = new BoxLayout(this.wrapper, BoxLayout.Y_AXIS);
		this.wrapper.setLayout(wrapperBoxLayout);

		this.addSection(new HomePageSection());
		this.addSection(new NewTabSection());
		this.addSection(new ToolBarSection());

		this.add(wrapper);
		this.setMinimumSize(new Dimension(300, 0));
		this.pack();
		this.setResizable(false);

		this.setLocationRelativeTo(null);
	}

	public static Settings get()
	{
		if (settings == null)
		{
			settings = new Settings();
			settings.setup();
		}
		return settings;
	}

	public void showSettings()
	{
		this.setVisible(true);
	}

	public void hideSettings()
	{
		this.setVisible(false);
	}

	private void addSection(Section section)
	{
		this.sections.add(section);
		this.wrapper.add(section);
//		this.updateGridLayout();
	}

	private void removeSection(Section section)
	{
		this.sections.remove(section);
		this.wrapper.remove(section);
//		this.updateGridLayout();
	}

	private void saveAllSections()
	{
		for (Section section : sections)
			section.save();
	}

//	private void updateGridLayout()
//	{
//		this.wrapperBoxLayout.setRows(this.sections.size());
//	}

	

	public String getHomePage()
	{
		return prefs.get("home page", null);
	}

	public void setHomePage(String homePage)
	{
		String oldValue = getHomePage();

		if (!oldValue.equals(homePage))
		{
			prefs.put("home page", homePage);
			notifySettingsChangeListeners("home page", oldValue, homePage);
		}
	}


	public NewTabState getNewTabState()
	{
		return NewTabState.valueOf(prefs.get("new tab state", NewTabState.NOT_SET.name()));
	}

	public void setNewTabState(NewTabState newTabState)
	{
		NewTabState oldValue = getNewTabState();

		if (oldValue != newTabState)
		{
			prefs.put("new tab state", newTabState.name());
			notifySettingsChangeListeners("new tab state", oldValue, newTabState);
		}
	}

	public boolean getShowHomeButton()
	{
		return prefs.getBoolean("show home button", true);
	}

	public void setShowHomeButton(boolean bool)
	{
		boolean oldValue = getShowHomeButton();

		if (oldValue != bool)
		{
			prefs.putBoolean("show home button", bool);
			notifySettingsChangeListeners("show home button", oldValue, bool);
		}
	}

	public boolean getAlwaysShowBookmarksBar()
	{
		return prefs.getBoolean("always show bookmarks bar", true);
	}

	public void setAlwaysShowBookmarksBar(boolean bool)
	{
		boolean oldValue = getAlwaysShowBookmarksBar();

		if (oldValue != bool)
		{
			prefs.putBoolean("always show bookmarks bar", bool);
			notifySettingsChangeListeners("always show bookmarks bar", oldValue, bool);
		}
	}





	public void windowOpened(WindowEvent we)
	{
		//
	}

	public void windowClosing(WindowEvent we)
	{
		this.saveAllSections();
	}

	public void windowClosed(WindowEvent we)
	{
		//
	}

	public void windowIconified(WindowEvent we)
	{
		//
	}

	public void windowDeiconified(WindowEvent we)
	{
		//
	}

	public void windowActivated(WindowEvent we)
	{
		Browser.get().setActiveFrame(this);
	}

	public void windowDeactivated(WindowEvent we)
	{
		//
	}

	public void close()
	{
		this.hideSettings();
	}

	private class Section extends JPanel
	{
		private Component component = null;
		private JPanel wrapper;
		private Font titleFont;

		public Section(String title)
		{
			this.titleFont = new Font("Verdana", Font.BOLD, 12);
			
			this.setLayout(new GridLayout(2,1));
			JLabel label = new JLabel(title);
			label.setFont(titleFont);
			this.add(label);

			this.wrapper = new JPanel();
			this.wrapper.setBorder(BorderFactory.createEmptyBorder(5, 20, 0, 0));
			this.wrapper.setLayout(new GridLayout());
			this.wrapper.setMinimumSize(new Dimension(300, 0));
			this.add(this.wrapper);
		}

		public Component getComponent()
		{
			return this.component;
		}

		public void setComponent(Component component)
		{
			this.component = component;
			this.wrapper.removeAll();
			this.wrapper.add(component);
		}

		public void save()
		{
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	private class HomePageSection extends Section
	{
		private JTextField homePageTextArea = new JTextField(Settings.get().getHomePage());

		public HomePageSection()
		{
			super("Home Page");
			this.setComponent(homePageTextArea);
		}

		@Override
		public void save()
		{
			Settings.get().setHomePage(homePageTextArea.getText());
		}
	}

	private class NewTabSection extends Section
	{
		private JPanel wrapper;

		private ButtonGroup group;
		private JRadioButton useNewTabPage;
		private JRadioButton useHomePage;

		public NewTabSection()
		{
			super("New Tab Page");
			
			useNewTabPage = new JRadioButton("Use the New Tab page");
			useHomePage = new JRadioButton("Use your Home page");

			group = new ButtonGroup();
			group.add(useNewTabPage);
			group.add(useHomePage);

			switch (Settings.get().getNewTabState())
			{
				case USE_HOME_PAGE:
					useHomePage.setSelected(true);
					break;
				case USE_NEW_TAB_PAGE:
					useNewTabPage.setSelected(true);
					break;
				case NOT_SET:
					useNewTabPage.setSelected(true);
			}

			wrapper = new JPanel();
			wrapper.setLayout(new GridLayout(2,1));
			wrapper.add(useNewTabPage);
			wrapper.add(useHomePage);

			this.setComponent(wrapper);
		}

		@Override
		public void save()
		{
			if (useNewTabPage.isSelected())
				Settings.get().setNewTabState(NewTabState.USE_NEW_TAB_PAGE);
			else if (useHomePage.isSelected())
				Settings.get().setNewTabState(NewTabState.USE_HOME_PAGE);
		}
	}

	private class ToolBarSection extends Section implements ActionListener
	{
		private JCheckBox checkBox = new JCheckBox("Show Home button", Settings.get().getShowHomeButton());

		public ToolBarSection()
		{
			super("Toolbar");
			this.setComponent(checkBox);

			checkBox.addActionListener(this);
		}

		@Override
		public void save()
		{
			Settings.get().setShowHomeButton(checkBox.isSelected());
		}

		public void actionPerformed(ActionEvent ae)
		{
			this.save();
			for (Window window : Browser.get().getWindows())
				for (Tab tab : window.getTabs().getAllTabs())
					tab.getToolBar().setHomeButtonVisible(Settings.get().getShowHomeButton());
		}
	}


	
	// Change Listeners

	public void addSettingsChangeListener(SettingsChangeListener scl)
	{
		this.changeListeners.add(scl);
	}

	public void removeSettingsChangeListener(SettingsChangeListener scl)
	{
		this.changeListeners.remove(scl);
	}

	public void notifySettingsChangeListeners(String settingName, Object oldValue, Object newValue)
	{
		for (SettingsChangeListener scl : changeListeners)
			scl.settingHasChanged(settingName, oldValue, newValue);
	}
}
