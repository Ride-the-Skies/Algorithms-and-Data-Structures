package priorityqueues;
import java.util.Comparator;
import utility.Entry;
import utility.SimpleEntry;

@SuppressWarnings("unchecked")
public class BinaryHeap<K,V> implements PriorityQueue<K,V> {

	public BinaryHeap() throws ClassCastException {
		this((a,b) -> ((Comparable<K>)a).compareTo(b), DEFAULT_CAPACITY);
	}
	
	public BinaryHeap(int initialCapacity) throws ClassCastException {
		this((a,b) -> ((Comparable<K>)a).compareTo(b), initialCapacity);
	}
	
	public BinaryHeap(Comparator<? super K> comparator) {
		this(comparator, DEFAULT_CAPACITY);
	}
	
	public BinaryHeap(Comparator<? super K> comparator, int initialCapacity) {
		this.comparator = comparator;
		entries = (Entry<K,V>[]) new Entry[initialCapacity];
	}
	
	
	public V push(K priority, V element) {
		if (size() == entries.length)
			doubleCapacity();
		Entry<K,V> newest = new SimpleEntry<>(priority, element);
		entries[size++] = newest;
		percolateUp(size - 1);
		return newest.value();
	}

	public Entry<K,V> peek() {
		return (isEmpty()) ? null : entries[0];
	}

	public Entry<K,V> pop() throws IllegalStateException {
		if (isEmpty())
			throw new IllegalStateException("Heap is empty.");
		Entry<K,V> popped = entries[0];
		entries[0] = entries[size - 1];
		entries[size - 1] = null;
		--size;
		percolateDown();
		return popped;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size() == 0;
	}
	
	private void percolateUp(int indexNewest) {
		for (int walk = indexNewest; walk != 0; walk = parent(walk))
			if (comparator.compare(entries[walk].key(), entries[parent(walk)].key()) > 0)
				swap(walk, parent(walk));
			else
				break;
	}
	
	private void percolateDown() {
		for (int walk = 0; hasLeft(walk);) {
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
	}

	private static int left(int indexParent) {return 2 * indexParent + 1;}
	private static int right(int indexParent) {return 2 * indexParent + 2;}
	private static int parent(int indexChild) {return (indexChild - 1) / 2;}
	private boolean hasLeft(int indexParent) {return left(indexParent) < size();}
	private boolean hasRight(int indexParent) {return right(indexParent) < size();}

	public void prettyPrint() {
		prettyPrint(0, "");
	}
	
	private void prettyPrint(int index, String buffer) {
		if (hasRight(index))
			prettyPrint(right(index), buffer + "---");
		System.out.println(buffer + entries[index].toString());
		if (hasLeft(index))
			prettyPrint(left(index), buffer + "---");
	}
	
	private Entry<K,V>[] entries;
	private Comparator<? super K> comparator;
	private int size;
	
	public static void main(String[] args) {
		BinaryHeap<Integer, Integer> heap = new BinaryHeap<>();
		for (int i = 0; i < 32; ++i)
			heap.push(i + 1, i + 1);
		heap.prettyPrint();
		System.out.println();
		
		while (!heap.isEmpty())
			System.out.println(heap.pop().toString());
	}
}
