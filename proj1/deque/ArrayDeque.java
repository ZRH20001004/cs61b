package deque;


public class ArrayDeque<T> {
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
        } else {
            if (first == 0) {
                System.arraycopy(items, (first + 1), copy, 0, size());
            } else {
                int copySize = items.length - first - 1;
                int start = copySize;
                System.arraycopy(items, (first + 1), copy, 0, copySize);
                System.arraycopy(items, 0, copy, start, last);
            }
        }
        items = copy;
        last = size();
        first = capacity - 1;

    }

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

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (size() < items.length / 4) {
            resize(items.length / 4);
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

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (size() < items.length / 4) {
            resize(items.length / 4);
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
