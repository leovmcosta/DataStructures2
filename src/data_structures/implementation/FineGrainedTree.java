package data_structures.implementation;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import data_structures.Sorted;

public class FineGrainedTree<T extends Comparable<T>> implements Sorted<T> {
	public class Node {
        T item;
        Lock lock = new ReentrantLock();
        Node left;
        Node right;

        public Node (T data) {
            this.item = data;
            this.left = null;
            this.right = null;
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

    private Node root;
    private Lock rootLock;

    public FineGrainedTree() {
        this.root = null;
        rootLock = new ReentrantLock();
    }

    public void add(T t) throws UnsupportedOperationException {
        Node n = new Node(t);
        Node curr, parent;
        int compare;

        rootLock.lock();
        try {
            if (root == null) {
                root = n;
                return;
            }
        } finally {
            rootLock.unlock();
        }

        curr = root;
        curr.lock();
        try {
            while(true) {
                parent = curr;
                try {
                    compare = curr.compareTo(n);
                    if(curr.compareTo(n) > 0) {
                        curr = curr.left;
                    } else {
                        curr = curr.right;
                    }
                    if (curr == null) break;
                } finally {
                    if (curr != null) {
                        curr.lock();
                    }
                    parent.unlock();
                }
            }
        } finally {
            if (curr != null) {
                curr.unlock();
            }
        }
        if(compare > 0)
            parent.left = n;
        else
            parent.right = n;
    }

    public void remove(T t) throws UnsupportedOperationException {
        Node curr, parent;
        int compare;
        boolean isLeftChild = false;

        rootLock.lock();
        if (root != null) {
            //Tree is not empty, search for the passed data.  Start by checking
            //the root separately.
            curr = root;
            parent = curr;
            curr.lock();
            if (curr.compareTo(t) == 0) {
                //Found the specified data, remove it from the tree
                Node successor = getSuccessor(curr);

                root = successor;

                if (successor != null) {
                    successor.left = curr.left;
                    successor.right = curr.right;
                }
                curr.unlock();
                rootLock.unlock();
                return;
            }
            curr.lock();
            rootLock.unlock();
            while(true) {
                compare = curr.compareTo(t);
                if(compare != 0) {
                    parent.unlock();
                    parent = curr;
                    if(compare > 0) {
                        //curNode is "bigger" than passed data, search the left
                        //subtree
                        curr = curr.left;
                        isLeftChild = true;
                    } else if(compare < 0) {
                        //curNode is "smaller" than passed data, search the right
                        //subtree
                        curr = curr.right;
                        isLeftChild = false;
                    }
                } else {
                    //Found the specified data, remove it from the tree
                    Node successor = getSuccessor(curr);

                    //Set the parent pointer to the new child
                    if(isLeftChild)
                        parent.left = successor;
                    else
                        parent.right = successor;

                    //Replace curNode with replacement
                    if(successor != null) {
                        successor.left = curr.left;
                        successor.right = curr.right;
                    }

                    curr.unlock();
                    parent.unlock();
                    return;
                }

                if(curr == null) {
                    break;
                } else {
                    curr.lock();
                }
            }
        } else {
            rootLock.unlock();
            return;
        }
        //The specified data was not in the tree
        parent.unlock();
    }

    public Node getSuccessor(Node deleteNode) {
        Node curr, parent;

        if(deleteNode.left != null) {
            //Find the "biggest" node in the left subtree as the replacement
            parent = deleteNode;
            curr = deleteNode.left;
            curr.lock();
            while(curr.right != null) {
                if(parent != deleteNode)
                    parent.unlock();
                parent = curr;
                curr = curr.right;
                curr.lock();
            }
            if(curr.left != null)
                curr.left.lock();
            if(parent == deleteNode)
                parent.left = curr.left;
            else {
                parent.right = curr.left;
                parent.unlock();
            }
            if(curr.left != null)
                curr.left.unlock();
            curr.unlock();
        } else if(deleteNode.right != null) {
            //Find the "smallest" node in the right subtree as the replacement
            parent = deleteNode;
            curr = deleteNode.right;
            curr.lock();
            while(curr.left != null) {
                if(parent != deleteNode)
                    parent.unlock();
                parent = curr;
                curr = curr.left;
                curr.lock();
            }
            if(curr.right != null)
                curr.right.lock();
            if(parent == deleteNode)
                parent.right = curr.right;
            else {
                parent.left = curr.right;
                parent.unlock();
            }
            if(curr.right != null)
                curr.right.unlock();
            curr.unlock();
        } else {
            //No children, no replacement needed
            return null;
        }
        return curr;
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
