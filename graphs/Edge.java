package graphs;

/** An interface for edges with mutable data in both directed and undirected graphs. */
public interface Edge<V,E> {
	
	/** Returns the data associated with this edge. */
	E data();

	/** Changes the data associated with this edge to that which is specified. */
	void setData(E data);
	
	/** Returns the source if the graph is directed, or an arbitrary vertex among the
	 * pair of vertices this edge connects otherwise. */
	Vertex<V> alpha();
	
	/** Returns the destination if the graph if directed, or an arbitrary vertex among
	 * the pair of vertices this edge connects otherwise. */
	Vertex<V> beta();
}
