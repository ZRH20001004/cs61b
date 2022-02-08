package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T> {
    private Node sentinel;
    private int size;

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<T> {
        private Node current = sentinel.next;

        @Override
        public boolean hasNext() {
            return current == null;
        }

        @Override
        public T next() {
            T item = current.item;
            current = current.next;
            return item;
        }
    }

    public class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, sentinel, sentinel);
        size = 0;
    }

    public int size() {
        return size;
    }

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

    public void addLast(T item) {
        if (size() == 0) {
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

    public boolean isEmpty() {
        return size() == 0;
    }

    public void printDeque() {
        Node current = sentinel.next;
        while (current != sentinel) {
            T item = current.item;
            current = current.next;
            System.out.print(item + " ");
        }
        System.out.println();
    }

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
            return recursive(index--, current.next);
        }
    }


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LinkedListDeque)) {
            return false;
        }
        if (((LinkedListDeque<?>) o).size() != this.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (((LinkedListDeque<?>) o).get(i) != this.get(i)) {
                return false;
            }
        }
        return true;
    }

}

