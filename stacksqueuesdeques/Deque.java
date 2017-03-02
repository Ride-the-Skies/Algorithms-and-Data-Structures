package stacksqueuesdeques;

/** An interface for an abstract data type allowing for insertion and removal to
 * and from both the front and the back of the underlying list.*/
public interface Deque<E> {

	/** Adds an element to the front of the deque and returns the 
	 * newly inserted element. */
	E prepend(E element) throws NullPointerException;
	
	/** Adds an element to the back of the deque and returns the
	 * newly inserted element. */
	E append(E element) throws NullPointerException;
	
	/** Returns and removes the element at the front of the deque.
	 * Throws an IllegalStateException if the deque is empty. */
	E popFront() throws IllegalStateException;
	
	/** Returns and removes the element at the back of the deque.
	 * Throws an IllegalStateException of the deque is empty. */
	E popBack() throws IllegalStateException;
	
	/** Returns but does not remove the element at the front of the deque,
	 * or null if the deque is empty. */
	E first();
	
	/** Returns but does not remove the element at the front of the deque,
	 * or null if the deque is empty. */
	E last();
	
	/** Returns an iteration of the elements from front to back. */
	Iterable<E> elements();
	
	/** Returns an iteration of the elements from back to front. */
	Iterable<E> elementsInReverse();
	
	/** Returns the number of elements in the deque. */
	int size();
	
	/** Returns true if the deque is empty, or false if it isn't. */
	boolean isEmpty();
}
