package com.anabivirtual.story.editor;

import java.util.Collection;
import java.util.Iterator;

/**
 * A wrapper class that allows random access to a {@code java.util.Collection}
 * object.
 *
 * <p>
 * The class mantains a pointer to the collection. If the collection changes
 * this is reflected in the class.
 *
 * <p>
 * To avoid having always O(n) time complexity in getting a value, besides an
 * internal iterator, we keep the last value returned and its index. If the
 * collection values are fetched from the begining to the end, then this results
 * in O(1) access time.
 *
 * <p>
 * This class provides a single method to get the value at a specific index.
 *
 * @author pedro
 * @date 6/nov/2018
 * @param <T> The type of the object in the collection.
 */
class RandomAccessIterator<T>
{
	private final Collection<T> collection;
	private Iterator<T> iterator;
	/**
	 * Index of the last value returned by method {@code getValueAt(int)}.
	 */
	private int last_index;
	/**
	 * Last value returned by method {@code getValueAt(int)}.
	 */
	private T last_value;
	/**
	 * Create a random access iterator for the given collection.
	 * @param collection the collection.
	 */
	RandomAccessIterator (Collection<T> collection)
	{
		this.collection = collection;
		this.iterator = collection.iterator ();
		if (this.iterator.hasNext ()) {
			this.last_value = this.iterator.next ();
			this.last_index = 0;
		}
		else {
			this.last_value = null;
			this.last_index = -1;
		}
	}
	public T getValueAt (final int index)
	{
		if (index < this.last_index) {
			this.iterator = this.collection.iterator ();
			this.last_value = this.iterator.next ();
			this.last_index = 0;
		}
		while (index > this.last_index) {
			this.last_value = this.iterator.next ();
			this.last_index++;
		}
		return this.last_value;
	}
}
