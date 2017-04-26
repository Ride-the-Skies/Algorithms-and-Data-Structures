package graphs;
import lists.*;
import utility.*;

public class EdgeListGraph<V,E> implements Graph<V,E> {
	//////////////////////////////////////////////////
	// Beginning of the inner EdgeListVertex class. //
	//////////////////////////////////////////////////
	private class EdgeListVertex implements Vertex<V> {
		public EdgeListVertex(V element, Position<EdgeListVertex> position) {
			this.element = element;
			this.position = position;
		}
		
		public V element() {return element;}
		public Position<EdgeListVertex> position() {return position;}
		public EdgeListGraph<V,E> outer() {return EdgeListGraph.this;}
		public int hashCode() {return element.hashCode();}
		public boolean equals(Object other) {return other == this;}
		
		private final V element;
		private final Position<EdgeListVertex> position;
	}
	
	////////////////////////////////////////////////
	// Beginning of the inner EdgeListEdge class. //
	////////////////////////////////////////////////
	
	// An EdgeListEdge has member variables for each of its adjacent vertices. If the graph is directed, alpha
	// refers to the incoming vertex, and beta returns to the outgoing one. If it is undirected, alpha and beta
	// refer to each of the adjacent vertices with no special meaning attached to them.
	private class EdgeListEdge implements Edge<E> {
		public EdgeListEdge(E data, Position<EdgeListEdge> position, EdgeListVertex alpha, EdgeListVertex beta) {
			this.data = data;
			this.position = position;
			this.alpha = alpha;
			this.beta = beta;
		}
		
		public E data() {return data;}
		public void setData(E data) {this.data = data;}
		public Position<EdgeListEdge> position() {return position;}
		public EdgeListVertex alpha() {return alpha;}
		public EdgeListVertex beta() {return beta;}
		public int hashCode() {return data.hashCode();}
		public boolean equals(Object other) {return other == this;}
		
		private E data;
		private final Position<EdgeListEdge> position;
		private final EdgeListVertex alpha, beta;
	}
	
	/////////////////////////////////////////////////////////
	// Beginning of the encapsulating EdgeListGraph class. //
	/////////////////////////////////////////////////////////
	public EdgeListGraph(boolean isDirected) {
		vertices = new LinkedPositionalList<>();
		edges = new LinkedPositionalList<>();
		this.isDirected = isDirected;
	}
	
	public int numVertices() {
		return vertices.size();
	}

	public Iterable<? extends Vertex<V>> vertices() {
		return vertices.elements();
	}

	public int numEdges() {
		return edges.size();
	}

	public Iterable<? extends Edge<E>> edges() {
		return edges.elements();
	}

	public boolean isDirected() {
		return isDirected;
	}

	public Edge<E> getEdge(Vertex<V> u, Vertex<V> v) {
		if (isDirected()) {
			for (EdgeListEdge edge: edges.elements())
				if (u == edge.alpha() && v == edge.beta())
					return edge;
		}
		else
			for (EdgeListEdge edge: edges.elements())
				if (u == edge.alpha() && v == edge.beta() || u == edge.beta() && v == edge.alpha)
					return edge;
		return null;
	}

	@SuppressWarnings("unchecked")
	public Vertex<V>[] endVertices(Edge<E> e) throws ClassCastException {
		EdgeListVertex[] endVertices = (EdgeListVertex[]) new Object[2];
		endVertices[0] = ((EdgeListEdge)e).alpha();
		endVertices[1] = ((EdgeListEdge)e).beta();
		return endVertices;
	}

	public Vertex<V> opposite(Vertex<V> v, Edge<E> e) throws IllegalArgumentException, ClassCastException {
		EdgeListEdge edge = (EdgeListEdge) e;
		if (v != edge.alpha() && v != edge.beta())
			throw new IllegalArgumentException("Edge is not incident to the vertex.");
		else if (v == edge.alpha() && v != edge.beta())
			return edge.beta();
		else return edge.alpha();
		// Implicitly covered by this multiway if statement if the condition in which the vertex is equal
		// to both edge.alpha() and edge.beta(). This would be a cycle (which is allowed by this graph 
		// implementation), and in this case edge.alpha() is returned.
	}

