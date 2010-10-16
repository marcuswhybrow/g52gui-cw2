
package net.marcuswhybrow.uni.g52gui.cw2;

/**
 *
 * @author Marcus Whybrow
 */
public interface Reopenable
{
	/**
	 * Reopen the window or tab which was closed
	 */
	public void reopen();

	/**
	 * Closes the window or tab and puts it on the reopen stack
	 */
	public void close();

	/**
	 * Determines if the window or tab is closed, used to pick the next object
	 * in the stack if the current object has alreay been opened
	 */
	public boolean isClosed();
}
