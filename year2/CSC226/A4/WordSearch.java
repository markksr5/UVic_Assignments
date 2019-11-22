/* WordSearch.java
   CSC 226 - Summer 2019
   Assignment 4 - Word Search Problem Template
   
   You should be able to compile your program with the command
   
	javac WordSearch.java
	
   To conveniently test the algorithm, create a text file
   containing a word and puzzle (in the format described below) 
   and run the program with
   
	java WordSearch file.txt
	
   where file.txt is replaced by the name of the text file.
   
   Note, I didn't need any of the algs4.jar code. You may include it
   if you need to.
   
   The input consists of a graph (as an adjacency matrix) in the following format:
   
    <dimnsion of puzzle>
	<word string>
	<character grid row 1>
	...
	<character grid row n>

   R. Little - 07/2/2018
*/

import java.util.*;
import java.io.File;

//Do not change the name of the WordSearch class.
public class WordSearch {
	// List of global variables. You may to this list.
	public int startRow; // Starting row of word in puzzle
	public int startCol; // Starting column of word in puzzle
	public int endRow; 	 // Ending row of word in puzzle
	public int endCol;   // Ending column of word in puzzle
	public int[][] dfa;  //dfa array
	public ArrayList<Character> alphabet;
	public int M;
	public int R;
	int alphabetIndex;

	/* WordSearch(word)
		Use the WordSearch construcutor to do any preprocessing
		of the search word that may be needed. Examples, the dfa[][]
		for KMP, the right[] for Boyer-Moore, the patHash for Rabin-
		Karp.
	*/
    public WordSearch(char[] word) {
		
		/* ... Your code here ... */
		
		alphabet = new ArrayList<Character>();
		boolean repeated = false;
		//adds all chars in range to the alphabet
		
		for (int i = 0; i < word.length; i++) {
			repeated = false;
			for (int j = 0; j < alphabet.size(); j++) {
				if (word[i] == alphabet.get(j)) {
					repeated = true;
					continue;
				}
			}
			if (!repeated) {
				alphabet.add(word[i]);
			}
		}
		
		
		//construct the dfa
		M = word.length;
		R = alphabet.size();
		System.out.println(M + " " + R);
		System.out.println(word);
		dfa = new int[R][M];
		int wordIndex = 1;
		dfa[0][0] = 1; //can't use word[0] as an index, maybe have another char array to track where the chars are
		for (int X = 0, j = 1; j < M; j++) {
			for (int c = 0; c < R; c++) {
			   dfa[c][j] = dfa[c][X];
			}
			dfa[numAlph(word[j])][j] = j+1;
			X = dfa[numAlph(word[j])][X];
		}
		
    } 
	
	private int numAlph(char c) {
		return alphabet.indexOf(c);
	}

	
 
	/* search(puzzle)
		Once you have preprocessed the word you need to search the
		puzzle for the word. That happens here. You will assign the
		given global variables here once you find the word and return
		the boolean value "true". If you can't find the word, return 
		"false"
	*/
    public boolean search(char[][] puzzle) {
		
		/* ... Your code here ... */
		/*prints puzzle*/
		for (int i = 0; i < puzzle[0].length; i++) {
			for (int j = 0; j < puzzle[0].length; j++) {
				System.out.print(puzzle[i][j]);
			}
			System.out.println();
		}
		/*prints puzzle*/
		
		int i, row;
		int j = 0;
		int N = puzzle[0].length;
		for (row = 0; row < puzzle.length; row++) {
			for (i = 0, j = 0; i < N && j < M; i++) {
				if (numAlph(puzzle[row][i]) != -1) {
					j = dfa[numAlph(puzzle[row][i])][j]; //only works for 1D array need to change
					if (j == M) {
						startRow = row;
						endRow = row;
						startCol = i - M + 1;
						endCol = i;
						return true;
					}
				}
				else j = 0;
				System.out.println("row: " + row + ", i: " + i + ", j: " + j);
			}
		}
		System.out.println("j: " + j);
		if (j == M) return true/*i - M*/;
		else return false/*N*/; 
    }


	/* main()
	   Contains code to test the WordSearch program. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below. 
	*/
    public static void main(String[] args) {
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int n = s.nextInt(); 			// dimension of the puzzle
		String wordAsString = s.next();
		char[] word = wordAsString.toCharArray(); // search word
		char[][] puzzle = new char[n][n];		  // the puzzle
		
		for (int i = 0; i < n; i++){
			String line = s.next();
			for (int j = 0; j < n; j++)
				puzzle[i][j] = line.charAt(j);
		}
		
		WordSearch searcher = new WordSearch(word); // Preprocess word
        boolean result = searcher.search(puzzle);	// Search for word in puzzle
		
		
		// Output the word, the puzzle and the solution
		System.out.println("Word: " + wordAsString);
		System.out.printf("\nPuzzle:    \n");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				 System.out.print(puzzle[i][j]);
			System.out.println();
		}
		
		if (!result)
			System.out.printf("\nSolution: Search word not found");
		else {
			int x1 = searcher.startRow;
			int y1 = searcher.startCol;
			int x2 = searcher.endRow;
			int y2 = searcher.endCol;
		
			System.out.printf("\nSolution: Search word starts at coordinate ");
			System.out.print("("+x1+","+y1+")");
			System.out.print(" and ends at coordinate ");
			System.out.print("("+x2+","+y2+")");
		}
    }
	
}


