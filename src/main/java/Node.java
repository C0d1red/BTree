import java.util.LinkedList;

class Node {
    public LinkedList<Integer> values;
    public LinkedList<Node> children;

    // Max degree
    private final int M;

    Node (int m) {
        values = new LinkedList<Integer>();
        children = new LinkedList<Node>();
        this.M = m;
    }

    public boolean isFull() {
        return values.size() == M - 1;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    // Splitting child of this node
    public void splitChild(int childIndex, Node childNode){
        // Right child
        Node rightChild = new Node(M);

        int middleIndex = (M - 1) / 2;
        int middleValue = childNode.values.get(middleIndex);

        // All values after the middle one add to the right child
        for (int j = middleIndex + 1; j < M - 1; j++) {
            rightChild.values.add( childNode.values.get(j) );
        }

        // Delete all values of the childNode after the middle one
        for (int j = 0; j < M - middleIndex - 1; j++) {
            childNode.values.removeLast();
        }

        // If childNode have child - add all children after middle one to the right child
        if (!childNode.isLeaf()) {
            // All children after the middle one add to the right child
            for (int j = middleIndex + 1; j < M; j++) {
                rightChild.children.add( childNode.children.get(j) );
            }

            // Delete all children of the childNode after the middle one
            for (int j = 0; j < M - middleIndex - 1; j++) {
                childNode.children.removeLast();
            }
        }

        // If this node hasn't children - add children (left and right)
        if (children.isEmpty()) {
            children.add(childNode);
            children.add(rightChild);
        // else - add right child to the end, add left child to the correct position
        } else {
            children.add(childIndex + 1, rightChild);
            children.set(childIndex, childNode);
        }

        // move up middle value
        values.add(childIndex, middleValue);
    }

    // Add element to non full node with correct position
    public void addNonFull(int newValue) {
        if (values.isEmpty()){
            values.add(newValue);
        } else {
            int i = 0;
            while (i < values.size() && newValue > values.get(i)){
                i++;
            }

            // If the element is inserted at the right end - just add it to the LL
            if (i >= values.size()){
                values.add(newValue);

            // else add it to the correct position
            } else {
                values.add(i, newValue);
            }
        }
    }

    @Override
    public String toString() {
        return "Node: " + values;
    }
}
