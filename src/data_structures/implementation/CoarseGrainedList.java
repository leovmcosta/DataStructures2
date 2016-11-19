package data_structures.implementation;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import data_structures.Sorted;

public class CoarseGrainedList<T extends Comparable<T>> implements Sorted<T> {
    public class Node {
        T item;
        int key;
        Node next;

        public Node(int key) {
            this.key = key;
        }

        public int compareTo(Node o) {
            return this.item == null ? 1 : this.item.compareTo(o.item);
        }
    }

    private Node head;
    private Lock lock = new ReentrantLock();

    public CoarseGrainedList() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
        head.item = null;
    }

    public void add(T t) throws UnsupportedOperationException {
        Node pred, curr;
        int key = t.hashCode();
        Node n = new Node(key);
        n.item = t;
        lock.lock();
        try {
            pred = head;
            curr = pred.next;
            while (curr.compareTo(n) == -1) {
                pred = curr;
                curr = curr.next;
            }
            n.next = curr;
            pred.next = n;
        } finally {
            lock.unlock();
        }
    }

    public void remove(T t) throws UnsupportedOperationException {
        Node pred, curr;
        int key = t.hashCode();
        Node n = new Node(key);
        n.item = t;
        lock.lock();
        try {
            pred = head;
            curr = pred.next;
            while (curr.compareTo(n) < 0) {
                pred = curr;
                curr = curr.next;
            }
            if (curr.compareTo(n) >= 0) {
                pred.next = curr.next;
            }
        } finally {
            lock.unlock();
        }
    }

    public ArrayList<T> toArrayList() throws UnsupportedOperationException {
        ArrayList<T> list = new ArrayList<>();
        Node curr;
        curr = head;
        while (curr.next.item != null) {
            list.add(curr.item);
            curr = curr.next;
        }
        return list;
    }
}
