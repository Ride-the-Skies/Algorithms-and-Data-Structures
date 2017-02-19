package searchtrees;

/** A tree set interface, based on that of Java SE 7. */
public interface TreeSet<E extends Comparable<E>> {
	
	/** Returns true if this set contains the specified element. */
	boolean contains(E element);
	
	/** Adds the specified element to this set if it is not already present. */
	boolean add(E element);
	
	/** Removes the specified element from this set if it is present. */
	boolean remove(E element);
	
	/** Returns true if this set contains no elements. */
	boolean isEmpty();
	
	/** Returns the number of elements in this set (its cardinality). */
	int size();
	
	/** Returns the first (lowest) element currently in this set. */
	E first();
	
	/** Returns the last (highest) element currently in this set. */
	E last();
	
	/** Returns the kth smallest element of the set. Throws an exception if k is less than the size of the set. */
	E kthSmallest(int k) throws IllegalArgumentException;
	
	/** Returns the kth largest element of the set. Throws an exception if k is less than the size of the set. */
	E kthLargest(int k) throws IllegalArgumentException;
}
