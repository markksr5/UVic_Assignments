import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Iterator;

/**
 * CSC115 Assignment 4 : Binary Trees
 * Expression.java
 * Created for use by CSC115 Fall 2018.
 */

/**
 * Expression is a shortened version of the ArithExpression that was used
 * in Assignment 3.  It converts an infix expression into a postfix expression
 * by inserting the infix tokens into a BinaryTree and then getting its
 * postfix iterator.
 * It also draws the Expression tree.
 * This class does not supply the method to evaluate teh solution to the expression.
 */
public class Expression {

	private String[] infixTokens;
	private String[] postfixTokens;
	private BinaryTree<String> expressionTree;

	/**
	 * Sets up a legal standard Arithmetic expression.
 	 * The only parentheses accepted are "(" and ")".
	 * @param exp An arithmetic expression in standard infix order.
	 * @throws InvalidExpressionException if the expression cannt be converted to postfix.
	 */
	public Expression(String exp) {
		if (!Tools.isBalancedBy("()",exp)) {
			throw new InvalidExpressionException("Parentheses don't match");
		}
		tokenizeInfix(exp);
		expressionTree = populate(0,infixTokens.length); // puts the expression into a tree
		makePostfix();
	}

	/*
	 * A private helper method that tokenizes a string by separating out
	 * any arithmetic operators or parens from the rest of the string.
	 * It does no error checking.
	 * The method makes use of Java Pattern matching and Regular expressions to
	 * isolate the operators and parentheses.
	 * The operands are assumed to be the substrings delimited by the operators and parentheses.
	 * The result is initially captured in an oversized array, where each token is
	 * an operator, a paren or a operand.
	 * Once the actual length of the array is determined, it is compacted so every
	 * 	array index is occupied by a token.
	 * @param express The string that is assumed to be an arithmetic expression.
	 */
	private void tokenizeInfix(String express) {
		String[] tmpArray = new String[express.length()];
		int index = 0;

		// regular expression that looks for operators or parentheses.
		Pattern opParenPattern = Pattern.compile("[-+*/^()]");
		Matcher opMatcher = opParenPattern.matcher(express);

		String matchedBit, nonMatchedBit;
		int lastNonMatchIndex = 0;
		String lastMatch = "";

		while (opMatcher.find()) {
			matchedBit = opMatcher.group();
			// get the substring between the matches
			nonMatchedBit = express.substring(lastNonMatchIndex, opMatcher.start());
			nonMatchedBit = nonMatchedBit.trim(); //removes outside whitespace
			// The very first '-' or a '-' that follows another operator or '(' is considered a negative sign
			if (matchedBit.charAt(0) == '-') {
				if (opMatcher.start() == 0 ||
					!lastMatch.equals(")") && nonMatchedBit.equals("")) {
					continue;  // ignore this match
				}
			}
			// nonMatchedBit can be empty when an operator follows a ')'
			if (nonMatchedBit.length() != 0) {
				tmpArray[index++] = nonMatchedBit;
			}
			lastNonMatchIndex = opMatcher.end();
			tmpArray[index++] = matchedBit;
			lastMatch = matchedBit;
		}
		// parse the final substring after the last operator or paren:
		if (lastNonMatchIndex < express.length()) {
			nonMatchedBit = express.substring(lastNonMatchIndex,express.length());
			nonMatchedBit = nonMatchedBit.trim();
			tmpArray[index++] = nonMatchedBit;
		}
		// Now create the actual sized token array.
		infixTokens = new String[index];
		for (int i=0; i<index; i++) {
			infixTokens[i] = tmpArray[i];
		}
	}

	/**
	 * Draws the expression tree.
	 */
	public void drawTree() {
		DrawableBTree t = new DrawableBTree<String>(expressionTree);
		t.showFrame();
	}

	/**
	 * Helper method.
	 * Gets the postfix expression as tokens from the expression tree.
	 */
	private void makePostfix() {
		String[] tmpArray = new String[infixTokens.length];
		Iterator<String> it = expressionTree.postorderIterator();
		int count=0;
		while(it.hasNext()) {
			tmpArray[count++] = it.next();
		}
		postfixTokens = new String[count];
		for (int i=0; i<count; i++) {
			postfixTokens[i] = tmpArray[i];
		}
	}

