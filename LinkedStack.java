package sqd;
import lists.DoublyLinkedList;

public class LinkedStack<E> implements Stack<E> {
	public LinkedStack() {
		elements = new DoublyLinkedList<>();
		capacity = 0;
	}
	
	public LinkedStack(int capacity) throws IllegalArgumentException {
		if (capacity < 1)
			throw new IllegalArgumentException("Capacity must be positive.");
		elements = new DoublyLinkedList<>();
		this.capacity = capacity;
	}
	
	public E push(E element) throws IllegalArgumentException {
		if (element == null)
			throw new IllegalArgumentException("Null elements not allowed.");		
		if (isCapacityFixed() && size() == capacity) 
			elements.popBack();
		return elements.prepend(element);
	}
	
	public E pop() throws IllegalStateException {
		if (isEmpty())
			throw new IllegalStateException("Stack is empty.");
		else return elements.popFront();
	}
	
	public E peek() {
		return elements.first();
	}
	
	public int size() {
		return elements.size();
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	public boolean isCapacityFixed() {
		return capacity > 0;
	}
	
	public int capacity() {
		return capacity;
	}
	
	private DoublyLinkedList<E> elements;
	private final int capacity;
}
