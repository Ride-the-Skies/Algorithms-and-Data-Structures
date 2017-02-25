package lists;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class CircularlyLinkedList<E> implements Iterable<E> {
	/** A static nested class for encapsulating the nodes of the list. */
	@SuppressWarnings("unused")
	private static class LN<E> {
		public LN() {}
		public LN(E element) {
			this.element = element;
		}
		public LN(E element, LN<E> next) {
			this.element = element;
			this.next = next;
		}
		
		public E element() {return element;}
		public LN<E> next() {return next;}
		public void setNext(LN<E> next) {this.next = next;}
		
		private E element;
		private LN<E> next;
	}

	public CircularlyLinkedList() {}
	
	/** Returns the first element in the list if the list is non-empty. 
	 * Otherwise returns null. */
	public E first() {
		return isEmpty() ? null : tail.next().element();
	}
	
	
	/** Returns the last element in the list if the list is non-empty.
	 * Otherwise returns null. */
	public E last() {
		return isEmpty() ? null : tail.element();
	}
	
	/** Adds an element to the front of the list and returns the newly inserted element. 
	 * Does not allow the insertion of null elements. */
	public E prepend(E element) throws IllegalArgumentException {
		if (element == null)
			throw new IllegalArgumentException("Null elements not allowed.");
		else if (isEmpty()) {
			tail = new LN<>(element);
			tail.setNext(tail);
		}
		else
			tail.setNext(new LN<>(element, tail.next()));
		++size;
		return tail.next().element();
	}
	
	/** Adds an element to the back of the list and returns the newly inserted element.
	 * Does not allow the insertion of null elements. */
	public E append(E element) throws IllegalArgumentException {
		if (element == null)
			throw new IllegalArgumentException("Null elements not allowed.");
		else if (isEmpty()) {
			tail = new LN<>(element);
			tail.setNext(tail);
		}
		else  {
			tail.setNext(new LN<>(element, tail.next()));
			tail = tail.next();
		}
		++size;
		return tail.element();
	}
	
	/** Returns the number of elements in the circularly linked list. */
	public int size() {
		return size;
	}
	
	/** Returns true if the list is empty or false if it isn't. */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/** Returns a String representation of the list. */
	public String toString() {
		if (isEmpty())
			return null;

		StringBuilder sb = new StringBuilder();
		LN<E> walk = tail.next();
		do {
			sb.append(walk.element().toString() + (walk.next() == tail.next() ? "" : " -> "));
			walk = walk.next();
		}
		while (walk != tail.next());
		
		return sb.toString();
	}
	
	public Iterator<E> iterator() {
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<E> {
		public ListIterator() {
			elements = (E[]) new Object[size];
			LN<E> walk = tail;
			for (int i = 0; i < size; ++i) {
				walk = walk.next();
				elements[index++] = walk.element();
			}
			index = 0;
		}
		
		public boolean hasNext() {return index < elements.length;}
		public E next() {return elements[index++];}
		
		private E[] elements;
		private int index;
	}
	
	private LN<E> tail;
	private int size;
	
	public static void main(String[] args) {
		CircularlyLinkedList<Integer> list = new CircularlyLinkedList<>();
		
		list.append(1);
		list.append(2);
		list.append(3);
		list.prepend(0);
		list.append(4);
		list.append(5);
		
		System.out.println(list.toString());
		System.out.println("First: "+list.first());
		System.out.println("Last:  "+list.last());
		for (Integer i: list)
			System.out.print(i + " ");
	}
}
