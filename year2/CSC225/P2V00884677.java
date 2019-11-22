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
    
    private static long recursiveSum(int n, int[] a, int[] b) {
        
        long sum = 0;
        
        if (n%2 == 1) {
            for (int i = 0; i < n; i++) {
                sum += a[i] * b[i];
            }
            return sum;
        }
        
        else {
            int[] a1 = new int[n/2];
            int[] a2 = new int[n/2];
            int[] b1 = new int[n/2];
            int[] b2 = new int[n/2];
            
            for (int i = 0; i < n/2; i++) {
                a1[i] = a[i];
                a2[i] = a[i+n/2];
                b1[i] = b[i];
                b2[i] = b[i+n/2];
            }
            long sum1 = recursiveSum(n/2, a1, a2);
            long sum2 = recursiveSum(n/2, b1, b2);
            long sum3 = recursiveSum(n/2, a1, b2);
            long sum4 = recursiveSum(n/2, a2, b1);
            return sum1 + sum2 + sum3 + sum4;
        }
        
    }
    
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        
        FastReader fr = new FastReader();
        int n = fr.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        
        for (int i = 0; i < n; i++) {
            a[i] = fr.nextInt();
        }
        for (int i = 0; i < n; i++) {
            b[i] = fr.nextInt();
        }
        
        long sum = recursiveSum(n,a,b);
        System.out.println(sum);
    }
}