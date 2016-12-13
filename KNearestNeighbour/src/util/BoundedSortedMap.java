package util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class BoundedSortedMap<K, V> extends TreeMap<K, V> {

	private static final long serialVersionUID = 1;

	private final int bound;

	public BoundedSortedMap(int bound) {
		this(bound, true);
	}

	public BoundedSortedMap(int bound, boolean retainHighest) {
		super(((retainHighest) ? null : new ReverseComparator<K>()));
		this.bound = bound;
	}

	public V put(K key, V value) {
		V old = super.put(key, value);
		if (size() > bound) {
			remove(firstKey());
		}
		return old;
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}


	static final class ReverseComparator<K> implements Comparator<K>, Serializable {

		private static final long serialVersionUID = 1;

		@SuppressWarnings("unchecked")
		public int compare(K c1, K c2) {
			return (((Comparable) c1).compareTo(c2));
		}
	}
}