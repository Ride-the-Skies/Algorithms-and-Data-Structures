package searchtrees;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("unused")
public class AVLTreeMap<K extends Comparable<K>, V> extends BinarySearchTreeMap<K,V> {
	/** A static nested AVL Tree Node class. */
	private static class AVLN<K,V> extends BinarySearchTreeMap.TN<K,V> {
		public AVLN(){}
		public AVLN(K key, V value) {super(key, value);}
		
		public int getHeight() {return height;}
		public AVLN<K,V> getParent() {return (AVLN<K,V>) super.getParent();}
		public AVLN<K,V> getLeft() {return (AVLN<K,V>) super.getLeft();}
		public AVLN<K,V> getRight() {return (AVLN<K,V>) super.getRight();}
		
		public AVLN<K,V> tallerChild() {
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
			for (AVLN<K,V> child: children())
				maxHeight = Math.max(maxHeight, 1+child.getHeight());
			height = maxHeight;
		}
		
		public Iterable<AVLN<K,V>> children() {
			ArrayList<AVLN<K,V>> list = new ArrayList<>(2);
			if (hasLeft())
				list.add(getLeft());
			if (hasRight())
				list.add(getRight());
			return list;
		}
		
		private int height = 0;
	}
	
	public AVLTreeMap() {}
	
	public AVLN<K,V> root() {return root;}
	public void setRoot(TN<K,V> root) {this.root = (AVLN<K,V>) root;}
	
	public V put(K key, V value) {
		if (size() == 0) {
			root = new AVLN<>(key, value);
			++size;
			return root.getValue();
		}
		
		AVLN<K,V> parent = (AVLN<K,V>) find(key);
		if (parent.getKey().equals(key)) {
			parent.setValue(value);
			return parent.getValue();
		}
		else {
			AVLN<K,V> newest = new AVLN<>(key, value);
			if (key.compareTo(parent.getKey()) < 0) {
				parent.setLeft(newest);
				adjustPath(parent);
			}
			else {
				parent.setRight(newest);
				adjustPath(parent);
			}
			++size;
			return newest.getValue();
		}
	}
	
	public V remove(K key) {
		TN<K,V> search = find(key);
		if (search == null || !search.getKey().equals(key))
			return null;
		V removed = search.getValue();
		
		switch (search.numChildren()) {
		case 0:
			if (size() == 1)
				root = null;
			else {
				AVLN<K,V> parent = (AVLN<K,V>) search.getParent();
				if (search.getKey().compareTo(parent.getKey()) < 0) 
					parent.left = search.parent = null;
				else 
					parent.right = search.parent = null;
				adjustPath(parent);
			}
			break;
			
		case 1:
			TN<K,V> child = search.hasLeft() ? search.getLeft() : search.getRight();
			if (search == root) {
				child.parent = root.left = root.right = null;
				root = (AVLN<K,V>) child;
				adjustPath(root);
			}	
			else {
				AVLN<K,V> parent = (AVLN<K,V>) search.getParent();
				if (search.getKey().compareTo(parent.getKey()) < 0)
					parent.setLeft(child);
				else
					parent.setRight(child);
				adjustPath(parent);
				search.parent = search.left = search.right = null;
			}
			break;
			
		case 2:
			TN<K,V> neighbor = random.nextBoolean() ? maxNode(search.getLeft()) : minNode(search.getRight());
			TN<K,V> parent = neighbor.getParent();
			search.setEntry(neighbor);
			if (neighbor.numChildren() == 0) {
				if (parent.getLeft() == neighbor)
					parent.left = null;
				else
					parent.right = null;
				neighbor.parent = null;
			}
			else {
				TN<K,V> orphan = neighbor.hasLeft() ? neighbor.getLeft() : neighbor.getRight();
				if (parent.getKey().compareTo(orphan.getKey()) > 0)
					parent.setLeft(orphan);
				else
					parent.setRight(orphan);
				neighbor.parent = neighbor.left = neighbor.right = null;
			}
			adjustPath((AVLN<K,V>) parent);
		}
		--size;
		return removed;
	}
	
	private void adjustPath(AVLN<K,V> node) {
		AVLN<K,V> walk = node;
		while (walk != null) {
			walk.adjustHeight();
			if (walk.heightDiscrepancy() >= 2)
				findTrinodeAndRestructure(walk);
			walk = walk.getParent();
		}
	}
	
	private void findTrinodeAndRestructure(AVLN<K,V> root) {
		AVLN<K,V> x, y, z;
		z = root;
		y = z.tallerChild();
		if (y.tallerChild() != null)
			x = y.tallerChild();
		else
			if (z.getKey().compareTo(y.getKey()) < 0)
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
	
	private AVLN<K,V> root;


	public static void main(String[] args) {
		// SEARCH TREE DEMO
		BinarySearchTreeMap<String, String> map = new AVLTreeMap<>();
		System.out.println("AVL TREE DEMO");
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
