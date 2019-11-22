public class Node {

	String data;
	Node next;
	
	public Node() {
		data = "";
		next = null;
	}
	
	public Node(String s) {
		data = s;
		next = null;
	}
	
	public Node(String s, Node n) {
		data = s;
		next = n;
	}
	
	public static void main (String[] args) {
		
	}

}