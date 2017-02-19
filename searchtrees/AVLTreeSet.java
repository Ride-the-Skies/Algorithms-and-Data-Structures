package searchtrees;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

@SuppressWarnings("unused")
public class AVLTreeSet<E extends Comparable<E>> extends BinarySearchTreeSet<E> {
	/** A static nested AVL Tree Node class. */
	private static class AVLN<E extends Comparable<E>> extends BinarySearchTreeSet.TN<E> {
		public AVLN(){}
		public AVLN(E element) {super(element);}
		
		public int getHeight() {return height;}
		public AVLN<E> getParent() {return (AVLN<E>) super.getParent();}
		public AVLN<E> getLeft() {return (AVLN<E>) super.getLeft();}
		public AVLN<E> getRight() {return (AVLN<E>) super.getRight();}
		
		public AVLN<E> tallerChild() {
			if (!hasLeft() && !hasRight())
				return null;
			else if (!hasLeft() && hasRight())
				return getRight();
			else if (!hasRight() && hasLeft())
				return getLeft();
			else if (getRight().getHeight() > getLeft().getHeight())
				return getRight();
			else if (getLeft().getHeight() > getRight().getHeight())
				return getLeft();
			else return null;
		}
		
		public int heightDiscrepancy() {
			if (hasLeft() && hasRight())
				return Math.abs(getLeft().getHeight() - getRight().getHeight());
			else if (hasLeft() && !hasRight())
				return getLeft().getHeight() - -1;
			else if (!hasLeft() && hasRight())
				return getRight().getHeight() - -1;
			else
				return 0;	// It has neither a left or a right child, so the height discrepancy is zero.
		}
		
		public void adjustHeight() {
			int maxHeight = 0;
			for (AVLN<E> child: children())
				maxHeight = Math.max(maxHeight, 1+child.getHeight());
			height = maxHeight;
		}
		
		public Iterable<AVLN<E>> children() {
			ArrayList<AVLN<E>> list = new ArrayList<>(2);
			if (hasLeft())
				list.add(getLeft());
			if (hasRight())
				list.add(getRight());
			return list;
		}
		
		private int height = 0;
	}

	public AVLTreeSet() {}
	@SafeVarargs
	public AVLTreeSet(E... elementList) {
		for (E element: elementList)
			add(element);
	}
	
	public AVLN<E> root() {return root;}
	public void setRoot(TN<E> root) {this.root = (AVLN<E>) root;}

	public boolean add(E element) {
		if (size() == 0) {
			root = new AVLN<>(element);
			++size;
			return true;
		}
		
		AVLN<E> parent = (AVLN<E>) find(element);
		if (parent.getElement().equals(element))
			return false;
		else {
			if (element.compareTo(parent.getElement()) < 0) {
				parent.setLeft(new AVLN<>(element));
				adjustPath(parent);
			}
			else {
				parent.setRight(new AVLN<>(element));
				adjustPath(parent);
			}
			++size;
			return true;
		}
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
				AVLN<E> parent = (AVLN<E>) search.getParent();
				if (search.getElement().compareTo(parent.getElement()) < 0) 
					parent.left = search.parent = null;
				else 
					parent.right = search.parent = null;
				adjustPath(parent);
			}
			break;
			
		case 1:
			TN<E> child = search.hasLeft() ? search.getLeft() : search.getRight();
			if (search == root) {
				child.parent = root.left = root.right = null;
				root = (AVLN<E>) child;
				adjustPath(root);
			}	
			else {
				AVLN<E> parent = (AVLN<E>) search.getParent();
				if (search.getElement().compareTo(parent.getElement()) < 0)
					parent.setLeft(child);
				else
					parent.setRight(child);
				adjustPath(parent);
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
			adjustPath((AVLN<E>) parent);
		}
		
		--size;
		return true;
	}
	
	private void adjustPath(AVLN<E> node) {
		AVLN<E> walk = node;
		while (walk != null) {
			walk.adjustHeight();
			if (walk.heightDiscrepancy() >= 2)
				findTrinodeAndRestructure(walk);
			walk = walk.getParent();
		}
	}
	
	private void findTrinodeAndRestructure(AVLN<E> root) {
		AVLN<E> x, y, z;
		z = root;
		y = z.tallerChild();
		if (y.tallerChild() != null)
			x = y.tallerChild();
		else
			if (z.getElement().compareTo(y.getElement()) < 0)
				x = y.getRight();
			else
				x = y.getLeft();
		restructureTrinode(x, y, z);
		z.adjustHeight();
		if (x == y.getRight() || x == y.getLeft()) {
			x.adjustHeight();
			y.adjustHeight();
		}
		else {
			y.adjustHeight();
			x.adjustHeight();
		}
	}
	
	private AVLN<E> root;
	
	public static void main(String[] args) {
		// AVL TREE DEMO
		BinarySearchTreeSet<Integer> set = new AVLTreeSet<>();
		System.out.println("AVL TREE DEMO");
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
