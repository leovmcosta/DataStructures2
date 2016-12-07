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
		int key;
		Node next;
		
		public Node(int key) {
			this.key = key;
		}
	}
	
	private Node head;
	private Lock lock = new Reentrantlock();
	public FineGrainedList() {
		head = new Node(Integer.MIN_VALUE);
		head.next = new Node(Integer.MAX_VALUE);
	}

    public void add(T t) throws UnsupportedOperationException {
        int key = item.hashCode();
        pred.lock();
        Node pred = head;
        try {
        	Node curr = pred.next;
        	curr.lock();
        	try {
        		while (curr.key < key) {
        			pred.unlock();
        			pred = curr;
        			curr = curr.next;
        			curr.lock();
        		}
        		if (curr.key == key) {
        			return false;
        		}
        		Node newNode = new Node(item);
        		newNode.next = curr;
        		pred.next = newNode;
        		return true;
        	} finally {
        		curr.unlock();
        	}
        } finally {
        	pred.unlock();
        }
    }

    public void remove(T t) throws UnsupportedOperationException {
        Node pred = null, curr = null;
        int key = item.hashCode();
        pred.lock();
        try {
        	pred = head;
        	curr = pred.next;
        	curr.lock();
        	try {
        		while (curr.key < key) {
        			pred.unlock();
        			pred = curr;
        			curr = curr.next;
        			curr.lock();
        		}
        		if (curr.key == key) {
        			pred.next = curr.next;
        			return true;
        		}
        		return false;
        	} finally {
        		curr.unlock();
        	}
        } finally {
        	pred.unlock();
        }
    }

    public ArrayList<T> toArrayList() {
        throw new UnsupportedOperationException();
    }
}
