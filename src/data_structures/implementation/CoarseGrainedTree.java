package data_structures.implementation;

import java.util.ArrayList;
import java.util.Array;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import data_structures.Sorted;

public class CoarseGrainedTree<T extends Comparable<T>> implements Sorted<T> {
	public class Node {
		T item;
		int key;
		Node left;
		Node right;
		
		public Node(int key) {
			this.key = key;
			right = null;
			left = null;
		}
		
		public int compareTo(Node o) {
			return this.item.compareTo(o.item);
		}
	}
	
	private Node root;
	private Lock lock = new ReentrantLock();
	
	public CoarseGrainedTree() {
		root = new Node();
		root.left = new Node();
		root.right = new Node();
	}

    public void add(T t) throws UnsupportedOperationException() {
	    //if root == null, node = root
	    //if t < root, add left
	    //else, add right
    }

    public void remove(T t) {
        throw new UnsupportedOperationException();
    }

    public ArrayList<T> toArrayList() {
        throw new UnsupportedOperationException();
    }
}
