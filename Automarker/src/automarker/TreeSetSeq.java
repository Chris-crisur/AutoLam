package automarker;

class TreeSetSeq {

    private static class Node {

        Comparable data;
        Node parent;
        Node left;
        Node right;
        int left_size;

        Node(Node paramNode, Comparable paramComparable) {
            this.data = paramComparable;
            this.parent = paramNode;
            this.left = null;
            this.right = null;
            this.left_size = 0;
        }
    }

    private Node root = null;
    int all_size = 0;

    public void add(Comparable paramComparable) {
        if (this.root == null) {
            this.root = new Node(null, paramComparable);
            this.all_size = 1;
            return;
        }

        this.all_size += 1;
        Node localNode = this.root;
        for (;;) {
            if (paramComparable.compareTo(localNode.data) < 0) {
                localNode.left_size += 1;
                if (localNode.left == null) {
                    localNode.left = new Node(localNode, paramComparable);
                    break;
                }
                localNode = localNode.left;
            } else {
                if (localNode.right == null) {
                    localNode.right = new Node(localNode, paramComparable);
                    break;
                }
                localNode = localNode.right;
            }
        }
    }

    public int getIndex(Comparable paramComparable) {
        if (this.root == null) {
            return -1;
        }
        int i = 0;
        Node localNode = this.root;
        while (localNode != null) {
            int j = paramComparable.compareTo(localNode.data);
            if (j < 0) {
                localNode = localNode.left;
            } else {
                if (j == 0) {
                    return i;
                }
                i += localNode.left_size + 1;
                localNode = localNode.right;
            }
        }
        return -1;
    }

    public int size() {
        return this.all_size;
    }

    public Object get(int paramInt) {
        if ((paramInt < 0) || (paramInt >= size())) {
            return null;
        }
        Node localNode = this.root;
        int i = paramInt;
        for (;;) {
            int j = localNode.left_size;
            if (i < j) {
                localNode = localNode.left;
            } else {
                if (i == j) {
                    return localNode.data;
                }
                i -= j + 1;
                localNode = localNode.right;
            }
        }
    }
}
