package graphs;

/** An interface for vertices with immutable data in both directed and undirected graphs. */
public interface Vertex<V> {
	
	/** Returns the data associated with this vertex. */
	V element();
}
