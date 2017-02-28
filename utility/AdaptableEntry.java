package utility;

public interface AdaptableEntry<K,V> extends Entry<K,V> {
	K setKey(K key);
}
