package setsmaps;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import lists.LinkedList;

public class BinarySearchTreeSet<E> extends AbstractSet<E> implements TreeSet<E> {
	// BEGINNING OF THE STATIC NESTED TREE NODE CLASS
	static class TN<E> {
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
		short aux() {return aux;}
		int numChildren() {return hasLeft() ? hasRight() ? 2 : 1 : hasRight() ? 1 : 0;}
		
		Iterable<TN<E>> children() {
			LinkedList<TN<E>> children = new LinkedList<>();
			if (hasLeft())
				children.append(left());
			if (hasRight())
				children.append(right());
			return children;
		}
		
		void setElement(E element) {
			this.element = element;
		}
		
		void setAux(short aux) {
			this.aux = aux;
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
	// END OF THE STATIC NESTED TREE NODE CLASS
	
	
	/** Creates an empty Binary Search Tree Set with the default comparator. */
	@SuppressWarnings("unchecked")
	public BinarySearchTreeSet() throws ClassCastException {
		this((a,b) -> ((Comparable<E>)a).compareTo(b));
	}
	
	/** Creates an empty Binary Search Tree Set with the specified comparator. */
	public BinarySearchTreeSet(Comparator<? super E> comparator) {
		this.comparator = comparator;
	}
	
	/** A convenience for testing. */
	@SuppressWarnings("unchecked")
	void add(E... toInsert) {
		for (E element: toInsert)
			add(element);
	}
	
	public boolean add(E toInsert) throws NullPointerException {
		if (toInsert == null)
			throw new NullPointerException("Null elements not allowed.");
		
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
		if (search != null) {
			rebalanceAccess(search);
			return search.element().equals(element);
		}
		else return false;
	}

	public boolean remove(E toRemove) throws NullPointerException {
		if (toRemove == null)
			throw new NullPointerException("Null elements not allowed.");
		
		TN<E> search = find(toRemove);
		if (search == null || !search.element().equals(toRemove))
			return false;
		else {
			if (!search.hasLeft() && !search.hasRight())
				removeElementInNodeWithNoChildren(search);
			else if (search.hasLeft() || search.hasRight())
				removeElementInNodeWithOneChild(search);
			else 
				removeElementInNodeWithTwoChildren(search);
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
		root = newRoot;
		if (root != null)
			root.parent = null;
	}
	
	private void removeElementInNodeWithNoChildren(TN<E> nodeWithNoChildren) {
		TN<E> parent = nodeWithNoChildren.parent();
		if (nodeWithNoChildren == root())
			setRoot(null);
		else {
			if (nodeWithNoChildren == parent.left())
				parent.setLeft(null);
			else
				parent.setRight(null);
			rebalanceDeletion(parent);
		}
	}
	
	private void removeElementInNodeWithOneChild(TN<E> nodeWithOneChild) {
		TN<E> parent = nodeWithOneChild.parent();
		if (nodeWithOneChild == root()) {
			setRoot(nodeWithOneChild.hasLeft() ? nodeWithOneChild.left() : nodeWithOneChild.right());
			root.parent = null;
		}
		else {
			if (nodeWithOneChild == parent.left())
				parent.setLeft(nodeWithOneChild.hasLeft() ? nodeWithOneChild.left() : nodeWithOneChild.right()); // Can't have both.
			else
				parent.setRight(nodeWithOneChild.hasRight() ? nodeWithOneChild.right() : nodeWithOneChild.left());
			rebalanceDeletion(parent);
		}
	}
	
	private void removeElementInNodeWithTwoChildren(TN<E> nodeWithTwoChildren) {
		TN<E> replacement = new Random().nextBoolean() ? largest(nodeWithTwoChildren.left()) : smallest(nodeWithTwoChildren.right());
		nodeWithTwoChildren.setElement(replacement.element());
		TN<E> parentOfReplacement = replacement.parent();
		if (replacement == parentOfReplacement.left())			// Replacement can have at most one child, and the setLeft and
			parentOfReplacement.setLeft(replacement.right());	// setRight methods will do the right thing even if the replacement
		else													// has no children.
			parentOfReplacement.setRight(replacement.left());
		rebalanceDeletion(parentOfReplacement);
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
	
	// METHODS FOR SELF BALANCING SUBCLASSES
	                                        
	/*        B             A                                
	 *       / \     ->    / \               
	 *      A   γ         α   B                                
	 *     / \               / \                         
	 *    α   β             β   γ                         
	 */
	void rotateRight(TN<E> A, TN<E> B) throws IllegalArgumentException {
		if (A != B.left())
			throw new IllegalArgumentException("A is not a left child of B.");
		if (B == root())
			setRoot(A);
		else
			if (B == B.parent().left())
				B.parent().setLeft(A);
			else
				B.parent().setRight(A);
		B.setLeft(A.right());
		A.setRight(B);
	}
	                                        
	/*     A                  B             
	 *    / \       ->       / \ 
	 *   α   B              A   γ             
	 *      / \            / \      
	 *     β   γ          α   β             
	 */
	void rotateLeft(TN<E> A, TN<E> B) throws IllegalArgumentException {
		if (B != A.right())
			throw new IllegalArgumentException("B is not a right child of A.");
		if (A == root())
			setRoot(B);
		else
			if (A == A.parent().left())
				A.parent().setLeft(B);
			else
				A.parent().setRight(B);
		A.setRight(B.left());
		B.setLeft(A);
	}
	
	/*      z                          y  
	 *     / \           ->          /   \
	 *    t1  y                     z     x
	 *       / \                   / \   / \                                              
	 *      t2  x                 t1 t2 t3 t4   
	 *         / \                    
	 *        t3 t4                
	 *                                 
	 *     
	 *          z                      y  
	 *         / \       ->          /   \      
	 *        y  t4                 x     z                        
	 *       / \                   / \   / \                            
	 *      x  t3                 t1 t2 t3 t4                           
	 *     / \                                                         
	 *    t1 t2                                                     
	 *                                                             
	 *                                                                
	 *      z                          x                               
	 *     / \           ->          /   \               
	 *    t1  y                     z     y              
	 *       / \                   / \   / \                  
	 *      x  t4                 t1 t2 t3 t4                      
	 *     / \                                                   
	 *    t2 t3                                                   
	 *                                                          
	 *        z                        x                     
	 *       / \        ->           /   \                      
	 *      y  t4                   y     z                     
	 *     / \                     / \   / \                    
	 *    t1  x                   t1 t2 t3 t4                        
	 *       / \                                                 
	 *      t2 t3                                                                                                                         
	 */
	void restructureTrinode(TN<E> x, TN<E> y, TN<E> z) throws IllegalArgumentException {
		if (y == z.right() && x == y.right())	// First drawing.
			rotateLeft(z,y);
		else if (y == z.left() && x == y.left())	// Second drawing.
			rotateRight(y,z);
		else if (y == z.right() && x == y.left()) {		// Third.
			rotateRight(x,y);
			rotateLeft(z,x);
		}
		else if (y == z.left() && x == y.right()) {		// Fourth.
			rotateLeft(y,x);
			rotateRight(x,z);
		}
		else throw new IllegalArgumentException("Either y is not a parent of x, z is not a parent of y, or both.");
	}
	
	void rebalanceInsertion(TN<E> justInserted) {}
	void rebalanceDeletion(TN<E> parentDeleted) {}
	void rebalanceAccess(TN<E> justAccessed) {}
	
	// FIELDS
	TN<E> root;
	Comparator<? super E> comparator;
	int size;
	
	
	public static void main(String[] args) {
		BinarySearchTreeSet<Integer> set = new AVLTreeSet<>();
		set.add(1,2,3,4,5,6,7);
		set.print();
		for (int i = 0; i < 5; ++i)
			set.remove(i);
		System.out.println();
		set.print();
	}
}
