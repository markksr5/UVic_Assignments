import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*; 
import java.nio.file.Files; 
import java.nio.file.Paths;
import java.io.File;
import java.util.Collections;


public class RedBlackBST {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the BST
	public int redNodes;	   // number of red nodes


    // BST helper node data type
    private class Node {
        private int key;           // key
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(int key, boolean color, int size) {
            this.key = key;
            this.color = color;
            this.size = size;
        }
    }

	public RedBlackBST() {
		redNodes = 0;
	}
   /***************************************************************************
    *  Node helper methods.
    ***************************************************************************/
    // is node x red; false if x is null ?
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
		//parent link is red
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    } 


    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

   /**
     * Is this symbol table empty?
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

   /***************************************************************************
    *  Red-black tree insertion.
    ***************************************************************************/

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param key the key
     * @param val the value
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public void put(int key) {

        root = put(root, key);
        root.color = BLACK;
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node h, int key) { 
        if (h == null) {
			//redNodes++;
			return new Node(key, RED, 1);
		}

        int cmp = key - h.key;
        if      (cmp < 0) h.left  = put(h.left,  key); 
        else if (cmp > 0) h.right = put(h.right, key); 
        else              h.key   = key;

        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
        h.size = size(h.left) + size(h.right) + 1;

        return h;
    }


   /***************************************************************************
    *  Red-black tree helper functions.
    ***************************************************************************/

    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        // assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
		if (isRed(x.right) && !isRed(x)) {
			redNodes++;
		} /*else if (!isRed(x.right) && isRed(x)) {
			redNodes--;
		}*/
        x.color = x.right.color;
		if (!isRed(x.right)) {
			redNodes++;
		}
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        // assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
		if (isRed(x.left) && !isRed(x)) {
			redNodes++;
		} /*else if (!isRed(x.left) && isRed(x)) {
			redNodes--;
		}*/
        x.color = x.left.color;
		if (!isRed(x.left)) {
			redNodes++;
		}
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
		
		if (isRed(h)) {
			redNodes--;
		} else redNodes++;
        h.color = !h.color;
		if (isRed(h.left)) redNodes--;
			else redNodes++;
        h.left.color = !h.left.color;
		if (isRed(h.right)) redNodes--;
			else redNodes++;
        h.right.color = !h.right.color;
    }
	
	public double trivialPercentRed() {
		int n = this.size();
		if (root == null) return 0;
		//root must be black
		double count = 0.0;
		count = countRed(root.left, count);
		count = countRed(root.right, count);
		/*if (root.left != null)
			count = countRed(root.left, count);
		if (root.right != null)
			count = countRed(x.right, count);
		*/
		return (count / n);
	}
	
	private double countRed(Node x, double count) {
		if (x == null) return 0;
		if (this.isRed(x)) count++;
		if (x.left != null) 
			count = countRed(x.left, count);
		if (x.right != null) 
			count = countRed(x.right, count);
		return count;
	}
	
	// finds the percentage of red nodes in the red black tree
	public double percentRed() {
		// number of black nodes >= n/2
		// root to leaf path of at most log(n+1) black nodes (~height)
		return this.redNodes*100.0 / this.size();
		
		//find max, min height
		//a parent can't have only one child
		//if it has one child, then it is a red node (actually the same 3-node)
	}


    /**
     * Unit tests the {@code RedBlackBST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) { 
	
		//test10, 100, 1000
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
			//String filename = args[0];
			BufferedReader sc = new BufferedReader(new FileReader(args[0]));
			System.out.printf("Reading input values from %s\n", args[0]);
			String line;
			while ((line = sc.readLine()) != null) {
				//add int to array
				String[] stringArray = line.split("\\s+");
				for (int i = 0; i < stringArray.length; i++) {
					list.add(Integer.parseInt(stringArray[i]));
				}
				//list.add(Integer.parseInt(line));
			}
			//System.out.println(list);
			
			RedBlackBST tree = new RedBlackBST();
			
			for (int i = 0; i < list.size(); i++) {
				tree.put(list.get(i));
			}
			//System.out.printf("Size of tree is %d\n", tree.size());
			//System.out.println("number of red nodes: " + tree.redNodes);
			System.out.printf("Percent of Red Nodes: %.0f\n", tree.percentRed());
			return;
		}
		catch (Exception e) {
			System.out.println("No file found, generating test trees\n");
			//e.printStackTrace();
		}
		
		int NUM_TRIALS = 100;
		int n;
	
		//unit testing
		System.out.println("Unit test 1 (given):");
		RedBlackBST st = new RedBlackBST();
		st.put(0);
		st.put(1);
		st.put(2);
		st.put(3);
		st.put(4);
		st.put(5);
		System.out.printf("Size of tree is %d\n", st.size());
		System.out.println("Number of Red Nodes: " + st.redNodes);
		System.out.printf("Percentage of Red Nodes: %.0f\n", st.percentRed());
		System.out.printf("\n\n");
		
		//n = 10^4
		n = 10*10*10*10;
		System.out.printf("Number of nodes: %d\n", n);
		for (int i = 0; i < NUM_TRIALS; i++) {
			//run for 100 trials
			RedBlackBST tree = new RedBlackBST();
			ArrayList<Integer> numlist = new ArrayList<Integer>();
			for (int j = 0; j < n; j++) {
				//add all numbers to list
				numlist.add(j);
			}
			Collections.shuffle(numlist);
			for (int j = 0; j < n; j++) {
				tree.put(numlist.get(j));
			}
			System.out.printf("Trial number %d\n", i+1);
			System.out.printf("Percent of Red Nodes: %.0f\n", tree.percentRed());
		}
		
		System.out.println("\n***********************\n");
		
		
		//n = 10^5
		n = 10*10*10*10*10;
		System.out.printf("Number of nodes: %d\n", n);
		for (int i = 0; i < NUM_TRIALS; i++) {
			//run for 100 trials
			RedBlackBST tree = new RedBlackBST();
			ArrayList<Integer> numlist = new ArrayList<Integer>();
			for (int j = 0; j < n; j++) {
				//add all numbers to list
				numlist.add(j);
			}
			Collections.shuffle(numlist);
			for (int j = 0; j < n; j++) {
				tree.put(numlist.get(j));
			}
			System.out.printf("Trial number %d\n", i+1);
			System.out.printf("Percent of Red Nodes: %.0f\n", tree.percentRed());
		}
		
		
		System.out.println("***********************");
		
		
		//n = 10^6
		n = 10*10*10*10*10*10;
		System.out.printf("Number of nodes: %d\n", n);
		for (int i = 0; i < NUM_TRIALS; i++) {
			//run for 100 trials
			RedBlackBST tree = new RedBlackBST();
			ArrayList<Integer> numlist = new ArrayList<Integer>();
			for (int j = 0; j < n; j++) {
				//add all numbers to list
				numlist.add(j);
			}
			Collections.shuffle(numlist);
			for (int j = 0; j < n; j++) {
				tree.put(numlist.get(j));
			}
			System.out.printf("Trial number %d\n", i+1);
			System.out.printf("Percent of Red Nodes: %.0f\n", tree.percentRed());
		}
		
       }
        
 
}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
