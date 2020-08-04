package tree;

import java.util.Collection;

/**
 * This class represents a non-empty search tree. An instance of this class
 * should contain:
 * <ul>
 * <li>A key
 * <li>A value (that the key maps to)
 * <li>A reference to a left Tree that contains key:value pairs such that the
 * keys in the left Tree are less than the key stored in this tree node.
 * <li>A reference to a right Tree that contains key:value pairs such that the
 * keys in the right Tree are greater than the key stored in this tree node.
 * </ul>
 * 
 */
public class NonEmptyTree<K extends Comparable<K>, V> implements Tree<K, V> {

	/* Provide whatever instance variables you need */
	private Tree<K, V> left, right;
	private K key;
	private V value;

	/**
	 * Only constructor we need.
	 * 
	 * @param key
	 * @param value
	 * @param left
	 * @param right
	 */
	public NonEmptyTree(K key, V value, Tree<K, V> left, Tree<K, V> right) {
		// TODO
		this.key = key;
		this.value = value;
		this.left = left;
		this.right = right;
	}

	public V search(K key) {
		// base case if the Node's keys when compared is equal to 0, then you know they
		// are the same
		if (this.key.compareTo(key) == 0) {
			return this.value;
		}

		// Recursion: if the value is negative then you know the key is greater than
		// that node and you need to look to the right
		else if (this.key.compareTo(key) < 0) {
			return right.search(key);
		}

		// Recursion: otherwise look at the left side of the tree
		else {
			return left.search(key);
		}
	}

	public NonEmptyTree<K, V> insert(K key, V value) {

		// If it returns a negative number you know the key is too low then you need to
		// look to the right
		if (this.key.compareTo(key) < 0) {
			this.right = this.right.insert(key, value);
		}

		// If it returns a positive number you know the key is too high then you need to
		// look to the left
		if (this.key.compareTo(key) > 0) {
			this.left = this.left.insert(key, value);
		}

		// If it is equal, then set the value equal to each other
		if (this.key.compareTo(key) == 0) {
			this.value = value;
		}
		return this;
	}

	public Tree<K, V> delete(K key) {
		//If the comparison returns 0 you need to try to delete the key from the left
		//If you get an exception, you know to look and try to delete the key from the right instead 
		if (this.key.compareTo(key) == 0) {
			try {
				K max = left.max();
				this.key = max;
				this.value = left.search(max);
				left = left.delete(max);
			} catch (TreeIsEmptyException exception1) {
				return right;
			}
		}

		//if it returns negative, you know it's too low and you need to look to the right for the key
		if (this.key.compareTo(key) < 0) {
			this.right = right.delete(key);
		}
		//If it returns positive, you know it's too high and you need to look to the left for the key now
		if (this.key.compareTo(key) > 0) {
			this.left = left.delete(key);
		}
		return this;
	}

	public K max() {
		try {
			return right.max();
		} catch (TreeIsEmptyException e) {
			return key;
		}
	}

	public K min() {
		//Same thing as finding max but instead min
		//Only if there is a min value available 
		try {
			return left.min();
		} catch (TreeIsEmptyException e) {
			return key;
		}
	}

	public int size() {
		return 1 + right.size() + left.size();
	}

	public void addKeysToCollection(Collection<K> c) {
		left.addKeysToCollection(c);
		c.add(key);
		right.addKeysToCollection(c);
	}

	public Tree<K, V> subTree(K fromKey, K toKey) {
		// If the comparison returns negative, you know you need to look to the right
		if (this.key.compareTo(fromKey) < 0) {
			return this.right.subTree(fromKey, toKey);
		}
		// If this comparison returns negative, you know to look to the right left for
		// the toKey
		else if (toKey.compareTo(this.key) < 0) {
			return this.left.subTree(fromKey, toKey);
		}
		// Otherwise you know you can return a new tree consisting of the values between
		// the fromKey and toKey
		else {
			return new NonEmptyTree<K, V>(this.key, this.value, left.subTree(fromKey, toKey),
					right.subTree(fromKey, toKey));
		}
	}

	public int height() {
		//The height is the largest height between the left and right + 1(for the root)
		return Math.max(left.height(), right.height()) + 1;
	}

	public void inorderTraversal(TraversalTask<K, V> p) {
		//left,root,right
		left.inorderTraversal(p);

		p.performTask(key, value);

		right.inorderTraversal(p);
	}

	public void rightRootLeftTraversal(TraversalTask<K, V> p) {
		//right,left
		right.rightRootLeftTraversal(p);

		p.performTask(key, value);

		left.rightRootLeftTraversal(p);
	}

}
