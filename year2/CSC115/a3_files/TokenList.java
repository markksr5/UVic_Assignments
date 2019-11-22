/**
 * CSC115 Assignment 3 : Calculator.
 * TokenList.java
 * Created for use by CSC115 Fall 2018
 */

/**
 * TokenList is a very simple list of String tokens.
 * Strings can be <i>appended</i>, added to the end of the list,
 * but cannot be removed.
 * Duplicate tokens are acceptable.
 */
public class TokenList {

	public String[] list;
	public int count;
	private static final int INIT_SIZE = 10;

	/**
	 * Creates a new empty token list.
	 */
	public TokenList() {
		list = new String[INIT_SIZE];
	}

	/**
	 * Creates an empty token list.
	 * @param capacity The initial storage available.
	 * 	This size is not binding as the size will increase
	 *	with the number of strings added.
	 * @throws NegativeArraySizeException if the capacity argument is less than 0.
	 */
	public TokenList(int capacity) {
		if (capacity < 0) {
			throw new NegativeArraySizeException();
		}
		list = new String[capacity];
	}

	/**
	 * Private helper method.
	 * Grows the list data field if it needs more capacity.
	 */
	private void growAndCopy() {
		String[] newList = new String[list.length*2];
		for (int i=0; i<count; i++) {
			newList[i] = list[i];
		}
		list = newList;
	}

	/**
	 * Adds a token to the end of the list.
	 * @param token The newly added token.
	 */
	public void append(String token) {
		if (list.length == count) {
			growAndCopy();
		}
		list[count++] = token;
	}

	/**
	 * Retrieves the token at a given index.
	 * @param index The index (0 ... size()-1) of the token.
	 * @return The token at that index.
	 * @throws IndexOutOfBoundsException if the index is not within 0 ... size()-1.
	 */
	public String get(int index) {
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException();
		} else {
			return list[index];
		}
	}

	/**
	 * @return The number of tokens in the list.
	 */
	public int size() {
		return count;
	}

	/**
	 * @return True if the list is empty, false if it is not.
	 */
	public boolean isEmpty() {
		return count == 0;
	}

	/**
	 * @param other The TokenList to compare to this one.
	 * @return True if this TokenList has exactly the same tokens, in the same order as the other TokenList.
	*/
	public boolean equals(TokenList other) {
		boolean result = true;
		if (count != other.count) {
			return false;
		}
		for (int i=0; i<count; i++) {
			if (!list[i].equals(other.list[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return A string that contains all the tokens, each separated by a single space.
	 * If the list is empty, the returned string is:<br><b>Empty List</b>.
	 */
	public String toString() {
		if (count==0) {
			return ("Empty list");
		}
		StringBuilder s = new StringBuilder(count*4);
		for (int i=0; i<count-1; i++) {
			s.append(list[i]+" ");
		}
		s.append(list[count-1]);
		return s.toString();
	}
}
