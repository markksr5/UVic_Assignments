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
    
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        
        FastReader fr = new FastReader();
        
        int n = fr.nextInt(); //first number = n
        int limit = fr.nextInt(); //second number = limit
        int count = 0; //counter for size of social group

        int[] a = new int[n]; //array of social scores
        for (int i = 0; i < n; i++) {
            a[i] = fr.nextInt();
        }
        Arrays.sort(a);
        
        for (int i = 0; i < n; i++) {
            int j = i;
            int k = i+1;
            while(j >= 0) {
                if (a[i] - a[j] > limit) {
                    break;
                }
                count++;
                //System.out.println(a[i] + " " + a[j]);
                j--;
            }
            while(k < n) {
                if (a[k] - a[i] > limit) {
                    break;
                }
                count++;
                //System.out.println(a[i] + " " + a[k]);
                k++;
            }
        }
        System.out.println(count);
    }
}