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
		
		public Node(T item) {
			this.item = item;
		}
		
		public int compareTo(Node o) {
			return this.item == null ? 1 : this.item.compareTo(o.item);
		}
	}
	
	private Node head;
	private Lock predlock = new ReentrantLock();
	private Lock currlock = new ReentrantLock();
	
	public FineGrainedList() {
		head = new Node(null);
		head.next = new Node(null);
	}

    public void add(T t) throws UnsupportedOperationException {
        Node pred, curr;
        Node n = new Node(t);
        predlock.lock();
        try {
        	pred = head;
        	curr = pred.next;
        	currlock.lock();
        	try {
        		while (curr.compareTo(n) < 0) {
        			currlock.unlock();
        			pred = curr;
        			curr = curr.next;
        			currlock.lock();
        		}
        		if (curr.compareTo(n) == 0) {
        			return;
        		}
        		n.next = curr;
        		pred.next = n;
        		return;
        	} finally {
        		currlock.unlock();
        	}
        } finally {
        	predlock.unlock();
        }
    }

    public void remove(T t) throws UnsupportedOperationException {
        Node pred = null, curr = null;
        Node n = new Node(t);
        predlock.lock();
        try {
        	pred = head;
        	curr = pred.next;
        	currlock.lock();
        	try {
        		while (curr.compareTo(n) < 0) {
        			currlock.unlock();
        			pred = curr;
        			curr = curr.next;
        			currlock.lock();
        		}
        		if (curr.compareTo(n) == 0) {
        			pred.next = curr.next;
        			return;
        		}
        		return;
        	} finally {
        		currlock.unlock();
        	}
        } finally {
        	predlock.unlock();
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
