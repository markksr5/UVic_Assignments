/**
 * CSC115 Assignment 4 : Binary Trees.
 * InvalidExpressionException.java.
 * Created for use by CSC115 Fall 2018
 */

/**
 * An exception thrown when an Arithmetic expression is determined to be invalid.
 */ 
public class InvalidExpressionException extends RuntimeException {

	/**
	 * Creates an exception.
	 * @param msg The message to the calling program.
	 */
	public InvalidExpressionException(String msg) {
		super(msg);
	}

	/**
	 * Creates an exception without a message.
	 */
	public InvalidExpressionException() {
		super();
	}
}
