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
		root = null;
	}

    public void add(T t) throws UnsupportedOperationException {
	    Node newNode = new Node(t);
	    if (root==null) {
		    root = newNode;
		    return;
	    }
	    Node current = root;
	    Node parent = null;
	    while (true) {
		    parent = current;
		    if (t<current.key) {
			    current = current.left;
			    if (current==null) {
				    parent.left = newNode;
				    return
			    }
		    } else {
			    current = current.right;
			    if (current==null) {
				    parent.right = newNode;
				    return;
			    }
		    }
	    }
    }

    public void remove(T t) throws UnsupportedOperationException {
        Node parent = root;
        Node current = root;
        boolean isLeftChild = false;
        while (current.key != t) {
	        parent = current;
	        if (current.key>t) {
		        isLeftChild = true;
		        current = current.left;
	        } else {
		        isLeftChild = false;
		        current = current.right;
	        }
	        if (current == null) {
		        return false;
	        }
        }
        if (current.left == null && current.right == null) {
	        if (current == root) {
		        root = null;
	        }
	        if (isLeftChild == true) {
		        parent.left = null;
	        } else {
		        parent.right = null;
	        }
        }
        else if (current.right == null) {
	        if (current == root) {
		        root = current.left
	        } else if (isLeftChild) {
		        parent.left = current.left;
	        } else {
		        parent.right = current.left;
	        }
        }
        else if (current.left == null) {
	        if (current == root) {
		        root = current.right;
	        } else if (isLeftChild) {
		        parent.left = current.right;
	        } else {
		        parent.right = current.right;
	        }
        }
        else if (current.left != null && current.right != null) {
	        Node successor = getSuccessor(current);
	        if (current == root) {
		        root = successor;
	        } else if (isLeftChild) {
		        parent.left = successor;
	        } else {
		        parent.right = successor;
	        }
	        successor.left = current.left;
        }
        return true;
    }
    
    public Node getSuccessor(Node deleteNode) {
	    Node successor = null;
	    Node successorParent = null;
	    Node current = deleteNode.right;
	    while (current != null) {
		    successorParent = successor;
		    successor = current;
		    current = current.left;
	    }
	    if (successor != deleteNode.right) {
		    successorParent.left = successor.right;
		    successor.right = deleteNode.right;
	    }
	    return successor;
    }

    public ArrayList<T> toArrayList() {
        throw new UnsupportedOperationException();
    }
}
