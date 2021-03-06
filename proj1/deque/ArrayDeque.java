package deque;


import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int first;
    private int last;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        first = 3;
        last = 4;
    }

    @Override
    public int size() {
        return size;
    }

    private void resize(int capacity) {
        T[] copy = (T[]) new Object[capacity];
        if (first < last) {
            System.arraycopy(items, (first + 1), copy, 0, size());
        } else {
            if (first == items.length - 1) {
                System.arraycopy(items, 0, copy, 0, last);
            } else {
                int firstSize = items.length - 1 - first;
                System.arraycopy(items, first + 1, copy, 0, firstSize);
                int lastSize = last;
                System.arraycopy(items, 0, copy, firstSize, last);
            }
        }
        items = copy;
        last = size();
        first = capacity - 1;

    }

    @Override
    public void addFirst(T item) {
        if (first == last) {
            resize(size() * 2);
        }
        items[first] = item;
        if (first == 0) {
            first = items.length - 1;
        } else {
            first--;
        }
        size++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (size() < items.length / 4) {
            resize(items.length / 2);
        }
        T removed;
        if (first == (items.length - 1)) {
            removed = items[0];
            items[0] = null;
            first = 0;
        } else {
            removed = items[first + 1];
            items[first + 1] = null;
            first++;
        }
        size--;
        return removed;
    }

    @Override
    public void addLast(T item) {
        if (first == last) {
            resize(size() * 2);
        }
        items[last] = item;
        if (last == (items.length - 1)) {
            last = 0;
        } else {
            last++;
        }
        size++;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (size() < (items.length / 4)) {
            resize(items.length / 2);
        }
        T removed;
        if (last == 0) {
            removed = items[items.length - 1];
            items[items.length - 1] = null;
            last = items.length - 1;
        } else {
            removed = items[last - 1];
            items[last - 1] = null;
            last--;
        }
        size--;
        return removed;
    }

    @Override
    public T get(int index) {
        if (index >= size()) {
            return null;
        }
        int current;
        if (first == (items.length - 1)) {
            current = 0;
        } else {
            current = first + 1;
        }
        while (index > 0) {
            if (current == (items.length - 1)) {
                current = 0;
            } else {
                current++;
            }
            index--;
        }
        return items[current];
    }

    @Override
    public void printDeque() {
        for (int i = (first + 1); i < last; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Deque<?>)) {
            return false;
        }
        Deque<?> ad = (Deque<?>) o;
        if (ad.size() != size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (!ad.get(i).equals(get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {
        int i = 0;

        @Override
        public boolean hasNext() {
            return i < size();
        }

        @Override
        public T next() {
            T item = get(i);
            i++;
            return item;
        }
    }
}
