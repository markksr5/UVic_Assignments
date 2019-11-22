import java.io.*;
import java.util.*;

public class Solution {
    
    /*This class is taken from geeksforgeeks.org*/
    static class FastReader {

        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }
    
    static class Cell {
        
        //public char character;
        public int x;
        public int y;
        //public int count;
        public int d;
        //public boolean visited; //1 = visited
        //public Cell parent;
        
        public Cell (int a, int b, int f) {
            //character = c;
            x = a;
            y = b;
            //count = d;
            d = f;
            //visited = 0;
            //parent = null;
        }
    
    }

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        
        FastReader fr = new FastReader();
        int n = fr.nextInt();
        char[][] arr = new char[n][n];
        boolean[][] visited = new boolean[n][n];
        int[][] distance = new int[n][n];
        int count = 0;
        
        Cell S = new Cell(-1, -1, 0);
        //Cell K = new Cell('K', -1, -1, Integer.MAX_VALUE);
        
        for (int i = 0; i < n; i++) {
            String line = fr.next();
            for (int j = 0; j < n; j++) {
                arr[i][j] = line.charAt(j);
                //System.out.print(arr[i][j]);
                if (arr[i][j] == 'x') visited[i][j] = true;
                else visited[i][j] = false;
                distance[i][j] = Integer.MAX_VALUE; 
                if (arr[i][j] == 'S') {
                    S.x = i;
                    S.y = j;
                    visited[i][j] = true;
                    distance[i][j] = 0;
                }
            }
            //System.out.println();
        }
        
        List<Cell>[] adj; //adjacency list
        
        Queue<Cell> queue = new ArrayDeque<>();
        
        queue.add(S);
        
        int shortest = -1;
        while (!queue.isEmpty()) {
            Cell cur = queue.poll();
            
            if (arr[cur.x][cur.y] == 'K') 
                shortest = cur.d;
            
            if (cur.x - 1 >= 0) {
                if (visited[cur.x - 1][cur.y] == false) {
                    queue.add(new Cell(cur.x - 1, cur.y, cur.d + 1));
                    visited[cur.x - 1][cur.y] = true;
                }
            }
            
            if (cur.x + 1 < n) {
                if (visited[cur.x + 1][cur.y] == false) {
                    queue.add(new Cell(cur.x + 1, cur.y, cur.d + 1));
                    visited[cur.x + 1][cur.y] = true;
                }
            }
            
            if (cur.y - 1 >= 0) {
                if (visited[cur.x][cur.y - 1] == false) {
                    queue.add(new Cell(cur.x, cur.y - 1, cur.d + 1));
                    visited[cur.x][cur.y - 1] = true;
                }
            }
            
            if (cur.y + 1 < n) {
                if (visited[cur.x][cur.y + 1] == false) {
                    queue.add(new Cell(cur.x, cur.y + 1, cur.d + 1));
                    visited[cur.x][cur.y + 1] = true;
                }  
            }
        }
        
        if (shortest == -1) {
            System.out.println("IMPOSSIBLE");
        } else {
            System.out.println(shortest);
        }
        
    }
}