package sorting;
import java.util.Comparator;

public class HeapSort {
	// BYTE ARRAYS
	public static void sort(byte[] elements) {
		for (int i = parent(elements.length - 1); i >= 0; --i) 
			percolateDown(elements, i, elements.length);
		for (int i = elements.length - 1; i >= 1; --i) {
			swap(elements, 0, i);
			percolateDown(elements, 0, i);
		}
	}
	
	private static void percolateDown(byte[] heap, int startingIndex, int size) {
		for (int walk = startingIndex; hasLeft(walk, size);) {
			int parentComparedToLeft = Byte.compare(heap[walk], heap[left(walk)]);
			if (hasRight(walk, size)) {
				int parentComparedToRight = Byte.compare(heap[walk], heap[right(walk)]);
				if (parentComparedToLeft >= 0 && parentComparedToRight >= 0)
					return;
				else if (parentComparedToLeft < 0 && parentComparedToRight >= 0) {
					swap(heap, walk, left(walk));
					walk = left(walk);
				}
				else if (parentComparedToLeft >= 0 && parentComparedToRight < 0) {
					swap(heap, walk, right(walk));
					walk = right(walk);
				}
				else {
					int leftComparedToRight = Byte.compare(heap[left(walk)], heap[right(walk)]);
					if (leftComparedToRight < 0) {
						swap(heap, walk, right(walk));
						walk = right(walk);
					}
					else {
						swap(heap, walk, left(walk));
						walk = left(walk);
					}
				}
			}
			else {
				if (parentComparedToLeft < 0)
					swap(heap, walk, left(walk));
				return;
			}
		}
	}
	
	private static void swap(byte[] array, int indexAlpha, int indexBeta) {
		byte temp = array[indexAlpha];
		array[indexAlpha] = array[indexBeta];
		array[indexBeta] = temp;
	}
	
	// SHORT ARRAYS
	public static void sort(short[] elements) {
		for (int i = parent(elements.length - 1); i >= 0; --i) 
			percolateDown(elements, i, elements.length);
		for (int i = elements.length - 1; i >= 1; --i) {
			swap(elements, 0, i);
			percolateDown(elements, 0, i);
		}
	}
	
	private static void percolateDown(short[] heap, int startingIndex, int size) {
		for (int walk = startingIndex; hasLeft(walk, size);) {
			int parentComparedToLeft = Short.compare(heap[walk], heap[left(walk)]);
			if (hasRight(walk, size)) {
				int parentComparedToRight = Short.compare(heap[walk], heap[right(walk)]);
				if (parentComparedToLeft >= 0 && parentComparedToRight >= 0)
					return;
				else if (parentComparedToLeft < 0 && parentComparedToRight >= 0) {
					swap(heap, walk, left(walk));
					walk = left(walk);
				}
				else if (parentComparedToLeft >= 0 && parentComparedToRight < 0) {
					swap(heap, walk, right(walk));
					walk = right(walk);
				}
				else {
					int leftComparedToRight = Short.compare(heap[left(walk)], heap[right(walk)]);
					if (leftComparedToRight < 0) {
						swap(heap, walk, right(walk));
						walk = right(walk);
					}
					else {
						swap(heap, walk, left(walk));
						walk = left(walk);
					}
				}
			}
			else {
				if (parentComparedToLeft < 0)
					swap(heap, walk, left(walk));
				return;
			}
		}
	}
	
	private static void swap(short[] array, int indexAlpha, int indexBeta) {
		short temp = array[indexAlpha];
		array[indexAlpha] = array[indexBeta];
		array[indexBeta] = temp;
	}
	
	// INT ARRAYS
	public static void sort(int[] elements) {
		for (int i = parent(elements.length - 1); i >= 0; --i) 
			percolateDown(elements, i, elements.length);
		for (int i = elements.length - 1; i >= 1; --i) {
			swap(elements, 0, i);
			percolateDown(elements, 0, i);
		}
	}
	
	private static void percolateDown(int[] heap, int startingIndex, int size) {
		for (int walk = startingIndex; hasLeft(walk, size);) {
			int parentComparedToLeft = Integer.compare(heap[walk], heap[left(walk)]);
			if (hasRight(walk, size)) {
				int parentComparedToRight = Integer.compare(heap[walk], heap[right(walk)]);
				if (parentComparedToLeft >= 0 && parentComparedToRight >= 0)
					return;
				else if (parentComparedToLeft < 0 && parentComparedToRight >= 0) {
					swap(heap, walk, left(walk));
					walk = left(walk);
				}
				else if (parentComparedToLeft >= 0 && parentComparedToRight < 0) {
					swap(heap, walk, right(walk));
					walk = right(walk);
				}
				else {
					int leftComparedToRight = Integer.compare(heap[left(walk)], heap[right(walk)]);
					if (leftComparedToRight < 0) {
						swap(heap, walk, right(walk));
						walk = right(walk);
					}
					else {
						swap(heap, walk, left(walk));
						walk = left(walk);
					}
				}
			}
			else {
				if (parentComparedToLeft < 0)
					swap(heap, walk, left(walk));
				return;
			}
		}
	}
	
