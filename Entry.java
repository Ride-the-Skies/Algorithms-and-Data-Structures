package searchtrees;

/** A simple interface for an entry within a tree map. */
public interface Entry<K, V> {
	/** Returns the key at this entry. */
	K getKey();
	/** Returns the value at this entry. */
	V getValue();
	/** Changes the value at this entry to that fed as an argument to this method. */
	void setValue(V value);
}
