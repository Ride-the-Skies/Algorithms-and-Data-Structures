package stacksqueuesdeques;

/** An interface for queues. All implementing classes are resizeable. */
public interface Queue<E> {

	/** Returns but does not remove the element in the front of the queue. 
	 * Returns null if the queue is empty. */
	E peek();
	
	/** Pushes the specified element to the back of the queue and returns
	 * that element. */
	E enqueue(E element);
	
	/** Returns and removes the least recently inserted element. 
	 * Throws an IllegalStateException if the queue is empty. */
	E dequeue() throws IllegalStateException;
	
	/** Returns the number of elements in the queue. */
	int size();
	
	/** Returns true if the queue is empty, or false if it isn't. */
	boolean isEmpty();
}
