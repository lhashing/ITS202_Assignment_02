import java.util.*;

public class Solution<Key extends Comparable<Key>, Value> {
    private Node root;             // root of BST
    private Node left, right;

    private class Node {
        private Key key;           // sorted by key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree

        public Node(Key key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public Solution() {
        root = null;
        left = null;
        right = null;
    }

    /**
     * Returns true if this symbol table is empty.
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        if(size() == 0)
            return true;
        return false;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

    // return number of key-value pairs in BST rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        return 1 + size(x.left) + size(x.right);
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param  key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    // public boolean contains(Key key) {
       
    // }

    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */

    public Value get(Key key) {
        if(key == null) throw new IllegalArgumentException("argument to get value() is null");

        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.val;
        }
        return null;
    }

    // private Value get(Node x, Key key) {

    // }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */

    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        Node z = new Node(key, val, 1);
        if (root == null) {
            root = z;
            return;
        }

        Node parent = null, x = root;
        while (x != null) {
            parent = x;
            int cmp = key.compareTo(x.key);
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else {
                x.val = val;
                return; 
            }
        }
        int cmp = key.compareTo(parent.key);
        if (cmp < 0) parent.left  = z;
        else         parent.right = z;

    }

    // private Node put(Node x, Key key, Value val) {

    // }

    /**
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    }

    private Node min(Node x) { 
        if(x.left == null){
            return x; 
        }
        for(x=x;x!=null;x=x.left){
            if(x.left == null){
                return x;
            }
        }
        return x;
    }

    /**
     * Returns the largest key in the symbol table less than or equal to {@code key}.
     *
     * @param  key the key
     * @return the largest key in the symbol table less than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key floor(Key key) {
        if(isEmpty()){
            throw new NoSuchElementException("calls floor() with empty symbol table");
        }
        Node x = root;
        Node parent = null;
        while(x != null){
            parent = x;
            int cmp = key.compareTo(parent.key);
            if(key == null){
                throw new IllegalArgumentException("calls floor() with a null key");
            }
            if(cmp == 0){
                return parent.key;
            }
            if(cmp < 0){
                x = parent.left;
            }
            //This condition is for checking floor of given key in right side
            else if(cmp > 0){
                x = parent.right;
                //This condition returns previous parent as floor of given key as it is less the right parent
                int cm = key.compareTo(x.key);
                if(cm < 0 ){
                    return parent.key;
                }
                //This is for if the key is still greater then right parent
                else{
                    x = parent.right;
                } 
            }
        }
        return parent.key;
    } 

    // private Node floor(Node x, Key key) {
    //     return null;
       
    // } 

    /**
     * Return the key in the symbol table whose rank is {@code k}.
     * This is the (k+1)st smallest key in the symbol table.
     *
     * @param  k the order statistic
     * @return the key in the symbol table of rank {@code k}
     * @throws IllegalArgumentException unless {@code k} is between 0 and
     *        <em>n</em>–1
     */

    public Key select(int k) {
        if (k < 0 || k >= size()) throw new IllegalArgumentException("argument to select() is invalid: " + k);
        
        Node temp = select(root, k);
        if (temp != null) return temp.key;
        else              return null;
    }

    // Return key of rank k. 
    private Node select(Node x, int k) {
        while (x != null) {
            int t = size(x.left);

            if (t > k) x = x.left;
            else if (t < k) {
                x = x.right; 
                k= k - t -1;
            }
            else return x;
        }
        return null;
    }

    /**
     * Returns all keys in the symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return all keys in the symbol table between {@code lo} 
     *         (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *         is {@code null}
     */
    public Iterable<Key> keys() {
        Stack<Node> stack = new Stack<Node>();
        ArrayList<Key> queue = new ArrayList<Key>();
        Node x = root;
        while (x != null || !stack.isEmpty()) {
            if (x != null) {
                stack.push(x);
                x = x.left;
            }
            else {
                x = stack.pop();
                queue.add(x.key);
                x = x.right;
            }
        }
        return queue;  
    }

    /* Run the program by giving the approriate command obtained from
    input files through input.txt files. The output should be displayed
    exactly like the file output.txt shows it to be.*/
  
    public static void main(String[] args) {
        Solution<String, Integer> obj = new Solution<String, Integer>();
    }
}