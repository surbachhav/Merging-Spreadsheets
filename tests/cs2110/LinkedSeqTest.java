package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedSeqTest {

    // Helper functions for creating lists used by multiple tests.  By constructing strings with
    // `new`, more likely to catch inadvertent use of `==` instead of `.equals()`.

    /**
     * Creates [].
     */
    static Seq<String> makeList0() {
        return new LinkedSeq<>();
    }

    /**
     * Creates ["A"].  Only uses prepend.
     */
    static Seq<String> makeList1() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B"].  Only uses prepend.
     */
    static Seq<String> makeList2() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B", "C"].  Only uses prepend.
     */
    static Seq<String> makeList3() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("C"));
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates a list containing the same elements (in the same order) as array `elements`.  Only
     * uses prepend.
     */
    static <T> Seq<T> makeList(T[] elements) {
        Seq<T> ans = new LinkedSeq<>();
        for (int i = elements.length; i > 0; i--) {
            ans.prepend(elements[i - 1]);
        }
        return ans;
    }

    @DisplayName("WHEN a LinkedSeq is first constructed, THEN it should be empty.")
    @Test
    void testConstructorSize() {
        Seq<String> list = new LinkedSeq<>();
        assertEquals(0, list.size());
    }

    @DisplayName("GIVEN a LinkedSeq, WHEN an element is prepended, " +
            "THEN its size should increase by 1 each time.")
    @Test
    void testPrependSize() {
        // Note: List creation helper functions use prepend.
        Seq<String> list;

        // WHEN an element is prepended to an empty list
        list = makeList1();
        assertEquals(1, list.size());

        // WHEN an element is prepended to a list whose head and tail are the same
        list = makeList2();
        assertEquals(2, list.size());

        // WHEN an element is prepended to a list with no nodes between its head and tail
        list = makeList3();
        assertEquals(3, list.size());
    }

    @DisplayName("GIVEN a LinkedSeq containing a sequence of values, " +
            "THEN its string representation should include the string representations of its " +
            "values, in order, separated by a comma and space, all enclosed in square brackets.")
    @Test
    void testToString() {
        Seq<String> list;

        // WHEN empty
        list = makeList0();
        assertEquals("[]", list.toString());

        // WHEN head and tail are the same
        list = makeList1();
        assertEquals("[A]", list.toString());

        // WHEN there are no nodes between head and tail
        list = makeList2();
        assertEquals("[A, B]", list.toString());

        // WHEN there are at least 3 nodes
        list = makeList3();
        assertEquals("[A, B, C]", list.toString());

        // WHEN values are not strings
        Seq<Integer> intList = makeList(new Integer[]{1, 2, 3, 4});
        assertEquals("[1, 2, 3, 4]", intList.toString());
    }

    @DisplayName("GIVEN a LinkedSeq with elements, "
            + "if the LinkedSeq contains a given String, "
            + "return true. Otherwise, return false. ")
    @Test
    void testContains() {
        Seq<String> list;

        list = makeList1();
        assertFalse(list.contains("B"));
        assertTrue(list.contains("A"));

        //Checks to see if true when elements are repeated.
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend("A");
        ans.prepend("A");
        assertTrue(ans.contains("A"));
    }

    @DisplayName("WHEN a LinkedSeq is created with prexisting String elements found in it,"
            + "THEN extract the element at a specific index and return it. "
            + "Check if the String element in the list matches the expected "
            + "String value." )
    @Test
    void testGet() {
        Seq<String> list;

        list = makeList0();
        assertEquals(null, list.get(0));

        list = makeList3();
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @DisplayName("GIVEN a LinkedSeq, "
            + "WHEN appending a new String element into the LinkedSeq, "
            + "THEN the size should grow by 1 and the appended String should be found at "
            + "the last index of the LinkedSeq.")
    @Test
    void testAppend() {
        Seq<String> list;

        // "A" is added at index 0 of empty list.
        list = makeList0();
        list.append("A");
        assertEquals(1 , list.size());
        assertEquals("A", list.get(0));

        //  "B" is added at last index of list with 2 elements.
        list = makeList1();
        list.append("B");
        assertEquals(2 , list.size());
        assertEquals("B", list.get(1));

        //  "D" is added at last index of list with 3 elements.
        list = makeList3();
        list.append("D");
        assertEquals(4 , list.size());
        assertEquals("D", list.get(3));
    }

    @DisplayName("GIVEN a LinkedSeq, "
            + "WHEN inserting a given String element into the LinkedSeq before "
            + "another given String in the existing LinkedSeq, "
            + "return the new list format with the new String element added. Check if "
            + "the list size is properly incremented.")
    @Test
    void testInsertBefore() {
        Seq<String> list;

        //Insert at index 0.
        list = makeList1();
        list.insertBefore("*", "A");
        assertEquals("[*, A]", list.toString());
        assertEquals(2, list.size());

        //Insert at index 1.
        list = makeList2();
        list.insertBefore("*", "B");
        assertEquals("[A, *, B]", list.toString());
        assertEquals(3, list.size());

        //Insert at index 2.
        list = makeList3();
        list.insertBefore("*", "C");
        assertEquals("[A, B, *, C]", list.toString());
        assertEquals(4, list.size());
    }

    @DisplayName("GIVEN a LinkedSeq,"
            + "WHEN removing an element from the LinkedSeq, "
            + "if the element was found in LinkedSeq, remove it, decrement "
            + "LinkedSeq size and return true. Check to make sure the element was removed correctly."
            + "Otherwise, if the element is not found, return false.")
    @Test
    void testRemove() {
        Seq<String> list;

        // Checks to make sure that the method doesn't allow for the removal of a String when a LinkedSeq is empty.
        list = makeList0();
        assertFalse(list.remove("A"));

        // Checks to make sure that the method doesn't allow for the removal of a String that is not in the LinkedSeq.
        list = makeList1();
        assertFalse(list.remove("B"));

        // Checks to make sure that if an element is in the LinkedSeq, it is removed properly,
        // true is returned the list size is decremented.
        list = makeList3();
        assertTrue(list.remove("B"));
        assertEquals("[A, C]", list.toString());
        assertEquals(2, list.size());

        //If two Strings in the LinkedSeq are the same removing one of these 2 String elements
        // removes the first instance of the String element.
        list = makeList2();
        list.append("B");
        assertTrue(list.remove("B"));
        assertEquals("[A, B]", list.toString());
        assertEquals(2, list.size());
    }

    @DisplayName("GIVEN two LinkedSeq,"
            + "if the two LinkedSeq have the same size, same elements, and those elements"
            + "are in the same order, return true. "
            + "If they are both empty, return true. Otherwise, return false")
    @Test
    void testEquals() {
        Seq<String> listOne;
        Seq<String> listTwo;

        // Compare an empty LinkedSeq to a LinkedSeq with 1 element.
        listOne = makeList0();
        listTwo = makeList1();
        assertFalse(listOne.equals(listTwo));

        // Compare an empty LinkedSeq to another empty LinkedSeq.
        listTwo = makeList0();
        assertTrue(listOne.equals(listTwo));

        // Compare a LinkedSeq with 2 elements to a LinkedSeq
        // with 3 elements that has the first LinkedSeq as its prefix.
        listOne = makeList2();
        listTwo = makeList3();
        assertFalse(listOne.equals(listTwo));

        // Compare the equivalence of LinkedSeq with an appended element
        // to another LinkedSeq that has the same elements.
        listOne = makeList1();
        listOne.append("B");
        listTwo = makeList2();
        assertTrue(listOne.equals(listTwo));

        // Compare the equivalence of 2 LinkedSeq when they
        // have the same size but different elements.
        listOne = makeList2();
        String[] letters = {"A", "D"};
        listTwo = makeList(letters);
        assertFalse(listOne.equals(listTwo));
    }

        /*
     * There is no need to read the remainder of this file for the purpose of completing the
     * assignment.  We have not yet covered `hashCode()` or `assertThrows()` in class.
     */

    @DisplayName("GIVEN two distinct LinkedSeqs containing equivalent values in the same order, " +
            "THEN their hash codes should be the same.")
    @Test
    void testHashCode() {
        // WHEN empty
        assertEquals(makeList0().hashCode(), makeList0().hashCode());

        // WHEN head and tail are the same
        assertEquals(makeList1().hashCode(), makeList1().hashCode());

        // WHEN there are no nodes between head and tail
        assertEquals(makeList2().hashCode(), makeList2().hashCode());

        // WHEN there are at least 3 nodes
        assertEquals(makeList3().hashCode(), makeList3().hashCode());
    }

    @DisplayName("GIVEN a LinkedSeq, THEN its iterator should yield its values in order " +
            "AND it should stop yielding after the last value.")
    @Test
    void testIterator() {
        Seq<String> list;
        Iterator<String> it;

        // WHEN empty
        list = makeList0();
        it = list.iterator();
        assertFalse(it.hasNext());
        Iterator<String> itAlias = it;
        assertThrows(NoSuchElementException.class, () -> itAlias.next());

        // WHEN head and tail are the same
        list = makeList1();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertFalse(it.hasNext());

        // WHEN there are no nodes between head and tail
        list = makeList2();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertTrue(it.hasNext());
        assertEquals("B", it.next());
        assertFalse(it.hasNext());
    }
}
