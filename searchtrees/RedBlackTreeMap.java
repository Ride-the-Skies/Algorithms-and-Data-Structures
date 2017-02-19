package searchtrees;

import java.util.Scanner;

@SuppressWarnings("unused")
public class RedBlackTreeMap<K extends Comparable<K>, V> extends BinarySearchTreeMap<K,V> {
	/** A nested Red Black Tree Node class. */
	private static class RBN<K,V> extends TN<K,V> {
		public RBN() {}
		public RBN(K key, V value, int color) {
			super(key, value);
			this.color = color;
		}
		
		public int getColor() {return color;}
		public RBN<K,V> getParent() {return (RBN<K,V>) super.getParent();}
		public RBN<K,V> getLeft() {return (RBN<K,V>) super.getLeft();}
		public RBN<K,V> getRight() {return (RBN<K,V>) super.getRight();}
		
		public RBN<K,V> getSibling() {
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
	
	public RedBlackTreeMap() {}
	
	public RBN<K,V> root() {return root;}
	public void setRoot(TN<K,V> root) {this.root = (RBN<K,V>) root;}
	public int color(RBN<K,V> node) {return node == null ? BLACK : node.getColor();}
	
	public V put(K key, V value) {
		if (size == 0) {
			root = new RBN<>(key, value, BLACK);	// The root is always black.
			++size;
			return root.getValue();
		}
		
		RBN<K,V> parent = (RBN<K,V>) find(key);
		if (parent.getKey().equals(key)) {
			parent.setValue(value);
			return parent.getValue();
		}
		else {
			RBN<K,V> newest = new RBN<>(key, value, RED);	// Newly inserted elements are always red.
			if (key.compareTo(parent.getKey()) < 0) {
				parent.setLeft(newest);
				adjustPathAfterInsertion(newest);
			}
			else {
				parent.setRight(newest);
				adjustPathAfterInsertion(newest);
			}
			++size;
			return newest.getValue();
		}		
	}
	
	public V remove(K key) {
		RBN<K,V> search = (RBN<K,V>) find(key);
		if (search == null || !search.getKey().equals(key))
			return null;
		V removed = search.getValue();
		
		switch (search.numChildren()) {
		case 0:
			if (size() == 1)
				root = null;
			else {
				RBN<K,V> parent = search.getParent();
				if (search.getKey().compareTo(parent.getKey()) < 0) 
					parent.left = null;
				else 
					parent.right = null;
				search.parent = null;
				
				if (search.getColor() == BLACK)  {
					RBN<K,V> doubleBlack = new RBN<>(null, null, DOUBLE_BLACK);
					if (search.getKey().compareTo(parent.getKey()) < 0)  {
						parent.setLeft(doubleBlack);
						adjustDoubleBlack(doubleBlack);
					}
					else {
						parent.setRight(doubleBlack);
						adjustDoubleBlack(doubleBlack);
					}
				}
			}
			break;
			
		case 1:
			RBN<K,V> child = search.hasLeft() ? search.getLeft() : search.getRight();
			RBN<K,V> parent = search.getParent();
			if (search == root) {
				child.parent = root.left = root.right = null;
				root = child;
				root.setColor(BLACK);
			}	
			else {
				if (search.getKey().compareTo(parent.getKey()) < 0)
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
			RBN<K,V> neighbor = (RBN<K,V>) (random.nextBoolean() ? maxNode(search.getLeft()) : minNode(search.getRight()));
			parent = neighbor.getParent();
			boolean parentLessThanNeighbor = parent.getKey().compareTo(neighbor.getKey()) < 0;
			search.setEntry(neighbor);
			if (neighbor.numChildren() == 0) {
				if (parent.getLeft() == neighbor)
					parent.left = null;
				else
					parent.right = null;
				neighbor.parent = null;
				if (neighbor.getColor() == BLACK) {
					RBN<K,V> doubleBlack = new RBN<>(null, null, DOUBLE_BLACK);
					if (parentLessThanNeighbor) {
						parent.setRight(doubleBlack);
						adjustDoubleBlack(doubleBlack);
					}
					else {
						parent.setLeft(doubleBlack);
						adjustDoubleBlack(doubleBlack);
					}
				}
			}
			else {
				RBN<K,V> orphan = neighbor.hasLeft() ? neighbor.getLeft() : neighbor.getRight();
				if (parent.getKey().compareTo(orphan.getKey()) > 0)
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
		return removed;
	}
	
	// Methods for restoring red-black tree properties after insertion.
	public void adjustPathAfterInsertion(RBN<K,V> inserted) {
		RBN<K,V> x = inserted;
		RBN<K,V> y = inserted.getParent();
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
	
	public RBN<K,V> restructureAfterInsertion(RBN<K,V> x) {
		RBN<K,V> y = x.getParent();
		RBN<K,V> z = y.getParent();
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
	
	public void recolorAfterInsertion(RBN<K,V> parent) {
		if (parent != root())
			parent.invertColor();
		parent.getLeft().invertColor();
		parent.getRight().invertColor();
	}
	
	
	// Methods for restoring red-black tree properties after deletion.
	private void adjustDoubleBlack(RBN<K,V> doubleBlack) {
		if (doubleBlack == root) {
			root.setColor(BLACK);
			return;
		}
		RBN<K,V> current = doubleBlack;
		RBN<K,V> sibling = current.getSibling();
		if (color(sibling) == BLACK) {
			if (sibling != null && (color(sibling.getLeft()) == RED || color(sibling.getRight()) == RED)) {	// Case One. Always terminal.
				fixDeletionCaseOne(doubleBlack);
			}
			else {	// Case Two. Terminal if the parent of p is red.
				RBN<K,V> parent = doubleBlack.getParent();
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
	
	private void fixDeletionCaseOne(RBN<K,V> doubleBlack) {
		RBN<K,V> sibling = doubleBlack.getSibling();
		RBN<K,V> red = color(sibling.getLeft()) == RED ? sibling.getLeft() : sibling.getRight();
		RBN<K,V> x = red;
		RBN<K,V> y = red.getParent();
		RBN<K,V> z = red.getParent().getParent();
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
		
		if (doubleBlack.getKey() == null) {
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
	
	private void fixDeletionCaseTwo(RBN<K,V> doubleBlack) {
		RBN<K,V> sibling = doubleBlack.getSibling();
		RBN<K,V> parent = doubleBlack.getParent();
		sibling.setColor(RED);
		parent.setColor(parent.getColor()+1);
		
		if (doubleBlack.getKey() == null) {
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
	
	private void fixDeletionCaseThree(RBN<K,V> doubleBlack) {
		RBN<K,V> sibling = doubleBlack.getSibling();
		RBN<K,V> parent = sibling.getParent();
		sibling.setColor(BLACK);
		parent.setColor(RED);
		if (sibling == parent.getLeft())
			rotateRight(sibling, parent);
		else
			rotateLeft(sibling, parent);
	}
	
	private RBN<K,V> root;
	public static final int RED = 0;
	public static final int BLACK = 1;
	public static final int DOUBLE_BLACK = 2;
	
	public static void main(String[] args) {
		// SEARCH TREE DEMO
		BinarySearchTreeMap<String, String> map = new RedBlackTreeMap<>();
		System.out.println("RED BLACK TREE DEMO");
		Scanner scanner = new Scanner(System.in);
		String input;
		do {
			System.out.format("%nG: Get%nP: Put%nR: Remove%nCK: containsKey%nCV: containsValue%nS: Size%nQ: Quit%nEnter a command: ");
			input = scanner.next().toUpperCase();
			if (input.equals("G")) {
				System.out.format("Enter a key: ");
				scanner.nextLine();	// Consumes '\n'.
				String key = scanner.nextLine();
				String value = map.get(key);
				System.out.println("The value associated with this key is " + value +".");
			}
			else if (input.equals("P")) {
				System.out.format("Enter a key: ");
				scanner.nextLine();
				String key = scanner.nextLine();
				System.out.format("Enter a value: ");
				String value = scanner.nextLine();
				map.put(key, value);
			}
			else if (input.equals("R")) {
				System.out.format("Enter a key: ");
				scanner.nextLine();	// Consumes '\n'.
				String toRemove = scanner.nextLine();
				map.remove(toRemove);
			}
			else if (input.equals("CK")) {
				System.out.format("Enter a key: ");
				scanner.nextLine();
				String key = scanner.nextLine();
				boolean ck = map.containsKey(key);
				System.out.format("The map does " + (ck ? "" : "not ") + "contain " + key + ".%n");
			}
			else if (input.equals("CV")) {
				System.out.format("Enter a value: ");
				scanner.nextLine();
				String value = scanner.nextLine();
				boolean cv = map.containsValue(value);
				System.out.format("The map does " + (cv ? "" : "not ") + "contain " + value + ".%n");
			}
			else if (input.equals("S")) {
				System.out.format("The size of the map is currently %d.%n", map.size());
			}
			else if (input.equals("Q"))
				continue;
			else {
				System.out.format("Invalid command.%n");
			}
			System.out.println("\nCurrent state:");
			map.print();
		}
		while (!input.equals("Q"));
		scanner.close();
	}
}
