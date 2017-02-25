package sqd;

@SuppressWarnings("unchecked")
public class ArrayStack<E> implements Stack<E> {

	public ArrayStack() {
		this(DEFAULT_CAPACITY, false);
	}
	
	public ArrayStack(int initialCapacity) {
		this(initialCapacity, false);
	}
	
	public ArrayStack(int initialCapacity, boolean isCapacityFixed) {
		elements = (E[]) new Object[initialCapacity];
		this.isCapacityFixed = isCapacityFixed;
	}
	
	public E push(E element) throws NullPointerException {
		if (element == null)
			throw new NullPointerException("Null elements not allowed.");
		if (isCapacityFixed()) {
			top = (top + 1) % elements.length;
			elements[top] = element;
			size = Math.min(size + 1, elements.length);
		}
		else {
			if (size() == elements.length)
				doubleCapacity();
			elements[++top] = element;
			++size;
		}
		return peek();
	}
	
	public E pop() throws IllegalStateException {
		if (isEmpty())
			throw new IllegalStateException("Stack is empty.");
		E toPop = elements[top];
		elements[top] = null;
		top = top == 0 ? elements.length - 1 : top - 1;
		--size;
		return toPop;
	}
	
	public E peek() {
		return isEmpty() ? null : elements[top];
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size() == 0;
	}
	
	public boolean isCapacityFixed() {
		return isCapacityFixed;
	}
	
	public int capacity() {
		return elements.length;
	}
	
	private void doubleCapacity() {
		E[] bigger = (E[]) new Object[elements.length * 2];
		for (int i = top, j = elements.length-1; j >= 0; i = (i==0 ? elements.length-1 : i-1), --j)
			bigger[j] = elements[i];
		elements = bigger;
	}
	
	private E[] elements;
	private int size;
	private int top = -1;
	private final boolean isCapacityFixed;
	private static final int DEFAULT_CAPACITY = 16;
	
	
	public static void main(String[] args) {
	
	}
}
