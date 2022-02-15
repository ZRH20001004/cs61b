package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;
    private int size;

    private class BSTNode {
        K key;
        V value;
        BSTNode left;
        BSTNode right;

        BSTNode(K key, V value, BSTNode left, BSTNode right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    public BSTMap() {
        size = 0;
    }

    @Override
    /** Removes all of the mappings from this map. */
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        return containsKey(key, root);
    }

    private boolean containsKey(K key, BSTNode T) {
        if (T == null) {
            return false;
        }
        int cmp = key.compareTo(T.key);
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return containsKey(key, T.left);
        } else {
            return containsKey(key, T.right);
        }
    }

    @Override
    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        } else {
            return get(key, root);
        }
    }

    private V get(K key, BSTNode T) {
        int cmp = key.compareTo(T.key);
        if (cmp == 0) {
            return T.value;
        } else if (cmp < 0) {
            return get(key, T.left);
        } else {
            return get(key, T.right);
        }
    }

    @Override
    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    @Override
    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        if (containsKey(key)) {
            return;
        } else {
            root = put(key, value, root);
            size++;
        }
    }

    private BSTNode put(K key, V value, BSTNode T) {
        if (T == null) {
            return new BSTNode(key, value, null, null);
        }
        int cmp = key.compareTo(T.key);
        if (cmp < 0) {
            T.left = put(key, value, T.left);
        } else {
            T.right = put(key, value, T.right);
        }
        return T;
    }

    public void printInOrder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
