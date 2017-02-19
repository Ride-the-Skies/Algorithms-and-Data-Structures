package searchtrees;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class BinarySearchTreeMap<K extends Comparable<K>, V> implements TreeMap<K,V> {
	/** A nested tree node class. */
	protected static class TN<K,V> implements Entry<K,V> {
		public TN(){}
		public TN(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		public K getKey() {return key;}
		public V getValue() {return value;}
		public void setValue(V value) {this.value = value;}
		public void setEntry(Entry<K,V> other) {
			this.key = other.getKey();
			this.value = other.getValue();
		}
		
		public boolean hasLeft() {return left != null;}
		public boolean hasRight() {return right != null;}
		public TN<K,V> getParent() {return parent;}
		public TN<K,V> getLeft() {return left;}
		public TN<K,V> getRight() {return right;}
		
		public void setLeft(TN<K,V> left) {
			this.left = left;
			left.parent = this;
		}
		
		public void setRight(TN<K,V> right) {
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
		
		public String toString() {
			StringBuilder sb = new StringBuilder("[");
			sb.append(key.toString());
			sb.append(" -> ");
			sb.append(value.toString());
			sb.append("]");
			return sb.toString();
		}
		
		private K key;
		private V value;
		protected TN<K,V> parent;
		protected TN<K,V> left;
		protected TN<K,V> right;
	}
	
	public BinarySearchTreeMap() {}
	
	public int size() {return size;}
	public boolean isEmpty() {return size() == 0;}
	public TN<K,V> root() {return root;}
	public void setRoot(TN<K,V> root) {this.root = root;}
	
	protected TN<K,V> find(K key) {
		if (root() == null)
			return null;
		else return find(root(), key);
	}
	
	protected TN<K,V> find(TN<K,V> node, K key) {
		while (!node.getKey().equals(key)) {
			if (node.hasLeft() && key.compareTo(node.getKey()) < 0)
				node = node.getLeft();
			else if (node.hasRight() && key.compareTo(node.getKey()) > 0)
				node = node.getRight();
			else break;
		}
		return node;
	}
	
	protected TN<K,V> maxNode(TN<K,V> subtreeRoot) {
		TN<K,V> walk = subtreeRoot;
		while (walk.hasRight())
			walk = walk.getRight();
		return walk;
	}
	
	protected TN<K,V> minNode(TN<K,V> subtreeRoot) {
		TN<K,V> walk = subtreeRoot;
		while (walk.hasLeft())
			walk = walk.getLeft();
		return walk;
	}
	
	public boolean containsKey(K key) {
		TN<K,V> search = find(key);
		return search != null && key.equals(search.getKey());
	}

	public boolean containsValue(V value) {
		for (V v: values())
			if (v.equals(value))
				return true;
		return false;
	}

	public V get(K key) {
		TN<K,V> search = find(key);
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
			parent.setValue(value);
			return parent.getValue();
		}
		else {
			TN<K,V> newest = new TN<>(key, value);
			if (key.compareTo(parent.getKey()) < 0)
				parent.setLeft(newest);
			else
				parent.setRight(newest);
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
				TN<K,V> parent = search.getParent();
				if (search.getKey().compareTo(parent.getKey()) < 0) 
					parent.left = search.parent = null;
				else 
					parent.right = search.parent = null;
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
		}
		
		--size;
		return removed;
	}

	protected void rotateLeft(TN<K,V> x, TN<K,V> y) throws IllegalArgumentException {
		if (x != y.getRight())
			throw new IllegalArgumentException("The first argument is not a right child of the second.");
		TN<K,V> parent = y.getParent();
		TN<K,V> beta = x.getLeft();
		x.setLeft(y);
		if (parent != null)
			if (parent.getKey().compareTo(y.getKey()) < 0)
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
	
	protected void rotateRight(TN<K,V> x, TN<K,V> y) throws IllegalArgumentException {
		if (x != y.getLeft())
			throw new IllegalArgumentException("The first argument is not a left child of the second.");
		TN<K,V> parent = y.getParent();
		TN<K,V> beta = x.getRight();
		x.setRight(y);
		if (parent != null)
			if (parent.getKey().compareTo(y.getKey()) < 0)
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
	
	protected void restructureTrinode(TN<K,V> x, TN<K,V> y, TN<K,V> z) {
		boolean xLessThanY = x.getKey().compareTo(y.getKey()) < 0;
		boolean yLessThanZ = y.getKey().compareTo(z.getKey()) < 0;
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
	
	// The key iterator.
	public Iterable<K> keys() {return new KeyIterable();}
	
	private class KeyIterable implements Iterable<K> {
		public Iterator<K> iterator() {return new KeyIterator();}
	}
	
	private class KeyIterator implements Iterator<K> {
		public KeyIterator() {it = new EntryIterator();}
		public boolean hasNext() {return it.hasNext();}
		public void remove() {throw new UnsupportedOperationException();}
		public K next() {return it.next().getKey();}
		private EntryIterator it;
	}
	
	// The value iterator.
	public Iterable<V> values() {return new ValueIterable();}
	
	private class ValueIterable implements Iterable<V> {
		public Iterator<V> iterator() {return new ValueIterator();}
	}
	
	private class ValueIterator implements Iterator<V> {
		public ValueIterator() {it = new EntryIterator();}
		public boolean hasNext() {return it.hasNext();}
		public void remove() {throw new UnsupportedOperationException();}
		public V next() {return it.next().getValue();}
		private EntryIterator it;
	}

	// The entry iterator.
	public Iterable<Entry<K, V>> entries() {return new EntryIterable();}
	
	private class EntryIterable implements Iterable<Entry<K,V>> {
		public Iterator<Entry<K, V>> iterator() {return new EntryIterator();}		
	}
	
	private class EntryIterator implements Iterator<Entry<K,V>> {
		@SuppressWarnings("unchecked")
		public EntryIterator() {
			snapshot = (TN<K,V>[]) new TN[size];
			if (!isEmpty())
				fillInorder(root(), snapshot);
			index = 0;
		}
		
		private void fillInorder(TN<K,V> walk, TN<K,V>[] snapshot) {
			if (walk.hasLeft())
				fillInorder(walk.getLeft(), snapshot);
			snapshot[index++] = walk;
			if (walk.hasRight())
				fillInorder(walk.getRight(), snapshot);
		}
		
		public boolean hasNext() {return index < snapshot.length;}
		public TN<K,V> next() {return snapshot[index++];}
		public void remove() {throw new UnsupportedOperationException();}
		
		private TN<K,V>[] snapshot;
		private int index = 0;
	}
	
	public void print() {if (!isEmpty()) print(root(), "");}
	protected void print(TN<K,V> node, String buffer) {
		if (node.hasRight())
			print(node.getRight(), buffer + "---");
		System.out.println(buffer + node.toString());
		if (node.hasLeft())
			print(node.getLeft(), buffer + "---");
	}
	
	private TN<K,V> root;
	protected int size;
	protected Random random = new Random();
	
	public static void main(String[] args) {
		// SEARCH TREE DEMO
		BinarySearchTreeMap<String, String> map = new BinarySearchTreeMap<>();
		System.out.println("BINARY SEARCH TREE DEMO");
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
