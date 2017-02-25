package lists;
import java.util.Iterator;
import utility.Position;

public class LinkedPositionalList<E> implements PositionalList<E>, Iterable<Position<E>> {
	/** A static nested class for encapsulation of the list nodes of the linked list. */
	private static class LN<E> implements Position<E> {
		private LN() {}
		private LN(E data) {this.data = data;} 
		
		private LN(E data, LN<E> prev, LN<E> next) {
			this.data = data;
			this.prev = prev; prev.next = this;
			this.next = next; next.prev = this;
		}
		
		public E data() {return data;}
		public LN<E> prev() {return prev;}
		public LN<E> next() {return next;}
		
		private void insertBetween(LN<E> prev, LN<E> next) {
			this.prev = prev; prev.next = this;
			this.next = next; next.prev = this;
		}
		
		public String toString() {
			return data.toString();
		}
		
		private E data;
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
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public Position<E> first() {
		return isEmpty() ? null : header.next();
	}

	public Position<E> last() {
		return isEmpty() ? null : trailer.prev();
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
		((LN<E>) p).data = e;
		return p.data();
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
		return toRemove.data();
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
			sb.append(walk.toString() + (walk.next() == trailer ? "" : " -> "));
		return sb.toString();
	}
	
	/** Returns an iterator over the positions in the list, from front to back. */
	public Iterator<Position<E>> iterator() {
		return new ListIterator(false);
	}
	
	/** Returns an iterable collection of the positions in the list, from back to front. */
	public Iterable<Position<E>> reverse() {
		return new Iterable<Position<E>>() {
			public Iterator<Position<E>> iterator() {
				return new ListIterator(true);
			}
		};
	}	
	
	/** Does not support removal of elements in the list. */
	private class ListIterator implements Iterator<Position<E>> {
		@SuppressWarnings("unchecked")
		public ListIterator(boolean reverse) {
			snapshot = (Position<E>[]) new Position[size];
			if (!reverse)
				for (LN<E> walk = header.next(); walk != trailer; walk = walk.next())
					snapshot[index++] = walk;
			else
				for (LN<E> walk = trailer.prev(); walk != header; walk = walk.prev())
					snapshot[index++] = walk;
			index = 0;
		}
		
		public boolean hasNext() {return index < snapshot.length;}
		public Position<E> next() {return snapshot[index++];}
		
		private Position<E>[] snapshot;
		private int index;
	}
	
	/** Returns an iteration over the elements in the list, from front to back. */
	public Iterable<E> elements() {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return new ElementIterator(false);
			}
		};
	}
	
	/** Returns an iteration over the elements in the list, from back to front. */
	public Iterable<E> elementsReverse() {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return new ElementIterator(true);
			}
		};
	}
	
	private class ElementIterator implements Iterator<E> {
		public ElementIterator(boolean reverse) {iterator = new ListIterator(reverse);}
		public boolean hasNext() {return iterator.hasNext();}
		public E next() {return iterator.next().data();}
		private ListIterator iterator;
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