	/**
	 * Prints the postfixTokens.
	 */
	public void printPostfix() {
		for (int i=0; i<postfixTokens.length; i++) {
			System.out.print(postfixTokens[i]+" ");
		}
		System.out.println();
	}

	/**
	 * Determines whether a single character string is an operator.
	 * The allowable operators are {+,-,*,/,^}.
	 * @param op The string in question.
	 * @return True if it is recognized as a an operator.
	 */
	public static boolean isOperator(String op) {
		switch(op) {
			case "+":
			case "-":
			case "/":
			case "*":
			case "^":
				return true;
			default:
				return false;
		}
	}

	/*
	 * A private helper method that checks a portion of the infix token list to
	 * determine whether there are a matching pair of parentheses at the beginning
	 * and end of the substring.
	 */
	private boolean hasEndParens(int start, int end) {
		if (!infixTokens[start].equals("(") || !infixTokens[end-1].equals(")")) {
			return false;
		}
		int parenBalance = 1;
		for (int i=start+1; i<end-1; i++) {
			if (infixTokens[i].equals("(")) {
				parenBalance++;
			} else if (infixTokens[i].equals(")")) {
				parenBalance--;
			}
			if (parenBalance == 0) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Helper method:
	 * Returns the precedence number of an operator.
	 */
	private int getPrecedence(char op) {
		switch(op) {
			case '^':
				return 3;
			case '*':
			case '/':
				return 2;
			case '+':
			case '-':
				return 1;
			default:
				return 0;
		}
	}

	/**
	 * Finds the index of the leftmost operator with the lowest precedence
	 * from within the subexpression starting from start and ending at end-1.
	 * If there is no such operator, then -1 is returned.
	 */
	private int findLowestOpIndex(int start,int end) {
		int insideParens = 0;
		int minPrecedence = 12; // any number bigger than 3
		int currPrecedence = 0;
		int lowestIndex = -1;
		String token;
		for (int i=start; i<end; i++) {
			if (infixTokens[i].equals("(")) {
				insideParens++;
			} else if (infixTokens[i].equals(")")) {
				insideParens--;
			} else if (isOperator(infixTokens[i]) && insideParens == 0) {
				token = infixTokens[i];
				currPrecedence = getPrecedence(token.charAt(0));
				if (currPrecedence <= minPrecedence) {
					minPrecedence = currPrecedence;
					lowestIndex = i;
				}
			}

		}
		return lowestIndex;
	}

	/**
	 * Recursive helper method
	 * Populates an expression subtree rooted at the left most least
	 *	precedenced operator in the sub-expression marked by the
	 *	the given indices of the infix token array.
	 * @param index of the first token of the subexpression.
	 * @param one past the index of the last token of the subexpression.
	 * @return A BinaryTree.
	 */
	private BinaryTree<String> populate(int start, int end) {
		// case 1: nothing in the subexpression
		if (start >= end) {
			throw new InvalidExpressionException("too many operators");
		}
		// case 2: remove outside parens and continue
		if (hasEndParens(start,end)) {
			start++;
			end--;
		}
		// case 3: the subexpression is a single number
		if (start+1 == end) {
			return new BinaryTree<String>(infixTokens[start]);
		}
		// case 4: find the lowest priority operator.
		int opIndex = findLowestOpIndex(start,end);
		if (opIndex == -1) {
			throw new InvalidExpressionException("not enough operators");
		}
		// recursive step:
		return new BinaryTree<String>(infixTokens[opIndex],populate(start,opIndex),populate(opIndex+1,end));
	}

	/**
	 * Test harness.
	 * Can be used to test a basic BinaryTree.
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		String word = null;
		Expression e = null;
		System.out.print("expression: ");
		word = console.nextLine();
		e = new Expression(word);
		e.drawTree();
		System.out.println("Postfix:");
		e.printPostfix();
	}
}



