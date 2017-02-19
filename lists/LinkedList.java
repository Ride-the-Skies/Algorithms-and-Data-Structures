package lists;
import java.util.Iterator;

/** A singly linked list, with a head and tail so that insertions to the front and back
 * of the list can be done in constant time. */
public class LinkedList<E> implements Iterable<E> {
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

	/** Sets the fields of the linked list to their default values. */
	public LinkedList() {}
	
	/** Creates a linked list storing just one node with the given element. */
	public LinkedList(E element) {
		LN<E> first = new LN<>(element);
		head = tail = first;
		size = 1;
	}
	
	/** Returns the element stored at the front of the list, or throws an IllegalStateException
	 * if the list is empty.*/
	public E first() throws IllegalStateException {
		if (isEmpty())
			throw new IllegalStateException("list is empty.");
		else return head.element();
	}
	
	/** Returns the element stored at the back of the list, or throws an IllegalStateException
	 * if the list is empty. */
	public E last() throws IllegalStateException {
		if (isEmpty())
			throw new IllegalStateException("list is empty.");
		else return tail.element();
	}
	
	/** Adds a node with the given element to the front of the list. */
	public void prepend(E element) {
		if (size() == 0)
			head = tail = new LN<>(element);
		else {
			LN<E> newest = new LN<>(element, head);
			head = newest;
		}
		++size;
	}
	
	/** Adds a node with the given element to the back of the list. */
	public void append(E element) {
		LN<E> newest = new LN<>(element);
		if (size() == 0)
			head = tail = newest;
		else {
			tail.setNext(newest);
			tail = newest;
		}
		++size;
	}
	
	/** Removes the node at the front of the list and returns its element, if the list is not empty.
	 * Otherwise, throws an IllegalStateException.
	 */
	public E popFront() throws IllegalStateException {
		if (isEmpty())
			throw new IllegalStateException("list is empty");
		LN<E> toRemove = head;
		head = head.next();
		if (size() == 1)
			tail = tail.next();
		toRemove.setNext(null);
		--size;
		return toRemove.element();
	}
	
	/** Removes the node at the back of the list and returns its element, if the list is not empty.
	 * Otherwise, throws an IllegalStateException.
	 * <b>NOTE:</b> Takes θ(n), because the list is singly linked, so the whole list must be traversed in order
	 * for the tail to be re-referenced to the node right before the one to be removed.
	 */
	public E popBack() throws IllegalArgumentException {
		if (isEmpty())
			throw new IllegalStateException("list is empty");
		
		LN<E> toRemove;
		if (size() == 1) {
			toRemove = head;
			head = tail = null;
		}
		else {
			LN<E> walk = head;
			while (walk.next() != tail)
				walk = walk.next();
			toRemove = walk.next();
			tail = walk;
			tail.setNext(null);
		}
		--size;
		return toRemove.element();
	}
	
	/** Returns the number of elements stored in the list. */
	public int size() {
		return size;
	}
	
	/** Returns true if the list is empty, or false if it isn't. */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/** Returns a String representation of the list that, while somewhat clunky, emphasizes the linked nature of the data structure. */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (LN<E> walk = head; walk != null; walk = walk.next())
			sb.append(walk.element().toString() + " -> ");
		sb.append("null");
		return sb.toString();
	}
	
	public Iterator<E> iterator() {
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<E> {
		@SuppressWarnings("unchecked")
		private ListIterator() {
			data = (E[]) new Object[size];
			for (LN<E> walk = head; walk != null; walk = walk.next())
				data[index++] = walk.element();
			index = 0;
		}
		
		public boolean hasNext() {return index != size;}
		public E next() {return data[index++];}
		
		private E[] data;
		private int index;
	}
	
	
	private LN<E> head;
	private LN<E> tail;
	private int size;
	
	public static void main(String[] args) {
		LinkedList<Integer> list = new LinkedList<>();
		list.prepend(5);
		list.prepend(4);
		list.prepend(3);
		list.prepend(2);
		list.prepend(1);
		list.append(6);
		list.append(7);
		System.out.println(list.toString());
		for (Integer i: list)
			System.out.println(i);
	}
}