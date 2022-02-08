package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T> {
    private T[] items;
    private int size;
    private int first;
    private int last;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        first = 3;
        first = 4;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    private class ArrayIterator<T> implements Iterator<T> {
        int current = (first - 1);

        @Override
        public boolean hasNext() {
            return current == last;
        }

        @Override
        public T next() {
            T item = (T) items[current];
            if (current == (items.length - 1)) {
                current = 0;
            } else {
                current++;
            }
            return item;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void resize(int capacity) {
        T[] copy = (T[]) new Object[capacity];
        if (first < last) {
            System.arraycopy(items, (first + 1), copy, 0, size());
        } else if (first > last) {
            int copySize = items.length - first - 1;
            int start = copySize;
            System.arraycopy(items, (first + 1), copy, 0, copySize);
            System.arraycopy(items, 0, copy, start, last);
        }
        items = copy;
        first = size();
        last = capacity - 1;

    }

    public void addFirst(T item) {
        items[first] = item;
        size++;
        if (size() == items.length) {
            resize(size() * 2);
        }
        if (first == 0) {
            first = (items.length - 1);
        } else {
            first--;
        }
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (size() < items.length / 4) {
            resize(items.length / 4);
        }
        T removed = items[first + 1];
        items[first + 1] = null;
        first++;
        size--;
        return removed;
    }

    public void addLast(T item) {
        items[last] = item;
        size++;
        if (size() == items.length) {
            resize(size() * 2);
        }
        if (last == (items.length - 1)) {
            last = 0;
        } else {
            last++;
        }
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (size() < items.length / 4) {
            resize(items.length / 4);
        }
        T removed = items[last - 1];
        items[last - 1] = null;
        last--;
        size--;
        return removed;
    }

    public T get(int index) {
        if (index >= items.length) {
            return null;
        } else {
            return items[index];
        }
    }

    public void printDeque() {
        for (int i = (first + 1); i < last; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArrayDeque)) {
            return false;
        }
        if (((ArrayDeque<?>) o).size() != this.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (((ArrayDeque<?>) o).get(i) != this.get(i)) {
                return false;
            }
        }
        return true;
    }

}
