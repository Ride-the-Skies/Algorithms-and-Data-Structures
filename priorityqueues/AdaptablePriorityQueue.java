package priorityqueues;
import utility.Entry;

public interface AdaptablePriorityQueue<K,V> extends PriorityQueue<K,V> {

	/** Changes the priority of given entry and returns it if it exists within the
	 * priority queue. Throws an IllegalArgumentException or a ClassCastException otherwise. */
	Entry<K,V> changePriority(Entry<K,V> entry, K priority) throws IllegalArgumentException, ClassCastException;
}
