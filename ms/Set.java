package ms;
import java.util.Collection;
import java.util.Iterator;

public interface Set<E> extends Iterable<E> {
	/** Returns true and adds the specified element if it is not already
	 * present in the set. Otherwise returns false. */
	boolean add(E element) throws NullPointerException;
	
	/** Adds to the current set instance all of the elements in the 
	 * specified collection that are not already in it. */
	void addAll(Collection<? extends E> source) throws NullPointerException;
	
	/** Empties the set. */
	void clear();
	
	/** Returns true if the set contains the given element. */
	boolean contains(E element) throws NullPointerException;
	
	/** Returns true if the set contains all of the elements of the specified collection. */
	boolean containsAll(Collection<? extends E> collection) throws ClassCastException;
	
	/** Returns true and removes the specified element from the set if it is present. */
	boolean remove(E element) throws NullPointerException;
	
	/** Removes from the current set all elements in the specfied collection. */
	void removeAll(Collection<? extends E> collection) throws NullPointerException;
	
	/** Retains only the elements in this set that are contained in the specified collection. */
	void retainAll(Collection<? extends E> collection) throws NullPointerException;
	
	/** Returns true if the size of this set is zero. */
	boolean isEmpty();
	
	/** Returns the cardinality of the set. */
	int size();
	
	/** Returns an iterator over the elements of the set. */
	Iterator<E> iterator();
	
	/** Returns an array containing all of the elements in the set. */
	E[] toArray();
	
	int DEFAULT_CAPACITY  = 16;
}
