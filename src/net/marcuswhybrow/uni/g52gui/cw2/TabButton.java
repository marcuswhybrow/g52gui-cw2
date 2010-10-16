package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author marcus
 */
public class TabButton extends JPanel
{
	/** The tab which this button should close */
	private final Tab tab;

	public TabButton(Tab tab)
	{
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));

		this.tab = tab;
        setOpaque(false);

        //make JLabel read titles from JTabbedPane
        JLabel label = new TabTitle(tab);
		add(label);

		//add more space between the label and the button
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

		//tab button
		add(new CloseTabButton());

		//add more space to the top of the component
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}

	private class TabTitle extends JLabel
	{
		private Tab tab;

		public TabTitle(Tab tab)
		{
			this.tab = tab;
		}
		
		@Override
		public String getText()
		{
			if (tab != null)
				return tab.getTitle();
			return null;
		}
	}

	private class CloseTabButton extends JButton implements ActionListener, MouseListener
	{

		public CloseTabButton()
		{
			int size = 17;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("close this tab");

			//Make the button looks the same for all Laf's
			setUI(new BasicButtonUI());

			//Make it transparent
			setContentAreaFilled(false);

			//No need to be focusable
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);

			//Making nice rollover effect
			//we use the same listener for all buttons
			addMouseListener(this);
			setRolloverEnabled(true);

			//Close the proper tab by clicking the button
			addActionListener(this);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();

			//shift the image for pressed buttons
			if (getModel().isPressed())
				g2.translate(1, 1);

			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);

			if (getModel().isRollover())
				g2.setColor(Color.MAGENTA);

			int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2.dispose();
		}

		public void actionPerformed(ActionEvent e)
		{
			tab.close();
		}

		public void mouseClicked(MouseEvent e)
		{
			// do nothing
		}

		public void mousePressed(MouseEvent e)
		{
			// do nothing
		}

		public void mouseReleased(MouseEvent e)
		{
			// do nothing
		}

		public void mouseEntered(MouseEvent e)
		{
			Component component = e.getComponent();
			if (component instanceof AbstractButton)
				((AbstractButton) component).setBorderPainted(true);
		}

		public void mouseExited(MouseEvent e)
		{
			Component component = e.getComponent();
			if (component instanceof AbstractButton)
				((AbstractButton) component).setBorderPainted(false);
		}
	}
}
