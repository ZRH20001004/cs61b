package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private Node sentinel;
    private int size;

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<T> {
        private int i = 0;

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

    private class Node {
        private T item;
        private Node prev;
        private Node next;

        Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(T item) {
        if (isEmpty()) {
            Node newNode = new Node(item, sentinel, sentinel);
            sentinel.prev = newNode;
            sentinel.next = newNode;
        } else {
            Node newNode = new Node(item, sentinel, null);
            Node oldFirst = sentinel.next;
            newNode.next = oldFirst;
            oldFirst.prev = newNode;
            sentinel.next = newNode;
        }
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (isEmpty()) {
            Node newNode = new Node(item, sentinel, sentinel);
            sentinel.next = newNode;
            sentinel.prev = newNode;
        } else {
            Node newNode = new Node(item, sentinel.prev, sentinel);
            sentinel.prev.next = newNode;
            sentinel.prev = newNode;
        }
        size += 1;
    }


    @Override
    public void printDeque() {
        Node current = sentinel.next;
        while (current != sentinel) {
            T item = current.item;
            current = current.next;
            System.out.print(item + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            Node removed = sentinel.next;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size--;
            return removed.item;
        }
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            Node removed = sentinel.prev;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size--;
            return removed.item;
        }
    }

    @Override
    public T get(int index) {
        if (index >= size()) {
            return null;
        } else {
            Node current = sentinel.next;
            while (index > 0) {
                current = current.next;
                index--;
            }
            return current.item;
        }
    }

    public T getRecursive(int index) {
        if (index >= size()) {
            return null;
        } else {
            return recursive(index, sentinel.next);
        }
    }

    private T recursive(int index, Node current) {
        if (index == 0) {
            return current.item;
        } else {
            return recursive((index - 1), current.next);
        }
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
        Deque<?> ld = (Deque<?>) o;
        if (ld.size() != size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (!ld.get(i).equals(get(i))) {
                return false;
            }
        }
        return true;
    }

}

