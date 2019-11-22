public class StringStack {

	public Node head;

	public boolean isEmpty() {
		if (head == null) return true;
		return false;
	}

	public String pop() {
		if (isEmpty()) throw new StackEmptyException("Stack is empty");
		Node temp = head;
		head = head.next;
		return temp.data;
	}

	public String peek() {
		if (isEmpty()) throw new StackEmptyException("Stack is empty");
		return head.data;
	}

	public void push(String item) {
		if (isEmpty()) {
			head = new Node(item);
		}
		else {
			Node temp = new Node(item, head);
			head = temp;
		}
	}

	public void popAll() {
		head = null;
	}
	
	public static void main (String[] args) {
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
			
		StringStack stack = new StringStack();
		System.out.println("empty: " + stack.isEmpty());
		for (int i = 0; i < test.length; i++) {
			stack.push(test[i]);
		}
		System.out.println("pushed all test items");
		System.out.println("empty: " + stack.isEmpty());
		System.out.println("popped: " + stack.pop());
		System.out.println("popped: " + stack.pop());
		stack.popAll();
		System.out.println("popped all");
		System.out.println("empty: " + stack.isEmpty());
	}
}
