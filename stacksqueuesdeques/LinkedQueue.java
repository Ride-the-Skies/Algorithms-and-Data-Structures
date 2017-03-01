package stacksqueuesdeques;
import lists.LinkedList;

public class LinkedQueue<E> implements Queue<E> {
	public LinkedQueue() {
		elements = new LinkedList<>();
	}
	
	public E peek() {
		return elements.first();
	}

	public E enqueue(E element) {
		elements.append(element);
		return elements.last();
	}

	public E dequeue() throws IllegalArgumentException {
		if (isEmpty())
			throw new IllegalArgumentException("Queue is empty.");
		return elements.popFront();
	}

	public int size() {
		return elements.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	private LinkedList<E> elements;
	
	public static void main(String[] args) {
		Queue<Integer> q = new LinkedQueue<>();
		for (int i = 0; i < 10; ++i)
			q.enqueue(i + 1);
		for (int i = 0; i < 10; ++i)
			System.out.println(q.dequeue());
	}
}
