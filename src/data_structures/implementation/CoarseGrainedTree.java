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
		}
	}
	
	private Node root;
	private Lock lock = new ReentrantLock();

    public void add(T t) {
        throw new UnsupportedOperationException();
    }

    public void remove(T t) {
        throw new UnsupportedOperationException();
    }

    public ArrayList<T> toArrayList() {
        throw new UnsupportedOperationException();
    }
}
