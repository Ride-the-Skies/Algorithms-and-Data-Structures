package searchtrees;
import java.util.Scanner;

@SuppressWarnings("unused")
public class RedBlackTreeSet<E extends Comparable<E>> extends BinarySearchTreeSet<E> {
	/** A nested Red Black Tree Node class. */
	private static class RBN<E> extends TN<E> {
		public RBN() {}
		public RBN(E element, int color) {
			super(element);
			this.color = color;
		}
		
		public int getColor() {return color;}
		public RBN<E> getParent() {return (RBN<E>) super.getParent();}
		public RBN<E> getLeft() {return (RBN<E>) super.getLeft();}
		public RBN<E> getRight() {return (RBN<E>) super.getRight();}
		
		public RBN<E> getSibling() {
			if (getParent() == null)
				return null;
			else if (this == getParent().getLeft())
				return getParent().getRight();
			else return getParent().getLeft();
		}
	
		public boolean hasRedChild() {
			return (hasLeft() && getLeft().getColor() == RED || hasRight() && getRight().getColor() == RED);
		}
		
		public void invertColor() {color = 1 - color;}
		public void setColor(int color) {this.color = color;}
		
		private int color;
	}
	
	public RedBlackTreeSet() {}
	@SafeVarargs
	public RedBlackTreeSet(E... elements) {
		for (E element: elements)
			add(element);
	}
	
	public RBN<E> root() {return root;}
	public void setRoot(TN<E> root) {this.root = (RBN<E>) root;}
	public int color(RBN<E> node) {return node == null ? BLACK : node.getColor();}
	
	public boolean add(E element) {
		if (root == null) {
			root = new RBN<>(element, BLACK);	// The root is always black.
			++size;
			return true;
		}
		
		RBN<E> parent = (RBN<E>) find(element);
		if (parent.getElement().equals(element))
			return false;
		else {
			if (element.compareTo(parent.getElement()) < 0) {
				parent.setLeft(new RBN<>(element, RED));	// Newly inserted elements are always red.
				adjustPathAfterInsertion(parent.getLeft());
			}
			else {
				parent.setRight(new RBN<>(element, RED));
				adjustPathAfterInsertion(parent.getRight());
			}
			++size;
			return true;
		}
	}
	
	
	public boolean remove(E element) {
		RBN<E> search = (RBN<E>) find(element);
		if (search == null || !search.getElement().equals(element))
			return false;
		
		switch (search.numChildren()) {
		case 0:
			if (size() == 1)
				root = null;
			else {
				RBN<E> parent = search.getParent();
				if (search.getElement().compareTo(parent.getElement()) < 0) 
					parent.left = null;
				else 
					parent.right = null;
				search.parent = null;
				
				if (search.getColor() == BLACK)
					if (search.getElement().compareTo(parent.getElement()) < 0)  {
						parent.setLeft(new RBN<>(null, DOUBLE_BLACK));
						adjustDoubleBlack(parent.getLeft());
					}
					else {
						parent.setRight(new RBN<>(null, DOUBLE_BLACK));
						adjustDoubleBlack(parent.getRight());
					}
			}
			break;
			
		case 1:
			RBN<E> child = search.hasLeft() ? search.getLeft() : search.getRight();
			RBN<E> parent = search.getParent();
			if (search == root) {
				child.parent = root.left = root.right = null;
				root = child;
				root.setColor(BLACK);
			}	
			else {
				if (search.getElement().compareTo(parent.getElement()) < 0)
					parent.setLeft(child);
				else
					parent.setRight(child);
				search.parent = search.left = search.right = null;
				if (search.getColor() == BLACK) {
					if (child.getColor() == RED)
						child.setColor(BLACK);
					else {
						child.setColor(DOUBLE_BLACK);
						adjustDoubleBlack(child);
					}
				}
			}
			break;
			
		case 2:
			RBN<E> neighbor = (RBN<E>) (random.nextBoolean() ? maxNode(search.getLeft()) : minNode(search.getRight()));
			parent = neighbor.getParent();
			boolean parentLessThanNeighbor = parent.getElement().compareTo(neighbor.getElement()) < 0;
			search.setElement(neighbor.getElement());
			if (neighbor.numChildren() == 0) {
				if (parent.getLeft() == neighbor)
					parent.left = null;
				else
					parent.right = null;
				neighbor.parent = null;
				if (neighbor.getColor() == BLACK) {
					if (parentLessThanNeighbor) {
						parent.setRight(new RBN<>(null, DOUBLE_BLACK));
						adjustDoubleBlack(parent.getRight());
					}
					else {
						parent.setLeft(new RBN<>(null, DOUBLE_BLACK));
						adjustDoubleBlack(parent.getLeft());
					}
				}
			}
			else {
				RBN<E> orphan = neighbor.hasLeft() ? neighbor.getLeft() : neighbor.getRight();
				if (parent.getElement().compareTo(orphan.getElement()) > 0)
					parent.setLeft(orphan);
				else
					parent.setRight(orphan);
				neighbor.parent = neighbor.left = neighbor.right = null;
				if (neighbor.getColor() == BLACK) {
					if (orphan.getColor() == RED)
						orphan.setColor(BLACK);
					else {
						orphan.setColor(DOUBLE_BLACK);
						adjustDoubleBlack(orphan);
					}
				}
			}
		}
		
		--size;
		return true;
	}
	
