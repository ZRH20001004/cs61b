package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */

public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();
        correct.addLast(4);
        correct.addLast(5);
        correct.addLast(6);
        broken.addLast(4);
        broken.addLast(5);
        broken.addLast(6);
        assertEquals(correct.getLast(), broken.getLast());
        assertEquals(correct.getLast(), broken.getLast());
        assertEquals(correct.getLast(), broken.getLast());

    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                correct.addLast(randVal);
                broken.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
               if (correct.size() > 0){
                   assertEquals(correct.getLast(), broken.getLast());
               }
            }else{
                if (correct.size() > 0){
                    assertEquals(correct.removeLast(), broken.removeLast());
                }
            }
        }
    }
}
