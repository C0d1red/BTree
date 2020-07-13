package com.btree;

import java.util.LinkedList;

public class BTree {

    // Max degree
    private final int M;

    private Node root;

    public BTree(int m) {
        this.M = m;
    }

    // Add array of values
    public void add(int[] array) {
        for (int value : array) {
            add(value);
        }
    }

    // Add value
    public void add(int value) {
        // if tree is empty - create root and insert value
        if (root == null) {
            root = new Node();
            root.addNonFull(value);

        // if tree is not empty - find leaf, make splitting and insert value
        } else {

            // Start from the root
            Node currentNode = root;
            Node parent = null;
            int childIndex = 0;

            while (true) {
                if (currentNode.isFull()) {

                    // Create new parent for root
                    if (parent == null) {
                        parent = new Node();
                        root = parent;
                    }

                    // Splitting node for new parent and move up middle value
                    parent.splitChild(childIndex, currentNode);

                    // Choose new currentNode for continue
                    if (value < parent.VALUES.get(childIndex)) {
                        currentNode = parent.CHILDREN.get(childIndex);
                    } else if (value > parent.VALUES.get(childIndex)) {
                        currentNode = parent.CHILDREN.get(childIndex + 1);
                    } else {
                        return;
                    }
                }

                // Search correct child index
                childIndex = binarySearchChildIdx(currentNode, value);

                // The value is already exists
                if (childIndex == -1) {
                    return;
                }

                // If found the leaf - exit from while
                if (currentNode.isLeaf()) {
                    break;
                }

                // Move down to the child
                parent = currentNode;
                currentNode = currentNode.CHILDREN.get(childIndex);
            }


            // Insert value to the leaf
            currentNode.addNonFull(value);

        }
    }

    // Find node for value
    public Node findNodeForValue(int value) {
        // Start from root
        Node currentNode = root;

        // Find leaf
        while (!currentNode.isLeaf()) {
            currentNode = currentNode.CHILDREN.get(binarySearchChildIdx(currentNode, value));
        }
        return currentNode;
    }

    // Print tree to console
    public void show(){
        showNode(root, 0);
    }

    // Binary search for find index of child
    // -1 - error (value is already exists)
    private int binarySearchChildIdx(Node node, int value){
        int left = 0;
        int right = node.VALUES.size() - 1;
        while (left <= right) {
            int middleIndex = left + (right - left) / 2;

            if (value < node.VALUES.get(middleIndex)) {
                right = middleIndex -1;
            } else if (value > node.VALUES.get(middleIndex)){
                left = middleIndex + 1;
            } else {
                // If value exists in tree - return -1
                return -1;
            }
        }

        return right +1;
    }

    // Recursion for show all nodes
    private void showNode(Node node, int lvl) {
        if (node == null) {
            return;
        }

        for (int i = 0; i < lvl; i++){
            System.out.print("        ");
        }
        System.out.println(node);
        if (!node.isLeaf()) {
            lvl++;
            for (Node child: node.CHILDREN) {
                showNode(child, lvl);
            }
        }
    }

    private class Node {
        private final LinkedList<Integer> VALUES = new LinkedList<Integer>();
        private final LinkedList<Node> CHILDREN = new LinkedList<Node>();

        private boolean isFull() {
            return VALUES.size() == M - 1;
        }

        private boolean isLeaf() {
            return CHILDREN.isEmpty();
        }

        // Splitting child of this node
        private void splitChild(int childIndex, Node childNode){
            // Right child
            Node rightChild = new Node();

            int middleIndex = (M - 1) / 2;
            int middleValue = childNode.VALUES.get(middleIndex);

            // All values after the middle one add to the right child
            for (int j = middleIndex + 1; j < M - 1; j++) {
                rightChild.VALUES.add( childNode.VALUES.get(j) );
            }

            // Delete all values of the childNode after the middle one
            for (int j = 0; j < M - middleIndex - 1; j++) {
                childNode.VALUES.removeLast();
            }

            // If childNode have child - add all children after middle one to the right child
            if (!childNode.isLeaf()) {
                // All children after the middle one add to the right child
                for (int j = middleIndex + 1; j < M; j++) {
                    rightChild.CHILDREN.add( childNode.CHILDREN.get(j) );
                }

                // Delete all children of the childNode after the middle one
                for (int j = 0; j < M - middleIndex - 1; j++) {
                    childNode.CHILDREN.removeLast();
                }
            }

            // If this node hasn't children - add children (left and right)
            if (CHILDREN.isEmpty()) {
                CHILDREN.add(childNode);
                CHILDREN.add(rightChild);
                // else - add right child to the end, add left child to the correct position
            } else {
                CHILDREN.add(childIndex + 1, rightChild);
                CHILDREN.set(childIndex, childNode);
            }

            // move up middle value
            VALUES.add(childIndex, middleValue);
        }

        // Add element to non full node with correct position
        private void addNonFull(int newValue) {
            if (VALUES.isEmpty()){
                VALUES.add(newValue);
            } else {
                // Find correct position by binary search
                int i = binarySearchChildIdx(this, newValue);

                // If the element is inserted at the right end - just add it to the LL
                if (i >= VALUES.size()){
                    VALUES.add(newValue);

                    // else add it to the correct position
                } else {
                    VALUES.add(i, newValue);
                }
            }
        }

        @Override
        public String toString() {
            return "Node: " + VALUES;
        }
    }

}
