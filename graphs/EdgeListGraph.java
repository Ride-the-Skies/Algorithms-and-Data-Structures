package graphs;
import lists.*;
import utility.*;

public class EdgeListGraph<V,E> implements Graph<V,E> {
	// BEGINNING OF STATIC NESTED EDGELISTVERTEX CLASS.
	private static class EdgeListVertex<V> implements Vertex<V> {
		public EdgeListVertex(V element, Position<Vertex<V>> position) {
			this.element = element;
			this.position = position;
		}
		
		public V element() {return element;}
		public Position<Vertex<V>> position() {return position;}
		
		private final V element;
		private final Position<Vertex<V>> position;
	}
	
	// BEGINNING OF STATIC NESTED EDGELISTEDGE CLASS.
	private static class EdgeListEdge<V,E> implements Edge<V,E> {
		public EdgeListEdge(E data, Vertex<V> alpha, Vertex<V> beta, Position<Edge<V,E>> position) {
			this.data = data;
			this.alpha = alpha;
			this.beta = beta;
			this.position = position;
		}
		
		public E data() {return data;}
		public void setData(E data) {this.data = data;}
		public Vertex<V> alpha() {return alpha;}
		public Vertex<V> beta() {return beta;}
		public Position<Edge<V,E>> position() {return position;}

		private E data;
		private final Vertex<V> alpha, beta;
		private final Position<Edge<V,E>> position;
	}
	
	// BEGINNING OF ENCAPSULATING EDGELISTGRAPH CLASS.
	public EdgeListGraph(boolean isDirected) {
		vertices = new LinkedPositionalList<>();
		edges = new LinkedPositionalList<>();
		this.isDirected = isDirected;
	}
	
	public int numVertices() {
		return vertices.size();
	}

	public Iterable<Vertex<V>> vertices() {
		return vertices.elements();
	}

	public int numEdges() {
		return edges.size();
	}

	public Iterable<Edge<V,E>> edges() {
		return edges.elements();
	}

	public boolean isDirected() {
		return isDirected;
	}
	
	public Edge<V,E> getEdge(Vertex<V> u, Vertex<V> v) {
		if (isDirected()) {
			for (Edge<V,E> edge: edges())
				if (edge.alpha() == u && edge.beta() == v)
					return edge;
		}
		else {
			for (Edge<V,E> edge: edges())
				if (edge.alpha() == u && edge.beta() == v || edge.alpha() == v && edge.beta() == u)
					return edge;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Vertex<V>[] endVertices(Edge<V,E> e) {
		Vertex<V>[] endVertices = (Vertex<V>[]) new Vertex[2];
		endVertices[0] = e.alpha();
		endVertices[1] = e.beta();
		return endVertices;
	}

	public Vertex<V> opposite(Vertex<V> v, Edge<V,E> e) throws IllegalArgumentException {
		if (e.alpha() != v && e.beta() != v)
			throw new IllegalArgumentException("Edge e is not incident to Vertex V in the graph.");
		else if (e.alpha() == v)
			return e.beta();
		else return e.alpha();
	}

	public int outDegree(Vertex<V> v) {
		int outDegree = 0;
		if (isDirected()) {
			for (Edge<V,E> edge: edges())
				if (edge.alpha() == v)
					++outDegree;
		}
		else {
			for (Edge<V,E> edge: edges())
				if (edge.alpha() == v || edge.beta() == v)
					++outDegree;
		}
		return outDegree;
	}

	public int inDegree(Vertex<V> v) {
		int inDegree = 0;
		if (isDirected()) {
			for (Edge<V,E> edge: edges())
				if (edge.beta() == v)
					++inDegree;
		}
		else {
			for (Edge<V,E> edge: edges())
				if (edge.alpha() == v || edge.beta() == v)
					++inDegree;
		}
		return inDegree;
	}

	public Iterable<Edge<V,E>> outgoingEdges(Vertex<V> v) {
		LinkedPositionalList<Edge<V,E>> outgoingEdges = new LinkedPositionalList<>();
		if (isDirected()) {
			for (Edge<V,E> edge: edges())
				if (edge.alpha() == v)
					outgoingEdges.append(edge);
		}
		else {
			for (Edge<V,E> edge: edges())
				if (edge.alpha() == v || edge.beta() == v)
					outgoingEdges.append(edge);
		}
		return outgoingEdges.elements();
	}

	public Iterable<Edge<V,E>> incomingEdges(Vertex<V> v) {
		LinkedPositionalList<Edge<V,E>> incomingEdges = new LinkedPositionalList<>();
		if (isDirected()) {
			for (Edge<V,E> edge: edges())
				if (edge.beta() == v)
					incomingEdges.append(edge);
		}
		else {
			for (Edge<V,E> edge: edges())
				if (edge.alpha() == v || edge.beta() == v)
					incomingEdges.append(edge);
		}
		return edges.elements();
	}

	public Vertex<V> insertVertex(V x) {
		Vertex<V> nullVertex = null;
		vertices.append(nullVertex);
		vertices.set(vertices.last(), new EdgeListVertex<>(x, vertices.last()));
		return vertices.last().data();
	}

	public Edge<V,E> insertEdge(Vertex<V> u, Vertex<V> v, E x) throws IllegalArgumentException {
		if (isDirected()) {
			for (Edge<V,E> edge: edges.elements())
				if (edge.alpha() == u && edge.beta() == v)
					throw new IllegalArgumentException("An edge already exists from u to v in the directed graph.");
		}
		else {
			for (Edge<V,E> edge: edges.elements())
				if (edge.alpha() == u && edge.beta() == v || edge.alpha() == v && edge.beta() == u)
					throw new IllegalArgumentException("An edge already exists between u and v in the undirected graph.");
		}
		Edge<V,E> nullEdge = null;
		edges.append(nullEdge);
		edges.set(edges.last(), new EdgeListEdge<>(x, u, v, edges.last()));
		return edges.last().data();
	}

	public void removeVertex(Vertex<V> v) {
		for (Edge<V,E> edge: edges())
			if (edge.alpha() == v || edge.beta() == v)
				edges.remove(((EdgeListEdge<V,E>)edge).position());
		vertices.remove(((EdgeListVertex<V>)v).position());
	}

	public void removeEdge(Edge<V,E> e) throws ClassCastException {
		edges.remove(((EdgeListEdge<V,E>)e).position());
	}
	
	private LinkedPositionalList<Vertex<V>> vertices;
	private LinkedPositionalList<Edge<V,E>> edges;
	private final boolean isDirected;
}
