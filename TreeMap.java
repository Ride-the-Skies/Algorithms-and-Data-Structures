package searchtrees;

/** A tree nap interface, based on that of Java SE 7. */
public interface TreeMap<K extends Comparable<K>, V> {
	/** Returns true if this map contains a mapping for the specified key. */
	boolean containsKey(K key);
	
	/** Returns true if this map maps one or more keys to the specified value. */
	boolean containsValue(V value);
	
	/** Returns the value associated with the given key if the map contains that key, or null if it doesn't. */
	V get(K key);
	
	/** Associates the given key with the given value in the map. */
	V put(K key, V value);
	
	/** Removes the mapping for a key from this map if it is present. */
	V remove(K key);
	
	/** Returns the cardinality of the map. */
	int size();
	
	/** Returns true if the set is empty, or false if it isn't. */
	boolean isEmpty();
	
	/** Returns an iterable collection of the keys in the map. */
	Iterable<K> keys();
	
	/** Returns an iterable collection of the values in the map. */
	Iterable<V> values();
	
	/** Returns an iterable collection of the entries in the map. */
	Iterable<Entry<K,V>> entries();
}