package setsmaps;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class BinarySearchTreeSet<E> extends AbstractSet<E> implements TreeSet<E> {
	// BEGINNING OF THE NESTED TREE NODE CLASS
	protected static class TN<E> {
		TN(E element) {this.element = element;}
		TN(E element, TN<E> left, TN<E> right) {
			this.element = element;
			this.left = left;
			this.right = right;
		}
		
		E element() {return element;}
		TN<E> left() {return left;}
		TN<E> right() {return right;}
		TN<E> parent() {return parent;}
		boolean hasLeft() {return left != null;}
		boolean hasRight() {return right != null;}
		int aux() {return aux;}
		int numChildren() {return hasLeft() ? hasRight() ? 2 : 1 : hasRight() ? 1 : 0;}
		
		void setElement(E element) {
			this.element = element;
		}
		
		void setLeft(TN<E> newLeft) {
			left = newLeft;
			if (newLeft != null)
				newLeft.parent = this;
		}
		
		void setRight(TN<E> newRight) {
			right = newRight;
			if (newRight != null)
				newRight.parent = this;
		}
		
		E element;
		TN<E> left, right, parent;
		short aux;
	}
	
	// MEMBERS VISIBLE TO THE WORLD
	@SuppressWarnings("unchecked")
	public BinarySearchTreeSet() throws ClassCastException {
		this((a,b) -> ((Comparable<E>)a).compareTo(b));
	}
	
	public BinarySearchTreeSet(Comparator<? super E> comparator) {
		this.comparator = comparator;
	}
	
	/** A convenience for testing. */
	@SuppressWarnings("unchecked")
	private void add(E... toInsert) {
		for (E element: toInsert)
			add(element);
	}
	
	public boolean add(E toInsert) throws NullPointerException {
		if (isEmpty()) {
			setRoot(new TN<>(toInsert));
			rebalanceInsertion(root());
			++size;
			return true;
		}
		
		TN<E> search = find(toInsert);
		if (search.element().equals(toInsert)) {
			rebalanceAccess(search);
			return false;
		}
		else {
			++size;
			if (comparator.compare(toInsert, search.element()) < 0) {
				search.setLeft(new TN<>(toInsert));
				rebalanceInsertion(search.left());
			}
			else {
				search.setRight(new TN<>(toInsert));
				rebalanceInsertion(search.right());
			}
			return true;
		}
	}

	public void clear() {
		setRoot(null);
		size = 0;
	}

	public boolean contains(E element) throws NullPointerException {
		TN<E> search = find(element);
		return search != null && search.element().equals(element);
	}

	public boolean remove(E toRemove) throws NullPointerException {
		if (toRemove == null)
			throw new NullPointerException("Null elements not allowed.");
		else if (isEmpty())
			return false;
		
		TN<E> search = find(toRemove);
		if (!search.element().equals(toRemove))
			return false;
		else {
			TN<E> parentOfSearch = search.parent();
			switch (search.numChildren()) {
			case 0:
				if (search == root()) {
					setRoot(null);
				}
				else {
					if (search == parentOfSearch.left())
						parentOfSearch.setLeft(null);
					else
						parentOfSearch.setRight(null);
					rebalanceDeletion(parentOfSearch);
				}
				break;
			
			case 1:
				if (search == root()) {
					if (search.hasLeft())
						setRoot(search.left());
					else
						setRoot(search.right());
					root.parent = null;
					rebalanceDeletion(parentOfSearch);
				}
				else {
					if (search == parentOfSearch.left())
						parentOfSearch.setLeft(search.hasLeft() ? search.left() : search.right());
					else
						parentOfSearch.setRight(search.hasRight() ? search.right() : search.left());
					rebalanceDeletion(parentOfSearch);
				}
				break;
			
			case 2:
				TN<E> replacement = new Random().nextBoolean() ? 
						largest(search.left()) : smallest(search.right());
				search.setElement(replacement.element());
				TN<E> parentOfReplacement = replacement.parent();
				if (replacement == parentOfReplacement.left())
					parentOfReplacement.setLeft(replacement.right());
				else
					parentOfReplacement.setRight(replacement.left());
				rebalanceDeletion(parentOfReplacement);
				break;
			}
			
			--size;
			return true;
		}
	}

	public void retainAll(Collection<? extends E> collection) throws NullPointerException {
		for (E element: this)
			if (!collection.contains(element))
				remove(element);
	}

	public int size() {
		return size;
	}

	public Iterator<E> iterator() {
		return new BST_Iterator();
	}

	public E[] toArray() {
		return new BST_Iterator().snapshot;
	}

	public E first() {
		return isEmpty() ? null : smallest(root()).element();
	}

	public E last() {
		return isEmpty() ? null : largest(root()).element();
	}

	public E pollFirst() {
		if (isEmpty())
			return null;
		E smallest = smallest(root()).element();
		remove(smallest);
		return smallest;
	}

	public E pollLast() {
		if (isEmpty())
			return null;
		E largest = largest(root()).element();
		remove(largest);
		return largest;
	}
	
	public void print() {
		if (isEmpty())
			System.out.println("{}");
		else print(root, new StringBuilder(""));
	}
	
	public void print(TN<E> node, StringBuilder buffer) {
		if (node.hasRight())
			print(node.right(), new StringBuilder(buffer).append("---"));
		System.out.println(buffer.toString() + node.element());
		if (node.hasLeft())
			print(node.left(), new StringBuilder(buffer).append("---"));
	}
	
	// BEGINNING OF MEMBERS HIDDEN FROM CLASSES OUTSIDE THE PACKAGE.
	/** Returns a reference to the node with the given element if it exists
	 * in the tree. Otherwise, returns the parent of that node, or null if
	 * if the tree is empty. */
	TN<E> find(E element) {
		if (isEmpty())
			return root;
		else return find(element, root);
	}
	
	TN<E> find(E key, TN<E> node) {
		int comparison = comparator.compare(key, node.element());
		if (comparison < 0 && node.hasLeft())
			return find(key, node.left());
		else if (comparison > 0 && node.hasRight())
			return find(key, node.right());
		else return node;
	}
	
	/** Returns a reference to the node with the smallest element in the 
	 * subtree rooted at the given root. Assumes non-null input. */
	TN<E> smallest(TN<E> root) {
		TN<E> walk = root;
		while (walk.hasLeft())
			walk = walk.left();
		return walk;
	}
	
	/** Returns a reference to the node in the subtree rooted at the given
	 * root with the greatest element. Assumes non-null input. */
	TN<E> largest(TN<E> root) {
		TN<E> walk = root;
		while (walk.hasRight())
			walk = walk.right();
		return  walk;
	}
	
	TN<E> root() {
		return root;
	}
	
	void setRoot(TN<E> newRoot) {
		this.root = newRoot;
	}
	
	void rebalanceInsertion(TN<E> justInserted) {
		// To be overriden by subclasses.
	}
	
	void rebalanceDeletion(TN<E> parentOfDeletedNode) {
		// To be overriden by subclasses.
	}
	
	void rebalanceAccess(TN<E> justAccessed) {
		// To be overriden by subclasses.
	}
	
	class BST_Iterator implements Iterator<E> {
		@SuppressWarnings("unchecked")
		public BST_Iterator() {
			snapshot = (E[]) new Object[size()];
			fillSnapshot(root());
			index = 0;
		}
		
		private void fillSnapshot(TN<E> walk) {
			if (walk.hasLeft())
				fillSnapshot(walk.left());
			snapshot[index++] = walk.element();
			if (walk.hasRight())
				fillSnapshot(walk.right());
		}
		
		public boolean hasNext() {return index < snapshot.length;}
		public E next() {return snapshot[index++];}
		
		private E[] snapshot;
		private int index;
	}
	
	// FIELDS
	TN<E> root;
	Comparator<? super E> comparator;
	int size;
	
	
	public static void main(String[] args) {
		BinarySearchTreeSet<Integer> set = new BinarySearchTreeSet<>();
		set.add(4,2,6,1,3,5,7);
		set.print();
		while (!set.isEmpty())
			System.out.print((new Random().nextBoolean() ? set.pollFirst() : set.pollLast()) + " ");
		System.out.println();
	}
}