	public int outDegree(Vertex<V> v) throws ClassCastException, IllegalArgumentException {
		if (((EdgeListVertex)v).outer() != this)
			throw new IllegalArgumentException("The vertex argument is not from the calling graph object.");
		int outDegree = 0;
		if (isDirected()) {
			for (EdgeListEdge edge: edges.elements())
				if (v == edge.alpha())
					++outDegree;
		}
		else {
			for (EdgeListEdge edge: edges.elements())
				if (v == edge.alpha() || v == edge.beta())
					++outDegree;
		}
		return outDegree;
	}

	public int inDegree(Vertex<V> v) throws ClassCastException, IllegalArgumentException {
		if (((EdgeListVertex)v).outer() != this)
			throw new IllegalArgumentException("The vertex argument is not from the calling graph object.");
		int inDegree = 0;
		if (isDirected()) {
			for (EdgeListEdge edge: edges.elements())
				if (v == edge.beta())
					++inDegree;
		}
		else {
			for (EdgeListEdge edge: edges.elements())
				if (v == edge.alpha() || v == edge.beta())
					++inDegree;
		}
		return inDegree;
	}

	public Iterable<? extends Edge<E>> outgoingEdges(Vertex<V> v) throws ClassCastException, IllegalArgumentException {
		if (((EdgeListVertex)v).outer() != this)
			throw new IllegalArgumentException("The vertex argument is not from the calling graph object.");
		LinkedList<EdgeListEdge> outgoing = new LinkedList<>();
		if (isDirected()) {
			for (EdgeListEdge edge: edges.elements())
				if (v == edge.alpha())
					outgoing.append(edge);
		}
		else {
			for (EdgeListEdge edge: edges.elements())
				if (v == edge.alpha() || v == edge.beta())
					outgoing.append(edge);
		}
		return outgoing;
	}

	public Iterable<? extends Edge<E>> incomingEdges(Vertex<V> v) {
		if (((EdgeListVertex)v).outer() != this)
			throw new IllegalArgumentException("The vertex argument is not from the calling graph object.");
		LinkedList<EdgeListEdge> incoming = new LinkedList<>();
		if (isDirected()) {
			for (EdgeListEdge edge: edges.elements())
				if (v == edge.beta())
					incoming.append(edge);
		}
		else {
			for (EdgeListEdge edge: edges.elements())
				if (v == edge.alpha() || v == edge.beta())
					incoming.append(edge);
		}
		return incoming;
	}

	public Vertex<V> insertVertex(V x) {
		EdgeListVertex nullVertex = null;
		vertices.append(nullVertex);
		vertices.set(vertices.last(), new EdgeListVertex(x, vertices.last()));
		return vertices.last().data();
	}

