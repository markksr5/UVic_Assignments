/**
 *
 * CSC115 Assignment 3 : Calculator.
 * Tools.java
 * Created for use by CSC115 Fall 2018
 * Written by B.Bultena
 */

/**
 * Tools class is a repository for useful methods not associated with
 * any particular object.
 * Methods are added as needed.
 *
 */ 
public class Tools {
	
	/**
 	 * Determines whether a string has balanced tokens that partition parts of 
	 * the text so that every <code>end</code> token has a matching <code>start</code> token,
	 * as the string is parsed from left to right.
	 * After parsing is complete, all <code>start</code> tokens have a matching <code>end</code> token.
	 * Generally the tokens are sets of parentheses characters, like (),{},[], or &lt;&gt;.
	 * However, the set of partition characters are not limited.
	 * @param tokens This is the complete set of tuples used as partitioning tokens.
	 * 		Each tuple consists of the opening character and its matching closing character.
	 *			Opening and closing characters cannot be the same character.
	 * 		For instance to check for {} and () balancing, tokens = "{}()" or "(){}".
	 * @param text The string that may or may not contain the token characters.
	 * @return True if the tokens are <i>matching</i> as described above.
	 *		If the token string is empty, then the result is trivially true.
	 */
	public static boolean isBalancedBy( String tokens, String text ) {
		return genBalanced(tokens,text);
	}
		
	/** 
	 * Private recursive method that does all the work for the isBalancedBy method.
	 * Based on the notion that every externally balanced matching set of parentheses must contain
	 * a balanced string.
	 * Also, a balanced string may have several non-nesting sets that are treated as separate strings.
	 * @param textPortion The text to check
	 * @param tokens The parentheses.
	 */
	private static boolean genBalanced(String tokens, String textPortion) {
		// base case: substring is empty
		if (textPortion.length() == 0) {
			return true;
		}
		// look for first open paren: everything before that can be discarded.
		// if closed paren found first: bad string
		int openIndex = -1;
		int openerIndex = -1;
		boolean stop = false;
		for (int i=0; i<textPortion.length() && !stop; i++) {
			// check each open paren as a possibility
			for (int k=0; k<tokens.length(); k+=2) {
				if (textPortion.charAt(i)==tokens.charAt(k+1)) {
					// closed paren found
					return false;
				}
				if (textPortion.charAt(i)==tokens.charAt(k)) {
					openIndex = i;
					openerIndex = k;
					stop = true;
					break;
				}
			}
		}
		if (openIndex == -1) {
			// no paren found
			return true;
		}
		// find the matching closing paren
		char opener = tokens.charAt(openerIndex);
		char closer = tokens.charAt(openerIndex+1);
		int closeIndex = -1;
		int balance = 1;
		for (int i=openIndex+1; i<textPortion.length(); i++) {
			if (textPortion.charAt(i) == opener) {
				balance++;
			} else if (textPortion.charAt(i) == closer) {
				balance--;
				if (balance == 0) {
					closeIndex = i;
					break;
				}
			}
		}
		if (closeIndex == -1) {
			// no matching closing paren
			return false;
		}
		// recurse on the substring inside the parentheses and 
		// then on any remaining substring proceeding these parentheses that we have not looked at yet.
		boolean firstResult = genBalanced(tokens,textPortion.substring(openIndex+1,closeIndex));
		boolean secondResult = true;
		if (closeIndex+1 < textPortion.length()) {
			secondResult = genBalanced(tokens,textPortion.substring(closeIndex+1,textPortion.length()));
		}
		return firstResult && secondResult;
	}
	
	public static boolean isInt(String token) {
		boolean isInteger = true;
		try {
			Integer.parseInt(token);
		}
		catch (NumberFormatException e) {
			isInteger = false;
			//System.out.println(token + " is not a valid integer number"); 
		}
		finally {
			return isInteger;
		}
	}
	
	public static boolean isLeftBracket(String token) {
		switch(token) {
			case "(":
			case "[":
			case "{":
				return true;
			default:
				return false;
		}
	}
	
	public static boolean isRightBracket(String token) {
		switch(token) {
			case ")":
			case "]":
			case "}":
				return true;
			default:
				return false;
		}
	}
	
	public static boolean isMatchingBracket(String lefttoken, String righttoken) {
		switch(righttoken) {
			case ")":
				if (lefttoken.equals("(")) return true;
			case "]":
				if (lefttoken.equals("[")) return true;
			case "}":
				if (lefttoken.equals("{")) return true;
			default:
				return false;
		}
	}
	
	public static int precedence(String token) {
		switch (token) {
			case "+":
				return 1;
			case "-":
				return 1;
			case "*":
				return 2;
			case "/":
				return 2;
			case "^":
				return 3;
			default:
				return -1;
		}
	}
	
	public static double getDouble(String token) throws InvalidExpressionException {
		double result = 0;
		try {
			result = Double.parseDouble(token);
		}
		catch (InvalidExpressionException e) {
			System.out.println("token is not a number");
		}
		return result;
	}
	
	public static double getPower(double a, double b) {
		double result = a;
		for (int i = 0; i < b-1; i++) {
			result *= a;
		}
		return result;
	}
			
	/**
	 * Test harness to check methods.
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		System.out.println("testing the isBalancedBy method:");
		String tokens = "(){}[]<>";
		String test = "()()(){()()}[]<>";
		System.out.println(test+" result is "+isBalancedBy("(){}<>",test));
		test = "hello";
		System.out.println(test+" result is "+isBalancedBy("(){}<>",test));
		test = "<<><<>>>>";
		System.out.println(test+" result is "+isBalancedBy("(){}<>",test));
		test = "((?)";
		System.out.println(test+" result is "+isBalancedBy("(){}<>",test));
		// and the one that was initially missing:
		test = "({)}";
		System.out.println(test+" result is "+isBalancedBy("{}()<>",test));
		System.out.println("test getpower");
		System.out.println("2^5= " + getPower(2,5));
		System.out.println("3^4= " + getPower(3,4));
		System.out.println("7^2= " + getPower(7,2));
	}
}
