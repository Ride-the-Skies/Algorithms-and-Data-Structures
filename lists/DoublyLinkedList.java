package lists;

import java.util.Iterator;

public class DoublyLinkedList<E> implements Iterable<E> {
	private static class LN<E> {
		public LN() {}
		public LN(E element) {this.element = element;}
		
		public E element() {return element;}
		public LN<E> prev() {return prev;}
		public LN<E> next() {return next;}
		
		public void setNext(LN<E> next) {
			this.next = next;
			next.prev = this;
		}
		
		public void insertBetween(LN<E> prev, LN<E> next) {
			prev.next = this;
			next.prev = this;
			this.prev = prev;
			this.next = next;
		}
		
		E element;
		LN<E> prev;
		LN<E> next;
	}
	
	/** Constructs a doubly linked list with a header and trailer node storing null values. */
	public DoublyLinkedList() {
		header = new LN<>();
		trailer = new LN<>();
		header.setNext(trailer);
	}
	
	/** Returns the element at the front of the list, or null if it is empty. */
	public E first() {
		return isEmpty() ? null : header.next().element();
	}
	
	/** Returns the element at the back of the list, or null if it is empty. */
	public E last() {
		return isEmpty() ? null : trailer.prev().element();
	}
	
	/** Adds an element to the front of the list and returns the newly inserted element. */
	public E prepend(E toPrepend) {
		LN<E> newest = new LN<>(toPrepend);
		newest.insertBetween(header, header.next());
		++size;
		return newest.element();
	}
	
	/** Adds an element to the back of the list and returns to newly inserted element. */
	public E append(E toAppend) {
		LN<E> newest = new LN<>(toAppend);
		newest.insertBetween(trailer.prev(), trailer);
		++size;
		return newest.element();
	}
	
	/** Removes the node at the front of the list and returns its element if the list
	 * is nonempty. Otherwise, returns null and leaves the list unaffected. */
	public E popFront() {
		if (isEmpty())
			return null;
		E popped = header.next().element();
		header.setNext(header.next().next());
		--size;
		return popped;
	}
	
	/** Removes the node at the back of the list and returns its element if the list
	 * is nonempty. Otherwise, returns null and leaves the list unaffected. */
	public E popBack() {
		if (isEmpty())
			return null;
		E popped = trailer.prev().element();
		trailer.prev().prev().setNext(trailer);
		--size;
		return popped;
	}
	
	/** Returns the number of elements in the doubly linked list. */
	public int size() {
		return size;
	}
	
	/** Returns true if the list if empty or false if it isn't. */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/** Returns a String representation of the contents of the list, emphasizing
	 * the underlying linked structure. */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (LN<E> walk = header.next(); walk != trailer; walk = walk.next())
			sb.append(walk.element() + (walk.next() == trailer ? "" : " -> "));
		return sb.toString();
	}
	
	/** Returns an iteration over the elements of the list, from front to back. */
	public Iterator<E> iterator() {
		return new ListIterator(false);
	}
	
	/** Returns an iteration over the elements of the list, from back to front. */
	public Iterable<E> reverse() {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return new ListIterator(true);
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private class ListIterator implements Iterator<E> {
		private ListIterator(boolean reverse) {
			elements = (E[]) new Object[size];
			if (reverse)
				for (LN<E> walk = trailer.prev(); walk != header; walk = walk.prev())
					elements[index++] = walk.element();
			else 
				for (LN<E> walk = header.next(); walk != trailer; walk = walk.next())
					elements[index++] = walk.element();
			index = 0;
		}
		
		public boolean hasNext() {return index != elements.length;}
		public E next() {return elements[index++];}
		
		private E[] elements;
		private int index;
	}
	
	private LN<E> header;
	private LN<E> trailer;
	private int size;
	
	public static void main(String[] args) {
		DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
		list.append(1);
		list.append(2);
		list.append(3);
		System.out.println(list.toString());
		for (Integer i: list)
			System.out.println(i);
	}
}
