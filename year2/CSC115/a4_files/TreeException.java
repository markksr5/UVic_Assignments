/**
 * CSC115 Assignment 4 : Binary Trees.
 * TreeException.java
 * Created for use by CSC115 Fall 2018
 */

/**
 * An exception thrown when illegal operations or requests
 * are made in a Binary Tree ADT.
 */
public class TreeException extends RuntimeException {
	/**
	 * Creates an exception.
	 * @param msg The message to the calling program.
	 */
	public TreeException(String msg) {
		super(msg);
	}

	/**
	 * Creates an exception without a message.
	 */
	public TreeException() {
		super();
	}
}
