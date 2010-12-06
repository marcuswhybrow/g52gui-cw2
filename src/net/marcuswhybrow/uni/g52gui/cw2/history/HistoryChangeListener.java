/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.marcuswhybrow.uni.g52gui.cw2.history;

import java.util.ArrayList;

/**
 *
 * @author marcus
 */
public interface HistoryChangeListener {
	public void historyHasChanged(ArrayList<HistoryEntry> history);
	public void visitCountHasChanged(ArrayList<VisitCountEntry> visitCount);
}
