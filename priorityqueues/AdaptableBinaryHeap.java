package priorityqueues;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import lists.LinkedList;
import utility.*;

public class AdaptableBinaryHeap<K,V> extends BinaryHeap<K,V> implements AdaptablePriorityQueue<K,V> {
	
	private static class SimpleAdaptableEntry<K,V> implements Entry<K,V> {
		private SimpleAdaptableEntry(K key, V value, int index) {
			this.key = key;
			this.value = value;
			this.index = index;
		}
		
		public K key() {return key;}
		public V value() {return value;}		
		private int index() {return index;}
		public String toString() {return key.toString() + "->" + value.toString();}
		
		private void setIndex(int index) {
			this.index = index;
		}
		
		public V setValue(V value) {
			this.value = value;
			return value;
		}
		
		private K setKey(K key) {
			K old = this.key;
			this.key = key;
			return old;
		}
		
		private K key;
		private V value;
		private int index;
	}
	
	// CONSTRUCTORS
	@SuppressWarnings("unchecked")
	public AdaptableBinaryHeap() throws ClassCastException {
		this((a,b) -> ((Comparable<K>)a).compareTo(b), DEFAULT_CAPACITY);
	}
	
	@SuppressWarnings("unchecked")
	public AdaptableBinaryHeap(int initialCapacity) throws ClassCastException {
		this((a,b) -> ((Comparable<K>)a).compareTo(b), initialCapacity);
	}
	
	public AdaptableBinaryHeap(Comparator<? super K> comparator) {
		this(comparator, DEFAULT_CAPACITY);
	}
	
	@SuppressWarnings("unchecked")
	public AdaptableBinaryHeap(Comparator<? super K> comparator, int initialCapacity) {
		this.comparator = comparator;
		entries = (Entry<K,V>[]) new Entry[initialCapacity];
	}
	
	// METHODS VISIBLE TO OTHER CLASSES
	public V push(K priority, V element) {
		if (size() == entries.length)
			doubleCapacity();
		Entry<K,V> newest = new SimpleAdaptableEntry<>(priority, element, size);
		entries[size++] = newest;
		percolateUp(size - 1);
		return newest.value();
	}
	
	
	public Entry<K,V> peek() {
		return isEmpty() ? null : entries[0];
	}
	
	public Entry<K,V> pop() throws IllegalStateException {
		if (isEmpty())
			throw new IllegalStateException("Heap is empty.");
		Entry<K,V> popped = entries[0];
		swap(0, size - 1);
		entries[--size] = null;
		percolateDown(0);
		return popped;
	}

	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public Entry<K,V> changeKey(Entry<K,V> entry, K priority) throws IllegalArgumentException, ClassCastException {
		SimpleAdaptableEntry<K,V> adaptable = (SimpleAdaptableEntry<K,V>) entry;
		int index = adaptable.index();
		if (index > size() || entries[index] != entry)
			throw new IllegalArgumentException("Specified entry is not contained within this heap.");
		
		K old = adaptable.setKey(priority);
		int oldComparedToNew = comparator.compare(old, adaptable.key());
		if (oldComparedToNew < 0)
			percolateUp(adaptable.index());
		else if (oldComparedToNew > 0)
			percolateDown(adaptable.index());
		return adaptable;
	}
	
	public Entry<K,V> changeValue(Entry<K,V> entry, V value) throws IllegalArgumentException, ClassCastException {
		SimpleAdaptableEntry<K,V> adaptable = (SimpleAdaptableEntry<K,V>) entry;
		int index = adaptable.index();
		if (index > size() || entries[index] != entry)
			throw new IllegalArgumentException("Specified entry is not contained within this heap.");
		adaptable.setValue(value);
		return adaptable;
	}
	
	public Entry<K,V> remove(Entry<K, V> entry) throws IllegalArgumentException, ClassCastException {
		SimpleAdaptableEntry<K,V> adaptable = (SimpleAdaptableEntry<K,V>) entry;
		int index = adaptable.index();
		if (index > size() || entries[index] != entry)
			throw new IllegalArgumentException("Specified entry is not contained within this heap.");
		
		K old = adaptable.key();
		swap(index, --size);
		if (index != size)
			entries[size] = null;
		int oldComparedToNew = comparator.compare(old, entries[index].key());
		if (oldComparedToNew < 0)
			percolateUp(index);
		else if (oldComparedToNew > 0)
			percolateDown(index);
		return adaptable;
	}
	
	public Iterable<Entry<K,V>> entries() {
		return new Iterable<Entry<K,V>>() {
			public Iterator<Entry<K,V>> iterator() {
				return new EntryIterator();
			}
		};
	}
	
	public Iterable<K> keys() {
		return new Iterable<K>() {
			public Iterator<K> iterator() {
				return new KeyIterator();
			}
		};
	}
	
