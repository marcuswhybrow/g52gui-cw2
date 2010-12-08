package net.marcuswhybrow.uni.g52gui.cw2;

import java.util.Stack;
import net.marcuswhybrow.uni.g52gui.cw2.visual.Reopenable;

/**
 *
 * @author marcus
 */
public interface ClosedItemsChangeListener
{
	public void closedItemsHaveChanged(Stack<Reopenable> items);
}
