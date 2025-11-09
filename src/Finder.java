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
    // Hashing constant
    public static final int R = 256;

    // Invalid constant
    private static final String INVALID = "INVALID KEY";

    // Sizes of table
    private static final int STARTING_SIZE = 1000;
    private int currentSize = STARTING_SIZE;

    // Max load factor of table (how full we will allow it to get)
    public static final double MAX_LOAD_FACTOR = 0.5; // Up to 50% full

    private int numElements = 0;

    // Arrays for keys and values
    private String[] keys;
    private String[] vals;

    // Initialize the arrays
    public Finder() {
        this.keys = new String[STARTING_SIZE];
        this.vals = new String[STARTING_SIZE];
    }

    // Builds our arrays keys and vals for the query function to use
    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        // Current piece of data
        String curr;

        // Get String of data from reader and check if it is not null without it going ahead after first check
        while ((curr = br.readLine()) != null) {
            // Split data into an array of Strings by comma
            String[] arr = curr.split(",");

            // Get only the necessary information
            String key = arr[keyCol];
            String val = arr[valCol];

            // If the load factor is above 50%
            if (shouldResize()) {
                // Dynamically resize our tables
                resizeTable();
            }

            // Insert the key val pair into the tables
            insertRecord(key, val);
        }
        // Close reader
        br.close();
    }

    // Returns if our inserted elements take up more than 50% of the tables
    private boolean shouldResize() {
        return numElements >= currentSize * MAX_LOAD_FACTOR;
    }

    // Resizes the table by creating larger arrays and filling our old info into the new arrays
    private void resizeTable() {
        // Double the table size
        currentSize *= 2;
        // Add one to make the size odd so that it is not a multiple of two in hash (will ignore some bits)
        currentSize += 1;

        // Create new arrays with the new size
        String[] newKey = new String[currentSize];
        String[] newVal = new String[currentSize];

        // Go through each key to transfer them over to the new arrays
        for (int i = 0; i < keys.length; i++) {
            // If we found a key
            if (keys[i] != null) {
                // Compute the new hash and employ linear probing if collision
                int newHash = findEmptySlot(newKey, keys[i]);
                // Fill in the new arrays
                newKey[newHash] = keys[i];
                newVal[newHash] = vals[i];
            }
        }
        // Have old arrays point to new arrays
        keys = newKey;
        vals = newVal;
    }

    // Inserts the given key value pair into the arrays
    private void insertRecord(String key, String val) {
        // Compute hashed index by employing linear probing if collision
        int index = findEmptySlot(keys, key);
        // Insert into our arrays
        keys[index] = key;
        vals[index] = val;
        // Add one to the number of elements stored
        numElements++;
    }

    // Finds empty slot in given array through linear probing if there is a collision
    private int findEmptySlot(String[] array, String key) {
        // Compute hashed index
        int index = (int)(hash(key, key.length()));

        // If we get a collision
        while (array[index] != null) {
            // Linear probing (move to next slot and recheck availability)
            index = (index + 1) % currentSize;
        }
        return index;
    }

    // Finds the given key by computing the hash and checking for a match with linear probing
    private int findKey(String key) {
        // Hash the key we were given
        int keyHash = (int)(hash(key, key.length()));

        // While we found something in the array
        while (keys[keyHash] != null) {
            // Check if we found a match
            if (keys[keyHash].equals(key)) {
                // Return associated value
                return keyHash;
            }
            // Move to next spot (in the case of a build-table collision)
            keyHash = (keyHash + 1) % currentSize;
        }
        // No key found
        return -1;
    }

    // Function to hash the key vals
    private long hash(String str, int length) {
        long h = 0;
        // Multiply each number by all possible outcomes (256 for extended ASCII) and mod by current table size
        for (int i = 0; i < length; i++) {
            h = (h * R + str.charAt(i)) % currentSize;
        }
        // Return computed hash
        return h;
    }

    // Function to search for data
    public String query(String key){
        // Get the index associated with the key
        int index = findKey(key);
        // If the key wasn't found return invalid
        if (index == -1) {
            return INVALID;
        }
        // Return the value associated with the key's index
        return vals[index];
    }
}