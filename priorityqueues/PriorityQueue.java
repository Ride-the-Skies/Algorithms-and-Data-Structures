package priorityqueues;
import utility.Entry;

/** An interface for PriorityQueues of key-value pairs. From classes implementing this interface, 
 * entries with keys with high priority will be popped before those with low priority. */
public interface PriorityQueue<K,V> {
	
	/** Pushes an element of type V into the queue with the associated priority of type K.
	 * Returns a reference to the value associated with that key. */
	V push(K priority, V element);
	
	/** Retrieves the entry with the highest priority under the specified ordering, but does not remove it. */
	Entry<K,V> peek();
	
	/** Returns and removes the element with the highest priority under the specified ordering. */
	Entry<K,V> pop() throws IllegalStateException;
	
	/** Returns the number of entries stored in the queue. */
	int size();
	
	/** Returns true if there are no entries stored in the queue, or false otherwise. */
	boolean isEmpty();
	
	int DEFAULT_CAPACITY = 16;
}
