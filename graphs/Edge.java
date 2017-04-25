package graphs;

/** An interface for edges with mutable data in both directed and undirected graphs. */
public interface Edge<E> {
	
	/** Returns the data associated with this edge. */
	E data();

	/** Changes the data associated with this edge to that which is specified. */
	void setData(E data);
}
