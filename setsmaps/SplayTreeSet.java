package setsmaps;
import java.util.Comparator;

public class SplayTreeSet<E> extends BinarySearchTreeSet<E> {
	
	/** Creates an empty Splay Tree Set with the default comparator. */
	public SplayTreeSet() {
		super();
	}
	
	/** Creates an empty Splay Tree Set with the specified comparator. */
	public SplayTreeSet(Comparator<? super E> comparator) {
		super(comparator);
	}
	
	void rebalanceInsertion(TN<E> justInserted) {splay(justInserted);}
	void rebalanceDeletion(TN<E> parentRemoved) {splay(parentRemoved);}
	void rebalanceAccess(TN<E> justAccessed) {splay(justAccessed);}
	
	/** Performs a sequence of rotations that moves a node all the way to the root. */
	private void splay(TN<E> toSplay) {
		for (TN<E> walk = toSplay; walk != root();) {
			TN<E> x = walk;
			TN<E> y = walk.parent();
			TN<E> z = y.parent();
			
			if (z == null)	{ 				// Zig.
				if (x == y.left())
					rotateRight(x, y);
				else
					rotateLeft(y, x);
			}
			else if (x == y.right() && y == z.right()) {	// Zig-zig.
				rotateLeft(z, y);
				rotateLeft(y, x);
			}
			else if (x == y.left() && y == z.left()) {		// Zig-zig.
				rotateRight(y, z);
				rotateRight(x, y);
			}
			else if (x == y.right() && y == z.left()) {		// Zig-zag.
				rotateLeft(y, x);
				rotateRight(x, z);
			}
			else {									// Zig-zag
				rotateRight(x, y);
				rotateLeft(z, x);
			}
		}
	}
	
	public static void main(String[] args) {
		BinarySearchTreeSet<Integer> set = new SplayTreeSet<>();
		set.add(5,7,4,2,8,12,14,1,18,20,15,3);
		set.contains(5);
		set.print();
	}
	
}
