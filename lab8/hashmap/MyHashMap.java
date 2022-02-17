package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private static final int initialSize = 16;
    private static final double loadFactor = 0.75;
    private static final int Max = Integer.MAX_VALUE;
    private int size;
    private double maxload;
    // You should probably define some more!

    /**
     * Constructors
     */
    public MyHashMap() {
        buckets = createTable(initialSize);
        maxload = loadFactor;
        size = 0;
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        maxload = loadFactor;
        size = 0;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        this.maxload = maxLoad;
        size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // Your code won't compile until you do so!
    private int getIndex(K key, int N) {
        int hashCode = key.hashCode();
        if (hashCode < 0) {
            hashCode = hashCode + Max + 1;
        }
        int index = (hashCode % N);
        return index;
    }

    private Node getNode(Collection<Node> collection, K target) {
        for (Node node : collection) {
            if (node.key.equals(target)) {
                return node;
            }
        }
        return null;
    }

    private void resize(int Capacity) {
        Collection<Node>[] newBuckets = createTable(Capacity);
        for (int i = 0; i < buckets.length; i++) {
            Collection<Node> bucket = buckets[i];
            if (bucket != null) {
                int index = getIndex(bucket.iterator().next().key, Capacity);
                newBuckets[index] = bucket;
            }
        }
        buckets = newBuckets;
    }

    @Override
    public void clear() {
        buckets = createTable(initialSize);
        maxload = loadFactor;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int i = getIndex(key, buckets.length);
        if (buckets[i] != null && getNode(buckets[i], key) != null) {
            return true;
        }
        return false;
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        }
        int i = getIndex(key, buckets.length);
        Node target = getNode(buckets[i], key);
        return target.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int loadFactor = size / buckets.length;
        if (loadFactor >= maxload) {
            resize(buckets.length * 2);
        }
        Node newNode = new Node(key, value);
        int index = getIndex(key, buckets.length);
        if (!containsKey(key)) {
            if (buckets[index] == null) {
                buckets[index] = createBucket();
            }
            size++;
        }else{
            Node oldNode = getNode(buckets[index],key);
            buckets[index].remove(oldNode);
        }
        buckets[index].add(newNode);
    }

    @Override
    public Set<K> keySet() {
        if (size == 0) {
            return null;
        }
        Set<K> set = new HashSet<>();
        int N = buckets.length;
        for (int i = 0; i < N; i++) {
            if (buckets[i] != null) {
                for (Node node : buckets[i]) {
                    set.add(node.key);
                }
            }
        }
        return set;
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)){
            return null;
        }
        int index = getIndex(key, buckets.length);
        Node removed = getNode(buckets[index],key);
        buckets[index].remove(removed);
        return removed.value;
    }

    @Override
    public V remove(K key, V value) {
        if (!containsKey(key)){
            return null;
        }
        int index = getIndex(key, buckets.length);
        Node removed = getNode(buckets[index],key);
        if(removed.value.equals(value)){
            return removed.value;
        }else{
            return null;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator<>();
    }

    private class MyHashMapIterator<K> implements Iterator<K> {
        int i;

        public MyHashMapIterator() {
            i = 0;
            moveIndex();
        }

        @Override
        public boolean hasNext() {
            return i < buckets.length;
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            K key = (K) buckets[i].iterator().next();
            moveIndex();
            return key;
        }

        private void moveIndex() {
            while (buckets[i] == null || !buckets[i].iterator().hasNext()) {
                i++;
            }
        }

    }
}
