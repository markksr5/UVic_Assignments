import java.io.*;
import java.util.*;

public class Solution {
    
    private static class Pair{
        int first;
        int second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    public static Pair[] inputGenerator(int n){ /* be careful to not modify this function */
        long last = 5000011;
        long mult1 = 5000087;
        long mult2 = 5000167;

        Pair[] arr = new Pair[n];
        for(int i = 0; i < n; i++){
            last = (last * mult1 + mult2)%n;
            int x = (int)last;
            last = (last * mult2 + i + mult1)%n;
            int y = (int)last;
            arr[i] = new Pair(x, y);
        }
        return arr;
    }
    
    static void usefulCountingSort(Pair[] A, int n, int x){
        ArrayList< ArrayList<Pair> > C = new ArrayList<>();
        
        for(int i = 0; i < n; i++)
            C.add(new ArrayList<>());
        
        for(int i = 0; i < n; i++){
            int key;
            if (x == 0)
                key = (A[i].second + i) % n;
            else
                key = (A[i].first + 2*i) % n;
            C.get(key).add(A[i]); //takes O(1) time
        }
        
        int index = 0;
        for(int i = 0; i < n; i++)
            for(int j = 0; j < C.get(i).size(); j++)
                A[index++] = C.get(i).get(j);
    }

    public static void main(String[] args) {
        
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int k = scan.nextInt();
        Pair[] inputArray = inputGenerator(n);
        
        usefulCountingSort(inputArray, n, 0);
        usefulCountingSort(inputArray, n, 1);
        
        for (int i = 0; i < inputArray.length; i++)
            if (i % k == 0)
                System.out.println("(" + inputArray[i].first + "," + inputArray[i].second + ")");
    }
}