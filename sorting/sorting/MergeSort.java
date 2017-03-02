package sorting;
import java.util.Comparator;

public class MergeSort {

	public static void sort(byte[] elements) {
		// Base case.
		if (elements.length < 2)
			return;
		
		// Dividing step.
		int mid = elements.length / 2;
		byte[] left = new byte[mid];
		byte[] right = new byte[elements.length - mid];
		for (int i = 0; i < left.length; ++i)
			left[i] = elements[i];
		for (int i = 0; i < right.length; ++i)
			right[i] = elements[mid + i];
		
		// Conquering step.
		sort(left);
		sort(right);
		
		// Combining step.
		merge(left, right, elements);
	}
	
	public static void merge(byte[] left, byte[] right, byte[] destination) {
		int i = 0; 
		int j = 0;
		int d = 0;
		
		while (i < left.length && j < right.length)
			destination[d++] = left[i] <= right[j] ? left[i++] : right[j++];
		while (i < left.length)
			destination[d++] = left[i++];
		while (j < right.length)
			destination[d++] = right[j++];
	}
	
	public static void sort(short[] elements) {
		// Base case.
		if (elements.length < 2)
			return;
		
		// Dividing step.
		int mid = elements.length / 2;
		short[] left = new short[mid];
		short[] right = new short[elements.length - mid];
		for (int i = 0; i < left.length; ++i)
			left[i] = elements[i];
		for (int i = 0; i < right.length; ++i)
			right[i] = elements[mid + i];
		
		// Conquering step.
		sort(left);
		sort(right);
		
		// Combining step.
		merge(left, right, elements);
	}
	
	public static void merge(short[] left, short[] right, short[] destination) {
		int i = 0; 
		int j = 0;
		int d = 0;
		
		while (i < left.length && j < right.length)
			destination[d++] = left[i] <= right[j] ? left[i++] : right[j++];
		while (i < left.length)
			destination[d++] = left[i++];
		while (j < right.length)
			destination[d++] = right[j++];
	}
	
	public static void sort(int[] elements) {
		// Base case.
		if (elements.length < 2)
			return;
		
		// Dividing step.
		int mid = elements.length / 2;
		int[] left = new int[mid];
		int[] right = new int[elements.length - mid];
		for (int i = 0; i < left.length; ++i)
			left[i] = elements[i];
		for (int i = 0; i < right.length; ++i)
			right[i] = elements[mid + i];
		
		// Conquering step.
		sort(left);
		sort(right);
		
		// Combining step.
		merge(left, right, elements);
	}
	
	public static void merge(int[] left, int[] right, int[] destination) {
		int i = 0; 
		int j = 0;
		int d = 0;
		
		while (i < left.length && j < right.length)
			destination[d++] = left[i] <= right[j] ? left[i++] : right[j++];
		while (i < left.length)
			destination[d++] = left[i++];
		while (j < right.length)
			destination[d++] = right[j++];
	}
	
	public static void sort(long[] elements) {
		// Base case.
		if (elements.length < 2)
			return;
		
		// Dividing step.
		int mid = elements.length / 2;
		long[] left = new long[mid];
		long[] right = new long[elements.length - mid];
		for (int i = 0; i < left.length; ++i)
			left[i] = elements[i];
		for (int i = 0; i < right.length; ++i)
			right[i] = elements[mid + i];
		
		// Conquering step.
		sort(left);
		sort(right);
		
		// Combining step.
		merge(left, right, elements);
	}
	
	public static void merge(long[] left, long[] right, long[] destination) {
		int i = 0; 
		int j = 0;
		int d = 0;
		
		while (i < left.length && j < right.length)
			destination[d++] = left[i] <= right[j] ? left[i++] : right[j++];
		while (i < left.length)
			destination[d++] = left[i++];
		while (j < right.length)
			destination[d++] = right[j++];
	}
	
	public static void sort(float[] elements) {
		// Base case.
		if (elements.length < 2)
			return;
		
		// Dividing step.
		int mid = elements.length / 2;
		float[] left = new float[mid];
		float[] right = new float[elements.length - mid];
		for (int i = 0; i < left.length; ++i)
			left[i] = elements[i];
		for (int i = 0; i < right.length; ++i)
			right[i] = elements[mid + i];
		
		// Conquering step.
		sort(left);
		sort(right);
		
		// Combining step.
		merge(left, right, elements);
	}
	
	public static void merge(float[] left, float[] right, float[] destination) {
		int i = 0; 
		int j = 0;
		int d = 0;
		
		while (i < left.length && j < right.length)
			destination[d++] = left[i] <= right[j] ? left[i++] : right[j++];
		while (i < left.length)
			destination[d++] = left[i++];
		while (j < right.length)
			destination[d++] = right[j++];
	}
	
	public static void sort(double[] elements) {
		// Base case.
		if (elements.length < 2)
			return;
		
		// Dividing step.
		int mid = elements.length / 2;
		double[] left = new double[mid];
		double[] right = new double[elements.length - mid];
		for (int i = 0; i < left.length; ++i)
			left[i] = elements[i];
		for (int i = 0; i < right.length; ++i)
			right[i] = elements[mid + i];
		
		// Conquering step.
		sort(left);
		sort(right);
		
		// Combining step.
		merge(left, right, elements);
	}
	
	public static void merge(double[] left, double[] right, double[] destination) {
		int i = 0; 
		int j = 0;
		int d = 0;
		
		while (i < left.length && j < right.length)
			destination[d++] = left[i] <= right[j] ? left[i++] : right[j++];
		while (i < left.length)
			destination[d++] = left[i++];
		while (j < right.length)
			destination[d++] = right[j++];
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void sort(T[] elements) {
		sort(elements, (a,b) -> ((Comparable<T>)a).compareTo(b));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void sort(T[] elements, Comparator<? super T> comparator) {
		// Base case.
		if (elements.length < 2)
			return;
		
		// Dividing step.
		int mid = elements.length / 2;
		T[] left = (T[]) new Object[mid];
		T[] right = (T[]) new Object[elements.length - mid];
		for (int i = 0; i < left.length; ++i)
			left[i] = elements[i];
		for (int i = 0; i < right.length; ++i)
			right[i] = elements[mid + i];
		
		// Conquering step.
		sort(left, comparator);
		sort(right, comparator);
		
		// Combining step.
		merge(left, right, elements, comparator);
	}
	
	public static <T> void merge(T[] left, T[] right, T[] destination, Comparator<? super T> comparator) {
		int i = 0; 
		int j = 0;
		int d = 0;
		
		while (i < left.length && j < right.length)
			destination[d++] = comparator.compare(left[i], right[j]) <= 0 ? left[i++] : right[j++];
		while (i < left.length)
			destination[d++] = left[i++];
		while (j < right.length)
			destination[d++] = right[j++];
	}
	
	
	public static void main(String[] args) {
		Integer[] elements = new Integer[15];
		for (int i = 0; i < elements.length; ++i)
			elements[i] = new java.util.Random().nextInt(100);
		System.out.println(java.util.Arrays.toString(elements));
		sort(elements);
		System.out.println(java.util.Arrays.toString(elements));
		
	}
}
