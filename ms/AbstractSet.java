package ms;
import java.util.Collection;

public abstract class AbstractSet<E> implements Set<E> {
	
	public void addAll(Collection<? extends E> source) throws NullPointerException {
		for (E element: source)
			add(element);
	}
	
	public boolean containsAll(Collection<? extends E> collection) throws NullPointerException {
		for (E element: collection)
			if (!contains(element))
				return false;
		return true;
	}
	
	public void removeAll(Collection<? extends E> collection) throws ClassCastException {
		for (E element: collection)
			remove(element);
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
}
