package name.glonki.teamwork.util;

/**
 * Created by Glonki on 16.10.2017.
 */

public class NumberUtil {

    public static int[] parseNumbers(String numbers) {
        String[] ns = numbers.split(",");
        int l = ns.length;
        int[] result = new int[l];
        for(int i=0; i<l; i++) {
            result[i] = Integer.parseInt(ns[i]);
        }
        return result;
    }

}
