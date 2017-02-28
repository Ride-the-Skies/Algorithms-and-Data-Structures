package priorityqueues;
import utility.Entry;

public interface PriorityQueue<K,V> {
	
	/** Pushes an element of type V into the queue with the associated priority of type K. */
	void push(K priority, V element);
	
	/** Retrieves the entry with the highest priority under the specified ordering, but does not remove it. */
	Entry<K,V> peek();
	
	/** Returns and removes the element with the highest priority under the specified ordering. */
	Entry<K,V> pop();
	
	/** Returns the number of entries stored in the queue. */
	int size();
	
	/** Returns true if there are no entries stored in the queue, or false otherwise. */
	boolean isEmpty();
}
