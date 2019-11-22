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

    public static void main(String[] args) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        FastReader fr = new FastReader();
        int n = fr.nextInt();
        int k = fr.nextInt();
        int[] grades = new int[n];
        
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        
        for (int i = 0; i < n; i++) {
            grades[i] = fr.nextInt();
        }
        
        TreeSet<Integer> tree = new TreeSet<Integer>();
        
        int x = 0;
        int y = 0;
        
        for (int i = 0; i < k; i++, x++) {
            tree.add(grades[x]);
        }
        
        while (x <= n) {
            int highest = tree.last();
            //System.out.println("highest: " + highest);
            int second = tree.lower(highest);
            //System.out.println("second: " + second);
            out.write(highest + second + " ");
            
            //remove first, add next
            if (x != n) {
                tree.remove(grades[y]);
                y++;
                tree.add(grades[x]);
                x++;
            }
            else {
                x++;
            }
        }
        out.flush();
        
    }
}