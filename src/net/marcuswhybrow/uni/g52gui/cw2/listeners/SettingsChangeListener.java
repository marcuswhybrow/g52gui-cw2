/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.marcuswhybrow.uni.g52gui.cw2.listeners;

/**
 *
 * @author marcus
 */
public interface SettingsChangeListener {
	public void settingHasChanged(String settingName, Object oldValue, Object newValue);
}
