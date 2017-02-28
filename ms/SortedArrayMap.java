package ms;
import java.util.Comparator;
import utility.Entry;

@SuppressWarnings("unchecked")
public class SortedArrayMap<K,V> extends AbstractMap<K,V> {

	public SortedArrayMap() throws ClassCastException {
		this((a,b) -> ((Comparable<K>)a).compareTo(b), DEFAULT_CAPACITY);
	}
	
	public SortedArrayMap(Comparator<? super K> comparator) {
		this(comparator, DEFAULT_CAPACITY);
	}
	
	public SortedArrayMap(int initialCapacity) throws ClassCastException {
		this((a,b) -> ((Comparable<K>)a).compareTo(b), initialCapacity);
	}
	
	public SortedArrayMap(Comparator<? super K> comparator, int initialCapacity) {
		this.comparator = comparator;
		entries = (Entry<K,V>[]) new Entry[initialCapacity];
	}
	
	@Override
	public V put(K key, V value) throws NullPointerException {
		// TODO Auto-generated method stub
		return null;
	}

	public V get(K key) throws NullPointerException {
		if (key == null)
			throw new NullPointerException("Null keys not allowed.");
		else if (isEmpty())
			return null;
		else {
			Entry<K,V> search = entries[find(key)];
			return search.key().equals(key) ? search.value() : null;
		}
	}

	@Override
	public V remove(K key) throws NullPointerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean containsKey(K key) throws NullPointerException {
		if (key == null)
			throw new NullPointerException("Null keys not allowed.");
		else return isEmpty() ? false : entries[find(key)].key().equals(key);
	}

	public void clear() {
		entries = (Entry<K,V>[]) new Entry[DEFAULT_CAPACITY];
		size = 0;
	}

	public int size() {
		return size;
	}

	/** Returns the index at which the array stores the entry with the given key if it
	 * exists in the map. Otherwise, returns the index at which an entry with the given
	 * key should be inserted. */
	private int find(K key) {
		return find(key, 0, size - 1);
	}
	
	private int find(K key, int low, int high) {
		if (low > high)
			return high;
		int mid = (int) ((double) low / 2 + (double) high / 2);
		int comparison = comparator.compare(key, entries[mid].key());
		if (comparison < 0)
			return find(key, low, mid - 1);
		else if (comparison > 0)
			return find(key, mid + 1, high);
		else return mid;
	}
	
	@Override
	protected EntryIterator buildEntryIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Entry<K,V>[] entries;
	private Comparator<? super K> comparator;
	private int size;
}
