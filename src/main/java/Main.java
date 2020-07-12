
public class Main {
    public static void main(String[] args) {
        BTree bTree = new BTree(5);
        int[] numbers = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -10, -9, 23, 34, 65, -56, 101, -22, -34, -54, 67, -43, 76, -89, 96
        };
        bTree.add(numbers);
        bTree.show();

        int value = 45;
        System.out.println("Node for value " + value + " -> " + bTree.findNodeForValue(value));
    }
}
