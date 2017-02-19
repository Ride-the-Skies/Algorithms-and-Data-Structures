package searchtrees;
import java.util.Scanner;

public class SplayTreeSet<E extends Comparable<E>> extends BinarySearchTreeSet<E> {

	public SplayTreeSet() {}
	@SafeVarargs
	public SplayTreeSet(E... elementList) {
		for (E element: elementList)
			add(element);
	}
	
	public TN<E> root() {return root;}
	public void setRoot(TN<E> root) {this.root = root;}
	
	public boolean contains(E element) {
		TN<E> search = find(element);
		if (search != null) {
			splay(search);
			return element.equals(search.getElement());
		}
		else return false;
	}
	
	public E first() {
		if (isEmpty())
			return null;
		TN<E> first = minNode(root);
		splay(first);
		return first.getElement();
	}
	
	public E last() {
		if (isEmpty())
			return null;
		TN<E> last = maxNode(root);
		splay(last);
		return last.getElement();
	}
	
	@SuppressWarnings("unchecked")
	public E kthSmallest(int k) throws IllegalArgumentException {
		if (size() < k)
			throw new IllegalArgumentException("Size of the tree is less than "+k+".");
		if (k <= 0)
			throw new IllegalArgumentException("K must be positive.");
		
		TN<E>[] answer = (TN<E>[]) new TN[1];
		findKthSmallest(root(), k, new Counter(), answer);
		splay(answer[0]);
		return answer[0].getElement();
	}
	
	@SuppressWarnings("unchecked")
	public E kthLargest(int k) throws IllegalArgumentException {
		if (size() < k)
			throw new IllegalArgumentException("Size of the tree is less than "+k+".");
		if (k <= 0)
			throw new IllegalArgumentException("K must be positive");
		
		TN<E>[] answer = (TN<E>[]) new TN[1];
		findKthLargest(root(), k, new Counter(), answer);
		splay(answer[0]);
		return answer[0].getElement();
	}
	
	public boolean add(E element) {
		if (size == 0) {
			root = new TN<>(element);
			++size;
			return true;
		}
		
		TN<E> parent = find(element);
		if (parent.getElement().equals(element)) {
			splay(parent);
			return false;
		}
		else {
			if (element.compareTo(parent.getElement()) < 0) {
				parent.setLeft(new TN<>(element));
				splay(parent.getLeft());
			}
			else {
				parent.setRight(new TN<>(element));
				splay(parent.getRight());
			}
			++size;
			return true;
		}
	}
	
	public boolean remove(E element) {
		TN<E> search = find(element);
		if (search == null)
			return false;
		if (!search.getElement().equals(element)) {
			splay(search);
			return false;
		}
		
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
				splay(parent);
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
				splay(parent);
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
			splay(parent);
		}
		
		--size;
		return true;
	}
	
	private void splay(TN<E> node) {
		TN<E> x, y, z;
		while (node != root) {
			x = node;
			y = node.getParent();
			z = y.getParent();
			if (z == null) {	// Zig.
				if (x == y.getRight())
					rotateLeft(x,y);
				else
					rotateRight(x,y);
			}
			else {
				if (y == z.getRight() && x == y.getRight()) {	// Zig-zig.
					rotateLeft(y,z);
					rotateLeft(x,y);
				}
				else if (y == z.getLeft() && x == y.getLeft()) {	// Zig-zig.
					rotateRight(y,z);
					rotateRight(x,y);
				}
				else if (y == z.getRight() && x == y.getLeft()) {	// Zig-zag.
					rotateRight(x,y);
					rotateLeft(x,z);
				}
				else {		// Zig-zag.
					rotateLeft(x,y);
					rotateRight(x,z);
				}
			}
			node = x;
		}
	}
	
	private TN<E> root;
	
	public static void main(String[] args) {		
		// SPLAY TREE DEMO
		BinarySearchTreeSet<Integer> set = new SplayTreeSet<>();
		System.out.println("SPLAY TREE DEMO");
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
