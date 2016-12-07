package data_structures.implementation;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import data_structures.Sorted;

public class CoarseGrainedTree<T extends Comparable<T>> implements Sorted<T> {
	public class Node {
		T item;
		Node left;
		Node right;
		
		public Node(T item) {
			this.item = item;
			this.left = null;
			this.right = null;
		}

		public int compareTo(Node o) {
			return this.item.compareTo(o.item);
		}
	}
	
	private Node root;
	private Lock lock = new ReentrantLock();
	
	public CoarseGrainedTree() {
		root = new Node(null);
	}

    public void add(T t) throws UnsupportedOperationException {
	    Node newNode = new Node(t);
	    lock.lock();
	    try {
		    if (root.item == null) {
			    root = newNode;
			    return;
		    }
		    Node current = root;
		    Node parent;
		    while (true) {
			    parent = current;
				if (current.compareTo(newNode) > 0) {
				    current = current.left;
				    if (current==null) {
					    parent.left = newNode;
					    return;
				    }
			    } else {
				    current = current.right;
				    if (current==null) {
					    parent.right = newNode;
					    return;
				    }
			    }
		    }
		} finally {
			lock.unlock();
		}
    }

    public void remove(T t) throws UnsupportedOperationException {
		Node removeNode = new Node(t);

        lock.lock();
        try {
			Node parent = root;
			Node current = root;
			boolean isLeftChild = false;
	        while (removeNode.compareTo(current) != 0) {
		        parent = current;
				if (current.compareTo(removeNode) > 0) {
			        isLeftChild = true;
			        current = current.left;
		        } else {
			        isLeftChild = false;
			        current = current.right;
		        }
		        if (current == null) {
			        return;
		        }
	        }
	        if (current.right == null) {
		        if (current == root) {
			        root = current.left;
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
	        else {
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
	    } finally {
		    lock.unlock();
	    }
    }
    
    public Node getSuccessor(Node deleteNode) {
	    Node successor = new Node(null);
	    Node successorParent = new Node(null);
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

    public ArrayList<T> toArrayList() throws UnsupportedOperationException {
	    return extractValues(this.root);
    }

	public ArrayList<T> extractValues(Node n) {
		ArrayList<T> result = new ArrayList<>();
		if (n == null) return result;
		if (n.left != null) {
			result.addAll(extractValues(n.left));
		}
		result.add(n.item);
		if (n.right != null) {
			result.addAll(extractValues(n.right));
		}

		return result;
	}
	   
}
