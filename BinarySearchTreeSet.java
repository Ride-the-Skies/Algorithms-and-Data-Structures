package searchtrees;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class BinarySearchTreeSet<E extends Comparable<E>> implements TreeSet<E> {	
	/** A static nested tree node class. */
	protected static class TN<E> {
		public TN(){}
		public TN(E element) {this.element = element;}
		
		public E getElement() {return element;}
		public boolean hasLeft() {return left != null;}
		public boolean hasRight() {return right != null;}
		public TN<E> getParent() {return parent;}
		public TN<E> getLeft() {return left;}
		public TN<E> getRight() {return right;}
		public void setElement(E element) {this.element = element;}
		
		public void setLeft(TN<E> left) {
			this.left = left;
			left.parent = this;
		}
		
		public void setRight(TN<E> right) {
			this.right = right;
			right.parent = this;
		}
		
		public int numChildren() {
			int numChildren = 0;
			if (hasLeft())
				++numChildren;
			if (hasRight())
				++numChildren;
			return numChildren;
		}
		
		private E element;
		protected TN<E> parent = null;
		protected TN<E> left = null;
		protected TN<E> right = null;
	}
	
	public BinarySearchTreeSet() {}
	@SafeVarargs
	public BinarySearchTreeSet(E... elementList) {
		for (E element: elementList)
			add(element);
	}
	
	public int size() {return size;}
	public boolean isEmpty() {return size() == 0;}
	public TN<E> root() {return root;}
	public void setRoot(TN<E> root) {this.root = root;}
	
	public E first() {
		if (isEmpty())
			return null;
		else return minNode(root()).getElement();
	}
	
	public E last() {
		if (isEmpty())
			return null;
		else return maxNode(root()).getElement();
	}
	
	protected TN<E> find(E element) {
		if (root() == null)
			return null;
		else return find(root(), element);
	}
	
	protected TN<E> find(TN<E> node, E key) {
		while(!node.getElement().equals(key)) {
			if (node.hasLeft() && key.compareTo(node.getElement()) < 0)
				node = node.getLeft();
			else if (node.hasRight() && key.compareTo(node.getElement()) > 0)
				node = node.getRight();
			else break;
		}
		return node;
	}
	
	protected TN<E> maxNode(TN<E> subtreeRoot) {
		TN<E> walk = subtreeRoot;
		while (walk.hasRight())
			walk = walk.getRight();
		return walk;
	}
	
	protected TN<E> minNode(TN<E> subtreeRoot) {
		TN<E> walk = subtreeRoot;
		while (walk.hasLeft())
			walk = walk.getLeft();
		return walk;
	}
	
	@SuppressWarnings("unchecked")
	public E kthSmallest(int k) throws IllegalArgumentException {
		if (size() < k)
			throw new IllegalArgumentException("Size of the tree is less than "+k+".");
		if (k <= 0)
			throw new IllegalArgumentException("K must be positive.");
		
		TN<E>[] answer = (TN<E>[]) new TN[1];
		findKthSmallest(root(), k, new Counter(), answer);
		return answer[0].getElement();
	}
	
	protected void findKthSmallest(TN<E> node, int k, Counter c, TN<E>[] answer) {
		if (node.hasLeft())
			findKthSmallest(node.getLeft(), k, c, answer);
		if (c.getValue()+1 == k) {
			answer[0] = node;
			c.increment();
			return;
		}
		else if (c.getValue()+1 > k)
			return;
		else 
			c.increment();
		if (node.hasRight())
			findKthSmallest(node.getRight(), k, c, answer);
	}
	
	@SuppressWarnings("unchecked")
	public E kthLargest(int k) throws IllegalArgumentException {
		if (size() < k)
			throw new IllegalArgumentException("Size of the tree is less than "+k+".");
		if (k <= 0)
			throw new IllegalArgumentException("K must be positive");
		
		TN<E>[] answer = (TN<E>[]) new TN[1];
		findKthLargest(root(), k, new Counter(), answer);
		return answer[0].getElement();
	}
	
	protected void findKthLargest(TN<E> node, int k, Counter c, TN<E>[] answer) {
		if (node.hasRight())
			findKthLargest(node.getRight(), k, c, answer);
		if (c.getValue()+1 == k) {
			answer[0] = node;
			c.increment();
			return;
		}
		else if (c.getValue()+1 > k)
			return;
		else 
			c.increment();
		if (node.hasLeft())
			findKthLargest(node.getLeft(), k, c, answer);
	}
	
	public boolean contains(E element) {
		TN<E> search = find(element);
		return search != null && element.equals(search.getElement());
	}
	
	public boolean add(E element) {
		if (size == 0) {
			root = new TN<>(element);
			++size;
			return true;
		}
		
		TN<E> parent = find(element);
		if (parent.getElement().equals(element))
			return false;
		else {
			if (element.compareTo(parent.getElement()) < 0)
				parent.setLeft(new TN<>(element));
			else
				parent.setRight(new TN<>(element));
			++size;
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void add(E... elements) {
		for (E element: elements)
			add(element);
	}
	
	public boolean remove(E element) {
		TN<E> search = find(element);
		if (search == null || !search.getElement().equals(element))
			return false;
		
		switch (search.numChildren()) {
		case 0:
			if (size() == 1)
				root = null;
			else {
				TN<E> parent = search.getParent();
				if (search.getElement().compareTo(parent.getElement()) < 0) 
					parent.left = search.parent = null;
				else 
					parent.right = search.parent = null;
			}
			break;
			
		case 1:
			TN<E> child = search.hasLeft() ? search.getLeft() : search.getRight();
			if (search == root) {
				child.parent = root.left = root.right = null;
				root = child;
			}	
			else {
				TN<E> parent = search.getParent();
				if (search.getElement().compareTo(parent.getElement()) < 0)
					parent.setLeft(child);
				else
					parent.setRight(child);
				search.parent = search.left = search.right = null;
			}
			break;
			
		case 2:
			TN<E> neighbor = random.nextBoolean() ? maxNode(search.getLeft()) : minNode(search.getRight());
			TN<E> parent = neighbor.getParent();
			search.setElement(neighbor.getElement());
			if (neighbor.numChildren() == 0) {
				if (parent.getLeft() == neighbor)
					parent.left = null;
				else
					parent.right = null;
				neighbor.parent = null;
			}
			else {
				TN<E> orphan = neighbor.hasLeft() ? neighbor.getLeft() : neighbor.getRight();
				if (parent.getElement().compareTo(orphan.getElement()) > 0)
					parent.setLeft(orphan);
				else
					parent.setRight(orphan);
				neighbor.parent = neighbor.left = neighbor.right = null;
			}
		}
		
		--size;
		return true;
	}
	
	protected void rotateLeft(TN<E> x, TN<E> y) throws IllegalArgumentException {
		if (x != y.getRight())
			throw new IllegalArgumentException("The first argument is not a right child of the second.");
		TN<E> parent = y.getParent();
		TN<E> beta = x.getLeft();
		x.setLeft(y);
		if (parent != null)
			if (parent.getElement().compareTo(y.getElement()) < 0)
				parent.setRight(x);
			else
				parent.setLeft(x);
		else
			x.parent = null;
		if (beta != null)
			y.setRight(beta);
		else
			y.right = null;
		if (y == root())
			setRoot(x);
	}
	
	protected void rotateRight(TN<E> x, TN<E> y) throws IllegalArgumentException {
		if (x != y.getLeft())
			throw new IllegalArgumentException("The first argument is not a left child of the second.");
		TN<E> parent = y.getParent();
		TN<E> beta = x.getRight();
		x.setRight(y);
		if (parent != null)
			if (parent.getElement().compareTo(y.getElement()) < 0)
				parent.setRight(x);
			else
				parent.setLeft(x);
		else
			x.parent = null;
		if (beta != null)
			y.setLeft(beta);
		else
			y.left = null;
		if (y == root())
			setRoot(x);
	}
	
	protected void restructureTrinode(TN<E> x, TN<E> y, TN<E> z) {
		boolean xLessThanY = x.getElement().compareTo(y.getElement()) < 0;
		boolean yLessThanZ = y.getElement().compareTo(z.getElement()) < 0;
		if (!xLessThanY && !yLessThanZ)
			rotateLeft(y,z);
		else if (xLessThanY && yLessThanZ)
			rotateRight(y,z);
		else if (xLessThanY && !yLessThanZ) {
			rotateRight(x,y);
			rotateLeft(x,z);
		}
		else {
			rotateLeft(x,y);
			rotateRight(x,z);
		}
	}

	public Iterable<TN<E>> nodes() {return new NodeIterable();}
	
	private class NodeIterable implements Iterable<TN<E>> {
		public Iterator<TN<E>> iterator() {return new NodeIterator();}
	}
	
	private class NodeIterator implements Iterator<TN<E>> {
		@SuppressWarnings("unchecked")
		public NodeIterator() {
			snapshot = (TN<E>[]) new TN[size];
			if (!isEmpty())
				fillInorder(root(), snapshot);
			index = 0;
		}
		
		private void fillInorder(TN<E> walk, TN<E>[] snapshot) {
			if (walk.hasLeft())
				fillInorder(walk.getLeft(), snapshot);
			snapshot[index++] = walk;
			if (walk.hasRight())
				fillInorder(walk.getRight(), snapshot);
		}
		
		public boolean hasNext() {return index < snapshot.length;}
		public TN<E> next() {return snapshot[index++];}
		public void remove() {throw new UnsupportedOperationException();}
		
		private TN<E>[] snapshot;
		private int index = 0;
	}
	
	public Iterable<E> elements() {return new ElementIterable();}
	
	private class ElementIterable implements Iterable<E> {
		public Iterator<E> iterator() {return new ElementIterator();}
	}
	
	private class ElementIterator implements Iterator<E> {
		public ElementIterator() {it = new NodeIterator();}
		public boolean hasNext() {return it.hasNext();}
		public void remove() {throw new UnsupportedOperationException();}
		public E next() {return it.next().getElement();}
		private NodeIterator it;
	}
	
	public void print() {if (!isEmpty()) print(root(), "");}
	protected void print(TN<E> node, String buffer) {
		if (node.hasRight())
			print(node.getRight(), buffer + "---");
		System.out.println(buffer + node.getElement().toString());
		if (node.hasLeft())
			print(node.getLeft(), buffer + "---");
	}
	
	private TN<E> root;
	protected int size;
	protected Random random = new Random();
	
	public static void main(String[] args) {		
		// SEARCH TREE DEMO
		BinarySearchTreeSet<Integer> set = new BinarySearchTreeSet<>();
		System.out.println("BINARY SEARCH TREE DEMO");
		Scanner scanner = new Scanner(System.in);
		String input;
		do {
			System.out.format("%nA: Add%nR: Remove%nC: Contains%nS: Size%nF: First%nL: Last%n"
					+ "KS: kthSmallest%nKL: kthLargest%nQ: Quit%nEnter a command: ");
			input = scanner.next().toUpperCase();
			if (input.equals("A")) {
				System.out.format("Enter an integer to add: ");
				Integer toAdd = scanner.nextInt();
				set.add(toAdd);
			}
			else if (input.equals("R")) {
				System.out.format("Enter an integer to remove: ");
				Integer toDelete = scanner.nextInt();
				set.remove(toDelete);
			}
			else if (input.equals("C")) {
				System.out.format("Enter an integer to search for: ");
				Integer search = scanner.nextInt();
				System.out.println("The set does "+(set.contains(search) ? "" : "not ")+"contain "+search+".");
			}
			else if (input.equals("S")) {
				System.out.format("The size of the set is currently %d.%n", set.size());
			}
			else if (input.equals("F")) {
				System.out.format("The first value in the set is %d.%n", set.first());
			}
			else if (input.equals("L")) {
				System.out.format("The last value in the set is %d.%n", set.last());
			}
			else if (input.equals("KS")) {
				System.out.format("Enter an integer for k: ");
				Integer k = scanner.nextInt();
				if (k > set.size())
					System.out.format("%d exceeds the size of the set.%n", k);
				else {
					int kthSmallest = set.kthSmallest(k);
					System.out.format("The kth smallest integer of the set for k=%d is %d.%n", k, kthSmallest);
				}
			}
			else if (input.equals("KL")) {
				System.out.format("Enter an integer for k: ");
				Integer k = scanner.nextInt();
				if (k > set.size())
					System.out.format("%d exceeds the size of the set.%n", k);
				else {
					int kthLargest = set.kthLargest(k);
					System.out.format("The kth largest integer of the set for k=%d is %d.%n", k, kthLargest);
				}

			}
			else if (input.equals("Q"))
				continue;
			else {
				System.out.format("Invalid command.%n");
			}
			System.out.println("\nCurrent state:");
			set.print();
		}
		while (!input.equals("Q"));
		scanner.close();
	}
}
