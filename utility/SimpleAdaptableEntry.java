package utility;

public class SimpleAdaptableEntry<K,V> extends SimpleEntry<K,V> implements AdaptableEntry<K,V> {
	public K setKey(K key) {
		this.key = key;
		return key;
	}
}
