package setsmaps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import lists.LinkedPositionalList;
import utility.Position;

@SuppressWarnings("unchecked")
public class ChainHashSet<E> extends AbstractSet<E> {
	public ChainHashSet() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}
	
	public ChainHashSet(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	public ChainHashSet(double loadFactor) {
		this(DEFAULT_CAPACITY, loadFactor);
	}
	
	public ChainHashSet(int initialCapacity, double loadFactor) {
		buckets = (LinkedPositionalList<E>[]) new LinkedPositionalList[initialCapacity];
		for (int i = 0; i < buckets.length; ++i)
			buckets[i] = new LinkedPositionalList<>();
		this.loadFactor = loadFactor;
	}
	
	public int size() {
		return size;
	}
	
	public boolean add(E element) throws NullPointerException {
		if (element == null)
			throw new NullPointerException("Null elements not allowed.");
		
		int h = hash(element);
		for (Position<E> p: buckets[h])
			if (p.data().equals(element))
				return false;
		
		if ((double) size / (double) buckets.length == loadFactor)
			doubleLength();
		
		buckets[h].append(element);
		++size;
		return true;
	}

	public void clear() {
		buckets = (LinkedPositionalList<E>[]) new LinkedPositionalList[DEFAULT_CAPACITY];
		for (int i = 0; i < buckets.length; ++i)
			buckets[i] = new LinkedPositionalList<>();
		size = 0;
	}

	public boolean contains(E element) {
		return find(element).data() != null;
	}

	public boolean remove(E element) throws NullPointerException {
		if (element == null)
			throw new NullPointerException("Null elements not allowed.");
		
		int h = hash(element);
		for (Position<E> p: buckets[h]) {
			if (p.data().equals(element)) {
				buckets[h].remove(p);
				--size;
				return true;
			}
		}
		return false;
	}

	public void retainAll(Collection<? extends E> collection) {
		Set<E> collectionSet = new ChainHashSet<>();
		for (E element: collection)
			collectionSet.add(element);
		
		for (int i = 0; i < buckets.length; ++i)
			for (Position<E> p: buckets[i])
				if (!collectionSet.contains(p.data()))
					buckets[i].remove(p);	
	}
	
	private int hash(E element) {
		return compress(element.hashCode() * 104_729);
	}
	
	private int compress(int hashcode) {
		return hashcode % buckets.length;
	}
	
	private Position<E> find(E element) {
		int b = hash(element);
		for (Position<E> p: buckets[b])
			if (p.data().equals(element))
				return p;
		return buckets[b].after(buckets[b].last());
	}

	public E[] toArray() {
		E[] array = (E[]) new Object[size];
		int i = 0;
		for (int j = 0; j < buckets.length; ++j)
			for (E element: buckets[j].elements())
				array[i++] = element;
		return array;
	}
	
	private void doubleLength() {
		LinkedPositionalList<E>[] temp = (LinkedPositionalList<E>[]) new LinkedPositionalList[buckets.length * 2];
		for (int i = 0; i < temp.length; ++i)
			temp[i] = new LinkedPositionalList<>();
		for (int i = 0; i < buckets.length; ++i)
			for (E e: buckets[i].elements())
				temp[e.hashCode() % temp.length].append(e);
		buckets = temp;
	}
	
	public Iterator<E> iterator() {
		return new HashSetIterator();
	}
	
	private class HashSetIterator implements Iterator<E> {
		public HashSetIterator() {
			elements = (E[]) new Object[size];
			for (int i = 0; i < buckets.length; ++i)
				for (E element: buckets[i].elements())
					elements[index++] = element;
			index = 0;
		}
		
		public boolean hasNext() {return index < elements.length;}
		public E next() {return elements[index++];}
		
		private E[] elements;
		private int index;
	}
	
	private LinkedPositionalList<E>[] buckets;
	private int size;
	private final double loadFactor;
	public static final double DEFAULT_LOAD_FACTOR = .75;
	
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
	
	public static void main(String[] args) {
		ChainHashSet<Integer> set = new ChainHashSet<>();
		
		int[] integers = {1, 2, 4, 10, 13, 18, 14, 16, 17, 5, 6, 9, 7};
		ArrayList<Integer> integersAsList = new ArrayList<>();
		for (int i: integers)
			integersAsList.add(i);
		set.addAll(integersAsList);
		
		set.print();
		
	}
}
