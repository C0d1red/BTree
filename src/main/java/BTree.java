class BTree {

    // Max degree
    private final int M;

    private Node root;

    BTree(int m) {
        this.M = m;
    }

    // Add array of values
    public void add (int[] array) {
        for (int value : array) {
            add(value);
        }
    }

    // Add value
    public void add(int value) {
        // if tree is empty - create root and insert value
        if (root == null) {
            root = new Node(M);
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
                        parent = new Node(M);
                        root = parent;
                    }

                    // Splitting node for new parent and move up middle value
                    parent.splitChild(childIndex, currentNode);

                    // Choose new currentNode for continue
                    if (value < parent.values.get(childIndex)) {
                        currentNode = parent.children.get(childIndex);
                    } else if (value > parent.values.get(childIndex)) {
                        currentNode = parent.children.get(childIndex + 1);
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
                currentNode = currentNode.children.get(childIndex);
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
            currentNode = currentNode.children.get(binarySearchChildIdx(currentNode, value));
        }
        return currentNode;
    }

    // Binary search for find index of child
    // -1 - error (value is already exists)
    private int binarySearchChildIdx(Node node, int value){
        int left = 0;
        int right = node.values.size() - 1;
        while (left <= right) {
            int middleIndex = left + (right - left) / 2;

            if (value < node.values.get(middleIndex)) {
                right = middleIndex -1;
            } else if (value > node.values.get(middleIndex)){
                left = middleIndex +1;
            } else {
                // If value exists in tree - return -1
                return -1;
            }
        }

        return right +1;
    }

    // Print tree to console
    public void show(){
        showNode(root, 0);
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
            for (Node child: node.children) {
                showNode(child, lvl);
            }
        }
    }

}
