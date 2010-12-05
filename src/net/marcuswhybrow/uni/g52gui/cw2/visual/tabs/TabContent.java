/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.marcuswhybrow.uni.g52gui.cw2.visual.tabs;

import net.marcuswhybrow.uni.g52gui.cw2.visual.tabs.Tab;
import java.awt.Component;

/**
 *
 * @author marcus
 */
public interface TabContent {
	public Component getContent();

	public void setTab(Tab tab);
	
	public void refresh();
	public void back();
	public void forward();
}
