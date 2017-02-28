package utility;

/** A trivial interface for a key-value pair. */
public interface Entry<K,V> {
	
	/** Returns the key associated with this entry. */
	K key();
	
	/** Returns the value associated with this entry. */
	V value();
	
	/** Sets the value associated with this entry to the parameterized value and returns it. */
	V setValue(V value);
}
