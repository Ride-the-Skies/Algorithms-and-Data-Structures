package ms;
import lists.LinkedPositionalList;
import utility.*;

@SuppressWarnings("unchecked")
public class ChainHashMap<K,V> extends AbstractMap<K,V> {
	public ChainHashMap() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}
	
	public ChainHashMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	public ChainHashMap(double loadFactor) {
		this(DEFAULT_CAPACITY, loadFactor);
	}
	
	public ChainHashMap(int initialCapacity, double loadFactor) {
		buckets = (LinkedPositionalList<Entry<K,V>>[]) new LinkedPositionalList[initialCapacity];
		for (int i = 0; i < buckets.length; ++i)
			buckets[i] = new LinkedPositionalList<>();
		this.loadFactor = loadFactor;
	}
	
	public V put(K key, V value) {
		if (key == null || value == null)
			throw new NullPointerException("Neither keys nor values may be null.");
		
		int h = hash(key);
		for (Entry<K,V> e: buckets[h].elements())
			if (e.key().equals(key)) {
				e.setValue(value);
				return value;
			}
		if ((double)(size + 1) / (double) buckets.length >= loadFactor) {
			doubleLength();
			h = hash(key);
		}
		++size;
		buckets[h].append(new SimpleEntry<>(key, value));
		return buckets[h].last().data().value();
	}

	public void putAll(Map<? extends K, ? extends V> source) {
		for (Entry<? extends K, ? extends V> e: source.entries())
			put(e.key(), e.value());
	}
	
	public V get(K key) {
		if (key == null)
			throw new NullPointerException("Null keys not allowed in the map.");
		Position<Entry<K,V>> p = find(key);
		return p == null ? null : p.data().value();
	}

	public V remove(K key) {
		int h = hash(key);
		for (Position<Entry<K,V>> p: buckets[h])
			if (p.data().key().equals(key)) {
				--size;
				Entry<K,V> removed = buckets[h].remove(p);
				return removed.value();
			}
		return null;
	}

	public boolean containsKey(K key) {
		return find(key) != null;
	}
	
	public boolean containsValue(V value) {
		if (value == null)
			throw new NullPointerException("Null values not allowed.");
		for (int i = 0; i < buckets.length; ++i)
			for (Entry<K,V> e: buckets[i].elements())
				if (e.value().equals(value))
					return true;
		return false;
	}
	
	public void clear() {
		buckets = (LinkedPositionalList<Entry<K,V>>[]) new LinkedPositionalList[DEFAULT_CAPACITY];
		for (int i = 0; i < buckets.length; ++i)
			buckets[i] = new LinkedPositionalList<>();
		size = 0;
	}
	
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size() == 0;
	}
	
	private int hash(K key) {
		return compress(key.hashCode());
	}
	
	private int compress(int hashcode) {
		return hashcode % buckets.length;
	}
	
	/** Returns the position in which the entry with the given key is located,
	 * or null no entry with that key is in the map. */
	private Position<Entry<K,V>> find(K key) {
		int h = hash(key);
		for (Position<Entry<K,V>> p: buckets[h])
			if (p.data().key().equals(key))
				return p;
		return null;
	}
	
	/** Doubles the length of the unerlying array of linked lists. */
	private void doubleLength() {
		LinkedPositionalList<Entry<K,V>>[] temp = (LinkedPositionalList<Entry<K,V>>[]) new LinkedPositionalList[buckets.length * 2];
		for (int i = 0; i < temp.length; ++i)
			temp[i] = new LinkedPositionalList<>();
		for (int i = 0; i < buckets.length; ++i)
			for (Entry<K,V> e: buckets[i].elements()) {
				int newBucket = e.key().hashCode() % temp.length;
				temp[newBucket].append(e);
			}
		buckets = temp;
	}

	private class ChainHashMapEntryIterator extends EntryIterator {
		public ChainHashMapEntryIterator() {
			snapshot = (Entry<K,V>[]) new Entry[size];
			for (int i = 0; i < buckets.length; ++i)
				for (Position<Entry<K,V>> p: buckets[i])
					snapshot[index++] = p.data();
			index = 0;
		}
		
		public boolean hasNext() {return index < snapshot.length;}
		public Entry<K,V> next() {return snapshot[index++];}
		
		private Entry<K,V>[] snapshot;
		private int index;
	}
	

	protected EntryIterator buildEntryIterator() {
		return new ChainHashMapEntryIterator();
	}
	
	private void print() {
		for (int i=0; i<buckets.length; ++i)
			if (buckets.length < 11)
				System.out.format("%d: %s%n", i, buckets[i].toString());
			else if (buckets.length < 101)
				System.out.format("%2d: %s%n", i, buckets[i].toString());
			else if (buckets.length < 1000)
				System.out.format("%3d: %s%n", i, buckets[i].toString());
			else
				System.out.format("%d: %s%n", i, buckets[i].toString());
	}
	
	private LinkedPositionalList<Entry<K,V>>[] buckets;
	private int size;
	private final double loadFactor;
	public static final int DEFAULT_CAPACITY = 16;
	public static final double DEFAULT_LOAD_FACTOR = .75;
	
	public static void main(String[] args) {
		ChainHashMap<Integer,Integer> map = new ChainHashMap<>();
		
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(6, 6);
		map.put(9, 9);
		map.put(0, 0);
		map.put(11, 11);
		map.put(16, 16);
		map.put(4, 4);
		map.put(5, 5);
		map.put(7, 7);
		
		map.print();
		System.out.println();
		
		for (Integer i: map.values())
			System.out.println(i.toString());
	}
}
