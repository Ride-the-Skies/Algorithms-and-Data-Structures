package graphs;
import lists.*;
import utility.*;

public class AdjacencyListGraph<V,E> implements Graph<V,E> {
	//////////////////////////////////////////////////////
	// Beginning of the inner AdjacencyListVertex class //
	//////////////////////////////////////////////////////
	
	// Instances of this inner class have two LinkedPositionalLists of 
	// AdjacencyListEdges, alpha and beta. In directed graphs, alpha 
	// stores the edges directed from other vertices and towards this 
	// vertex, and beta stores the outgoing edges. In undirected  graphs, 
	// alpha stores all of the edges incident to each vertex.
	
	private class AdjacencyListVertex implements Vertex<V> {
		public AdjacencyListVertex(V element, Position<AdjacencyListVertex> position) {
			this.element = element;
			this.position = position;
			alpha = new LinkedPositionalList<>();
			beta = new LinkedPositionalList<>();
		}

		public V element() {return element;}
		public Position<AdjacencyListVertex> position() {return position;}
		public LinkedPositionalList<AdjacencyListEdge> alpha() {return alpha;}
		public LinkedPositionalList<AdjacencyListEdge> beta() {return beta;}
		public AdjacencyListGraph<V,E> outer() {return AdjacencyListGraph.this;}
		public int hashCode() {return element.hashCode();}
		public boolean equals(Object other) {return other == this;}
		
		private final V element;
		private final Position<AdjacencyListVertex> position;
		private final LinkedPositionalList<AdjacencyListEdge> alpha, beta;
	}
	
	////////////////////////////////////////////////////
	// Beginning of the inner AdjacencyListEdge class //
	////////////////////////////////////////////////////
	
	// An AdjacencyListEdge has member variables for each of its adjacent
	// vertices. If the graph is directed, alpha refers to the incoming 
	// vertex, and beta returns to the outgoing one. If it is undirected,
	// alpha and beta refer to each of the adjacent vertices with no special
	// meaning attached to them.
	
	private class AdjacencyListEdge implements Edge<E> {
		public AdjacencyListEdge(E data, Position<AdjacencyListEdge> position, AdjacencyListVertex alpha, AdjacencyListVertex beta) {
			this.data = data;
			this.position = position;
			this.alpha = alpha;
			this.beta = beta;
		}
		
		public E data() {return data;}
		public void setData(E data) {this.data = data;}
		public Position<AdjacencyListEdge> position() {return position;}
		public AdjacencyListVertex alpha() {return alpha;}
		public AdjacencyListVertex beta() {return beta;}
		public int hashCode() {return data.hashCode();}
		public boolean equals(Object other) {return other == this;}
		
		private E data;
		private final Position<AdjacencyListEdge> position;
		private final AdjacencyListVertex alpha, beta;
	}
	
