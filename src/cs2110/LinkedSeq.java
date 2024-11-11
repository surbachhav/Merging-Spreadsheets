package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A list of elements of type `T` implemented as a singly linked list.  Null elements are not
 * allowed.
 */
public class LinkedSeq<T> implements Seq<T> {

    /**
     * Number of elements in the list.  Equal to the number of linked nodes reachable from `head`.
     */
    private int size;

    /**
     * First node of the linked list (null if list is empty).
     */
    private Node<T> head;

    /**
     * Last node of the linked list starting at `head` (null if list is empty).  Next node must be
     * null.
     */
    private Node<T> tail;

    /**
     * Assert that this object satisfies its class invariants.
     */
    private void assertInv() {
        assert size >= 0;
        if (size == 0) {
            assert head == null;
            assert tail == null;
        } else {
            assert head != null;
            assert tail != null;

            int count = 1;
            Node<T> n = head;
            while (n.next() != null){
                n = n.next();
                count++;
            }
            assert size == count;
            assert tail == n;
        }
    }

    /**
     * Create an empty list.
     */
    public LinkedSeq() {
        size = 0;
        head = null;
        tail = null;

        assertInv();
    }

    /**
     * Returns size of LinkedSeq.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Adds elem to the beginning of a LinkedSeq given that class invariants are satisfied.
     * @param elem
     */
    @Override
    public void prepend(T elem) {
        assertInv();
        assert elem != null;

        head = new Node<>(elem, head);
        // If list was empty, assign tail as well
        if (tail == null) {
            tail = head;
        }
        size += 1;

        assertInv();
    }

    /**
     * Return a text representation of this list with the following format: the string starts with
     * '[' and ends with ']'.  In between are the string representations of each element, in
     * sequence order, separated by ", ".
     * <p>
     * Example: a list containing 4 7 8 in that order would be represented by "[4, 7, 8]".
     * <p>
     * Example: a list containing two empty strings would be represented by "[, ]".
     * <p>
     * The string representations of elements may contain the characters '[', ',', and ']'; these
     * are not treated specially.
     */
    @Override
    public String toString() {
        String str = "[";

        Node<T> n = head;
        if (n != null){
            str += n.data();
            while (n.next() != null){
                str += ", ";
                n = n.next();
                str += n.data();
            }
        }

        str += "]";
        assertInv();
        return str;
    }

    /**
     * Checks if elem is present within LinkedSeq at least once.
     * @param elem
     * @return true if present; false if not present.
     */
    @Override
    public boolean contains(T elem) {
        assertInv();
        Node<T> n = head;
        if (n != null){
            if (n.data().equals(elem)) { return true; }
            while (n.next() != null){
                n = n.next();
                if (n.data().equals(elem)) { return true; }
            }
        }
        assertInv();
        return false;
    }

    /**
     * Extracts the data found at a specific index within a LinkedSeq.
     * @param index
     * @return the data found at index. If LinkedSeq is empty, return null.
     */
    @Override
    public T get(int index) {
        assertInv();
        Node<T> n = head;

        if (n == null){
            return null;
        }
        if (index == 0){
            return n.data();
        }
        else {
            int count = 0;
            while (count != index){
                n = n.next();
                count++;
            }
            return n.data();
        }
    }

    /**
     * Adds elem to the end of a LinkedSeq by storing it as data within
     * a node and adding this node to LinkedSeq. Increments the size of LinkedSeq
     * by 1. If LinkedSeq is empty, the node with elem becomes the head.
     * @param elem
     */
    @Override
    public void append(T elem) {
        assertInv();
        Node<T> last = new Node<>(elem, null);
        Node<T> n = head;

        if (n == null){
            head = last;
            tail = last;
        }
        else {
            while (n.next() != null){
                n = n.next();
            }
            n.setNext(last);
            tail = last;
        }
        size++;
        assertInv();
    }


