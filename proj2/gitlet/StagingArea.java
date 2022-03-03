package gitlet;


import java.io.Serializable;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class StagingArea implements Serializable {
    private final TreeMap<String, String> addition;
    private final Set<String> removal;

    public StagingArea() {
        addition = new TreeMap<>();
        removal = new TreeSet<>();
    }

    public void clear() {
        addition.clear();
        removal.clear();
    }

    public TreeMap<String, String> getAddition() {
        return addition;
    }

    public Set<String> getRemoval() {
        return removal;
    }

    public boolean isEmpty() {
        return addition.isEmpty() && removal.isEmpty();
    }
}
