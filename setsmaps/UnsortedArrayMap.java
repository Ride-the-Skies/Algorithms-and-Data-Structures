package setsmaps;
import utility.*;

@SuppressWarnings("unchecked")
public class UnsortedArrayMap<K,V> extends AbstractMap<K,V> {

	public UnsortedArrayMap() {
		this(DEFAULT_CAPACITY);
	}
	
	public UnsortedArrayMap(int initialCapacity) {
		entries = (Entry<K,V>[]) new Entry[initialCapacity];
	}
	
	public V put(K key, V value) throws NullPointerException {
		if (key == null)
			throw new NullPointerException("Null keys not allowed.");
		
		Entry<K,V> search = find(key);
		if (search != null) {
			search.setValue(value);
			return search.value();
		}
		else {
			if (size == entries.length)
				doubleCapacity();
			entries[size] = new SimpleEntry<>(key, value);
			return entries[size++].value();
		}
	}

	public V get(K key) throws NullPointerException {
		if (key == null)
			throw new NullPointerException("Null keys not allowed.");
		
		Entry<K,V> search = find(key);
		return search == null ? null : search.value();
	}

	public V remove(K key) throws NullPointerException {
		if (key == null)
			throw new NullPointerException("Null Elements not allowed.");
		
		V removed = null;
		for (int i = 0; i < size; ++i) {
			if (entries[i].equals(key)) {
				removed = entries[i].value();
				entries[i] = null;
				for (int j = i; j < size - 1; ++j)
					entries[j] = entries[j+1];
			}
		}
		return removed;
	}

	public boolean containsKey(K key) throws NullPointerException {
		if (key == null)
			throw new NullPointerException("Null keys not allowed.");
		else return find(key) != null;
	}

	public void clear() {
		entries = (Entry<K,V>[]) new Entry[DEFAULT_CAPACITY];
		size = 0;
	}

	public int size() {
		return size;
	}

	private class UnsortedArrayMapIterator extends EntryIterator {
		public UnsortedArrayMapIterator() {
			snapshot = (Entry<K,V>[]) new Entry[size];
			for (int i = 0; i < size; ++i)
				snapshot[i] = entries[i];
		}
		
		public boolean hasNext() {return index < snapshot.length;}
		public Entry<K,V> next() {return snapshot[index++];}
		
		private Entry<K,V>[] snapshot;
		private int index;
	}
	
	
	protected EntryIterator buildEntryIterator() {
		return new UnsortedArrayMapIterator();
	}
	
	private Entry<K,V> find(K key) {
		for (int i = 0; i < size; ++i)
			if (entries[i].equals(key))
				return entries[i];
		return null;
	}

	private void doubleCapacity() {
		Entry<K,V>[] temp = (Entry<K,V>[]) new Entry[entries.length * 2];
		for (int i = 0; i < entries.length; ++i)
			temp[i] = entries[i];
		entries = temp;
	}
	
	private Entry<K,V>[] entries;
	private int size;
}
