package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        c = comparator;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }
        T maxElement = get(0);
        for (int i = 1; i < size(); i++) {
            if (comparator.compare(maxElement, get(i)) < 0) {
                maxElement = get(i);
            }
        }
        return maxElement;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        T maxElement = get(0);
        for (int i = 1; i < size(); i++) {
            if (c.compare(maxElement, get(i)) < 0) {
                maxElement = get(i);
            }
        }
        return maxElement;
    }
}
