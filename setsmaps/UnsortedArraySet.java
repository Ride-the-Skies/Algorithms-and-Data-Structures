package setsmaps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

@SuppressWarnings("unchecked")
public class UnsortedArraySet<E> extends AbstractSet<E> {
	
	public UnsortedArraySet() {
		this(DEFAULT_CAPACITY);
	}
	
	public UnsortedArraySet(int capacity) {
		elements = (E[]) new Object[capacity];
	}
	
	public int size() {
		return size;
	}
	
	public boolean add(E element) throws NullPointerException {
		if (element == null)
			throw new NullPointerException("Null elements not allowed.");
		
		if (contains(element))
			return false;
		if (size == elements.length)
			doubleCapacity();
		elements[size++] = element;
		return true;
	}
	
	public void clear() {
		elements = (E[]) new Object[DEFAULT_CAPACITY];
		size = 0;
	}
	
	public boolean contains(E element) throws NullPointerException {
		if (element == null)
			throw new NullPointerException("Null elements not allowed.");
		
		for (int i = 0; i < size; ++i)
			if (elements[i].equals(element))
				return true;
		return false;
	}
	
	public boolean remove(E element) throws NullPointerException {
		if (element == null)
			throw new NullPointerException("Null elements not allowed in the set.");
		
		for (int i = 0; i < elements.length; ++i) {
			if (elements[i].equals(element)) {
				elements[i] = null;
				for (int j = i; i < size - 1; ++j)
					elements[j] = elements[j+1];
				--size;
				return true;
			}
		}
		return false;
	}
	
	public void retainAll(Collection<? extends E> collection) throws NullPointerException {
		for (int i = 0; i < size; ++i) {
			if (!collection.contains(elements[i])) {
				elements[i] = null;
				for (int j = i; i < size - 1; ++j)
					elements[j] = elements[j+1];
				--size;
			}
		}
	}

	public Iterator<E> iterator() {
		return new ArraySetIterator();
	}
	
	private class ArraySetIterator implements Iterator<E> {
		public ArraySetIterator() {
			snapshot = (E[]) new Object[size];
			for (int i = 0; i < size; ++i)
				snapshot[i] = elements[i];
		}
		
		public boolean hasNext() {return index < snapshot.length;}
		public E next() {return snapshot[index++];}
		
		private E[] snapshot() {
			return snapshot;
		}
		
		E[] snapshot;
		private int index;
	}
	
	public E[] toArray() {
		return new ArraySetIterator().snapshot();
	}
	
	private void doubleCapacity() {
		E[] temp = (E[]) new Object[elements.length * 2];
		for (int i = 0; i < elements.length; ++i)
			temp[i] = elements[i];
		elements = temp;
	}
	
	private E[] elements;
	private int size;
	
	public static void main(String[] args) {
		Set<Integer> set = new UnsortedArraySet<>();
		for (int i = 0; i < 10; ++i)
			set.add(new Random().nextInt(10));
		for (Integer i: set)
			System.out.print(i + " ");
	}
}
