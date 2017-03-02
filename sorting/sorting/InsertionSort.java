package sorting;
import java.util.Comparator;

public class InsertionSort {

	public static void sort(byte[] elements) {
		for (int i = 1; i < elements.length; ++i) {
			for (int j = i; j >= 1; --j) {
				while (elements[j] < elements[j - 1]) {
					byte temp = elements[j];
					elements[j] = elements[j - 1];
					elements[j - 1] = temp;
				}
			}
		}
	}
	
	public static void sort(short[] elements) {
		for (int i = 1; i < elements.length; ++i) {
			for (int j = i; j >= 1; --j) {
				while (elements[j] < elements[j - 1]) {
					short temp = elements[j];
					elements[j] = elements[j - 1];
					elements[j - 1] = temp;
				}
			}
		}
	}
	
	public static void sort(int[] elements) {
		for (int i = 1; i < elements.length; ++i) {
			for (int j = i; j >= 1; --j) {
				while (elements[j] < elements[j - 1]) {
					int temp = elements[j];
					elements[j] = elements[j - 1];
					elements[j - 1] = temp;
				}
			}
		}
	}
	
	public static void sort(long[] elements) {
		for (int i = 1; i < elements.length; ++i) {
			for (int j = i; j >= 1; --j) {
				while (elements[j] < elements[j - 1]) {
					long temp = elements[j];
					elements[j] = elements[j - 1];
					elements[j - 1] = temp;
				}
			}
		}
	}
	
	public static void sort(float[] elements) {
		for (int i = 1; i < elements.length; ++i) {
			for (int j = i; j >= 1; --j) {
				while (elements[j] < elements[j - 1]) {
					float temp = elements[j];
					elements[j] = elements[j - 1];
					elements[j - 1] = temp;
				}
			}
		}
	}
	
	public static void sort(double[] elements) {
		for (int i = 1; i < elements.length; ++i) {
			for (int j = i; j >= 1; --j) {
				while (elements[j] < elements[j - 1]) {
					double temp = elements[j];
					elements[j] = elements[j - 1];
					elements[j - 1] = temp;
				}
			}
		}
	}
	
	public static void sort(char[] elements) {
		for (int i = 1; i < elements.length; ++i) {
			for (int j = i; j >= 1; --j) {
				while (elements[j] < elements[j - 1]) {
					char temp = elements[j];
					elements[j] = elements[j - 1];
					elements[j - 1] = temp;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> void sort(T[] elements) throws ClassCastException {
		sort(elements, (a,b) -> ((Comparable<T>)a).compareTo(b));
	}
	
	public static <T> void sort(T[] elements, Comparator<? super T> comparator) {
		for (int i = 1; i < elements.length; ++i) {
			for (int j = i; j >= 1; --j) {
				while (comparator.compare(elements[j], elements[j - 1]) < 0) {
					T temp = elements[j];
					elements[j] = elements[j - 1];
					elements[j - 1] = temp;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		int[] elements = new int[50];
		for (int i = 0; i < elements.length; ++i)
			elements[i] = new java.util.Random().nextInt(25);
		System.out.println(java.util.Arrays.toString(elements));
		sort(elements);
		System.out.println(java.util.Arrays.toString(elements));
		
	}
}
