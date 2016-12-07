package data_structures.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import data_structures.Sorted;

public class FineGrainedTree<T extends Comparable<T>> implements Sorted<T> {
	public class TreeNode {
		Counter counter;
		TreeNode parent, right, left;
		Bin<T> bin;
		public boolean isleaf() {
			return right == null;
		}
	}
	
	private Lock lock;
	int range;
	List<TreeNode> leaves;
	TreeNode root;
	
	public FineGrainedTree(int logRange) {
		lock = new ReentrantLock();
		range = (1 << logRange);
		leaves = new ArrayList<TreeNode>(range);
		root = null;
	}

    public void add(T t) throws UnsupportedOperationException() {
    	lock.Lock();
        TreeNode node = leaves.get(score);
        node.bin.put(item);
        while(node != root) {
        	TreeNode parent = node.parent;
        	if (node == parent.left) {
        		parent.counter.getAndIncrement();
        	}
        	node = parent;
        }
    }

    public void remove(T t) throws UnsupportedOperationException() {
        TreeNode node = root;
        while (!node.isleaf()) {
        	if (node.counter.boundedGetAndDecrement() > 0) {
        		node = node.left;
        	} else {
        		node = node.right;
        	}
        }
        return node.bin.get();
    }

    public ArrayList<T> toArrayList() {
        throw new UnsupportedOperationException();
    }
}
