import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class ArithExpression {

	public TokenList postfixTokens;
	public TokenList infixTokens;

	
	public ArithExpression(String word) {
		if (Tools.isBalancedBy("()",word)) {
			tokenizeInfix(word);
			infixToPostfix();
		} else {
			throw new InvalidExpressionException("Parentheses unbalanced");
		}
	}

	private void tokenizeInfix(String express) {
		infixTokens  = new TokenList(express.length());

		// regular expression that looks for any operators or parentheses.
		Pattern opParenPattern = Pattern.compile("[-+*/^()]");
		Matcher opMatcher = opParenPattern.matcher(express);

		String matchedBit, nonMatchedBit;
		int lastNonMatchIndex = 0;
		String lastMatch = "";

		// find all occurrences of a matched substring
		while (opMatcher.find()) {
			matchedBit = opMatcher.group();
			// get the substring between matches
			nonMatchedBit = express.substring(lastNonMatchIndex, opMatcher.start());
			nonMatchedBit = nonMatchedBit.trim(); //removes outside whitespace
			// The very first '-' or a '-' that follows another operator is considered a negative sign
			if (matchedBit.charAt(0) == '-') {
				if (opMatcher.start() == 0 ||
					!lastMatch.equals(")") && nonMatchedBit.equals("")) {
					continue;  // ignore this match
				}
			}
			// nonMatchedBit can be empty when an operator follows a ')'
			if (nonMatchedBit.length() != 0) {
				infixTokens.append(nonMatchedBit);
			}
			lastNonMatchIndex = opMatcher.end();
			infixTokens.append(matchedBit);
			lastMatch = matchedBit;
		}
		// parse the final substring after the last operator or paren:
		if (lastNonMatchIndex < express.length()) {
			nonMatchedBit = express.substring(lastNonMatchIndex,express.length());
			nonMatchedBit = nonMatchedBit.trim();
			infixTokens.append(nonMatchedBit);
		}
	}

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
	
	
	public static boolean isInt(String token) {
		boolean isInteger = true;
		try {
			Integer.parseInt(token);
		}
		catch (NumberFormatException e) { //token is not an int
			isInteger = false;
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
				return 1; //+ and - lowest precedence
			case "*":
				return 2;
			case "/":
				return 2; //* and / higher
			case "^":
				return 3; //^ highest
			default:
				return -1; //-1 if not an operator, ie less than everything else
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

	public void infixToPostfix() {
		StringStack stack = new StringStack();
		postfixTokens = new TokenList();
		
		for (int i = 0; i < infixTokens.count; i++) {
			String token = infixTokens.list[i];
			
			if (isOperator(token)) { //token is operator
				if (stack.isEmpty() 
					|| precedence(token) > precedence(stack.peek()) ) {
					stack.push(token);
				}
				else { //output operators until found operator with lower precedence or stack empty
					while (!stack.isEmpty() && precedence(token) <= precedence(stack.peek())) {
						postfixTokens.append(stack.pop());
					}
					stack.push(token);
				}
			}
			
			else if (isLeftBracket(token)) { //token is left bracket
				stack.push(token);
			}
			
			else if (isRightBracket(token)) { //token is right bracket
				while (!isMatchingBracket(stack.peek(), token)) { //while peek is not matching left bracket
					postfixTokens.append(stack.pop());
				} //end: found matching left bracket
				stack.pop();
			}
			
			else { //token is int or invalid expression char, but still output
				//output to postfixTokens
				postfixTokens.append(token);
			}
		} //end for
		
		while (!stack.isEmpty()) {
			String top = stack.peek();
			if (isOperator(top))
				postfixTokens.append(stack.pop());
			else {
				stack.pop(); //catch any non-operators
			}
		}
		
	}	

	public String getInfixExpression() {
		return infixTokens.toString();
	}

	public String getPostfixExpression() {
		return postfixTokens.toString();
	}

	public double evaluate() {
		StringStack stack = new StringStack();
		double result = 0;
		String resultstring = "";
		
		for (int i = 0; i < postfixTokens.count; i++) {
			String token = postfixTokens.list[i];
			
			if (isInt(token)) { //token is int
				stack.push(token);
			}
			
			else { //token is operator
				String s1 = stack.pop();
				String s2 = stack.pop(); //get 2 integers from stack
				
				double op1 = getDouble(s1);
				double op2 = getDouble(s2); //parse string -> double
				
				switch(token) {
					case "+":
						result = op1 + op2;
						resultstring = Double.toString(result); //convert back to string
						stack.push(resultstring); //push to string stack
						break;
					case "-":
						result = op2 - op1;
						resultstring = Double.toString(result);
						stack.push(resultstring);
						break;
					case "*":
						result = op1 * op2;
						resultstring = Double.toString(result);
						stack.push(resultstring);
						break;
					case "/":
						result = op2 / op1;
						resultstring = Double.toString(result);
						stack.push(resultstring);
						break;
					case "^":
						result = getPower(op1, op2);
						resultstring = Double.toString(result);
						stack.push(resultstring);
						break;
					default:
						throw new InvalidExpressionException("not an operator");
				}
			}
			
		} //end for
		resultstring = stack.pop(); //last value = result of expression
		result = Double.parseDouble(resultstring); //parse to double
		return result;
	}

	public static void main(String[] args) {
		/*String a = "(";
		String b = ")";
		String c = "[";
		String d = "]";
		String e = "{";
		String f = "}";
		String g = "4";
		String h = "f";
		System.out.println(a + isRightBracket(a));
		System.out.println(b + isRightBracket(b));
		System.out.println(c + isRightBracket(c));
		System.out.println(d + isRightBracket(d));
		System.out.println(e + isRightBracket(e));
		System.out.println(f + isRightBracket(f));
		System.out.println(g + isRightBracket(g));
		System.out.println(h + isRightBracket(h));
		*/
		
		String [] test = {"2 + 5",
                                    "2 + 5 - 2",
                                    "2 + 5 * 2",
                                    "(2 + 5) * 2",
                                    "(2 + 5) * 2^2",
                                    "(2 + 50 / 5) * 2 ^2 + 20 / 4 + 2",
                                    "a",                                             // non numeric input
                                    "(2 5) + 12",                                // erroneous input
                                    "2 2",
                                    "+ +"

            };
		for (int i = 0; i < test.length; i++) {
			ArithExpression ae = new ArithExpression(test[i]);
			String s = ae.getInfixExpression() + " -> " + ae.getPostfixExpression();
			System.out.println(s);
			try {
				System.out.println("result: " + ae.evaluate() + "\n");
			}
			catch (StackEmptyException e) {
				System.out.println("invalid expression\n");
				continue;
			}
		}
		//ArithExpression ae = new ArithExpression(test[0]);
	}

}
