import java.io.BufferedReader;
import java.io.IOException;

/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 *
 * Completed by: Lily Kassaei
 **/

public class Finder {
    // Hashing constants
    public static final int R = 256;
    public static final long p = 54321102419L;

    // Invalid constant
    private static final String INVALID = "INVALID KEY";

    // Max size of products
    private static final int SIZE = 10000000;

    // Arrays for keys and values
    private String[] keys;
    private String[] vals;

    // Initialize the arrays
    public Finder() {
        this.keys = new String[SIZE];
        this.vals = new String[SIZE];
    }

    // Tester calls buildTable
    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        // TODO: Complete the buildTable() function!
        // Current piece of data
        String curr;
        // Get String of data from reader and check if it is not null without it going ahead after first check
        while ((curr = br.readLine()) != null) {
            // Split data into an array of Strings by comma
            String[] arr = curr.split(",");

            // Get only the necessary information
            String key = arr[keyCol];
            String val = arr[valCol];

            // Hash the key so we can store it in our arrays
            int keyHash = (int)(hash(key, key.length()) % SIZE);

            // If we collide
            while (keys[keyHash] != null) {
                // Try the next slot
                keyHash = keyHash + 1;
            }
            // If we found an empty slot
            if (keys[keyHash] == null) {
                // Update keys and vals
                keys[keyHash] = key;
                vals[keyHash] = val;
            }
        }
        // Close reader
        br.close();
    }

    // Function to hash the key vals
    public long hash(String str, int length) {
        long h = 0;
        // Multiply each number by all possible outcomes (256 for extended ASCII) and mod by large prime unique hashes
        for (int i = 0; i < length; i++) {
            h = (h * R + str.charAt(i)) % p;
        }
        // Return computed hash
        return h;
    }

    // Function to search for data
    public String query(String key){
        // TODO: Complete the query() function!
        // Hash the key we were given
        int keyHash = (int)(hash(key, key.length()) % SIZE);

        // While we found something in the array
        while (keys[keyHash] != null) {
            // Check if we found a match
            if (keys[keyHash].equals(key)) {
                // Return associated value
                return vals[keyHash];
            }
            // Move to next spot (in the case of a build-table collision)
            keyHash = keyHash + 1;
        }
        // Nothing was found so return invalid
        return INVALID;
    }
}