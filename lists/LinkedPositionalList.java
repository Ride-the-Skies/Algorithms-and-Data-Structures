package lists;
import java.util.Iterator;
import utility.Position;

public class LinkedPositionalList<E> implements PositionalList<E>, Iterable<E> {
	/** A static nested class for encapsulation of the list nodes of the linked list. */
	private static class LN<E> implements Position<E> {
		private LN() {}
		private LN(E element) {this.element = element;} 
		
		private LN(E element, LN<E> prev, LN<E> next) {
			this.element = element;
			this.prev = prev; prev.next = this;
			this.next = next; next.prev = this;
		}
		
		public E element() {return element;}
		public LN<E> prev() {return prev;}
		public LN<E> next() {return next;}
		
		private void insertBetween(LN<E> prev, LN<E> next) {
			this.prev = prev; prev.next = this;
			this.next = next; next.prev = this;
		}
		
		public String toString() {return element.toString();}
		
		private E element;
		private LN<E> prev;
		private LN<E> next;
	}
	
	/** Creates a header and a trailer node and links them to each other. */
	public LinkedPositionalList() {
		header = new LN<>();
		trailer = new LN<>();
		header.next = trailer;
		trailer.prev = header;
	}
	
	public int size() {return size;}
	public boolean isEmpty() {return size == 0;}
	
	public Position<E> first() {
		if (isEmpty())
			return null;
		else return header.next();
	}

	public Position<E> last() {
		if (isEmpty())
			return null;
		else return trailer.prev();
	}
	
	public Position<E> before(Position<E> p) throws IllegalArgumentException {
		if (!isValid(p))
			throw new IllegalArgumentException("The specified position is not valid.");
		else return ((LN<E>) p).prev();
	}

	public Position<E> after(Position<E> p) throws IllegalArgumentException {
		if (!isValid(p))
			throw new IllegalArgumentException("The specified position is not valid.");
		else return ((LN<E>) p).next();
	}
	
	/** Adds an element to the front of the list. */
	public Position<E> prepend(E element) {
		LN<E> newest = new LN<>(element);
		newest.insertBetween(header, header.next());
		++size;
		return newest;
	}
	
	/** Adds a sequence of elements to the front of the list. */
	@SuppressWarnings("unchecked")
	public void prepend(E... elements) {
		for (E element: elements)
			prepend(element);
	}
	
	/** Adds an element to the back of the list. */
	public Position<E> append(E element) {
		LN<E> newest = new LN<>(element);
		newest.insertBetween(trailer.prev(), trailer);
		++size;
		return newest;
	}
	
	/** Adds a sequence of elements to the back of the list. */
	@SuppressWarnings("unchecked")
	public void append(E... elements) {
		for (E element: elements)
			append(element);
	}
	
	
	public Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException {
		if (!isValid(p))
			throw new IllegalArgumentException("The specified position is not valid.");
		LN<E> newest = new LN<>(e);
		newest.insertBetween(((LN<E>) p).prev(), ((LN<E>) p));
		++size;
		return newest;
	}

	public Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException {
		if (!isValid(p))
			throw new IllegalArgumentException("The specified position is not valid.");
		LN<E> newest = new LN<>(e);
		newest.insertBetween(((LN<E>) p), ((LN<E>) p).next());
		++size;
		return newest;
	}
	
	public E set(Position<E> p, E e) throws IllegalArgumentException {
		if (!isValid(p))
			throw new IllegalArgumentException("The specified position is not valid.");
		((LN<E>) p).element = e;
		return p.element();
	}
	
	public E remove(Position<E> p) throws IllegalArgumentException {
		if (!isValid(p))
			throw new IllegalArgumentException("The specified position is not valid.");
		
		LN<E> toRemove = (LN<E>) p;
		LN<E> prev = toRemove.prev();
		LN<E> next = toRemove.next();
		prev.next = next;
		next.prev = prev;
		toRemove.prev = toRemove.next = null;
		--size;
		return toRemove.element();
	}
	
	/** A position is valid if it is one of those containing an element in a LinkedPositionalList. */
	private boolean isValid(Position<E> p) {
		if (!(p instanceof LN) || p == header || p == trailer || ((LN<E>) p).next() == null)
			return false;
		else return true;
	}
	
	/** Overrides Object's toString method, and provides a String representation of the state of the list. */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (LN<E> walk = header.next(); walk != trailer; walk = walk.next())
			sb.append(walk.element.toString() + (walk.next() == trailer ? "" : " -> "));
		return sb.toString();
	}
	
	/** Returns an iterator over the elements in the list, from front to back. */
	public Iterator<E> iterator() {return new ListIterator(false);}
	
	/** Returns an iterable collection of the elements in the list, from back to front. */
	public Iterable<E> reverse() {return new ReverseListIterable();}
	
	private class ReverseListIterable implements Iterable<E> {
		public Iterator<E> iterator() {return new ListIterator(true);}
	}
	
	/** Does not support removal of elements in the list. */
	private class ListIterator implements Iterator<E> {
		@SuppressWarnings("unchecked")
		public ListIterator(boolean reverse) {
			snapshot = (E[]) new Object[size];
			if (!reverse)
				for (LN<E> walk = header.next(); walk != trailer; walk = walk.next())
					snapshot[index++] = walk.element();
			else
				for (LN<E> walk = trailer.prev(); walk != header; walk = walk.prev())
					snapshot[index++] = walk.element();
			index = 0;
		}
		
		public boolean hasNext() {return index < snapshot.length;}
		public E next() {return snapshot[index++];}
		
		private E[] snapshot;
		private int index;
	}
	
	private LN<E> header;
	private LN<E> trailer;
	private int size;
	
	public static void main(String[] args) {
		LinkedPositionalList<Integer> list = new LinkedPositionalList<>();
		list.append(1,2,3,4,5);
		System.out.println(list.toString());
		Position<Integer> p = list.after(list.first());
		list.addBefore(p,0);
		System.out.println(list.toString());
	}
}
