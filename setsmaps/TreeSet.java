package setsmaps;

public interface TreeSet<E> extends Set<E> {
	/** Returns the element that comes first under the ordering of the comparator. */
	E first();
	
	/** Returns the element that comes last under the ordering of the comparator. */
	E last();
	
	/** Returns and removes the element that comes first under the ordering of the comparator. */
	E pollFirst();
	
	/** Returns and removes the elemenet that comes last under the ordering of the comparator. */
	E pollLast();
}
