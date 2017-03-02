package stacksqueuesdeques;
import lists.DoublyLinkedList;

public class LinkedDeque<E> implements Deque<E> {
	public LinkedDeque() {
		elements = new DoublyLinkedList<>();
	}
	
	public E prepend(E element) throws NullPointerException {
		if (element == null)
			throw new NullPointerException("Null elements not allowed.");
		else return elements.prepend(element);
	}

	public E append(E element) throws NullPointerException {
		if (element == null)
			throw new NullPointerException("Null elements not allowed.");
		else return elements.append(element);
	}

	public E popFront() throws IllegalStateException {
		if (isEmpty())
			throw new IllegalStateException("Deque is empty.");
		else return elements.popFront();
	}

	public E popBack() throws IllegalStateException {
		if (isEmpty())
			throw new IllegalStateException("Deque is empty.");
		else return elements.popBack();
	}

	public E first() {
		return elements.first();
	}

	public E last() {
		return elements.last();
	}

	public Iterable<E> elements() {
		return elements;
	}

	public Iterable<E> elementsInReverse() {
		return elements.elementsInReverse();
	}

	public int size() {
		return elements.size();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	private DoublyLinkedList<E> elements;
	
	public static void main(String[] args) {
		Deque<Integer> deque = new LinkedDeque<>();
		deque.prepend(3);
		deque.prepend(2);
		deque.prepend(1);
		for (Integer i: deque.elementsInReverse())
			System.out.print(i + " ");
	}
}
