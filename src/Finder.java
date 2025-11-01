import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 *
 * Completed by: [YOUR NAME HERE]
 **/

public class Finder {
    public static final int R = 256;
    public static final long p = 54321102419L;
    private static final String INVALID = "INVALID KEY";
    private static final int SIZE = 1000000;

    private String[] keys;
    private String[] vals;

    public Finder() {
        String[] keys = new String[SIZE];
        String[] vals = new String[SIZE];
    }

    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        // TODO: Complete the buildTable() function!
        String curr;
        while ((curr = br.readLine()) != null) {
            String[] arr = curr.split(",");

            String key = arr[keyCol];
            String val = arr[valCol];

            int keyHash = (int)(hash(key, key.length()) % SIZE);
            keys[keyHash] = key;
            vals[keyHash] = val;
        }
        br.close();
    }

    public long hash(String str, int length) {
        long h = 0;
        // Multiply each number by all possible outcomes (256 for extended ASCII) and mod by large prime unique hashes
        for (int i = 0; i < length; i++) {
            h = (h * R + str.charAt(i)) % p;
        }
        // Return computed hash
        return h;
    }

    public String query(String key){
        // TODO: Complete the query() function!
        int keyHash = (int)(hash(key, key.length()) % SIZE);
        while (keys[keyHash] != null) {
            if (keys[keyHash].equals(key)) {
                return vals[keyHash];
            }
        }
        return INVALID;
    }
}