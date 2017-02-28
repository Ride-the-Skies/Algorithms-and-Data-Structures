package sqd;

@SuppressWarnings("unchecked")
public class ArrayQueue<E> implements Queue<E> {
	
	public ArrayQueue() {
		this(DEFAULT_CAPACITY);
	}
	
	public ArrayQueue(int initialCapacity) {
		elements = (E[]) new Object[DEFAULT_CAPACITY];
		front = 0;
		back = -1;
	}
	
	public E peek() {
		return (isEmpty()) ? null : elements[front];
	}
	
	
	public E enqueue(E element) {
		if (size() == elements.length)
			doubleCapacity();
		back = (back + 1) % elements.length;
		elements[back] = element;
		++size;
		return elements[back];
	}
	
	public E dequeue() throws IllegalStateException {
		if (isEmpty())
			throw new IllegalStateException("Queue is empty.");
		E dequeued = elements[front];
		elements[front] = null;
		front = (front + 1) % elements.length;
		return dequeued;
	}
	
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size() == 0;
	}
	
	private void doubleCapacity() {
		E[] temp = (E[]) new Object[elements.length * 2];
		for (int i = 0; i < size; ++i)
			temp[i] = elements[(front + i) % elements.length];
		elements = temp;
	}
	
	private E[] elements;
	private int size;
	private int front, back;
	public static final int DEFAULT_CAPACITY = 16;
	
	public static void main(String[] args) {
		ArrayQueue<Integer> q = new ArrayQueue<>();
		for (int i = 0; i < 25; ++i)
			q.enqueue(i + 1);
		
		System.out.println(java.util.Arrays.toString(q.elements));
		
		for (int i = 0; i < 10; ++i)
			q.dequeue();
		
		System.out.println(java.util.Arrays.toString(q.elements));
	}
}