	/////////////////////////////////////////////////////////////
	// Beginning of the encapsulating AdjacencyListGraph class //
	/////////////////////////////////////////////////////////////
	public AdjacencyListGraph(boolean isDirected) {
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

	public Edge<E> getEdge(Vertex<V> u, Vertex<V> v) throws ClassCastException {
		AdjacencyListVertex left = (AdjacencyListVertex) u;
		AdjacencyListVertex right = (AdjacencyListVertex) v;
		if (isDirected()) {
			if (left.beta().size() <= right.alpha().size()) {
				for (AdjacencyListEdge edge: left.beta().elements()) 
					if (edge.beta() == right) 
						return edge;
			}
			else {
				for (AdjacencyListEdge edge: right.alpha().elements())
					if (edge.alpha() == left)
						return edge;
			}
			
		}
		else {
			if (left.alpha().size() <= right.alpha().size()) {
				for (AdjacencyListEdge edge: left.alpha().elements())
					if (edge.alpha() == v || edge.beta() == right)
						return edge;
			}
			else {
				for (AdjacencyListEdge edge: right.alpha().elements())
					if (edge.alpha() == left || edge.beta() == left)
						return edge;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Vertex<V>[] endVertices(Edge<E> e) throws ClassCastException {
		AdjacencyListEdge edge = (AdjacencyListEdge) e;
		AdjacencyListVertex[] endVertices = (AdjacencyListVertex[]) new Object[2];
		endVertices[0] = edge.alpha();
		endVertices[1] = edge.beta();
		return endVertices;
	}

	public Vertex<V> opposite(Vertex<V> v, Edge<E> e) throws IllegalArgumentException, ClassCastException {
		AdjacencyListEdge edge = (AdjacencyListEdge) e;
		if (edge.alpha() == v)
			return edge.beta();
		else if (edge.beta() == v)
			return edge.alpha();
		else throw new IllegalArgumentException("The edge is not incident to the vertex.");
	}

	public int outDegree(Vertex<V> v) throws ClassCastException {
		AdjacencyListVertex vertex = (AdjacencyListVertex) v;
		return vertex.alpha().size();
	}

	public int inDegree(Vertex<V> v) throws ClassCastException {
		AdjacencyListVertex vertex = (AdjacencyListVertex) v;
		return isDirected() ? vertex.beta().size() : vertex.alpha().size();
	}

	public Iterable<? extends Edge<E>> outgoingEdges(Vertex<V> v) throws ClassCastException {
		AdjacencyListVertex vertex = (AdjacencyListVertex) v;
		return vertex.alpha().elements();
	}

	public Iterable<? extends Edge<E>> incomingEdges(Vertex<V> v) throws ClassCastException {
		AdjacencyListVertex vertex = (AdjacencyListVertex) v;
		return isDirected() ? vertex.beta().elements() : vertex.alpha().elements();
	}

	public Vertex<V> insertVertex(V x) {
		AdjacencyListVertex nullVertex = null;
		vertices.append(nullVertex);
		vertices.set(vertices.last(), new AdjacencyListVertex(x, vertices.last()));
		return vertices.last().data();
	}

	public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E x) throws IllegalArgumentException, ClassCastException {
		AdjacencyListVertex left = (AdjacencyListVertex) u;
		AdjacencyListVertex right = (AdjacencyListVertex) v;
		if (left.outer() != right.outer())
			throw new IllegalArgumentException("The two vertices are not from the same graph.");
		if (areAdjacent(left, right)) {
			if (isDirected())
				throw new IllegalArgumentException("An edge already exists from u to v in the directed graph.");
			else
				throw new IllegalArgumentException("An edge already exists between u and v in the undirected graph.");
		}
		
		AdjacencyListEdge newest, nullEdge = null;
		edges.append(nullEdge);
		newest = edges.set(edges.last(), new AdjacencyListEdge(x, edges.last(), left, right));
		if (isDirected()) {
			left.beta().append(newest);
			right.alpha().append(newest);
		}
		else {
			left.alpha().append(newest);
			if (left != right)	// If this edge is not a cycle.
				right.alpha().append(newest);
		}
		
		return newest;
	}

	
	public void removeVertex(Vertex<V> v) throws ClassCastException {
		AdjacencyListVertex vertex = (AdjacencyListVertex) v;
		vertices.remove(vertex.position());
		if (isDirected()) {
			for (AdjacencyListEdge edge: vertex.alpha().elements())
				removeEdge(edge);
			for (AdjacencyListEdge edge: vertex.beta().elements())
				removeEdge(edge);
		}
		else {
			for (AdjacencyListEdge edge: vertex.alpha().elements())
				removeEdge(edge);
		}
	}

	public void removeEdge(Edge<E> e) throws ClassCastException {
		AdjacencyListEdge edge = (AdjacencyListEdge) e;
		edges.remove(edge.position());
	
		if (isDirected()) {
			AdjacencyListVertex from = edge.alpha();
			AdjacencyListVertex to = edge.beta();
			Position<AdjacencyListEdge> fromPosition = null;
			Position<AdjacencyListEdge> toPosition = null;
			for (Position<AdjacencyListEdge> pos: from.beta())
				if (pos.data() == edge)
					fromPosition = pos;
			for (Position<AdjacencyListEdge> pos: to.alpha())
				if (pos.data() == edge)
					toPosition = pos;
			from.beta().remove(fromPosition);
			to.alpha().remove(toPosition);
		}
		
		else {
			AdjacencyListVertex left = edge.alpha();
			AdjacencyListVertex right = edge.beta();
			Position<AdjacencyListEdge> leftPosition = null;
			Position<AdjacencyListEdge> rightPosition = null;
			for (Position<AdjacencyListEdge> pos: left.alpha())
				if (pos.data() == edge)
					leftPosition = pos;
			for (Position<AdjacencyListEdge> pos: right.alpha())
				if (pos.data() == edge)
					rightPosition = pos;
			left.alpha().remove(leftPosition);
			right.alpha().remove(rightPosition);
		}
	}
	
	public String toString() {
		if (vertices.isEmpty())
			return "{Ø}\n";
		StringBuilder sb = new StringBuilder(isDirected() ? "Directed Graph:\n" : "Undirected Graph:\n");
		if (isDirected()) {
			for (AdjacencyListVertex vertex: vertices.elements()) {
				sb.append(vertex.element().toString());
				if (vertex.beta().size() > 0) {
					sb.append(" -> {");
					for (AdjacencyListEdge outgoing: vertex.beta().elements())
						sb.append(outgoing.beta().element().toString() + ", ");
					sb.delete(sb.length() - 2, sb.length());
					sb.append("}");
				}
				sb.append("\n");
			}
		}
		else {
			for (AdjacencyListVertex vertex: vertices.elements()) {
				sb.append(vertex.element().toString());
				if (vertex.alpha().size() > 0) {
					sb.append(" -> {");
					for (AdjacencyListEdge incident: vertex.alpha().elements()) {
						if (incident.alpha() == vertex)
							sb.append(incident.beta().element().toString() + ", ");
						else
							sb.append(incident.alpha().element().toString() + ", ");
					}
					sb.delete(sb.length() - 2, sb.length());
					sb.append("}");
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	private boolean areAdjacent(AdjacencyListVertex u, AdjacencyListVertex v) {
		if (isDirected()) {
			if (u.beta().size() <= v.alpha().size()) {
				for (AdjacencyListEdge edge: u.beta().elements()) 
					if (edge.beta() == v) 
						return true;
			}
			else {
				for (AdjacencyListEdge edge: v.alpha().elements())
					if (edge.alpha() == u)
						return true;
			}
			
		}
		else {
			if (u.alpha().size() <= v.alpha().size()) {
				for (AdjacencyListEdge edge: u.alpha().elements())
					if (edge.alpha() == v || edge.beta() == v)
						return true;
			}
			else {
				for (AdjacencyListEdge edge: v.alpha().elements())
					if (edge.alpha() == u || edge.beta() == u)
						return true;
			}
		}
		return false;
	}
	
	private LinkedPositionalList<AdjacencyListVertex> vertices;
	private LinkedPositionalList<AdjacencyListEdge> edges;
	private final boolean isDirected;
	
	
	
	public static void main(String[] args) {
		Graph<String,String> g = new AdjacencyListGraph<>(true);
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
		
		g.removeVertex(fifteen);
		System.out.println("\n" + g.toString());
		System.out.println("Vertices: "+g.numVertices());
		System.out.println("Edges: "+g.numEdges());
	}
}