	// Methods for restoring red-black tree properties after insertion.
	public void adjustPathAfterInsertion(RBN<E> inserted) {
		RBN<E> x = inserted;
		RBN<E> y = inserted.getParent();
		while (y != null && y != root) {
			if (x.getColor() == RED && y.getColor() == RED) {
				if (color(y.getSibling()) == BLACK) {
					x = restructureAfterInsertion(x);
					y = x.getParent();
					continue;
				}
				else {
					recolorAfterInsertion(y.getParent());
				}
			}
			x = x.getParent();
			y = y.getParent();
		}
	}
	
	public RBN<E> restructureAfterInsertion(RBN<E> x) {
		RBN<E> y = x.getParent();
		RBN<E> z = y.getParent();
		restructureTrinode(x, y, z);
		z.invertColor();
		if (y == z.getParent()) {
			y.invertColor();
			return y;
		}
		else {
			x.invertColor();
			return x;
		}
	}
	
	public void recolorAfterInsertion(RBN<E> parent) {
		if (parent != root())
			parent.invertColor();
		parent.getLeft().invertColor();
		parent.getRight().invertColor();
	}
	
	
	// Methods for restoring red-black tree properties after deletion.
	private void adjustDoubleBlack(RBN<E> doubleBlack) {
		if (doubleBlack == root) {
			root.setColor(BLACK);
			return;
		}
		RBN<E> current = doubleBlack;
		RBN<E> sibling = current.getSibling();
		if (color(sibling) == BLACK) {
			if (sibling != null && (color(sibling.getLeft()) == RED || color(sibling.getRight()) == RED)) {	// Case One. Always terminal.
				fixDeletionCaseOne(doubleBlack);
			}
			else {	// Case Two. Terminal if the parent of p is red.
				RBN<E> parent = doubleBlack.getParent();
				fixDeletionCaseTwo(doubleBlack);
				if (parent.getColor() == DOUBLE_BLACK)
					adjustDoubleBlack(parent);
			}
		}
		else {	// Case Three. Leads to case one or case two, both of which terminal after case three is applied.
			fixDeletionCaseThree(doubleBlack);
			adjustDoubleBlack(doubleBlack);
		}
	}
	
	private void fixDeletionCaseOne(RBN<E> doubleBlack) {
		RBN<E> sibling = doubleBlack.getSibling();
		RBN<E> red = color(sibling.getLeft()) == RED ? sibling.getLeft() : sibling.getRight();
		RBN<E> x = red;
		RBN<E> y = red.getParent();
		RBN<E> z = red.getParent().getParent();
		restructureTrinode(x, y, z);
		if (x == y.getParent()) {
			x.setColor(z.getColor());
			y.setColor(BLACK);
			z.setColor(BLACK);
		}
		else {
			y.setColor(z.getColor());
			x.setColor(BLACK);
			z.setColor(BLACK);
		}
		
		if (doubleBlack.getElement() == null) {
			if (doubleBlack == doubleBlack.getParent().getLeft())
				doubleBlack.getParent().left = null;
			else
				doubleBlack.getParent().right = null;
			doubleBlack.parent = null;
		}
		else {
			doubleBlack.setColor(BLACK);
		}
	}
	
	private void fixDeletionCaseTwo(RBN<E> doubleBlack) {
		RBN<E> sibling = doubleBlack.getSibling();
		RBN<E> parent = doubleBlack.getParent();
		sibling.setColor(RED);
		parent.setColor(parent.getColor()+1);
		
		if (doubleBlack.getElement() == null) {
			if (doubleBlack == doubleBlack.getParent().getLeft())
				doubleBlack.getParent().left = null;
			else
				doubleBlack.getParent().right = null;
			doubleBlack.parent = null;
		}
		else {
			doubleBlack.setColor(BLACK);
		}
	}
	
	private void fixDeletionCaseThree(RBN<E> doubleBlack) {
		RBN<E> sibling = doubleBlack.getSibling();
		RBN<E> parent = sibling.getParent();
		sibling.setColor(BLACK);
		parent.setColor(RED);
		if (sibling == parent.getLeft())
			rotateRight(sibling, parent);
		else
			rotateLeft(sibling, parent);
	}
	
	public void print() {
		if (!isEmpty())
			print(root,"");
	}
	private void print(RBN<E> node, String buffer) {
		if (node.hasRight())
			print(node.getRight(), buffer + "----");
		System.out.println(buffer + node.getElement().toString() + (node.getColor() == RED ? "r" : "b"));
		if (node.hasLeft())
			print(node.getLeft(), buffer + "----");
	}
	
	
	private RBN<E> root;
	public static final int RED = 0;
	public static final int BLACK = 1;
	public static final int DOUBLE_BLACK = 2;
	
	public static void main(String[] args) {
		// RED BLACK TREE DEMO
		RedBlackTreeSet<Integer> set = new RedBlackTreeSet<>();
		System.out.println("RED BLACK TREE DEMO");
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