	public Iterable<V> values() {
		return new Iterable<V>() {
			public Iterator<V> iterator() {
				return new ValueIterator();
			}
		};
	}
	
	public void print() {
		if (!isEmpty())
			print(0, "");
		else System.out.println("");
	}
	
	// UTILITY METHODS AND CLASSES HIDDEN FROM OTHER CLASSES
	private void print(int index, String buffer) {
		if (hasRight(index))
			print(right(index), buffer + "----");
		System.out.println(buffer + entries[index].toString());
		if (hasLeft(index))
			print(left(index), buffer + "----");
	}
	
	private void percolateUp(int indexNewest) {
		for (int walk = indexNewest; hasParent(walk); walk = parent(walk))
			if (comparator.compare(entries[walk].key(), entries[parent(walk)].key()) > 0)
				swap(walk, parent(walk));
			else
				break;
	}
	
	private void percolateDown(int start) {
		for (int walk = start; hasLeft(walk);) {
			int parentComparedToLeft = comparator.compare(entries[walk].key(), entries[left(walk)].key());
			if (hasRight(walk)) {
				int parentComparedToRight = comparator.compare(entries[walk].key(), entries[right(walk)].key());
				if (parentComparedToLeft >= 0 && parentComparedToRight >= 0)
					return;
				else if (parentComparedToLeft < 0 && parentComparedToRight >= 0) {
					swap(walk, left(walk));
					walk = left(walk);
				}
				else if (parentComparedToLeft >= 0 && parentComparedToRight < 0) {
					swap(walk, right(walk));
					walk = right(walk);
				}
				else {
					int leftComparedToRight = comparator.compare(entries[left(walk)].key(), entries[right(walk)].key());
					if (leftComparedToRight < 0) {
						swap(walk, right(walk));
						walk = right(walk);
					}
					else {
						swap(walk, left(walk));
						walk = left(walk);
					}
				}
			}
			else {
				if (parentComparedToLeft < 0)
					swap(walk, left(walk));
				return;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void doubleCapacity() {
		Entry<K,V>[] temp = (Entry<K,V>[]) new Entry[entries.length * 2];
		for (int i = 0; i < entries.length; ++i)
			temp[i] = entries[i];
		entries = temp;
	}
	
	private void swap(int indexAlpha, int indexBeta) {
		Entry<K,V> temp = entries[indexAlpha];
		entries[indexAlpha] = entries[indexBeta];
		entries[indexBeta] = temp;
		((SimpleAdaptableEntry<K,V>)entries[indexBeta]).setIndex(indexBeta);
		((SimpleAdaptableEntry<K,V>)entries[indexAlpha]).setIndex(indexAlpha);
	}
	
	private static int left(int indexParent) {return 2 * indexParent + 1;}
	private static int right(int indexParent) {return 2 * indexParent + 2;}
	private static int parent(int indexChild) {return (indexChild - 1) / 2;}
	private boolean hasLeft(int indexParent) {return left(indexParent) < size();}
	private boolean hasRight(int indexParent) {return right(indexParent) < size();}
	private static boolean hasParent(int indexChild) {return parent(indexChild) >= 0;}
	
	@SuppressWarnings("unchecked")
	private class EntryIterator implements Iterator<Entry<K,V>> {
		public EntryIterator() {
			snapshot = (Entry<K,V>[]) new Entry[size];
			for (int i = 0; i < size(); ++i)
				snapshot[i] = entries[i];
		}
		
		public boolean hasNext() {return index < snapshot.length;}
		public Entry<K,V> next() {return snapshot[index++];}
		
		private Entry<K,V>[] snapshot;
		private int index;
	}
	
	private class KeyIterator implements Iterator<K> {
		public KeyIterator() {iterator = new EntryIterator();}
		public boolean hasNext() {return iterator.hasNext();}
		public K next() {return iterator.next().key();}
		private EntryIterator iterator;
	}
	
	private class ValueIterator implements Iterator<V> {
		public ValueIterator() {iterator = new EntryIterator();}
		public boolean hasNext() {return iterator.hasNext();}
		public V next() {return iterator.next().value();}
		private EntryIterator iterator;
	}
	
	private Entry<K,V>[] entries;
	private Comparator<? super K> comparator;
	private int size;
	
	public static void main(String[] args) {
		BinaryHeap<Integer, Integer> heap = new AdaptableBinaryHeap<>();
		for (int i = 1; i < 32; ++i) {
			int random = new Random().nextInt(100);
			heap.push(random, random);
		}
		heap.print();
		System.out.println();
		
		LinkedList<Integer> list = new LinkedList<>();
		
		while (!heap.isEmpty()) {
			list.prepend(heap.pop().key());
		}
		
		for (Integer i: list)
			System.out.println(i);
	}
}
