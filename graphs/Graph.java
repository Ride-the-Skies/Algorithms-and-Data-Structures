package graphs;

public interface Graph<V,E> {
	
	/** Returns the number of vertices of the graph. */
	int numVertices();
	
	/** Returns an iteration of all the vertices of the graph. */
	Iterable<? extends Vertex<V>> vertices();
	
	/** Returns the number of edges of the graph. */
	int numEdges();
	
	/** Returns an iteration of all the edges of the graph. */
	Iterable<? extends Edge<E>> edges();
	
	/** Returns true if the graph is directed, or false if it isn't. */
	boolean isDirected();
	
	/** Returns the edge from vertex u to vertex v, if one exists; otherwise returns null.
	 * For an undirected graph, there is no difference between getEdge(u,v) and getEdge(v,u). */
	Edge<E> getEdge(Vertex<V> u, Vertex<V> v);

	/** Returns an array containing the two endpoint vertices of edge e. If the graph if directed,
	 * the first vertex is the origin and the second is the destination. */
	Vertex<V>[] endVertices(Edge<E> e);
	
	/** For edge e incident to vertex v, returns the other vertex of the edge; an error occurs
	 * if e is not incident to v. */
	Vertex<V> opposite(Vertex<V> v, Edge<E> e) throws IllegalArgumentException;
	
	/** Returns the number of outgoing edges from vertex v. */
	int outDegree(Vertex<V> v);
	
	/** Returns the number of incoming edges to vertex v. For an undirected graph, this returns
	 * the same value as does outDegree(v). */
	int inDegree(Vertex<V> v);	
	
	/** Returns an iteration of all outgoing edges from vertex v. */
	Iterable<? extends Edge<E>> outgoingEdges(Vertex<V> v);
	
	/** Returns an iteration of all incoming edges to vertex v. For an undirected graph, this 
	 * returns the same collection as does outgoingEdges(v). */
	Iterable<? extends Edge<E>> incomingEdges(Vertex<V> v);
	
	/** Creates and returns a new Vertex storing element x. */
	Vertex<V> insertVertex(V x);
	
	/** Creates and returns a new Edge from vertex u to vertex v, storing element x; an error 
	 * occurs if there already exists an edge from u to v. */
	Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E x) throws IllegalArgumentException;
	
	/** Removes vertex v and all its incident edges from the graph. */
	void removeVertex(Vertex<V> v);
	
	/** Removes Edge e from the graph. */
	void removeEdge(Edge<E> e);
	
}
