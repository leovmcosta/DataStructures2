package data_structures.implementation;

import java.util.ArrayList;
import java.util.Arrays;
<<<<<<< HEAD
import java.utils.List;
=======
import java.util.List;
>>>>>>> 15c252646c838bef6b4d931f6e1f161963174b8c
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import data_structures.Sorted;

public class CoarseGrainedTree<T extends Comparable<T>> implements Sorted<T> {
<<<<<<< HEAD
    
    public class Node {
        T item;
        int key;
        Node left;
        Node right;
        
        Public Node(int key) {
            this.key = key;
        }
    }
    
    private Node root;
    private Lock lock = new ReentrantLock();
    
=======
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
	}
	
	private Node root;
	private Lock lock = new ReentrantLock();
	
	public CoarseGrainedTree() {
		root = null;
	}
>>>>>>> 15c252646c838bef6b4d931f6e1f161963174b8c

    public void add(T t) throws UnsupportedOperationException {
	    int key = t.hashCode();
	    Node newNode = new Node(key);
	    lock.lock();
	    try {
		    System.out.println("locked add");
		    if (root==null) {
			    root = newNode;
			    return;
		    }
		    Node current = root;
		    Node parent = null;
		    while (true) {
			    parent = current;
			    if (key<current.key) {
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
			System.out.println("unlocked add");
			lock.unlock();
		}
    }

    public void remove(T t) throws UnsupportedOperationException {
        int key = t.hashCode();
        Node parent = root;
        Node current = root;
        boolean isLeftChild = false;
        lock.lock();
        try {
	        System.out.println("locked remove");
	        while (current.key != key) {
		        parent = current;
		        if (current.key>key) {
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
	    } finally {
		    System.out.println("unlocked remove");
		    lock.unlock();
	    }
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

    public ArrayList<T> toArrayList() throws UnsupportedOperationException {
	    ArrayList<T> list = new ArrayList<>();
	    System.out.println("Arraylist");
	    return list;
    }
	   
}
