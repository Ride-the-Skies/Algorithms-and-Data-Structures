package setsmaps;
import utility.Entry;

public interface Map<K,V> {
	
	/** Associates the given key with the given value in the map, and returns the newly
	 * inserted value. Changes the value associated with the given key if the key is
	 * already present.
	 */
	V put(K key, V value) throws NullPointerException;
	
	/** Puts all of the specified entries from the specified source to the current map. */
	void putAll(Map<? extends K, ? extends V> source);
	
	/** Returns the value associated with the given key if that key is present in the
	 * map. Otherwise, returns null. */
	V get(K key) throws NullPointerException;
	
	/** Returns and removes the value associated with the given key if that key is present
	 * in the map. Otherwise, leaves the map unaffected and returns null. */
	V remove(K key) throws NullPointerException;
	
	/** Returns true if the map contains an entry with the given key, or false if it doesn't. */
	boolean containsKey(K key) throws NullPointerException;
	
	/** Returns true if the map contains an entry with the given value, or false if it doesn't. */
	boolean containsValue(V value);
	
	/** Removes all entries from the map. */
	void clear();
	
	/** Returns the number of entries stored in the map. */
	int size();
	
	/** Returns true if the map is empty, or false if it isn't. */
	boolean isEmpty();
	
	/** Returns an iteration over the keys in the map. */
	Iterable<K> keys();
	
	/** Returns an iteration over the values in the map. */
	Iterable<V> values();
	
	/** Returns an iteration over the entries in the map. */
	Iterable<Entry<K,V>> entries();
	
	int DEFAULT_CAPACITY = 16;
}
