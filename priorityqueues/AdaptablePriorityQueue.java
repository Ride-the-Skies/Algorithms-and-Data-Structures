package priorityqueues;
import utility.Entry;

public interface AdaptablePriorityQueue<K,V> extends PriorityQueue<K,V> {

	/** Changes the priority of the given entry and returns it if it exists within the
	 * priority queue. Throws an IllegalArgumentException or a ClassCastException otherwise. */
	Entry<K,V> changeKey(Entry<K,V> entry, K priority) throws IllegalArgumentException, ClassCastException;

	/** Changes the value of the given entry and returns it if it exists within the
	 * priority queue. Throws an IllegalArgumentException or a ClassCastException otherwise. */
	Entry<K,V> changeValue(Entry<K,V> entry, V value) throws IllegalArgumentException, ClassCastException;
	
	/** Removes the entry from the heap and returns the removed entry. */
	Entry<K,V> remove(Entry<K,V> entry) throws IllegalArgumentException, ClassCastException;
}
