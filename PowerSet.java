package math;
import java.util.Set;
import java.util.HashSet;

public class PowerSet {

	public static <T> Set<Set<T>> build(Set<T> set) {
		Set<Set<T>> powerSet = new HashSet<>();
		build(set, powerSet);
		return powerSet;
	}
	
	public static <T> void build(Set<T> input, Set<Set<T>> destination) {
		Set<T> copy = new HashSet<>();
		for (T element: input)
			copy.add(element);
		destination.add(copy);
		
		if (input.isEmpty())
			return;
		for (T t1: input) {
			HashSet<T> reduced = new HashSet<>();
			for (T t2: input)
				reduced.add(t2);
			reduced.remove(t1);
			build(reduced, destination);
		}
	}
	
	public static void main(String[] args) {
		Set<Character> set = new HashSet<>();
		set.add('A');
		set.add('B');
		set.add('C');
		Set<Set<Character>> powerSet = PowerSet.build(set);
		for (Set<Character> subset: powerSet)
			System.out.println(subset.toString());
	}
}