	public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E x) throws IllegalArgumentException, ClassCastException {
		EdgeListVertex alpha = (EdgeListVertex) u;
		EdgeListVertex beta = (EdgeListVertex) v;
		if (alpha.outer() != beta.outer())
			throw new IllegalArgumentException("The two vertices are from different graphs.");
		if (isDirected()) {
			for (EdgeListEdge edge: edges.elements())
				if (u == edge.alpha() && v == edge.beta())
					throw new IllegalArgumentException("There already exists an edge from u to v in the directed graph.");
		}
		else
			for (EdgeListEdge edge: edges.elements())
				if (u == edge.alpha() && v == edge.beta() || u == edge.beta() && v == edge.alpha())
					throw new IllegalArgumentException("There already exists an edge between u and v in the undirected graph.");
		
		EdgeListEdge nullEdge = null;
		edges.append(nullEdge);
		edges.set(edges.last(), new EdgeListEdge(x, edges.last(), alpha, beta));
		return edges.last().data();
	}

	public void removeVertex(Vertex<V> v) throws ClassCastException {
		EdgeListVertex vertex = (EdgeListVertex) v;
		LinkedList<EdgeListEdge> incident = new LinkedList<>();
		for (EdgeListEdge edge: edges.elements())
			if (edge.alpha() == vertex || edge.beta() == vertex)
				incident.append(edge);
		for (EdgeListEdge edge: incident)
			edges.remove(edge.position());
		vertices.remove(vertex.position());
	}

	
	public void removeEdge(Edge<E> e) throws ClassCastException {
		EdgeListEdge edge = (EdgeListEdge) e;
		edges.remove(edge.position());
	}
	
	public String toString() {
		if (vertices.isEmpty())
			return "{Ø}\n";
		StringBuilder output = new StringBuilder(isDirected() ? "Directed Graph:\n" : "Undirected Graph:\n");
		for (EdgeListVertex vertex: vertices.elements()) {
			output.append(vertex.element());
			if (outDegree(vertex) >  0) {
				output.append(" -> {");
				for (Edge<E> outgoing: outgoingEdges(vertex))
					output.append(opposite(vertex, outgoing).element().toString() + ", ");
				output.delete(output.length() - 2, output.length());
				output.append("}");
			}
			output.append("\n");
		}
		return output.toString();
	}

	
	private LinkedPositionalList<EdgeListVertex> vertices;
	private LinkedPositionalList<EdgeListEdge> edges;
	private final boolean isDirected;
	
	public static void main(String[] args) {
		Graph<String,String> g = new EdgeListGraph<>(true);
		Vertex<String> one = g.insertVertex("1");
		Vertex<String> two = g.insertVertex("2");
		Vertex<String> three = g.insertVertex("3");
		Vertex<String> four = g.insertVertex("4");
		Vertex<String> five = g.insertVertex("5");
		Vertex<String> six = g.insertVertex("6");
		Vertex<String> seven = g.insertVertex("7");
		Vertex<String> eight = g.insertVertex("8");
		Vertex<String> nine = g.insertVertex("9");
		Vertex<String> ten = g.insertVertex("10");
		Vertex<String> eleven = g.insertVertex("11");
		Vertex<String> twelve = g.insertVertex("12");
		Vertex<String> thirteen = g.insertVertex("13");
		Vertex<String> fourteen = g.insertVertex("14");
		Vertex<String> fifteen = g.insertVertex("15");
	
		g.insertEdge(one, one, "");
		g.insertEdge(one, three, "");
		g.insertEdge(two, one, "");
		g.insertEdge(three, two, "");
		g.insertEdge(three, five, "");
		g.insertEdge(four, one, "");
		g.insertEdge(four, two, "");
		g.insertEdge(four, twelve, "");
		g.insertEdge(four, thirteen, "");
		g.insertEdge(five, six, "");
		g.insertEdge(five, eight, "");
		g.insertEdge(six, seven, "");
		g.insertEdge(six, eight, "");
		g.insertEdge(six, ten, "");
		g.insertEdge(seven, ten, "");
		g.insertEdge(eight, nine, "");
		g.insertEdge(eight, ten, "");
		g.insertEdge(nine, five, "");
		g.insertEdge(nine, eleven, "");
		g.insertEdge(ten, nine, "");
		g.insertEdge(ten, eleven, "");
		g.insertEdge(ten, fourteen, "");
		g.insertEdge(eleven, twelve, "");
		g.insertEdge(eleven, fourteen, "");
		g.insertEdge(twelve, thirteen, "");
		g.insertEdge(thirteen, eleven, "");
		g.insertEdge(thirteen, fifteen, "");
		g.insertEdge(fourteen, thirteen, "");
		g.insertEdge(fifteen, fourteen, "");
		
		System.out.println(g.toString());
		System.out.println("Vertices: "+g.numVertices());
		System.out.println("Edges: "+g.numEdges());
		
		g.removeVertex(nine);
		System.out.println("\n" + g.toString());
		System.out.println("Vertices: "+g.numVertices());
		System.out.println("Edges: "+g.numEdges());
	}
}
