import java.io.*;
import java.util.*;
import java.math.*;

public class Solution {
    
    public static int fourth;
    public static int cubed;
    public static int squared;
    public static int x;
    public static int const_count;
    public static BigInteger[] const_arr;
    
    public static BigInteger compareSides (BigInteger min, BigInteger right) {
        BigInteger two = new BigInteger("2");
        BigInteger mid = BigInteger.ZERO;
        BigInteger max = right;
        mid = mid.add(right);
        mid = mid.divide(two);
        
        BigInteger left = getLeft(const_arr, mid);
        
        while (left.compareTo(right) != 0) {    
            //System.out.println(left);
            if (left.compareTo(right) > 0) { //take lower half
                max = mid;
                mid = min;
                mid = mid.add(max);
                mid = mid.divide(two);
                //System.out.println("take lower");
                //System.out.println(min + " " + mid + " " + max);
            }
            else if (left.compareTo(right) < 0) { //take upper half
                min = mid;
                mid = mid.add(max);
                mid = mid.divide(two);
                //System.out.println("take upper");
                //System.out.println(min + " " + mid + " " + max);
            }
            else {
                break;
            }
            
            left = getLeft(const_arr, mid);
        }
        return mid;
    }
    
    public static BigInteger getLeft (BigInteger[] const_arr, BigInteger root) {
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < fourth; i++) {
            sum = sum.add(root.pow(4));
        }
        for (int i = 0; i < cubed; i++) {
            sum = sum.add(root.pow(3));
        }
        for (int i = 0; i < squared; i++) {
            sum = sum.add(root.pow(2));
        }
        for (int i = 0; i < x; i++) {
            sum = sum.add(root);
        }
        for (int i = 0; i < const_count; i++) {
            sum = sum.add(const_arr[i]);
        }
        return sum;
    }

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String[] arr = str.split("\\+|=");
        BigInteger left = new BigInteger("-1");
        const_arr = new BigInteger[10];
        const_count = 0;
        fourth = 0;
        cubed = 0;
        squared = 0;
        x = 0;
        BigInteger right = new BigInteger(arr[arr.length - 1]);
        //System.out.println(right);
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].contains("x^4")) {
                fourth++;
            }
            else if (arr[i].contains("x^3")) {
                cubed++;
            }
            else if (arr[i].contains("x^2")) {
                squared++;
            }
            else if (arr[i].contains("x")) {
                x++;
            }
            else {
                const_arr[const_count] = new BigInteger(arr[i]);
                const_count++;
                //System.out.println(const_arr[const_count]);
            }
            
        }//for
        BigInteger root = compareSides(BigInteger.ZERO, right);
        System.out.println(root);
    }
}