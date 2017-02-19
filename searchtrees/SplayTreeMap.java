package searchtrees;
import java.util.Scanner;

public class SplayTreeMap<K extends Comparable<K>, V> extends BinarySearchTreeMap<K,V> {
	
	public SplayTreeMap() {}
	
	public TN<K,V> root() {return root;}
	public void setRoot(TN<K,V> root) {this.root = root;}
	
	public boolean containsKey(K key) {
		TN<K,V> search = find(key);
		if (search != null)
			splay(search);
		return search != null && key.equals(search.getKey());
	}
	
	public V get(K key) {
		TN<K,V> search = find(key);
		if (search != null)
			splay(search);
		if (search == null || !key.equals(search.getKey()))
			return null;
		else
			return search.getValue();
	}

	
	public V put(K key, V value) {
		if (size == 0) {
			root = new TN<>(key, value);
			++size;
			return root.getValue();
		}
		
		TN<K,V> parent = find(key);
		if (parent.getKey().equals(key)) {
			splay(parent);
			parent.setValue(value);
			return parent.getValue();
		}
		else {
			TN<K,V> newest = new TN<>(key, value);
			if (key.compareTo(parent.getKey()) < 0) {
				parent.setLeft(newest);
				splay(newest);
			}
			else {
				parent.setRight(newest);
				splay(newest);
			}
			++size;
			return newest.getValue();
		}
	}
	
	public V remove(K key) {
		TN<K,V> search = find(key);
		if (search == null)
			return null;
		if (!search.getKey().equals(key)) {
			splay(search);
			return null;
		}
		V removed = search.getValue();
		
		switch (search.numChildren()) {
		case 0:
			if (size() == 1)
				root = null;
			else {
				TN<K,V> parent = search.getParent();
				if (search.getKey().compareTo(parent.getKey()) < 0) 
					parent.left = search.parent = null;
				else 
					parent.right = search.parent = null;
				splay(parent);
			}
			break;
			
		case 1:
			TN<K,V> child = search.hasLeft() ? search.getLeft() : search.getRight();
			if (search == root) {
				child.parent = root.left = root.right = null;
				root = child;
			}	
			else {
				TN<K,V> parent = search.getParent();
				if (search.getKey().compareTo(parent.getKey()) < 0)
					parent.setLeft(child);
				else
					parent.setRight(child);
				search.parent = search.left = search.right = null;
				splay(parent);
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
			splay(parent);
		}
		
		--size;
		return removed;
	}
	
	private void splay(TN<K,V> node) {
		TN<K,V> x, y, z;
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
	
	private TN<K,V> root;
	
	public static void main(String[] args) {
		// SEARCH TREE DEMO
		BinarySearchTreeMap<String, String> map = new SplayTreeMap<>();
		System.out.println("SPLAY TREE DEMO");
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
