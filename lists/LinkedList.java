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
	
	/** Returns the element stored at the front of the list, or null if the list is empty. */
	public E first() throws IllegalStateException {
		return isEmpty() ? null : head.element();
	}
	
	/** Returns the element stored at the back of the list, or null if the list is empty. */
	public E last() throws IllegalStateException {
		return isEmpty() ? null : tail.element();
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
	
	/** Returns and removes the first occurrence of the element if it exists in the list.
	 * Leaves the list unchanged and returns null there is no such occurrence. */
	public E remove(E toRemove) {
		E removed = null;
		if (size() == 0)
			return removed;
		else if (size() == 1)
			if (head.element().equals(toRemove)) {
				removed = head.element();
				head = tail = null;
				--size;
			}
			else return removed;
		else if (head.element().equals(toRemove)) {
			removed = head.element();
			head = head.next();
			--size;
		}
		else {
			for (LN<E> walk = head; walk.next() != null; walk = walk.next()) {
				if (walk.next().element().equals(toRemove)) {
					if (tail == walk.next())
						tail = walk;
					removed = walk.next().element();
					walk.next = walk.next().next();
					--size;
					break;
				}
			}
		}
		return removed;
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
	
	/** Returns the number of elements stored in the list. */
	public int size() {
		return size;
	}
	
	/** Returns true if the list is empty, or false if it isn't. */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/** Iteratively reverses the list. */
	public void reverse() {
		if (isEmpty() || size == 1)
			return;
		
		tail = head;
		LN<E> prev = head;
		LN<E> current = head.next();
		LN<E> next = head.next().next();
		while (next != null) {
			current.setNext(prev);
			prev = current;
			current = next;
			next = next.next();
		}
		current.setNext(prev);
		tail.setNext(null);
		head = current;
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
		
		list.remove(1);
		list.remove(7);
		list.remove(4);
		list.remove(2);
		list.remove(6);
		list.remove(3);
		list.remove(4);
		list.remove(5);
		list.remove(0);
		
		list.prepend(1);
		list.prepend(2);
		
		System.out.println(list.toString());
		System.out.println(list.size());
	}
}