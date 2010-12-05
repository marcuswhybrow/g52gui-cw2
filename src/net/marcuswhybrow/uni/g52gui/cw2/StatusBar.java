package net.marcuswhybrow.uni.g52gui.cw2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author marcus
 */
public class StatusBar extends JPanel
{
	private JPanel addressInfoPanel;
	private enum State {BUSY, FREE};
	private State state = State.FREE;
	private JLabel addressInfoLabel;
	private Window window;

	public StatusBar(Window window)
	{
		this.window = window;
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		this.setMinimumSize(new Dimension(200, 200));

		this.addressInfoPanel = new JPanel();
		this.addressInfoPanel.setLayout(new FlowLayout());

		this.addressInfoLabel = new JLabel(" ");
		this.addressInfoLabel.setFont(new Font("Verdana", Font.PLAIN, 10));
		this.addressInfoPanel.add(addressInfoLabel);

		this.add(addressInfoPanel, BorderLayout.WEST);
	}

	public boolean setText(String text)
	{
		if (state == State.FREE)
		{
			this.addressInfoLabel.setText(text);
			return true;
		}
		return false;
	}

	public boolean clear()
	{
		if (state == State.FREE)
		{
			this.addressInfoLabel.setText(" ");
			return true;
		}
		return false;
	}

	public void setImportantText(String text)
	{
		state = State.BUSY;
		this.addressInfoLabel.setText(text);
	}

	public void importantClear()
	{
		this.addressInfoLabel.setText(" ");
	}

	public void doneWithImportantText()
	{
		this.state = State.FREE;
		importantClear();
	}
}
