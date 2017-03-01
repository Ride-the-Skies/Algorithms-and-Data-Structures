package setsmaps;
import java.util.Iterator;
import utility.Entry;

public abstract class AbstractMap<K,V> implements Map<K,V> {

	public void putAll(Map<? extends K, ? extends V> source) {
		for (Entry<? extends K, ? extends V> e: source.entries())
			put(e.key(), e.value());
	}
	
	public boolean containsValue(V value) {
		for (Entry<K,V> e: entries())
			if (value.equals(e.value()))
				return true;
		return false;
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public Iterable<K> keys() {
		return new Iterable<K>() {
			public Iterator<K> iterator() {
				return new KeyIterator();
			}
		};
	}

	private class KeyIterator implements Iterator<K> {
		public KeyIterator() {iterator = buildEntryIterator();}
		public boolean hasNext() {return iterator.hasNext();}
		public K next() {return iterator.next().key();}
		private EntryIterator iterator;
	}
	
	public Iterable<V> values() {
		return new Iterable<V>() {
			public Iterator<V> iterator() {
				return new ValueIterator();
			}
		};
	}

	private class ValueIterator implements Iterator<V> {
		public ValueIterator() {iterator = buildEntryIterator();}
		public boolean hasNext() {return iterator.hasNext();}
		public V next() {return iterator.next().value();}
		private EntryIterator iterator;
	}

	public Iterable<Entry<K,V>> entries() {
		return new Iterable<Entry<K,V>>() {
			public Iterator<Entry<K,V>> iterator() {
				return buildEntryIterator();
			}
		};
	}
	
	protected abstract class EntryIterator implements Iterator<Entry<K,V>> {}
	protected abstract EntryIterator buildEntryIterator();
}