	private static void swap(int[] array, int indexAlpha, int indexBeta) {
		int temp = array[indexAlpha];
		array[indexAlpha] = array[indexBeta];
		array[indexBeta] = temp;
	}
	
	// LONG ARRAYS
	public static void sort(long[] elements) {
		for (int i = parent(elements.length - 1); i >= 0; --i) 
			percolateDown(elements, i, elements.length);
		for (int i = elements.length - 1; i >= 1; --i) {
			swap(elements, 0, i);
			percolateDown(elements, 0, i);
		}
	}
	
	private static void percolateDown(long[] heap, int startingIndex, int size) {
		for (int walk = startingIndex; hasLeft(walk, size);) {
			int parentComparedToLeft = Long.compare(heap[walk], heap[left(walk)]);
			if (hasRight(walk, size)) {
				int parentComparedToRight = Long.compare(heap[walk], heap[right(walk)]);
				if (parentComparedToLeft >= 0 && parentComparedToRight >= 0)
					return;
				else if (parentComparedToLeft < 0 && parentComparedToRight >= 0) {
					swap(heap, walk, left(walk));
					walk = left(walk);
				}
				else if (parentComparedToLeft >= 0 && parentComparedToRight < 0) {
					swap(heap, walk, right(walk));
					walk = right(walk);
				}
				else {
					int leftComparedToRight = Long.compare(heap[left(walk)], heap[right(walk)]);
					if (leftComparedToRight < 0) {
						swap(heap, walk, right(walk));
						walk = right(walk);
					}
					else {
						swap(heap, walk, left(walk));
						walk = left(walk);
					}
				}
			}
			else {
				if (parentComparedToLeft < 0)
					swap(heap, walk, left(walk));
				return;
			}
		}
	}
	
	private static void swap(long[] array, int indexAlpha, int indexBeta) {
		long temp = array[indexAlpha];
		array[indexAlpha] = array[indexBeta];
		array[indexBeta] = temp;
	}
	
	// GENERIC ARRAYS
	@SuppressWarnings("unchecked")
	public static <T> void sort(T[] elements) throws ClassCastException {
		sort(elements, (a,b) -> ((Comparable<T>)a).compareTo(b));
	}
	
	public static <T> void sort(T[] elements, Comparator<? super T> comparator) {
		for (int i = parent(elements.length - 1); i >= 0; --i) 
			percolateDown(elements, i, elements.length, comparator);
		for (int i = elements.length - 1; i >= 1; --i) {
			swap(elements, 0, i);
			percolateDown(elements, 0, i, comparator);
		}
	}
	
	private static <T> void percolateDown(T[] heap, int startingIndex, int size, Comparator<? super T> comparator) {
		for (int walk = startingIndex; hasLeft(walk, size);) {
			int parentComparedToLeft = comparator.compare(heap[walk], heap[left(walk)]);
			if (hasRight(walk, size)) {
				int parentComparedToRight = comparator.compare(heap[walk], heap[right(walk)]);
				if (parentComparedToLeft >= 0 && parentComparedToRight >= 0)
					return;
				else if (parentComparedToLeft < 0 && parentComparedToRight >= 0) {
					swap(heap, walk, left(walk));
					walk = left(walk);
				}
				else if (parentComparedToLeft >= 0 && parentComparedToRight < 0) {
					swap(heap, walk, right(walk));
					walk = right(walk);
				}
				else {
					int leftComparedToRight = comparator.compare(heap[left(walk)], heap[right(walk)]);
					if (leftComparedToRight < 0) {
						swap(heap, walk, right(walk));
						walk = right(walk);
					}
					else {
						swap(heap, walk, left(walk));
						walk = left(walk);
					}
				}
			}
			else {
				if (parentComparedToLeft < 0)
					swap(heap, walk, left(walk));
				return;
			}
		}
	}
	
	private static <T> void swap(T[] array, int indexAlpha, int indexBeta) {
		T temp = array[indexAlpha];
		array[indexAlpha] = array[indexBeta];
		array[indexBeta] = temp;
	}
	
	
	private static int left(int index) {return 2 * index + 1;}
	private static int right(int index) {return 2 * index + 2;}
	private static int parent(int index) {return (index - 1) / 2;}
	private static boolean hasLeft(int index, int size) {return left(index) < size;}
	private static boolean hasRight(int index, int size) {return right(index) < size;}
	
	public static void main(String[] args) {
		Integer[] elements = new Integer[63];
		for (int i = 0; i < elements.length; ++i)
			elements[i] = new java.util.Random().nextInt(100);
		System.out.println(java.util.Arrays.toString(elements));
		sort(elements);
		System.out.println(java.util.Arrays.toString(elements));
	}
}
