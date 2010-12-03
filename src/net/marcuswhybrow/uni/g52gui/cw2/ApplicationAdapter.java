package net.marcuswhybrow.uni.g52gui.cw2;

import com.apple.eawt.Application;

/**
 * It's very important that this class is *never* used if on an operating sytem
 * other Mac OS. This is becuase imports are made which are only availble on Mac
 * OS.
 *
 * @author Marcus Whybrow
 */
public class ApplicationAdapter
{

	/**
	 * A method which allows all of the Mac type stuff to remain inside
	 * of this class alone and no where else.
	 */
	public void setup()
	{
		Application.getApplication();
	}
}
