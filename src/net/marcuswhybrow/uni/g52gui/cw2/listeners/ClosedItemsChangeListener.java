package net.marcuswhybrow.uni.g52gui.cw2.listeners;

import java.util.Stack;
import net.marcuswhybrow.uni.g52gui.cw2.visual.IReopenable;

/**
 *
 * @author marcus
 */
public interface ClosedItemsChangeListener
{
	public void closedItemsHaveChanged(Stack<IReopenable> items);
}
