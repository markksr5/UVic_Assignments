/**
 * CSC115 Assignment 4 : Binary Trees
 * TreeNode.java
 * Created for use by CSC115 Fall 2018
 */

/**
 * TreeNode is a protected class, used exclusively by a BinaryTree object.
 * It is th node that registers an item's location in a tree.
 */

class TreeNode<E> {
	
	E item;
	TreeNode<E> parent;
	TreeNode<E> left;
	TreeNode<E> right;

	/**
	 * Creates a tree node.
	 * @param item The element contained within the tree.
	 * @param parent The reference to the node that is the parent of this node.
	 * 	Note that the root node of a tree has a null parent reference.
	 * @param left The reference to the node that is the left child of this node.
	 * 	Note that if there is no left child, this reference is null.
	 * @param right The reference to the node that is the right child of this node.
	 * 	Note that if there is no right child, this reference is null.
	 */
	TreeNode(E item, TreeNode<E> parent, TreeNode<E> left, TreeNode<E> right) {
		this.item = item;
		this.parent = parent;
		this.left = left;
		this.right = right;
	}

	/**
	 * Creates a tree node with a null or absent parent reference.
	 * @param item The element contained within the tree.
	 * @param left The reference to the node that is the left child of this node.
	 *	Note that if there is no left child, this reference is null.
	 * @param right The reference to the node tha tis the right child of this node.
	 *	Note that if there is no right child, this reference is null.
	 */
	TreeNode(E item, TreeNode<E> left, TreeNode<E> right) {
		this(item,null,left,right);
	}

	/**
	 * Creates a tree node with no parent and no left or right children.
	 * @param item The element contined within the tree.
	 */
	TreeNode(E item) {
		this(item,null,null);
	}
}
		
