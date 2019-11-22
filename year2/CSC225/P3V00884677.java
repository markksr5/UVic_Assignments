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
    
    public static void insert(int item, PriorityQueue<Integer> max, PriorityQueue<Integer> min) {
        if (max.size() == 0 || max.peek() > item) {
            max.add(item);
        }
        else {
            min.add(item);
        }
    }
    
    public static void adjustQueues(PriorityQueue max, PriorityQueue min) {
        //always want max.size to be equal or bigger by 1
        if (min.size() > max.size()) {
            max.add(min.poll());
        }
        if (max.size() > min.size() + 1) {
            min.add(max.poll());
        }
    }
    
    public static int median(PriorityQueue max, PriorityQueue min) {
        return (int)max.peek();
    }
    
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should           be named Solution. */
        FastReader fr = new FastReader();
        int n = fr.nextInt();
        long result = 1;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = fr.nextInt();
        }
        //median will be root of max pq of lower numbers
        PriorityQueue<Integer> max = new PriorityQueue<Integer>(n, new Comparator<Integer>() {
            public int compare(Integer int1, Integer int2) {
                return -1 * int1.compareTo(int2);
            }
        });
        PriorityQueue<Integer> min = new PriorityQueue<Integer>(n);
        
        for (int i = 0; i < n; i++) {
            insert(arr[i], max, min);
            adjustQueues(max, min);
            result = (result*median(max, min)) % 1000000007;
            //System.out.println(median(max, min));
        }
        System.out.println(result);
    }
}