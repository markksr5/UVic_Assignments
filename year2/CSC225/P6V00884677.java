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
        int n = fr.nextInt();
        HashMap<String, Integer> table = new HashMap<>(n);
        
        for (int i = 0; i < n; i++) {
            String key = fr.next();
            if (table.containsKey(key)) {
                //key.value++
                int count = table.get(key);
                table.replace(key, ++count);
            }
            else {
                table.put(key, 1);
            }
        }
        
        //print out key with highest value
        String[] keys = table.keySet().toArray(new String[table.keySet().size()]);
        int max = -1;
        String maxkey = "";
        int[] max_index = new int[table.keySet().size()];
        max_index[0] = -1;
        int j = 1;
        for (int i = 0; i < keys.length; i++) {
            if (table.get(keys[i]) > max) {
                max = table.get(keys[i]);
                maxkey = keys[i];
                for (int k = 1; k < max_index.length; k++) {
                    max_index[k] = 0;
                }
                max_index[0] = i;
                j = 1;
            }
            else if (table.get(keys[i]) == max) {
                //System.out.println("z: " + keys[i]);
                //System.out.println("maxkey: " + maxkey);
                if (keys[i].compareTo(maxkey) < 0) {
                    //System.out.println("y");
                    maxkey = keys[i];
                }
                
                max_index[j] = i;
                j++;
            }
        }
        /*for (int i = 0; max_index[i] != 0; i++) {
            System.out.println(keys[max_index[i]]);
        }
        //find minimum word length of keys[] using max_index[]
        String min = keys[max_index[0]];
        System.out.println("original min: " + min);
        if (max_index.length >= 1) {
            System.out.println("x");
            for (int i = 1; max_index[i] != 0; i++) {
                System.out.println("i: " + i);
                System.out.println(keys[max_index[i]] + max_index[i]);
                if (keys[max_index[i]].compareTo(min) < 0) {
                    System.out.println("new min: " + min);
                    min = keys[max_index[i]];
                }
            }
        }*/
        
        System.out.println(maxkey + " " + max);
        
        
        //System.out.println(table.keySet());
        //System.out.println(table.values());
        
    }
}