    /**
     * Adds a node with data elem to a LinkedSeq before a given index. Assumes that
     * LinkedSeq is not empty because a node must contain successor data based on the program
     * arguments.
     * @param elem
     * @param successor
     */
    @Override
    public void insertBefore(T elem, T successor) {
        assertInv();
        Node<T> pred = head;
        Node<T> succ;
        Node<T> insert = new Node<>(elem, null);

        if (pred.data().equals(successor)){
            head = insert;
            head.setNext(pred);
        } else {
            while (!pred.next().data().equals(successor)) {
                pred = pred.next();
            }

            succ = pred.next();
            pred.setNext(insert);
            insert.setNext(succ);
        }

        size++;
        assertInv();
    }

    /**
     * Removes the node from LinkedSeq that contains
     * elem and returns true. If no node in LinkedSeq contains elem, return false.
     * If multiple nodes contain elem, remove first occurrence of the node that contains elem.
     * If LinkedSeq is empty, return false.
     * @param elem
     */
    @Override
    public boolean remove(T elem) {
        assertInv();
        Node<T> prev = head;

        if (prev == null){
            return false;
        }
        else if (prev.data().equals(elem)){
            head = null;
        }
        else {
            if (prev.next() == null) {
                return false;
            }
            while (!prev.next().data().equals(elem)) {
                if (prev.next() == null) {
                    return false;
                }
                prev = prev.next();
            }

            if (prev.next().next() != null) { prev.setNext(prev.next().next()); }
            else { prev.setNext(null); }
            prev.next().setNext(null);
        }

        size--;
        assertInv();
        return true;
    }

    /**
     * Return whether this and `other` are `LinkedSeq`s containing the same elements in the same
     * order.  Two elements `e1` and `e2` are "the same" if `e1.equals(e2)`.  Note that `LinkedSeq`
     * is mutable, so equivalence between two objects may change over time.  See `Object.equals()`
     * for additional guarantees.
     */
    @Override
    public boolean equals(Object other) {
        // Note: In the `instanceof` check, we write `LinkedSeq` instead of `LinkedSeq<T>` because
        // of a limitation inherent in Java generics: it is not possible to check at run-time
        // what the specific type `T` is.  So instead we check a weaker property, namely,
        // that `other` is some (unknown) instantiation of `LinkedSeq`.  As a result, the static
        // type returned by `currNodeOther.data()` is `Object`.
        assertInv();
        if (!(other instanceof LinkedSeq)) {
            return false;
        }
        LinkedSeq otherSeq = (LinkedSeq) other;
        Node<T> currNodeThis = head;
        Node currNodeOther = otherSeq.head;

        if (size != otherSeq.size()){
            return false;
        }
        else if (currNodeThis == null && currNodeOther == null){
            return true;
        }
        else {
            while (currNodeThis != null && currNodeOther != null){
                if (!currNodeThis.data().equals(currNodeOther.data())){
                    return false;
                }
                if (currNodeThis.next() != null && currNodeOther.next() != null){
                    currNodeThis = currNodeThis.next();
                    currNodeOther = currNodeOther.next();
                }
                else {
                    return true;
                }
            }

            assertInv();
            return true;
        }
    }

    /*
     * There is no need to read the remainder of this file for the purpose of completing the
     * assignment.  We have not yet covered the implementation of these concepts in class.
     */

    /**
     * Returns a hash code value for the object.  See `Object.hashCode()` for additional
     * guarantees.
     */
    @Override
    public int hashCode() {
        // Whenever overriding `equals()`, must also override `hashCode()` to be consistent.
        // This hash recipe is recommended in _Effective Java_ (Joshua Bloch, 2008).
        int hash = 1;
        for (T e : this) {
            hash = 31 * hash + e.hashCode();
        }
        return hash;
    }

    /**
     * Return an iterator over the elements of this list (in sequence order).  By implementing
     * `Iterable`, clients can use Java's "enhanced for-loops" to iterate over the elements of the
     * list.  Requires that the list not be mutated while the iterator is in use.
     */
    @Override
    public Iterator<T> iterator() {
        assertInv();

        // Return an instance of an anonymous inner class implementing the Iterator interface.
        // For convenience, this uses Java features that have not eyt been introduced in the course.
        return new Iterator<>() {
            private Node<T> next = head;

            public T next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T result = next.data();
                next = next.next();
                return result;
            }

            public boolean hasNext() {
                return next != null;
            }
        };
    }
}
