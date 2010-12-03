package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.util.prefs.Preferences;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author Marcus Whybrow
 */
public class Settings extends JFrame implements WindowListener
{
	private static Settings settings;

	private Preferences prefs;
	private JPanel wrapper;
	private GridLayout wrapperGridLayout;
	private ArrayList<Section> sections;

	private Settings() {
		super("Preferences");
	}

	private void setup()
	{
		this.addWindowListener(this);
		this.sections = new ArrayList<Section>();

		prefs = Preferences.userRoot().node("/net/marcuswhybrow/uni/g52gui/cw2");

		this.wrapper = new JPanel();
		this.wrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
		this.wrapperGridLayout = new GridLayout(1,1);
		this.wrapper.setLayout(wrapperGridLayout);

		this.addSection(new HomePageSection());

		this.add(wrapper);
		this.setMinimumSize(new Dimension(300, 100));
		this.pack();
		this.setResizable(false);
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

	public void showPreferences()
	{
		this.setVisible(true);
	}

	public void hidePreferences()
	{
		this.setVisible(false);
	}

	private void addSection(Section section)
	{
		this.sections.add(section);
		this.wrapper.add(section);
		this.updateGridLayout();
	}

	private void removeSection(Section section)
	{
		this.sections.remove(section);
		this.wrapper.remove(section);
		this.updateGridLayout();
	}

	private void saveAllSections()
	{
		for (Section section : sections)
			section.save();
	}

	private void updateGridLayout()
	{
		this.wrapperGridLayout.setRows(this.sections.size());
	}

	

	public String getHomePage()
	{
		return prefs.get("home page", null);
	}

	public void setHomePage(String homePage)
	{
		prefs.put("home page", homePage);
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
		//
	}

	public void windowDeactivated(WindowEvent we)
	{
		//
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
		private JTextArea homePageTextArea = new JTextArea(Settings.get().getHomePage());

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
}
