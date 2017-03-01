package stacksqueuesdeques;

/**<p> An interface for the stack abstract data type. Does not allow null elements to be inserted. </p>
 * 
 * <p> Regardless of the underlying implementation, the stack may have a fixed capacity. If so,
 * pushing new elements to the stack when it has reached its capacity will remove the oldest
 * element to make room for the newest one. Applications of this include "ctrl+z" in some
 * text editing programs, where a limited amount of memory may be allocated to allow the user
 * to undo a finite sequence of operations. </p> */
public interface Stack<E> {
	/** Pushes an element to the top of the stack and returns the newly added element. */
	E push(E element) throws NullPointerException;
	
	/** Returns and removes the element atop the stack. Throws an IllegalStateException if the stack is empty. */
	E pop() throws IllegalStateException;
	
	/** Returns but does not remove the element atop the stack, or null if the stack is empty. */
	E peek();
	
	/** Returns the number of elements stored in the stack. */
	int size();
	
	/** Returns true if the stack is empty, or false if it isn't. */
	boolean isEmpty();
	
	/** Returns true if the capacity of the stack is fixed, or false if it isn't. */
	boolean isCapacityFixed();
	
	/** Returns the capacity of the stack if it is fixed, or -1 if it can fit an arbitrary number of elements. */
	int capacity();
}
