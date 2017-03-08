package setsmaps;
import java.util.Comparator;

public class AVLTreeSet<E> extends BinarySearchTreeSet<E> {
	public AVLTreeSet() {
		super();
	}
	
	public AVLTreeSet(Comparator<? super E> comparator) {
		super(comparator);
	}
	
	/** <p> Walks from the node just inserted to the root, adjusting the heights
	 * of the nodes it visits. Stops when either a single trinode restructing operation 
	 * made* or when the root has been passed. </p>
	 * 
	 * <p> *An AVL Tree never needs more than a single trinode restructuring operation
	 * after insertions. </p>
	 * */
	void rebalanceInsertion(TN<E> justInserted) {
		for (TN<E> walk = justInserted; walk != null; walk = walk.parent()) {
			short previousHeight = height(walk);
			adjustHeightAfterInsertion(walk);
			if (height(walk) == previousHeight && (walk.hasLeft() || walk.hasRight()))
				return;
			if (heightDiscrepancy(walk) > 1) {
				findTrinode(walk);
				return;
			}
		}
	}
	
	/** Walks from the parent of the node just deleted to the root, adjusting 
	 * the heights of the nodes it visits. Can only stop after passing over
	 * the root. */
	void rebalanceDeletion(TN<E> parentDeleted) {
		for (TN<E> walk = parentDeleted; walk != null; walk = walk.parent()) {
			adjustHeightAfterDemotion(walk);
			if (heightDiscrepancy(walk) > 1)
				findTrinode(walk);
		}
	}
	
	/** <p>Selects node z as the root of the rotation. Then selects the child of z
	 * with larger height as y, the child of z*. </p>
	 * 
	 *  
	 *  <p> *There is always a child of z with a height that is greater than that
	 *  of the other. </p> */
	void findTrinode(TN<E> z) {		
		TN<E> x, y;
		if (height(z.left()) > height(z.right())) {
			y = z.left();
			if (height(y.left()) > height(y.right()))
				x = y.left();
			else if (height(y.right()) > height(y.left()))
				x = y.right();
			else x = y.left();
		}
		else {
			y = z.right();
			if (height(y.left()) > height(y.right()))
				x = y.left();
			else if (height(y.right()) < height(y.right()))
				x = y.right();
			else x = y.right();
		}
		restructureTrinode(x, y, z);
		adjustHeightsAfterRestructuring(x, y, z);
	}
	
	/** <p> Corrects the heights of nodes x, y, and z after a trinode restructuring
	 * operation. Ensures that the two nodes among this trio with the height that 
	 * is lower than the third have their heights adjusted before that node. */
	void adjustHeightsAfterRestructuring(TN<E> x, TN<E> y, TN<E> z) {
		adjustHeightAfterDemotion(z);			
		if (x == y.left() || x == y.right()) {
			adjustHeightAfterDemotion(x);
			adjustHeightAfterDemotion(y);
		}
		else {
			adjustHeightAfterDemotion(y);
			adjustHeightAfterDemotion(x);	
		}
	}
	
	private short height(TN<E> node) {
		return node == null ? -1 : node.aux();
	}
	
	// Assumes non-null input.
	private void setHeight(TN<E> node, short newAux) {
		node.setAux(newAux);
	}
	
	private int heightDiscrepancy(TN<E> node) {
		return Math.abs(height(node.left()) - height(node.right()));
	}
	
	private short heightTallestChild(TN<E> node) {
		short heightTallestChild = Short.MIN_VALUE;
		for (TN<E> child: node.children())
			if (height(child) > heightTallestChild)
				heightTallestChild = height(child);
		return heightTallestChild;
	}
	
	private void adjustHeightAfterInsertion(TN<E> node) {
		setHeight(node, (short) Math.max(height(node), heightTallestChild(node) + 1));
	}
	
	private void adjustHeightAfterDemotion(TN<E> node) {
		setHeight(node, (short) Math.max(0, heightTallestChild(node) + 1));
	}
	
	public static void main(String[] args) {
		AVLTreeSet<Integer> set = new AVLTreeSet<>();
		for (int i = 0; i < 2047; ++i)
			set.add(i + 1);
		set.print();
	}
}
