package data_structures.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import data_structures.Sorted;

public class FineGrainedList<T extends Comparable<T>> implements Sorted<T> {
	public class Node {
		T item;
		Node next;
		Lock lock = new ReentrantLock();
		
		public Node(T item) {
			this.item = item;
		}

		public void lock() {
			this.lock.lock();
		}

		public void unlock() {
			this.lock.unlock();
		}
		
		public int compareTo(Node o) {
			return this.item == null ? 1 : this.item.compareTo(o.item);
		}

		public int compareTo (T data) {return this.item == null ? 1 : this.item.compareTo(data); }
	}
	
	private Node head;
	
	public FineGrainedList() {
		head = new Node(null);
		head.next = new Node(null);
	}

    public void add(T t) throws UnsupportedOperationException {
        Node pred, curr;
        Node n = new Node(t);
        head.lock();
        pred = head;
        try {
        	curr = pred.next;
        	curr.lock();
        	try {
        		while (curr.compareTo(n) < 0) {
        			pred.unlock();
        			pred = curr;
        			curr = curr.next;
        			curr.lock();
        		}
        		if (curr.compareTo(n) == 0) {
        			return;
        		}
        		n.next = curr;
        		pred.next = n;
			} finally {
        		curr.unlock();
        	}
        } finally {
        	pred.unlock();
        }
    }

    public void remove(T t) throws UnsupportedOperationException {
        Node pred = null, curr;
        head.lock();
        try {
        	pred = head;
        	curr = pred.next;
        	curr.lock();
        	try {
        		while (curr.compareTo(t) < 0) {
        			pred.unlock();
        			pred = curr;
        			curr = curr.next;
        			curr.lock();
        		}
        		if (curr.compareTo(t) == 0) {
        			pred.next = curr.next;
				}
			} finally {
        		curr.unlock();
        	}
        } finally {
        	pred.unlock();
        }
    }

    public ArrayList<T> toArrayList() throws UnsupportedOperationException {
        ArrayList<T> list = new ArrayList<>();
        Node curr;
        curr = head;
        while (curr.next != null) {
        	if (curr.item != null) {
        		list.add(curr.item);
        	}
        	curr = curr.next;
        }
        return list;
    }
}
