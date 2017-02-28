package utility;

public class SimpleEntry<K,V> implements Entry<K,V> {
	public SimpleEntry() {};
	public SimpleEntry(K key, V value) {
		this.key = key; 
		this.value = value;
	}
	
	public K key() {return key;}
	public V value() {return value;}
	public V setValue(V value) {
		this.value = value; 
		return value;
	}
	
	public String toString() {
		return "[" + key.toString() + "->" + value.toString() + "]";
	}
	
	protected K key;
	protected V value;
}
