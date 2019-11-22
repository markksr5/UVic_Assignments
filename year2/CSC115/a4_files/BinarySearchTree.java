import java.util.Iterator;

/*
 * Mark Kaiser
 * V00884677
 * CSC 115 Assignment 4
 * BinarySearchTree.java
 */

/**
 * BinarySearchTree is an ordered binary tree, where the element in each node
 * comes after all the elements in the left subtree rooted at that node
 * and before all the elements in the right subtree rooted at that node.
 * For this assignment, we can assume that there are no elements that are
 * identical in this tree. 
 * A small modification will allow duplicates.
 */
public class BinarySearchTree<E extends Comparable<E>> extends BinaryTree<E> {

	// the root is inherited from BinaryTree.

	/**
	 * Create an empty BinarySearchTree.
	 */
	public BinarySearchTree() {
		super();
	}

	/**
	 * Creates a BinarySearchTree with a single item.
	 * @param item The single item in the tree.
	 */
	public BinarySearchTree(E item) {
		super(item);
	}

	/**
 	 * <b>This method is not allowed in a BinarySearchTree.</b>
	 * It's description from the subclass:<br>
	 * <br>
	 * {@inheritDoc}
	 * @throws UnsupportedOperationException if this method is invoked.
	 */
	public void attachLeftSubtree(BinaryTree<E> left) {
		throw new UnsupportedOperationException();
	}

	public void attachRightSubtree(BinaryTree<E> right) {
		throw new UnsupportedOperationException();
	}

	public void insert(E item) {
		root = insertnode(root, item);
	}
	
	public TreeNode<E> insertnode(TreeNode<E> node, E item) {
		TreeNode<E> newNode = new TreeNode<E>(item);
		
		if (node == null) {
			node = newNode;
		}
		else if (item.compareTo(node.item) == 0) {
			node = newNode;
		}
		else if (item.compareTo(node.item) < 0) { //item is less than node.left
			//recurse down left subtree
			node.left = insertnode(node.left, item);
		}
		else if (item.compareTo(node.item) > 0) {
			//recurse down right subtree
			node.right = insertnode(node.right, item);
		}
		
		return node;
	}
	
	public E retrieve(E item) {
		TreeNode<E> newNode = retrieveItem(root, item);
		if (newNode != null)
			return newNode.item;
		return null;
	}
	
	public TreeNode<E> retrieveItem(TreeNode<E> node, E item) {
		if (node == null)
			return node;
		//else recurse down tree
		if (item.compareTo(node.item) < 0) //item is less than node.item
			return retrieveItem(node.left, item); //recurse left subtree
		else if (item.compareTo(node.item) > 0) //item is more than node.item
			return retrieveItem(node.right, item); //recurse right subtree
		else //compare == 0; items same
			return node; //return found node
	}

	public E delete(E item) {
		TreeNode<E> newNode = deleteNode(root, item);
		return item;
	}
	
	public TreeNode<E> deleteNode(TreeNode<E> node, E item) {
		if (node == null)
			return node;
		//recurse down tree
		if (item.compareTo(node.item) < 0) 
			node.left = deleteNode(node.left, item);
		else if (item.compareTo(node.item) > 0) 
			node.right = deleteNode(node.right, item);
		else { //item == node.item; found node to be deleted
			if (node.left == null)
				return node.right; //could be null (if leaf node)
			else if (node.right == null)
				return node.left; //could be null (if leaf node)
			//otherwise, two children
			TreeNode<E> leftNode = node.left;
			while (leftNode.right != null)
				leftNode = leftNode.right; //iterate rightwards
			node.item = leftNode.item; //replace deleted node with rightmost node in left subtree
			node.left = deleteNode(node.left, node.item); //deletes the rightmost node in left subtree
		}
		return node;
	}

	/**
	 * Internal test harness.
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		BinarySearchTree<String> tree = new BinarySearchTree<String>();
		String s1 = new String("optimal");
		String s2 = new String("needs");
		String s3 = new String("programmers");
		String s4 = new String("CSC115");
		String s5 = new String("string5");
		String s6 = new String("string6");
		String s7 = new String("put");
		String s8 = new String("parallel");
		tree.insert(s1);
		//System.out.println("s1: " + tree.root.item);
		tree.insert(s2);
		tree.insert(s3);
		tree.insert(s4);
		tree.insert(s5);
		tree.insert(s6);
		tree.insert(s7);
		tree.insert(s8);
		String deletedItem = tree.delete(s6);
		System.out.println("deleted: " + deletedItem);
		String test = tree.retrieve(s6);
		/*if (test != null && !test.equals("")) {
			System.out.println("retrieving the node that contains "+s4);
			if (test.equals(s4)) {
				System.out.println("Confirmed");
			} else {
				System.out.println("retrieve returns the wrong item");
			}
		} else {
			System.out.println("retrieve returns nothing when it should not");
		}*/
		if (test == null || test == "") {
			System.out.println("confirmed: could not retrieve deleted string");
		}
		else {
			System.out.println("found a string");
		}
		System.out.println("Height: " + tree.height());
		Iterator<String> it = tree.inorderIterator();
		System.out.println("printing out the contents of the tree inorder:");
		while (it.hasNext()) {
			System.out.print(it.next()+" ");
		} 
		System.out.println();//inorder
		
		DrawableBTree<String> dbt = new DrawableBTree<String>(tree);
		dbt.showFrame();
		
		Iterator<String> it1 = tree.preorderIterator();
		System.out.println("printing out the contents of the tree in preorder:");
		while (it1.hasNext()) {
			System.out.print(it1.next()+" ");
		} 
		System.out.println();//preorder
		
		Iterator<String> it2 = tree.postorderIterator();
		System.out.println("printing out the contents of the tree in postorder:");
		while (it2.hasNext()) {
			System.out.print(it2.next()+" ");
		} 
		System.out.println();//postorder
		
		
	}
}

	

	
